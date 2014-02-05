package ca.mcgill.cs.comp303.capone.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class maps to a single speech (that is part of a house aDebate)
 * resource from the openparliament.ca API.
 * 
 * Here is an example of the data format: http://api.openparliament.ca/debates/2013/6/18/tom-lukiwski-1/
 * 
 * Immutable.
 * 
 */
public class Speech implements Serializable
{
	private static final long serialVersionUID = 8885625604489282824L;
	private String aMPKey;
	private String aHeader1;
	private String aHeader2;
	private String aContent;
	private String aTime;
	
	/**
	 * Construct a Speech with specified headers, content, time.
	 * @param pMPKey the key of the MP that gave this speech.
	 * @param pHeader1 the 1st header.
	 * @param pHeader2 the second header.
	 * @param pContent the content.
	 * @param pTime the time.
	 */
	public Speech(String pMPKey, String pHeader1, String pHeader2, String pContent, String pTime)
	{
		this.aMPKey = pMPKey;
		this.aHeader1 = pHeader1;
		this.aHeader2 = pHeader2;
		this.aContent = pContent;
		this.aTime = pTime;
	}
	
	/**
	 * @return the key of the MP that gave this speech.
	 */
	public String getMPKey()
	{
		return this.aMPKey;
	}
	
	/**
	 * @return The main label for this speech. e.g., "Routine Proceedings"
	 */
	public String getHeader1()
	{
		return this.aHeader1;
	}

	/**
	 * @return The secondary label for this speech. e.g., "Government Response to Petitions"
	 */
	public String getHeader2()
	{
		return this.aHeader2;
	}

	/**
	 * @return The content of the speech.
	 */
	public String getContent()
	{
		return this.aContent;
	}

	/**
	 * @return The time at which the speech was given.
	 */
	public Date getTime()
	{
		try
		{
			Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(this.aTime);
			return (Date) date.clone();
		}
		catch (ParseException e)
		{
			return null;
		}
	}
	
	@Override
	public String toString() 
	{
		return this.aHeader1+" - "+this.aHeader2;
	}

	@Override
	public int hashCode()
	{
		final int lPrime = 31;
		int result = 1;
		if (this.aMPKey == null)
		{
			result = lPrime * result + 0;
		}
		else
		{
			result = lPrime * result + this.aMPKey.hashCode();
		}
		if (this.aTime == null)
		{
			result = lPrime * result + 0;
		}
		else
		{
			result = lPrime * result + this.aTime.hashCode();
		}
		return result;
	}

	@Override
	public boolean equals(Object pObject)
	{
		if (this == pObject)
		{
			return true;
		}
		if (pObject == null)
		{
			return false;
		}
		if (getClass() != pObject.getClass())
		{
			return false;
		}
		Speech other = (Speech) pObject;
		if (this.aMPKey == null)
		{
			if (other.aMPKey != null)
			{
				return false;
			}
		}
		else if (!this.aMPKey.equals(other.aMPKey))
		{
			return false;
		}
		if (this.aTime == null)
		{
			if (other.aTime != null)
			{
				return false;
			}
		}
		else if (!this.aTime.equals(other.aTime))
		{
			return false;
		}
		return true;
	}
	
}
