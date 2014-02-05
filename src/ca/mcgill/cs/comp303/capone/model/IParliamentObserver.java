package ca.mcgill.cs.comp303.capone.model;

/**
 * Interface for the observer design pattern.
 * @author Nicolas A.G.
 */
public interface IParliamentObserver
{
	/**
	 * Update the specified MP.
	 * @param pMP the mp to be updated.
	 */
	void update(MP pMP);
	
//	/**
//	 * Update the specified Party.
//	 * @param pParty the Party to be updated.
//	 */
//	void update(Party pParty);
//	
//	/**
//	 * Update the specified Riding.
//	 * @param pRiding the Riding to be updated.
//	 */
//	void update(Riding pRiding);
}
