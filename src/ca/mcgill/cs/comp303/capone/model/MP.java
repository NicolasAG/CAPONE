package ca.mcgill.cs.comp303.capone.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import org.apache.log4j.Logger;


/**
 * This class represents a Member of Parliament, the representative of the 
 * voters to the Parliament of Canada, the federal legislative branch of Canada. 
 * [Reference: Wikipedia (http://en.wikipedia.org/wiki/Member_of_Parliament)]
 * 
 * This class directly maps to a single politician resource from the openparliament.ca API.
 * For an example, check out the data format of an MP: api.openparliament.ca/politicians/tom-lukiwski/
 * 
 * The minimum set of data to capture in this class is indicated through the getter methods.
 * 
 * This class should not be immutable. It will be a dynamic entity in the final application.
 */
public class MP implements Serializable
{
	private static final transient long serialVersionUID = -3174317898736076482L;
	private static final transient int IMAGE_SUBSTRING_START = 15;
	private static final transient int HASH_CONSTANT = 101;
	private static final transient Logger LOGGER = Logger.getLogger(MP.class.getName());
	
	private String aPrimaryKey;
	private transient String aFamilyName;
	private transient String aGivenName;
	private String aName;
	private String aEmail;
	private transient String aPhoneNumber;
	private transient String aImage;
	private transient ArrayList<Membership> aMemberships = new ArrayList<>();
	private transient HashMap<String, Speech> aSpeeches = new HashMap<>();
	private Membership aCurrentMembership = null;
	
	/**
	 * Constructor for an MP with a given primary key.
	 * @param pPK the primary key.
	 */
	public MP(String pPK)
	{
		this.aPrimaryKey = pPK;
	}

	/**
	 * @return A primary key (unique identifier) for this object. We will use an MP's email
	 * as primary key.
	 */
	public String getPrimaryKey()
	{
		return this.aPrimaryKey;
	}
	
	/**
	 * Set the family name to a specified String.
	 * @param pFM the family name.
	 */
	public void setFamilyName(String pFM)
	{
		this.aFamilyName = pFM;
	}
	/**
	 * @return The family name(s) of the MP
	 */
	public String getFamilyName()
	{
		return this.aFamilyName;
	}
	
	/**
	 * Set the given name to a specified string.
	 * @param pGN the given name.
	 */
	public void setGivenName(String pGN)
	{
		this.aGivenName = pGN;
	}
	/**
	 * @return The given name(s) of the MP
	 */
	public String getGivenName()
	{
		return this.aGivenName;
	}
	
	/**
	 * Set the name to a specified string.
	 * @param pName the name.
	 */
	public void setName(String pName)
	{
		this.aName = pName;
	}
	/**
	 * @return The given and family name(s), separated by a white space.
	 */
	public String getName()
	{
		return this.aName;
	}
	
	/**
	 * Set the email to a specified string.
	 * @param pEmail the email.
	 */
	public void setEmail(String pEmail)
	{
		this.aEmail = pEmail;
	}
	/**
	 * @return The email address of the MP. This is used as the primary key.
	 */
	public String getEmail()
	{
		return this.aEmail;
	}
	
	/**
	 * Set the phone number to a specified string.
	 * @param pPN the phone number.
	 */
	public void setPhoneNumber(String pPN)
	{
		this.aPhoneNumber = pPN;
	}
	/**
	 * @return The phone number of the MP.
	 */
	public String getPhoneNumber()
	{
		return this.aPhoneNumber;
	}
	
	/**
	 * Set the image url to a given string.
	 * ex: "/media/polpics/8538_1.jpg".
	 * @param pImg the image.
	 */
	public void setImage(String pImg)
	{
		this.aImage = pImg;
	}
	
	/**
	 * @return the image url of the MP.
	 */
	public String getImage()
	{
		return this.aImage;
	}
	
	/**
	 * @return This MP's official RSS feed URL.
	 * Note that this is not found directly in the MP's JSON data. You
	 * have to find it somewhere else. Hint: look at the image field 
	 * in the JSON data, and the corresponding RSS URL from the website. 
	 */
	public String getRSSFeedURL()
	{
		try
		{
			String feedNumber = this.aImage.substring(IMAGE_SUBSTRING_START, this.aImage.length()); //feedNumber = "8538_1.jpg" in the example.
	        feedNumber = feedNumber.split("_")[0]; //feedNumber = 8538 in the example.
			return "http://openparliament.ca/politicians/"+feedNumber+"/rss/activity/";
		}
		catch (NullPointerException e)
		{
			LOGGER.warn("NULL POINTER EXCEPTION: CANNOT GET RSS FEED");
			return null;
		}	
	}
	
	/**
	 * Add a specified Membership to the list of memberships of the MP.
	 * If the Membership already exists, it will be updated.
	 * @param pMem the membership.
	 */
	public void addMembership(Membership pMem)
	{
		if (!this.aMemberships.contains(pMem))
		{
			this.aMemberships.add(pMem);
		}
		else
		{
			this.aMemberships.remove(pMem);
			this.aMemberships.add(pMem);
		}
	}
	
	/**
	 * @return the list of memberships of the MP.
	 */
	public ArrayList<Membership> getMemberships()
	{
		return this.aMemberships;
	}
	
	/**
	 * @return The total number of memberships for this MP, including the current one.
	 */
	public int getNumberOfMemberships()
	{
		return this.aMemberships.size();
	}
	
	/**
	 * @return The current MP membership  
	 */
	public Membership getCurrentMembership() 
	{
		Collections.sort(this.aMemberships);
		Date now = new Date();
		Membership mostRecentMembership = this.aMemberships.get(0);
		if (mostRecentMembership.getEndDate() == null || mostRecentMembership.getEndDate().after(now))
		{
			this.aCurrentMembership = mostRecentMembership;
			return this.aCurrentMembership;
		}
		return null;
	}
	
	/**
	 * Add a specified speech to the list of speeches of the MP.
	 * @param pSpeech the speech.
	 */
	public void addSpeech(Speech pSpeech)
	{
		//use header1 as a key.
		this.aSpeeches.put(pSpeech.getHeader1(), pSpeech);
	}
	
	/**
	 * @return the list of speeches of the MP.
	 */
	public HashMap<String, Speech> getSpeeches()
	{
		return this.aSpeeches;
	}
	
	/**
	 * Get a specific speech from the list of speeches.
	 * @param pHeader1 header of speech
	 * @return a specific speech
	 */
	public Speech getSpeech(String pHeader1)
	{
		return this.aSpeeches.get(pHeader1);
	}
	
	/**
	 * @param pUser the user profile
	 * @return true if the user profile has this MP, else return false.
	 */
	public boolean isInUserProfile(UserProfile pUser)
	{
		if (pUser.getMPs().contains(this))
		{
			return true;
		}
		return false;
	}
	
	/* 
	 * Two MPs objects are equals if they represent the same
	 * physical MP.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object pMP)
	{
		if (this == pMP)
		{
			return true;
		}
		if (pMP == null)
		{
			return false;
		}
		if (this.getClass() != pMP.getClass())
		{
			return false;
		}
		return this.getPrimaryKey().equals(((MP)pMP).getPrimaryKey());
	}

	@Override
	public int hashCode()
	{
		return HASH_CONSTANT*(this.getPrimaryKey().hashCode());
	}

	@Override
	public String toString() 
	{
		return this.aName;
	}
}
