package ca.mcgill.cs.comp303.capone.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ca.mcgill.cs.comp303.capone.loaders.ParliamentLoaderException;


/**
 * This class maps to a single politician membership resource from the openparliament.ca API. 
 * See an example of the data format from this example: api.openparliament.ca/politicians/memberships/1534/
 * 
 * Memberships should be naturally sortable in decreasing chronological order.
 * 
 * Not all data must be captured by this class. The minimum set is represented by the methods.
 * 
 * The class should be immutable.
 * 
 * @see http://api.openparliament.ca/politicians/memberships/
 */
public class Membership implements Comparable<Membership>, Serializable
{
	private static final transient long serialVersionUID = -5961804060290134554L;
	private static final transient int HASH_CONSTANT = 101;
	
	private Party aParty;
	private Riding aRiding;
	private String aStartDate;
	private String aEndDate;
	
	/**
	 * @param pParty - party of membership
	 * @param pRiding - riding of membership
	 * @param pStartDate - start date of membership
	 * @param pEndDate - end date of membership
	 */
	public Membership(Party pParty, Riding pRiding, String pStartDate, String pEndDate)
	{
		this.aParty = pParty;
		this.aRiding = pRiding;
		this.aStartDate = pStartDate;
		this.aEndDate = pEndDate;
	}
	
	/**
	 * @return party of membership
	 */
	public Party getParty()
	{
		return this.aParty; 
	}

	/**
	 * @return riding of membership
	 */
	public Riding getRiding()
	{
		return this.aRiding;
	}

	/**
	 * @return the start date of this membership 
	 */
	public Date getStartDate()
	{
		try
		{
			Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(this.aStartDate);
			return (Date) date.clone();
		}
		catch (ParseException e)
		{
			throw new ParliamentLoaderException();
		}	
	}

	/**
	 * @return If this is a past membership, it returns the end date of that membership.
	 * If this is a current membership, return null;
	 */
	public Date getEndDate()
	{
		try
		{
			Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(this.aEndDate);
			return (Date) date.clone();
		}
		catch (ParseException | NullPointerException e)
		{
			return null;
		}
	}
	
	@Override
	public int compareTo(Membership pMembership)
	{
		if (this.getStartDate().after(pMembership.getStartDate())) 
		{
			return -1;
		}
		else if (this.getStartDate().before(pMembership.getStartDate()))
		{
			return 1;
		}
		return 0;
	}
	
	@Override
	public boolean equals(Object pOther)
	{
		if (this == pOther)
		{
			return true;
		}
		if (pOther == null)
		{
			return false;
		}
		if (this.getClass() != pOther.getClass())
		{
			return false;
		}
		return this.aParty.equals(((Membership)pOther).aParty) &&
				this.aRiding.equals(((Membership)pOther).aRiding) &&
				this.aStartDate.equals(((Membership)pOther).aStartDate);
	}
	
	@Override
	public int hashCode()
	{
		return HASH_CONSTANT*(this.aParty.hashCode() + this.aRiding.hashCode() +
				this.aStartDate.hashCode());
	}
	
	@Override
	public String toString() 
	{
		return "Membership[Party="+this.aParty+", Riding="+this.aRiding+", sartDate="+this.aStartDate+", endDate="+this.aEndDate+"]";
	}
}
