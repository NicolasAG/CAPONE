package ca.mcgill.cs.comp303.capone.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * Concrete Strategy (for the Binary format) of the IFormat interface.
 * @author Nicolas A.G.
 */
public class BinaryFormat implements IFormat
{
	protected static final String OUTPUT_DIR = "."+File.separator+"output"+File.separator;
	protected static final String HOME_DIR =  System.getProperty("user.home") + File.separator;
	private static final transient Logger LOGGER = Logger.getLogger(UserProfile.class.getName());
	
	@Override
	public void saveUserProfile(UserProfile pUser, String pFilePath) throws IOException
	{
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(pFilePath));
		out.writeObject(pUser);
		out.close();
	}

	@Override
	public UserProfile loadUserProfile()
	{
		try
		{
			ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(HOME_DIR+ UserProfile.getInstance().getName() + ".dat"));
			UserProfile obj = (UserProfile) inputStream.readObject();
			inputStream.close();
			return obj;
		}
		catch (ClassNotFoundException e)
		{
			LOGGER.debug("Class not found");
		}
		catch (IOException e) 
		{
			LOGGER.debug("IO Exception");
		}
		return null;
	}
	
}