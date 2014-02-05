package ca.mcgill.cs.comp303.capone.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @author Andrej
 * This class recommends speeches to a User Profile according to MPs similar to MPs in his list,
 * and according to his expressions list.
 * We give a bonus of 1 each time an expression is in the content of a speech.
 * We give a bonus of 3 each time an expression is in the header 2 of a speech.
 * We decided to not check Header1 as Header1 was very vague, and not very representative of the speech's content.
 * If an MP is in the same party as an MP in the user's list, we give a bonus of 3.
 * If an MP is in the same province as an MP in the user's list, we give a bonus of 2.
 * If an MP is in the same family as an MP in the user's list, we give a bonus of 5.
 *
 */
public class SimilarityBasedRecommender implements ISpeechRecommender
{
	private static final Logger LOGGER = Logger.getLogger(ContentBasedRecommender.class.getName());

	private static final int BONUS_SAME_PARTY = 3;
	private static final int BONUS_SAME_PROVINCE = 2;
	private static final int BONUS_SAME_FAMILY = 5;
	private static final int KEYWORD_IN_SPEECH = 1;
	private static final int KEYWORD_IN_HEADER = 3;
	
	@Override
	public ArrayList<Speech> recommend(UserProfile pUser, List<MP> pParliamentMPs)
	{
		int i;
		int j;
		int count = 0;
		int highestSimilarityBoost = 0;
		int currentSimilarityBoost = 0;
		Hashtable<Speech, Integer> searchedSpeeches = new Hashtable<>();
		
		//for all MPs in the graph,
		for(i = 0; i < pParliamentMPs.size(); i++)
		{
			//if the MP is NOT in the User's list,
			if(!pUser.getMPs().contains(pParliamentMPs.get(i)))
			{
				//for all MPs in the user's list,
				for (j = 0; j < pUser.getMPs().size(); j++)
				{
					// compares MPs from the graph to MPs from user List
					// if both MPs are from the same family, the priority is boosted
					if(pParliamentMPs.get(i).getFamilyName().equals(pUser.getMPs().get(j).getFamilyName()))
					{
						currentSimilarityBoost += BONUS_SAME_FAMILY;
					}
					// checks in case either of the current Memberships are null
					if(pParliamentMPs.get(i).getCurrentMembership() == null || pUser.getMPs().get(j).getCurrentMembership() == null)
					{
						// chooses the most generous priority boost
						if(currentSimilarityBoost > highestSimilarityBoost)
						{
							highestSimilarityBoost = currentSimilarityBoost;										
						}
						currentSimilarityBoost = 0; //reset the priority for the next MP in the user's list.
						continue; //skip the following comparisons.
					}
					// if both MPs are currently in the same party, the priority is boosted 
					if(pParliamentMPs.get(i).getCurrentMembership().getParty().equals(pUser.getMPs().get(j).getCurrentMembership().getParty()))
					{
						currentSimilarityBoost += BONUS_SAME_PARTY;
					}
					// if both MPs are currently in the same province, the priority is boosted
					String parlMPProvince = pParliamentMPs.get(i).getCurrentMembership().getRiding().getProvince();
					String userMPProvince = pUser.getMPs().get(j).getCurrentMembership().getRiding().getProvince();
					if(parlMPProvince.equals(userMPProvince))
					{
						currentSimilarityBoost += BONUS_SAME_PROVINCE;
					}					
					// chooses the most generous priority boost
					if(currentSimilarityBoost > highestSimilarityBoost)
					{
						highestSimilarityBoost = currentSimilarityBoost;										
					}
					currentSimilarityBoost = 0; //reset the priority for the next MP in the user's list.
				}//end of for each MP in the user's list.
			}//end of if MP not in the user's list.
			
			// parses through each speech of the MP from the graph,
			ArrayList<Speech> speeches = new ArrayList<>(pParliamentMPs.get(i).getSpeeches().values());
			for (Speech speech : speeches)
            {
				if (UserProfile.getInstance().getReadSpeeches().contains(speech.hashCode()))
				{
					LOGGER.debug("Speech already read");
					continue;
				}
				//check for all expressions,
                for(String expression : pUser.getExpressions())
                {
                	//if the expression is in the content.
                	if(speech.getContent().toLowerCase().contains(expression.toLowerCase()))
                	{
                		count+=KEYWORD_IN_SPEECH;
                	}
                	//if the expression is in the Header2.
                	if(speech.getHeader2().toLowerCase().contains(expression.toLowerCase()))
                	{
                		count+=KEYWORD_IN_HEADER;
                	}
                }
                // adds the speech to the searchedSpeeches hash table with the speech content/title similarity and the MP similarity boost.
                searchedSpeeches.put(speech, count + highestSimilarityBoost);                
    	        count = 0; //reset counter for the next speech.
            }
			highestSimilarityBoost = 0; //reset counter for next MP in the graph.
		}//end of for each MP in the graph.
		
		// sorts the searchedSpeeches hash table in descending order:
		ArrayList<Map.Entry<Speech, Integer>> sortedList = new ArrayList<>(searchedSpeeches.entrySet());
        Collections.sort(sortedList, new Comparator<Map.Entry<Speech, Integer>>()
        {
            @Override
			public int compare(Map.Entry<Speech, Integer> pO1, Map.Entry<Speech, Integer> pO2)
            {
                 return pO2.getValue().compareTo(pO1.getValue());
            }
        });
        
        // creates an array list of SPEECHES from the sorted array list of speech/integer ENTRIES.
        ArrayList<Speech> returnList = new ArrayList<>();
        for(Map.Entry<Speech, Integer> entry : sortedList)
        {
        	returnList.add(entry.getKey());
        }
		return returnList;
	}
}
