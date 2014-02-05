package ca.mcgill.cs.comp303.capone.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Nicolas A.G.
 *
 */
public interface ISpeechRecommender
{
	/**
	 * @param  pUser the user that needs recommendations.
	 * @param pParliamentMPs the list of all loaded MPs in the graph.
	 * @return a ranked ArrayList of Speeches according to some recommendation strategies. 
	 */
	ArrayList<Speech> recommend(UserProfile pUser, List<MP> pParliamentMPs);
}
