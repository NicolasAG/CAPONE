package ca.mcgill.cs.comp303.capone.model;

import java.io.Serializable;

/**
 * This class represents an electoral district in Canada, a geographical constituency 
 * upon which a Member of Parliament to the Canadian House of Commons represents. 
 * [Reference: Wikipedia (http://en.wikipedia.org/wiki/Electoral_district_%28Canada%29)]
 * 
 * Immutable.
 * There should only even one instance of each unique Riding object
 * within a given JVM.
 */
public final class Riding implements Serializable
{
	private static final long serialVersionUID = -8035475465461061130L;
	private static final int HASH_CONSTANT = 997;
	
	//The unique ID for this riding, as obtained from OpenParliament. E.g., 4700
	private int aID;
	private String aName;
	private String aProvince;
	
	/**
	 * Constructs a riding if it doesn't exist.
	 * @param pID the id.
	 * @param pName the name.
	 * @param pProvince the province.
	 */
	public Riding(int pID, String pName, String pProvince)
	{
		this.aID = pID;
		this.aName = pName;
		this.aProvince = pProvince;
		
	}
	
	/**
	 * The id of the riding.
	 * @return id of riding
	 */
	public int getId()
	{
		return this.aID;
	}

	/**
	 * The official name of the riding, e.g., "Regina\u2014Lumsden\u2014Lake Centre".
	 *@return official name of riding
	 */
	public String getName()
	{
		return this.aName; 
	}

	/**
	 * The province code, e.g, SK.
	 * @return province of riding
	 */
	public String getProvince()
	{
		return this.aProvince;
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
		return this.aID == (((Riding)pOther).aID) &&
				this.aName == (((Riding)pOther).aName) &&
				this.aProvince == (((Riding)pOther).aProvince);
	}
	
	@Override
	public int hashCode()
	{
		return HASH_CONSTANT * (this.aID + this.aName.hashCode() + this.aProvince.hashCode());
	}
	
	@Override
	public String toString() 
	{
		return "Riding[ID="+this.aID+", Name="+this.aName+", Province="+this.aProvince+"]";
	}
}
