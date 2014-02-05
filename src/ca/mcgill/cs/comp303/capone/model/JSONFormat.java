package ca.mcgill.cs.comp303.capone.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.Logger;

import com.google.gson.Gson;

/**
 * Concrete Strategy (for the JSON format) of the IFormat interface.
 * @author Nicolas A.G.
 */
public class JSONFormat implements IFormat
{
	
	protected static final String OUTPUT_DIR = "."+File.separator+"output"+File.separator;
	protected static final String HOME_DIR =  System.getProperty("user.home") + File.separator;
	private static final transient Logger LOGGER = Logger.getLogger(UserProfile.class.getName());

	@Override
	public void saveUserProfile(UserProfile pUser, String pFilePath) throws IOException
	{
		Gson gson = new Gson();
		String json = gson.toJson(pUser);
		
		FileWriter writer = new FileWriter(pFilePath);
		writer.write(json);
		writer.close();		
	}
	
	@Override
	public UserProfile loadUserProfile()
	{
		try
		{
			Gson gson = new Gson();
			FileInputStream stream = new FileInputStream(new File(HOME_DIR + UserProfile.getInstance().getName() + ".json"));
			InputStreamReader json = new InputStreamReader(stream);
			
			UserProfile profile = gson.fromJson(json, UserProfile.class);
			System.out.println(profile.getName());

			stream.close();
			return profile;
		}
		catch (FileNotFoundException e)
		{
			LOGGER.debug("File not found");
		}
		catch (IOException e) 
		{
			LOGGER.debug("IO Exception");
		}
		
		return null;
	}
}
