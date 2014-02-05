package ca.mcgill.cs.comp303.capone.loaders.op;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ca.mcgill.cs.comp303.capone.loaders.MPEvent;
import ca.mcgill.cs.comp303.capone.loaders.MPEvent.Descriptor;
import ca.mcgill.cs.comp303.capone.loaders.ParliamentLoader;
import ca.mcgill.cs.comp303.capone.loaders.ParliamentLoaderException;
import ca.mcgill.cs.comp303.capone.loaders.PropertyLoader;
import ca.mcgill.cs.comp303.capone.loaders.op.stubs.JMP;
import ca.mcgill.cs.comp303.capone.loaders.op.stubs.JMPList;
import ca.mcgill.cs.comp303.capone.loaders.op.stubs.JMembership;
import ca.mcgill.cs.comp303.capone.loaders.op.stubs.JSpeech;
import ca.mcgill.cs.comp303.capone.model.Capone;
import ca.mcgill.cs.comp303.capone.model.MP;
import ca.mcgill.cs.comp303.capone.model.Membership;
import ca.mcgill.cs.comp303.capone.model.Party;
import ca.mcgill.cs.comp303.capone.model.Riding;
import ca.mcgill.cs.comp303.capone.model.Speech;
import ca.mcgill.cs.comp303.capone.model.Parliament;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * A builder that can build the model from serialized 
 * JSON objects stored at specific locations on disk. Objects of this class should 
 * store the root of the data tree internally (e.g., C:\workspace...\data). The 
 * last branches of the path map directly to the OpenParliament API.
 * 
 * We packaged the data under Capone-M1/data. You can (should) use this as the root path.
 */
public class OpenParliamentFileLoader implements ParliamentLoader
{
	
	private static final int BUFFER_SIZE = 1024; //used to split class name string.
	
	// Logger
	private static final Logger LOGGER = Logger.getLogger(OpenParliamentFileLoader.class.getName());

	// You should keep this root to make sure we can run your project from our environment.
	private static final String M1_DATA_FILE_SUBDIR = "." + File.separator + "data" + File.separator;
	private static final String M1_QUERY_CONTEXT = new File(M1_DATA_FILE_SUBDIR).getAbsolutePath() + File.separator;
	
	private static final String BEFORE_JUNE_DATA_SNAPSHOT_SUBDIR_STRING = "." + File.separator 
																			  + "Capone-M2-Complete-v4" 
																			  + File.separator 
																			  + "data-snapshot-before-june-2013" 
																			  + File.separator;
	private static final String AFTER_JUNE_DATA_SNAPSHOT_SUBDIR_STRING = "." + File.separator 
																			 + "Capone-M2-Complete-v4" 
																			 + File.separator 
																			 + "data-snapshot-since-june-2013" 
																			 + File.separator;

	private static final String BEFORE_JUNE_QUERY_CONTEXT = new File(BEFORE_JUNE_DATA_SNAPSHOT_SUBDIR_STRING).getAbsolutePath() + File.separator;
	private static final String AFTER_JUNE_QUERY_CONTEXT = new File(AFTER_JUNE_DATA_SNAPSHOT_SUBDIR_STRING).getAbsolutePath() + File.separator;

	
	private static String aFileSubdir;
	private static String aQueryContext;
	
	// Relative paths to JSON files	
	private static final String CONTEXT_POLITICIANS = "politicians" + File.separator;
	private static final String CONTEXT_SPEECHES = "speeches" + File.separator;
	private static final String CONTEXT_RSS = "rss" + File.separator;

