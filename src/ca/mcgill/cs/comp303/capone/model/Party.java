package ca.mcgill.cs.comp303.capone.model;

import java.io.Serializable;

/**
 * This represents a political party in Canada. Immutable.
 * There should only even one instance of each unique Party object
 * within a given JVM.
 */
public final class Party implements Serializable
{
	private static final long serialVersionUID = -4922408178842898505L;
	private static final int HASH_CONSTANT = 101;
	
	private String aName;
	private String aShortName;
	
	/**
	 * Constructs a party if it doesn't exist.
	 * @param pName the name.
	 * @param pShortName the short name.
	 */
	public Party(String pName, String pShortName)
	{
		this.aName = pName;
		this.aShortName = pShortName;	
	}
	
	/**
	 * For example, "Conservative Party of Canada".
	 * @return party name
	 */
	public String getName() 
	{
		return this.aName;
	}

	/**
	 * For example, "Conservatives".
	 * @return party short name
	 */
	public String getShortName()
	{
		return this.aShortName;
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
		return this.aShortName.equals(((Party)pOther).aShortName);
	}
	
	@Override
	public int hashCode()
	{
		return HASH_CONSTANT*(this.aShortName.hashCode());
	}
	
	@Override
	public String toString() 
	{
		return "Party[name="+this.aName+", short name="+this.aShortName+"]";
	}
}
