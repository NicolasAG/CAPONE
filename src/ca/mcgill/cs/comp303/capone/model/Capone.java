package ca.mcgill.cs.comp303.capone.model;

/**
 * An object representing the "head" of the entire application.
 * Uses the singleton design pattern.
 * @author Nicolas A.G.
 */
public final class Capone
{
	
	private static final Capone INSTANCE = new Capone();
	private static final Parliament PARLIAMENT = new Parliament();
	
	
	/**
	 * Support for the singleton design pattern.
	 * Private constructor.
	 */
	private Capone() {}
	
	
	/**
	 * Support for the singleton design pattern.
	 * @return The model instance.
	 */
	public static Capone getInstance()
	{
		return INSTANCE;
	}
	
	
	/**
	 * Give access to the Parliament instance.
	 * @return The Parliament instance.
	 */
	public static Parliament getParliament()
	{
		return PARLIAMENT;
	}
	
	
	@Override
	public String toString()
	{
		return "Capone";
	}
}
