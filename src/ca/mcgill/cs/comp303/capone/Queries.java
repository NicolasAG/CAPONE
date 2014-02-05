package ca.mcgill.cs.comp303.capone;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

import ca.mcgill.cs.comp303.capone.model.Capone;
import ca.mcgill.cs.comp303.capone.model.MP;
import ca.mcgill.cs.comp303.capone.model.Membership;
import ca.mcgill.cs.comp303.capone.model.Party;
import ca.mcgill.cs.comp303.capone.model.Speech;

/**
 * Compute the results of two queries on the data.
 */
public final class Queries
{
	private static final int HOURS = 24;
	private static final int MINUTES = 60;
	private static final int SECONDS = 60;
	private static final int MILLISECONDS = 1000;
	
	private Queries(){}
	
	/**
	 * @return The email address of the MP who has served the longest 
	 * in parliament, in terms of total number of days in any membership.
	 * If there are ties, return all ties in any order.
	 */
	public static Set<String> q1LongestServingMPs()
	{
		ArrayList<String> emailAddresses = new ArrayList<>();
		
		long term = 0;
		long diffDays = 0;
		long longestOffice = 0;

		for (MP m : Capone.getParliament().getMPs())
		{
			for (Membership mem : m.getMemberships())
			{
				Date startdate = mem.getStartDate();
				Date enddate = mem.getEndDate();
				
				if(startdate == null)
				{
					startdate = new Date();
				}
				if(enddate == null)
				{
					enddate = new Date();
				}
				
				long diff = enddate.getTime() - startdate.getTime();
				diffDays = diff / (HOURS * MINUTES * SECONDS * MILLISECONDS);
				
				term += diffDays;				
			}
			if(term > longestOffice)
			{
				longestOffice = term;
				emailAddresses.clear();
				emailAddresses.add(m.getEmail());		
			}
			else if(term == longestOffice)
			{
				emailAddresses.add(m.getEmail());
			}
			
			term = 0;
			diffDays = 0;
		}
		
        
		Set<String> set = new HashSet<>(emailAddresses);
		return set;
	}
	
	/**
	 * @return The email address of the MP who has served in the largest
	 * number of different political parties. If there are ties, return 
	 * all ties in any order.
	 */
	public static Set<String> q2LargestNumberOfParties()
	{
		ArrayList<String> emailAddresses = new ArrayList<>();
		ArrayList<Party> knownParties = new ArrayList<>();
		
		int largest = 0;
		int current = 0;
		
		for (MP m : Capone.getParliament().getMPs())
		{
			for (Membership mem : m.getMemberships())
			{
				if(!knownParties.contains(mem.getParty()))
				{
					knownParties.add(mem.getParty());
					current++;
				}
			}
			if(current > largest)
			{
				largest = current;
				emailAddresses.clear();
				emailAddresses.add(m.getEmail());	
			}
			else if(current == largest)
			{
				emailAddresses.add(m.getEmail());
			}
			current = 0;
			knownParties.clear();
		}
		
		Set<String> set = new HashSet<>(emailAddresses);
		return set;	
		}
	
	/**
	 * @return The number of speeches in which Thomas Mulcair
	 * uses the word "Conservative" (not the plural variant, 
	 * but case-insensitive).
	 */
	public static int q3NumberOfConservativeWordUsage()
	{
		int count = 0;
		int numberOfSpeeches = 0;
		String mpKey = "thomas.mulcair@parl.gc.ca";

		
		MP m = Capone.getParliament().getMP(mpKey);

		String finding = "conservative";	
		
		if(m == null)
		{
			return numberOfSpeeches;
		}
		for (Speech speech : new ArrayList<>(m.getSpeeches().values()))
		{
			String content = speech.getContent();
			for(String word : content.split("[^A-Za-z]"))
			{
				if(word.equalsIgnoreCase(finding))
				{
					count++;
				}
			}
			if(count > 0)
			{
				numberOfSpeeches++;
			}
			count = 0;
		}
		return numberOfSpeeches;
	}
}