	private static final String JSON_SUFFIX = ".json";
	private static final String XML_SUFFIX = ".xml";
	
	
	// URL paths
	private static final String BASE_URL = "http://api.openparliament.ca";
	private static final String URL_CONTEXT_POLITICIANS = "politicians";
	private static final String URL_CONTEXT_JSONFORMAT = "?format=json";
	private static final String SLASH = "/";
	/**
	 * 
	 *Enum for data path.
	 *
	 */
	public enum DataDirectory
	{
		M1, M2BEFOREJUNE, M2AFTERJUNE, FROMDISK;
	}
	/* 
	 * The relative location indicates the subpath leading to a
	 * specific politician. For the complete list, see:
	 * http://api.openparliament.ca/politicians/
	 * an example of input for pRelativeLocation is: gord-brown
	 * @see ca.mcgill.cs.comp303.capone.loaders.ParliamentLoader#loadMP(java.lang.String, ca.mcgill.cs.comp303.capone.model.Parliament)
	 */
	@Override
	public String loadMP(String pRelativeLocation, Parliament pParliament, DataDirectory pDataDirectory)
	{
		// This method produces a complete path pointing to the
		// JSON file for this MP.
		
		switch (pDataDirectory)
		{
		case M1:
			aQueryContext = M1_QUERY_CONTEXT;
			aFileSubdir = M1_DATA_FILE_SUBDIR;
			break;
		case M2BEFOREJUNE:
			aQueryContext = BEFORE_JUNE_QUERY_CONTEXT;
			aFileSubdir = BEFORE_JUNE_DATA_SNAPSHOT_SUBDIR_STRING;
			
			break;
		case M2AFTERJUNE:
			aQueryContext = AFTER_JUNE_QUERY_CONTEXT;	
			aFileSubdir = AFTER_JUNE_DATA_SNAPSHOT_SUBDIR_STRING;
			
			break;
		
		case FROMDISK:
			try
			{
				PropertyLoader.getInstance().load();
				aQueryContext = PropertyLoader.getInstance().getProperty("FilePath") + File.separator;
				aFileSubdir = PropertyLoader.getInstance().getProperty("FilePath") + File.separator;
			}
			catch (IOException e)
			{
				//Couldn't load properties
			}
		
			break;
		default:
			aQueryContext = M1_QUERY_CONTEXT;
			break;
		}
		
		String jsonUri = getMPJsonUri(pRelativeLocation);

		
		try
		{
			// JSON file can get loaded easily into stub objects
			// using the Google GSON library. See the ...op.stubs 
			// package. Do not change any stub.
			JMP jmp = getGson(jsonUri, JMP.class);
			
			String key = jmp.getEmail();
			if (key == null)
			{
				return null;
			}
			MP tMp = Capone.getParliament().getMP(key);
			if (tMp != null)
			{
				return tMp.getPrimaryKey();
			}
			
			// Load all the information available about the MP from the MP's
			// page on OpenParliament.ca.
			MP mp = new MP(jmp.getEmail()); //create an MP - set the Primary Key to the Email.
			mp.setFamilyName(jmp.getFamily_name()); //load the family name.
			mp.setGivenName(jmp.getGiven_name()); //load the given name.
			mp.setName(jmp.getName()); //load the name.se
			mp.setEmail(jmp.getEmail()); //load the email.
			mp.setPhoneNumber(jmp.getVoice()); //load the number.
			mp.setImage(jmp.getImage()); //load the image url.		
			//load the memberships to the MP.
			
			for(JMembership jMem: jmp.getMemberships())
			{
				Capone.getParliament().addParty(jMem.getParty().getShort_name(), jMem.getParty().getName());
				Capone.getParliament().addRiding(jMem.getRiding().getId(), jMem.getRiding().getName(), jMem.getRiding().getProvince());
				Party tParty = Capone.getParliament().getParty(jMem.getParty().getShort_name());
				Riding tRiding = Capone.getParliament().getRiding(jMem.getRiding().getId());
				mp.addMembership(new Membership(tParty, tRiding, jMem.getStart_date(), jMem.getEnd_date()));
			}
			Capone.getParliament().addMP(mp); //add the mp to the Parliament.
			return mp.getPrimaryKey(); // The primary key of the MP just loaded.
		}
		catch (ParliamentLoaderException | NullPointerException e)
		{
			LOGGER.debug("COULD NOT LOAD MP");
			throw new ParliamentLoaderException();
		}
	}

