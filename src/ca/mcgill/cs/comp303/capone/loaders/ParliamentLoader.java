package ca.mcgill.cs.comp303.capone.loaders;

import ca.mcgill.cs.comp303.capone.loaders.op.OpenParliamentFileLoader.DataDirectory;
import ca.mcgill.cs.comp303.capone.model.Parliament;

/**
 * Services for creating model elements.
 */
public interface ParliamentLoader
{
	/**
	 * Loads an MP record into the Parliament object.
	 * 
	 * @param pRelativeLocation An indicator of where to find the MP's record 
	 * 							relative to the context of the concrete loader.
	 * @param pParliament The object to load the data into.
	 * @param pDataDirectory the directory where to get the info.
	 * @return The primary key of the MP just loaded.
	 */
	String loadMP(String pRelativeLocation, Parliament pParliament, DataDirectory pDataDirectory);

	/**
	 * Loads recent events related to this MP into the Parliament object. 
	 * For M1 these events are all the speeches.
	 * 
	 * @param pMPKey The primary key identifier of the MP
	 * @param pParliament The object to load the data into.
	 * @param pDataDirectory the directory where to get the info.
	 */
	void loadRecentEvents(String pMPKey, Parliament pParliament, DataDirectory pDataDirectory);
	
	/**
	 * Loads MP and Events from disk.
	 * Only adds MP if it currently doesn't exist in Parliament.
	 * Removes MPs in parliament that aren't being loaded from disk.
	 * @param pFilePath - File path of data to be loaded.
	 */
	void loadFromDisk(String pFilePath);
	
	/**
	 * Downloads All data to specific file path.
	 * @param pFilePath - File path to storage location.
	 */
	void downloadFromWeb(String pFilePath);

	/**
	 * 
	 * @param pFilePath - File path to storage location.
	 */
	void updateSpeeches(String pFilePath);

}
