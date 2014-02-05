package ca.mcgill.cs.comp303.capone.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Map;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 
 * @author Andrej
 * 
 * This class recommends speeches to a User Profile according to his MP list and his expressions list.
 * We give a bonus of 1 each time an expression is in the content of a speech.
 * We give a bonus of 3 each time an expression is in the header 2 of a speech.
 * We decided to not check Header1 as Header1 was very vague, and not very representative of the speech's content.
 *
 */
public class ContentBasedRecommender implements ISpeechRecommender
{
	private static final Logger LOGGER = Logger.getLogger(ContentBasedRecommender.class.getName());
	private static final int KEYWORD_IN_SPEECH = 1;
	private static final int KEYWORD_IN_HEADER = 3;
	
	@Override
	public ArrayList<Speech> recommend(UserProfile pUser, List<MP> pParliamentMPs)
	{	
        int i;
        int count = 0;
        Hashtable<Speech, Integer> searchedSpeeches = new Hashtable<>();      
       
        for(i = 0; i<pUser.getMPs().size(); i++)        
        {     
        	// parses through each speech of the i'th MP from the graph,
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
                searchedSpeeches.put(speech, count); //add the speech
                count = 0; //reset counter for next speech.
            }
        }    
        
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