	/* 
     * For milestone 1, only load the speeches into the MP object.
     * However, load the entire speech data for each speech entry 
     * in the RSS feed. This will require you to fetch complementary
     * data from the JSON files under /debates.
     * Only a subset of the speeches are available in the local folder
     * (we did not include speeches in Committees, from example). 
     * If you don't find a certain speech mentioned in the RSS feed,
     * just ignore it.
	 * @see ca.mcgill.cs.comp303.capone.loaders.ParliamentLoader#loadRecentEvents(java.lang.String, ca.mcgill.cs.comp303.capone.model.Parliament)
	 */
	@Override
	public void loadRecentEvents(String pMPKey, Parliament pParliament, DataDirectory pDataDirectory)
	{
		
		switch (pDataDirectory)
		{
		case M1:
			aQueryContext = M1_QUERY_CONTEXT;
			break;
		case M2BEFOREJUNE:
			aQueryContext = BEFORE_JUNE_QUERY_CONTEXT;
			
			break;
		case M2AFTERJUNE:
			aQueryContext = AFTER_JUNE_QUERY_CONTEXT;	
			
			break;
		case FROMDISK:
			try
			{
				PropertyLoader.getInstance().load();
				aQueryContext = PropertyLoader.getInstance().getProperty("FilePath") + File.separator;
				aFileSubdir = PropertyLoader.getInstance().getProperty("FilePath") + File.separator;
			}
			catch (IOException e)
			{
				System.out.println("Couldn't load properties");
			}
		
			break;
		default:
			aQueryContext = M1_QUERY_CONTEXT;
			break;
		}
		
		try
		{
			//pMPKey is an email address of the form "FisrtName.LastName@parl.gc.ca".
			String key = pMPKey.split("@")[0]; //key = "FirstName.LastName".
			key = key.replace('.', '-'); //key = "FirstName-LastName".
			key = key.toLowerCase(); //key = "firstname-lastname".
			
			String mpJsonUri = getMPJsonUri(key); //get the absolute path according to the relative path.
			
			JMP jmp = getGson(mpJsonUri, JMP.class); //get the jMP object according to the absolute path.
			MP mp = Capone.getParliament().getMP(jmp.getEmail());

			String rssURL = jmp.getRelated().getActivity_rss_url();
			String feedNumber = rssURL.split("/")[2];
			
			FileInputStream xmlFile = null;
			ArrayList<MPEvent> events = new ArrayList<>();
			try //to get all events of the xmlFile in the ArrayList events
			{
				xmlFile = new FileInputStream(new File(aFileSubdir+CONTEXT_RSS+feedNumber+XML_SUFFIX));
				events = (ArrayList<MPEvent>) new RssSaxParser().parse(xmlFile); // API Call to use the SAX Parser
			}
			catch (FileNotFoundException e)
			{
				LOGGER.debug("FILE NOT FOUND EXCEPTION: CANNOT FIND XML FILE FOR EVENTS", e);
				return;
			}
			try 
			{
				xmlFile.close();
			}
			catch (IOException e)
			{
				LOGGER.debug("IO EXCEPTION: PROBLEM CLOSING XML FILE", e);
				return;
			}
			//Now 'events' contains all recent events of the MP.
			for (MPEvent e : events)
			{
				if (e.getType().equals(Descriptor.SPEECH)) //if the event is a speech:
				{
					String speechContext = getSpeechContext(e.getLink()); //get the path of the speech file.
					String speechJsonUri = getSpeechJsonUri(speechContext); //get the speech file name.
					try
					{
						//System.out.println("loading" + speechJsonUri);
						JSpeech js = getGson(speechJsonUri, JSpeech.class); //get the speech object.
						PropertyLoader.getInstance().load();
						Speech s;
						if (PropertyLoader.getInstance().getProperty("Language").equals("FR"))
						{
							 s = new Speech(mp.getPrimaryKey(), js.getH1().getFr(), js.getH2().getFr(), js.getContent().getFr(), js.getTime());
						} 
						else
						{
							 s = new Speech(mp.getPrimaryKey(), js.getH1().getEn(), js.getH2().getEn(), js.getContent().getEn(), js.getTime());
						}
						
						mp.addSpeech(s); //add the speech to the MP HashMap.
					}
					catch (ParliamentLoaderException ex)
					{
						LOGGER.debug("PARLIAMENT LOADER EXCEPTION: PROBLEM ADDING SPEECH WITH NAME:" + speechJsonUri, ex);
					}
					catch (IllegalStateException e2) 
					{
						LOGGER.debug("Illegal State Exception within load events", e2);
					}
					catch (NullPointerException e2) 
					{
						LOGGER.debug("Missing speech element for " + e.getLink(), e2);

					}
					catch (JsonSyntaxException e3)
					{
						LOGGER.debug("Trying to log non-json file", e3);
					}
					catch (IOException e4)
					{
						LOGGER.debug("Problem Loading language", e4);
					}
				}
			}
		}
		catch (ParliamentLoaderException e)
		{
			LOGGER.debug("COULD NOT LOAD EVENTS: PARLIAMENT LOADER EXCEPTION: PROBLEM GETTING THE SPECIFIED MP", e);
		}
		catch (NullPointerException e)
		{
			LOGGER.debug("COULD NOT LOAD EVENTS: NULL POINTER EXCEPTION", e);
		}
	}
	
