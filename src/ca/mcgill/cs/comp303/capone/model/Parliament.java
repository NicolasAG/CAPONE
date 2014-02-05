package ca.mcgill.cs.comp303.capone.model;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * An object representing a graph of data about the Canadian parliament, including MPs, speeches, etc.
 * 
 * There should only ever be one instance of this class in any given JVM.
 * It is permissible to access this instance through a global variable.
 * 
 */
public final class Parliament
{
	private static final Logger LOGGER = Logger.getLogger(Parliament.class.getName());
	
	private HashMap<String, MP> aMPs = new HashMap<>();
	private HashMap<String, Party> aParties = new HashMap<>();
	private HashMap<Integer, Riding> aRidings = new HashMap<>();
	private ArrayList<IParliamentObserver> aObservers = new ArrayList<>();
	/**
	 * Enumeration of types that can be updated in this Parliament.
	 * @author Nicolas A.G.
	 */
	public enum UpdatedType
	{
		MP, PARTY, RIDING;
	}
	
	
	/**
	 * Protected constructor for the parliament.
	 * Can be access by the Capone class.
	 */
	protected Parliament() {}

	
	/**
	 * Returns the MP with this key, or null if no information is available.
	 * @param pKey the key of the specified MP.
	 * @return the specified MP.
	 */
	public MP getMP(String pKey)
	{
		return this.aMPs.get(pKey);
	}
	
	/**
	 * @return the list of all MPs in the graph.
	 */
	public List<MP> getMPs()
	{
		List<MP> list = new ArrayList<>(this.aMPs.values());
		return list;
	}
	
	
	/**
	 * Add the object MP to the graph if doesn't exist.
	 * If the MP already exists, update it but don't override it.
	 * (^this is done in UserProfile.update(MP pMP)).
	 * @param pMP the object to add.
	 */
	public void addMP(MP pMP)
	{
		this.aMPs.put(pMP.getPrimaryKey(), pMP);
		LOGGER.debug("UPDATED MP:" + pMP.getName());
		this.notifyObservers(pMP, UpdatedType.MP);
	}
	
	/**
	 * Remove the MP from the graph if it exists.
	 * @param pMP the object to remove.
	 */
	public void removeMP(MP pMP)
	{
		if (this.aMPs.containsKey(pMP.getPrimaryKey()))
		{
			this.aMPs.remove(pMP.getPrimaryKey());
			this.notifyObservers(pMP, UpdatedType.MP);
		}
	}
	
	
	/**
	 * Returns the specified party.
	 * @param pShortName the short name of the UNIQUE party. 
	 * @return the party.
	 */
	public Party getParty(String pShortName)
	{
		return this.aParties.get(pShortName);
	}
	
	/**
	 * @return the list of all Parties in the graph.
	 */
	public List<Party> getParties()
	{
		List<Party> list = new ArrayList<>(this.aParties.values());
		return list;
	}
	
	
	/**
	 * Adds the specified Party to the graph if it doesn't exist.
	 * If the Party already exists, it will be replaced.
	 * @param pShortName the short name party to be added.
	 * @param pName the name party to be added.
	 */
	public void addParty(String pShortName, String pName)
	{
		Party party = new Party(pName, pShortName);
		this.aParties.put(pShortName, party);
	}
	
	
	/**
	 * Remove the Party from the graph if it exists.
	 * @param pParty the Party to remove.
	 */
	public void removeParty(Party pParty)
	{
		if (this.aParties.containsKey(pParty.getShortName()))
		{
			this.aParties.remove(pParty.getShortName());
			this.notifyObservers(pParty, UpdatedType.PARTY);
		}
	}
	
	
	/**
	 * Returns the specified riding.
	 * @param pID the id of the riding.
	 * @return the riding.
	 */
	public Riding getRiding(int pID)
	{
		return this.aRidings.get(pID);
	}
	
	/**
	 * @return the list of all Ridings in the graph.
	 */
	public List<Riding> getRidings()
	{
		List<Riding> list = new ArrayList<>(this.aRidings.values());
		return list;
	}
	
	/**
	 * Adds the specified riding to the graph if it doesn't exist.
	 * If the Riding is already in the graph, it will be replaced.
	 * @param pID the id of riding
	 * @param pName the name of riding
	 * @param pProvince theProvince of riding
	 */
	public void addRiding(int pID, String pName, String pProvince)
	{
		Riding riding = new Riding(pID, pName, pProvince);
		this.aRidings.put(pID, riding);
	}
	
	
	/**
	 * Remove the Riding from the graph if it exists.
	 * @param pRiding the Riding to remove.
	 */
	public void removeRiding(Riding pRiding)
	{
		if (this.aRidings.containsKey(pRiding.getId()))
		{
			this.aRidings.remove(pRiding.getId());
			this.notifyObservers(pRiding, UpdatedType.RIDING);
		}
	}
	
	/**
	 * @return the list of all Observers.
	 */
	public List<IParliamentObserver> getObservers()
	{
		@SuppressWarnings("unchecked")
		List<IParliamentObserver> list = (List<IParliamentObserver>) this.aObservers.clone();
		return list;
	}
	
	/**
	 * Add the specified ParliamentObserver for this parliament if it doesn't exist.
	 * @param pObs the observer to add.
	 * @return true if the observer was added, false otherwise.
	 */
	public boolean addObserver(IParliamentObserver pObs)
	{
		if ( !this.aObservers.contains(pObs) )
		{
			this.aObservers.add(pObs);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Remove the specified observer for this parliament if it exists.
	 * @param pObs the observer to delete.
	 */
	public void removeObserver(IParliamentObserver pObs)
	{
		if (this.aObservers.contains(pObs))
		{
			this.aObservers.remove(pObs);
		}
	}
	
	
	/**
	 * Notify all observers of a change in a specific class.
	 * Using the PULL model of the observer design pattern.
	 * @param pObject the object that is modified.
	 * @param pType the type of the modified object.
	 */
	public void notifyObservers(Object pObject, Enum<UpdatedType> pType)
	{
		if (pType.equals(UpdatedType.MP))
		{
			for (IParliamentObserver observer : this.aObservers)
			{
				observer.update((MP)pObject);
			}
		}
//		else if (pType.equals(UpdatedType.PARTY))
//		{
//			for (IParliamentObserver pobs : this.aObservers)
//			{
//				pobs.update((Party)pObject);
//			}
//		}
//		else if (pType.equals(UpdatedType.RIDING))
//		{
//			for (IParliamentObserver pobs : this.aObservers)
//			{
//				pobs.update((Riding)pObject);
//			}
//		}
	}
	
	
	@Override
	public String toString()
	{
		return "Parliament";
	}
}
