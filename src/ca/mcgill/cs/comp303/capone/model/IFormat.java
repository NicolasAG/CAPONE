package ca.mcgill.cs.comp303.capone.model;

import java.io.IOException;

/**
 * Interface for the strategy design pattern.
 * 
 * @author Nicolas A.G.
 *
 */
public interface IFormat
{
	/**
	 * Save specified user profile to a specific file format.
	 * @param pUser The user profile to save.
	 * @param pFilePath The location to save user profile.
	 * @throws IOException if the file is not found.
	 */
	void saveUserProfile(UserProfile pUser, String pFilePath) throws IOException;
	
	/**
	 * Load specified user profile to a specific file format.
	 * @return User profile that is loaded.
	 */
	UserProfile loadUserProfile();
}