	@Override
	public void loadFromDisk(String pFilePath)
	{
		File root = new File(pFilePath + File.separator + CONTEXT_POLITICIANS);
		File[] list = root.listFiles();
		ArrayList<MP> recentlyMp = new ArrayList<>();
		
		if (list==null)
		{
			return;
		}
		for (File f: list) 
		{
			if (f.isDirectory()) 
			{
				loadFromDisk(f.getAbsolutePath());
				//System.out.println("->Dir: " + f.getAbsoluteFile());
			}
			else if (!f.isDirectory())
			{
				String ext = FilenameUtils.getExtension(f.getAbsoluteFile().getName());
				if (ext.compareTo("json") == 0) 
				{
					
					String fileName = FilenameUtils.getBaseName(f.getAbsoluteFile().getName());
					String key = loadMP(fileName, Capone.getParliament(), DataDirectory.FROMDISK);
					if (key == null)
					{
						LOGGER.debug("MP is missing email. So no primary key.... yeah.");
						continue;
					}
					LOGGER.info("Loaded "+ key + " from disk location " + f.getAbsolutePath());
					loadRecentEvents(key, Capone.getParliament(), DataDirectory.FROMDISK);
					recentlyMp.add(Capone.getParliament().getMP(key));
					//System.out.println("-->File: "+ f.getAbsoluteFile());
					//System.out.println("-->MP: "+fileName);
				}
			}
		}
		for (MP mp: Capone.getParliament().getMPs())
		{
			if (!recentlyMp.contains(mp))
			{
				Capone.getParliament().removeMP(mp);
			}
		}
	}
	
	
	
	
	@Override
	public void downloadFromWeb(String pFilePath)
	{
		int totalSpeeches = 0;
		try
		{
			String mpListJson = readURL(BASE_URL + SLASH + URL_CONTEXT_POLITICIANS + SLASH + URL_CONTEXT_JSONFORMAT);

			Gson gson = new Gson();
			JMPList mpList = gson.fromJson(mpListJson, JMPList.class);
			for (JMP tJmp : mpList.getObjects())
			{
				String name = tJmp.getName();
				String url = tJmp.getUrl();
				
				name = Normalizer.normalize(name, Normalizer.Form.NFD);
				name = name.replaceAll("[^\\p{ASCII}]", "");

				name = name.replace(' ', '-'); //key = "FirstName-LastName".
				name = name.toLowerCase(); //key = "firstname-lastname".
				
				
				LOGGER.info("Downloading "+ name);
				String tMP = readURL(BASE_URL + url + URL_CONTEXT_JSONFORMAT);
				String fullMPFilePath = pFilePath + File.separator + CONTEXT_POLITICIANS;
				toDisk(tMP, fullMPFilePath, name + JSON_SUFFIX);
				
				JMP t2Jmp = getGson(fullMPFilePath + name + JSON_SUFFIX, JMP.class);
				String rssURL = t2Jmp.getRelated().getActivity_rss_url();
				
				String feedNumber = rssURL.split("/")[2];
						
				String fullXMLFilePath = pFilePath + File.separator + CONTEXT_RSS;
				String xmlFile = readURL(BASE_URL + rssURL);
				toDisk(xmlFile, fullXMLFilePath, feedNumber + XML_SUFFIX);
				
				totalSpeeches = totalSpeeches + downloadAllSpeeches(pFilePath, fullXMLFilePath + feedNumber + XML_SUFFIX);
			}
			LOGGER.info("Loaded " + totalSpeeches + " speeches");
		}
		catch (IOException e) 
		{
			//Problem reading URL.
		}
	}
	
