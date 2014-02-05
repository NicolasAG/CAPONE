package ca.mcgill.cs.comp303.capone.loaders;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author Jeff
 *
 */
public final class PropertyLoader
{
	private static final String HOME_PATH = System.getProperty("user.home");
	private static final String PROPERTY_PATH = System.getProperty("user.home") + File.separator + "Capone.properties";
	
	private static PropertyLoader instance = null;
	
	private Properties aProperties = null;

	private PropertyLoader()
	{
		this.aProperties = new Properties();		
	}
	
	/**
	 * 
	 * @return - 
	 */
	public static synchronized PropertyLoader getInstance()
	{
		if (instance == null)
		{
			instance = new PropertyLoader();
		}
		return instance;
	}
	
	/**
	 * Sets defaults for property file.
	 */
	private void setDefaults()
	{
		this.aProperties.put("AutoLoad", "false");
		this.aProperties.put("FilePath", HOME_PATH);
		this.aProperties.put("Language", "EN");
		this.aProperties.put("ExportProfilePath", HOME_PATH + File.separator + "output.json");
		this.aProperties.put("BinaryProfileFilePath", HOME_PATH);
	}
	
	/**
	 * 
	 * @throws IOException - 
	 */
	public void load() throws IOException
	{
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(PROPERTY_PATH);
			this.aProperties.load(fis);
		}
		catch (FileNotFoundException e)
		{
			this.setDefaults();
			this.save();
		}
		finally
		{
			if (fis != null) 
			{
				fis.close();
			}
		}
	}
	
	/**
	 * 
	 * @throws IOException - 
	 */
	public void save() throws IOException
	{
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(PROPERTY_PATH);
			this.aProperties.store(fos, "Capone Properties");
		}
		catch (FileNotFoundException e)
		{
			//this will happen for sure! :/
		}
		finally
		{
			if (fos != null)
			{
				fos.close();
			}
		}
	}
	
	/**
	 * 
	 * @param pKey - 
	 * @return - 
	 */
	public String getProperty(String pKey)
	{
		return this.aProperties.getProperty(pKey);
	}
	
	/**
	 * 
	 * @param pKey - 
	 * @param pValue - 
	 */
	public void setProperty(String pKey, String pValue)
	{
		this.aProperties.setProperty(pKey, pValue);
	}

}
