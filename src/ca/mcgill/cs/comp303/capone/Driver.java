package ca.mcgill.cs.comp303.capone;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;

import org.apache.log4j.PropertyConfigurator;

import ca.mcgill.cs.comp303.capone.gui.AppFrame;
import ca.mcgill.cs.comp303.capone.loaders.ParliamentLoader;
import ca.mcgill.cs.comp303.capone.loaders.PropertyLoader;
import ca.mcgill.cs.comp303.capone.loaders.op.OpenParliamentFileLoader;
import ca.mcgill.cs.comp303.capone.model.BinaryFormat;
import ca.mcgill.cs.comp303.capone.model.UserProfile;

/**
 * The main method of the application.
 * @author Jeff
 */
public final class Driver
{
	protected static final String HOME_DIR_FILE =  System.getProperty("user.home") + File.separator;
	private static final String COUNTRY = "CA";
	private static String aLANGUAGE = "en";
	
	private static JFrame mainFrame = null;

	/**
	 * Private Constructor.
	 */
	private Driver()
	{
	}

	/**
	 * @param pArgs - not used. 
	 */
	public static void main(String[] pArgs)
	{
		PropertyConfigurator.configure("log4j.properties");
		ParliamentLoader loader = new OpenParliamentFileLoader();
		try
		{
			PropertyLoader.getInstance().load();
			if ( Boolean.valueOf(PropertyLoader.getInstance().getProperty("AutoLoad")) )
			{
				loader.loadFromDisk(PropertyLoader.getInstance().getProperty("FilePath"));
			}
			aLANGUAGE = PropertyLoader.getInstance().getProperty("Language");
		}
		catch (IOException e)
		{
			System.out.println("problem with properties");
		}
		
		UserProfile current = UserProfile.getInstance("current");
		try
		{ //if user profile already saved, grab this one.
			current.load(new BinaryFormat());
		}
		catch(NullPointerException e)
		{ //if new user, save it.
	    	current.save(new BinaryFormat(), HOME_DIR_FILE + UserProfile.getInstance().getName() + ".dat");

		}
		
		final ResourceBundle lMESSAGES = ResourceBundle.getBundle("ca.mcgill.cs.comp303.capone.gui.MessagesBundle",
																	new Locale(aLANGUAGE, COUNTRY ));
		current.triggerSpeechRecommendation();
		mainFrame = new AppFrame(lMESSAGES);
		
		/********************************************
		 * Default settings:                        *
		 * * * * *  * * * * * * * * * * * * * * * * *
		 * Need Load from where in disk. file path? *
		 * auto load property false by default      *
		 ********************************************/
	}
	
	
	/**
	 * Instance of main frame of application.
	 * @return Instance of main frame of application.
	 */
	public static JFrame getMainFrameInstance()
	{
		return mainFrame;
	} 

}