	@Override
	public void updateSpeeches(String pFilePath)
	{
		int totalSpeeches = 0;
		for (MP mp : Capone.getParliament().getMPs())
		{
			try
			{
				String rssURL = mp.getRSSFeedURL(); //rssURL is in the form of: "http://openparliament.ca/politicians/65/rss/activity/"
				
				String xmlFile = readURL(rssURL);
				String fullXMLFilePath = pFilePath + File.separator + CONTEXT_RSS;
				String feedNumber = rssURL.split("/")[4]; //ex:65

				toDisk(xmlFile, fullXMLFilePath, feedNumber + XML_SUFFIX);
				 
				totalSpeeches = totalSpeeches + downloadAllSpeeches(pFilePath, fullXMLFilePath + feedNumber + XML_SUFFIX);
			}
			catch (NullPointerException e)
			{
				LOGGER.debug("problem with rss url");
			}
			catch (IOException e)
			{
				LOGGER.debug("Problem with rss url");
			}
			catch (ParliamentLoaderException e) {
				LOGGER.debug("Strange IOExeption from RSS Parser");
			}

		}
		LOGGER.info("Loaded " + totalSpeeches + " speeches");
	}
	
	
	@SuppressWarnings("null")
	private static int downloadAllSpeeches(String pFilePath, String pXMLFilePath)
	{
		
		int counter = 0;
		FileInputStream xmlFile = null;
		ArrayList<MPEvent> events = new ArrayList<>();
		try //to get all events of the xmlFile in the ArrayList events
		{
			xmlFile = new FileInputStream(new File(pXMLFilePath));
			events = (ArrayList<MPEvent>) new RssSaxParser().parse(xmlFile); // API Call to use the SAX Parser
		}
		catch (FileNotFoundException e)
		{
			LOGGER.debug("FILE NOT FOUND EXCEPTION: CANNOT FIND XML FILE FOR EVENTS", e);
			//e.printStackTrace();
			return counter;
		}
		catch (ParliamentLoaderException e) 
		{
			LOGGER.debug("Strange IOExeption from RSS Parser");
		}
		try 
		{
			xmlFile.close();
		}
		catch (IOException e)
		{
			LOGGER.debug("IO EXCEPTION: PROBLEM CLOSING XML FILE", e);
			//e.printStackTrace();
			return counter;
		}

		//Now 'events' contains all recent events of the MP.
		for (MPEvent e : events)
		{
			if (e.getType().equals(Descriptor.SPEECH)) //if the event is a speech:
			{
				try
				{
					String speechURL = e.getLink().replaceAll("(?i)http://", "http://api.");
					//String speechURL = getSpeechContext(e.getLink());
					
					String adjustedStringFilePath = e.getLink().replaceAll("(?i)http://openparliament.ca/", "");
					
					String[] splitPath = adjustedStringFilePath.split("/");
					String name = splitPath[splitPath.length - 1];
					String fullspeechFilePath = pFilePath 
											  + File.separator 
											  + CONTEXT_SPEECHES 
											  + StringUtils.join(splitPath, SLASH, 0, splitPath.length - 1) 
											  + File.separator;
					
					if (!new File(fullspeechFilePath + name + JSON_SUFFIX).exists())
					{
						String speech = readURL(speechURL + URL_CONTEXT_JSONFORMAT);
						counter++;
						LOGGER.info("Loading speech: " + name);
						toDisk(speech, fullspeechFilePath, name + JSON_SUFFIX);
					} 
					else 
					{
						LOGGER.info("Didn't load speech: "+ name);
					}
					
				}
				catch (IOException e2)
				{
					LOGGER.debug("IOException within speech download", e2);
				}
			
			}
		}
		return counter;
	}
	
	private static String readURL(String pURLString) throws IOException
	{
		BufferedReader reader = null;
		InputStreamReader input = null;

		try
		{
			URL url = new URL(pURLString);
			input = new InputStreamReader(url.openStream());
			reader = new BufferedReader(input);
			StringBuffer buffer = new StringBuffer();
			int len = 0;
			char[] charBuffer = new char[BUFFER_SIZE];
			 while ((len = reader.read(charBuffer)) != -1)
			 {
		            buffer.append(charBuffer, 0, len); 
			 }			
			return buffer.toString();
		}
		finally
		{
			 if (reader != null)
			    {
			    	try 
			    	{ 
			    		reader.close(); 
			    	} 
			    	catch (IOException e) 
			    	{
			    		LOGGER.debug("COULDN'T CLOSE READERSTREAM", e);
			    	}
			    }
		}
	}
	
	private static void toDisk(String pDataString, String pFilePath, String pFileName) throws IOException
	{
		OutputStream output = null;
		try
		{
			File dir = new File(pFilePath);
			dir.mkdirs();
			output = new FileOutputStream(pFilePath + File.separator + pFileName);
			byte[] buffer = pDataString.getBytes();
			output.write(buffer);
		}
		finally
		{
			 if (output != null)
			    {
			    	try 
			    	{ 
			    		output.close(); 	
			    	} 
			    	catch (IOException e) 
			    	{
			    		LOGGER.debug("COULDN'T CLOSE OUTPUTSTREAM", e);
			    	}
			    }
		}
		
	}
	
	// Below are some potentially useful utility methods. You may need to 
	// experiment with them a bit to make sure you understand how they work.
	
	/*
	 * Takes as input the speech link in the RSS item and returns 
	 * the partial path leading to the corresponding speech file in
	 * the debate directory tree.
	 */
	private static String getSpeechContext(String pRssSpokeInTheHouseLink) 
	{
		String link = pRssSpokeInTheHouseLink.replaceAll("(?i)http://openparliament.ca/", "");
		String context = link.substring(0, link.length()-1).replace("/", File.separator);
		return context;
	}	
	
	/**
	 * Uses the google-gson library to convert JSON to Java objects (prefixed 
	 * those objects with J, in package ...loaders.op.stubs directory).
	 * 
	 * @param pJsonUri Filename of the JSON data
	 * @param ptype A Class object from the package ...loaders.op.stubs
	 * @return A GSON stub object, specified by the parameter "type" 
	 * CSOFF:
	 */
	public static <T> T getGson(String pJsonUri, Class<T> ptype) throws ParliamentLoaderException
	{
		InputStreamReader json = new InputStreamReader(getInputStream(pJsonUri));
		T gson = new Gson().fromJson(json, ptype);
		return gson;
	}
	
	/**
	 * @param pMPRelativeLocation Relative location that identifies the MP, returned by getMPRelativeLocations
	 * @return Filename of the JSON data  on the given MP
	 */
	public static String getMPJsonUri(String pMPRelativeLocation)
	{
		return aQueryContext + CONTEXT_POLITICIANS + pMPRelativeLocation + JSON_SUFFIX;
	}

	/**
	 * @param pSpeechContext The speech context obtained from the link from the 
	 * 		   RSS feed "Spoke in the house" event, e.g., debates/2013/6/17/thomas-mulcair-4/
	 * @return  Filename of the JSON data on transcript of the given MP's speeches
	 */
	public static String getSpeechJsonUri(String pSpeechContext)
	{
		return aQueryContext + CONTEXT_SPEECHES + pSpeechContext + JSON_SUFFIX;
	}

	/**
	 *  @param pMPRelativeLocation Relative location that identifies the MP, returned by getMPRelativeLocations
	 * @return Filename of the RSS feed for the specified MP.
	 */
	public static String getRssUri(String pMPRelativeLocation)
	{
		return aQueryContext + CONTEXT_RSS + pMPRelativeLocation + ".xml";
	}

	/*
	 * Return a FileInputStream for the given path
	 */
	private static InputStream getInputStream(String pFilePath)
	{
		try
		{
			File file = new File(pFilePath);
			return new FileInputStream(file);
		}
		catch (FileNotFoundException e)
		{
			throw new ParliamentLoaderException(e);
		}
	}
	
	/**
	 * @return the relative locations of all the MPs.
	 */
	public static Iterator<String> getMPRelativeLocations() 
	{
		List<String> result = new ArrayList<>();
		String dir = aQueryContext + CONTEXT_POLITICIANS; 
		Collection<File> files = FileUtils.listFiles(new File(dir), new WildcardFileFilter("*.json"), null);
		
		for ( File file : files )
		{
			String f = file.getName();
			result.add(f.substring(0, f.length()-".json".length()));
		}
		return result.iterator();
	}
}
