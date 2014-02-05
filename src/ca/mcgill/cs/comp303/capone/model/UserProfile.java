package ca.mcgill.cs.comp303.capone.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;

import org.apache.log4j.Logger;

import ca.mcgill.cs.comp303.capone.gui.MPView;
import ca.mcgill.cs.comp303.capone.gui.ProfileView;
import ca.mcgill.cs.comp303.capone.gui.RecommendationView;

/**
 * @author Jeffrey Ng, Nicolas A.G.
 * A user profile will represent the personal interests of a user of the Capone application.
 */
public final class UserProfile implements IParliamentObserver, Serializable
{
	private static transient UserProfile instance = null;
	private static final transient long serialVersionUID = -7546866148733864298L;
	private static final transient Logger LOGGER = Logger.getLogger(UserProfile.class.getName());
	private static final transient int HASH_CONSTANT = 101;
	private static final transient String DEFAULT_NAME = "current";

	
	private String aName;
	private HashMap<String, MP> aMPs = new HashMap<>();	
	private ArrayList<String> aExpressions = new ArrayList<>();
	private transient ArrayList<JComponent> aViews = new ArrayList<>();
	
	private ArrayList<Integer> aReadSpeeches = new ArrayList<>();
	private transient ArrayList<Speech> aRecommendedSpeeches = new ArrayList<>();
	
	private RecommenderStrategy aUserRecommenderStrategy;
	
	/**
	 * @author jeffrey
	 * Enum for recommendation strategy.
	 */
	public enum RecommenderStrategy
	{
		CONTENT, SIMILARITY;
	}
	
	/**
	 * Constructor for a User Profile.
	 * @param pName The name of this user.
	 */
	private UserProfile(String pName)
	{
		this.aName = pName;
		this.aUserRecommenderStrategy = RecommenderStrategy.CONTENT;
	}
	
	
	/**
	 * 
	 * @param pName - Name of new user profile
	 * @return instance of user profile.
	 */
	public static synchronized UserProfile getInstance(String pName)
	{
		if (instance == null)
		{
			instance = new UserProfile(pName);
		}
		return instance;
	}
	
	/**
	 * 
	 * @return Instance of user profile
	 */
	public static synchronized UserProfile getInstance()
	{
		if (instance == null)
		{
			instance = new UserProfile(DEFAULT_NAME);
		}
		return instance;
	}
	
	/**
	 * @return the name of this user.
	 */
	public String getName()
	{
		return this.aName;
	}
	
	/**
	 *  Get Specific MP.
	 * @param pKey - Key of MP.
	 * @return MP of key.
	 */
	public MP getMP(String pKey) 
	{		
		return 	this.aMPs.get(pKey);
	}
	
	/**
	 * Add MP to map.
	 * @param pMP - MP object
	 * @return true if added.
	 */
	public boolean addMP(MP pMP)
	{
		if (!(this.aMPs.containsKey(pMP.getPrimaryKey())))
		{ //if the specified MP is not in this user list, add it.
			this.aMPs.put(pMP.getPrimaryKey(), Capone.getParliament().getMP(pMP.getPrimaryKey()));
			this.triggerSpeechRecommendation();
			return true;
		}
		return false;
	}
	
	/**
	 * Remove MP from map.
	 * @param pMP - the MP.
	 */
	public void removeMP(MP pMP) 
	{
		if (this.aMPs.containsKey(pMP.getPrimaryKey()))
		{ //if the specified MP is in this user list, remove it.
			this.aMPs.remove(pMP.getPrimaryKey());
			this.triggerSpeechRecommendation();
		}
	}
	
	/**
	 * Get immutable List of MPs.
	 * @return List of MPs.
	 */
	public List<MP> getMPs() 
	{
		return new ArrayList<>(this.aMPs.values());
	}
	
	/**
	 * Get immutable list of Expressions.
	 * @return cloned ArrayList of Expressions.
	 */
	@SuppressWarnings("unchecked")
	public List<String> getExpressions()
	{
	    return (List<String>) this.aExpressions.clone();
	}
	
	/**
	 * Add Expression to list.
	 * @param pExpression - Expression.
	 * @return true if the expression is added.
	 */
	public boolean addExpression(String pExpression)
	{
		if (!this.aExpressions.contains(pExpression))
		{
			this.aExpressions.add(pExpression);
			this.triggerSpeechRecommendation();
			return true;
		}
		return false;
	}
	
	/**
	 * Removes expression.
	 * @param pExpression - expression to be removed.
	 */
	public void removeExpression(String pExpression)
	{
		if (this.aExpressions.contains(pExpression)) 
		{
			this.aExpressions.remove(pExpression);
			this.triggerSpeechRecommendation();
		}
	}
	
	/**
	 * List of read speeches.
	 * @return read speeches
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> getReadSpeeches()
	{
		return (List<Integer>) this.aReadSpeeches.clone();
	}
	
	/**
	 * Add speech as read.
	 * @param pSpeech - speech
	 * @return true if speech H1 is added.
	 */
	public boolean addReadSpeech(Speech pSpeech)
	{
		if (!this.aReadSpeeches.contains(pSpeech.hashCode()))
		{
			this.aReadSpeeches.add(pSpeech.hashCode());
			return true;
		}
		return false;
	}
	
	/**
	 * reset read speeches.
	 */
	public void resetReadSpeeches()
	{
		this.aReadSpeeches = new ArrayList<>();
		this.triggerSpeechRecommendation();
	}
	
	/**
	 * Triggers speech recommendation.
	 */
	public void triggerSpeechRecommendation()
	{
		this.aRecommendedSpeeches = this.recommend(this.getRecommenderStrategy());
		this.refresh();
	}
	
	/**
	 * 
	 * @return recommended speeches for user.
	 */
	@SuppressWarnings("unchecked")
	public List<Speech> getRecommendedSpeeches()
	{
		return (List<Speech>) this.aRecommendedSpeeches.clone();
	}
	
	/**
	 * Add a specific view for this UserProfile.
	 * @param pView - the view to add.
	 * @return true if the view was added.
	 */
	public boolean addView(JComponent pView)
	{
		if (!this.aViews.contains(pView))
		{
			this.aViews.add(pView);
			return true;
		}
		return false;
	}
	
	/**
	 * Remove views.
	 * @param pView - the view to remove.
	 */
	public void removeView(JComponent pView)
	{
		if (this.aViews.contains(pView))
		{
			this.aViews.remove(pView);
		}
	}
	
	/**
	 * Returns current users recommendation strategy.
	 * @return current users recommendation strategy
	 */
	public RecommenderStrategy getRecommenderStrategy()
	{
		return this.aUserRecommenderStrategy;
	}
	
	/**
	 * Set user recommender strategy.
	 * @param pRecommenderStrategy - Recommender strategy to set.
	 */
	public void setRecommenderStrategy(RecommenderStrategy pRecommenderStrategy)
	{
		this.aUserRecommenderStrategy = pRecommenderStrategy;
		this.triggerSpeechRecommendation();
	}
	
	/**
	 * Repaint every views of this user profile.
	 */
	public void refresh()
	{
		for (JComponent v : this.aViews)
		{
			v.getTopLevelAncestor().revalidate();
			v.getTopLevelAncestor().repaint();
			
			v.revalidate();
			v.repaint();
			
			if (v.getClass().equals(MPView.class))
			{
				((MPView) v).trigerDataChanges();
			}
			else if (v.getClass().equals(ProfileView.class))
			{
				((ProfileView) v).trigerDataChanges();
			}
			else if (v.getClass().equals(RecommendationView.class))
			{
				((RecommendationView) v).trigerDataChanges();
			}
			//System.out.println("repainted:"+v.getClass());
		}
	}
	
	/**
	 * Save this user in a specified format.
	 * @param pFormat - the specified format.
	 * @param pFilePath The location to save user profile.
	 */
	public void save(IFormat pFormat, String pFilePath)
	{
		String format = pFormat.getClass().getName();
		if (pFormat instanceof BinaryFormat)
		{
			format = "Binary Format.";
		}
		else if (pFormat instanceof JSONFormat)
		{
			format = "JSON Format.";
		}
		try
		{
			pFormat.saveUserProfile(this, pFilePath);
			LOGGER.info("SAVED USER IN "+format);
		}
		catch (IOException e)
		{
			LOGGER.warn("IO EXCEPTION: CANNOT SAVE USER TO A FILE.");
			e.printStackTrace();
		}
	}
	
	/**
	 * Load specified user profile to a specific file format.
	 * @param pFormat Format of user profile to be loaded.
	 */
	public void load(IFormat pFormat)
	{
		String format = pFormat.getClass().getName();
		if (pFormat instanceof BinaryFormat)
		{
			format = "Binary Format.";
		}
		else if (pFormat instanceof JSONFormat)
		{
			format = "JSON Format.";
		}

		UserProfile temp = pFormat.loadUserProfile();
		this.aName = temp.getName();
		this.aExpressions = new ArrayList<>(temp.getExpressions());
		this.aReadSpeeches = new ArrayList<>(temp.getReadSpeeches());
		this.aUserRecommenderStrategy = temp.getRecommenderStrategy();
		
		this.aMPs = new HashMap<>();
		for (MP m :temp.getMPs())
		{
			this.addMP(m);
		}
			
		LOGGER.info("LOAD USER IN "+format);

	}
	
	/**
	 * Recommend speeches for this user.
	 * @param pRecommender the way of recommending speeches.
	 * @return the list of recommended speeches.
	 */
	public ArrayList<Speech> recommend(RecommenderStrategy pRecommender)
	{
		ISpeechRecommender tempISpeechRecommender;
		if (pRecommender == RecommenderStrategy.CONTENT)
		{
			tempISpeechRecommender = new ContentBasedRecommender();
		} 
		else 
		{
			tempISpeechRecommender = new SimilarityBasedRecommender();
		}
		if (Capone.getParliament().getMPs().isEmpty())
		{
			return new ArrayList<>();
		}
		return tempISpeechRecommender.recommend(this, Capone.getParliament().getMPs());
		
	}

	@Override
	public void update(MP pMP)
	{
		if (this.getMPs().contains(pMP))
		{ //if the updated mp is in the list of this user, then update it.
			if (!Capone.getParliament().getMPs().contains(pMP))
			{ //if the updated mp is not anymore in the Parliament graph, then delete it.
				this.removeMP(pMP);
			}
			else
			{ //if the updated mp is in the parliament graph, then update the MP for this user.
				MP toUpdate = this.aMPs.get(pMP.getPrimaryKey());
				if (toUpdate.getFamilyName()==null || !toUpdate.getFamilyName().equals(pMP.getFamilyName()))
				{
					toUpdate.setFamilyName(pMP.getFamilyName());
				}
				if (toUpdate.getGivenName()==null || !toUpdate.getGivenName().equals(pMP.getGivenName()))
				{
					toUpdate.setGivenName(pMP.getGivenName());
				}
				if (toUpdate.getName()==null || !toUpdate.getName().equals(pMP.getName()))
				{
					toUpdate.setName(pMP.getName());
				}
				if (toUpdate.getEmail()==null || !toUpdate.getEmail().equals(pMP.getEmail()))
				{
					toUpdate.setEmail(pMP.getEmail());
				}
				if (toUpdate.getPhoneNumber()==null || !toUpdate.getPhoneNumber().equals(pMP.getPhoneNumber()))
				{
					toUpdate.setPhoneNumber(pMP.getPhoneNumber());
				}
				if (toUpdate.getImage()==null || !toUpdate.getImage().equals(pMP.getImage()))
				{
					toUpdate.setImage(pMP.getImage());
				}
				if (toUpdate.getMemberships()==null || !toUpdate.getMemberships().equals(pMP.getMemberships()))
				{ //this will keep old memberships EVEN if they are not in the new MP. 
					for (Membership m : pMP.getMemberships())
					{ //for all new memberships,
						if (toUpdate.getMemberships().contains(m))
						{ //if the membership is already here, update it.
							toUpdate.getMemberships().remove(m); //remove the old one.
							toUpdate.getMemberships().add(m); //add the new one.
						}
						else
						{ //if the membership is not here, add it.
							toUpdate.addMembership(m);
						}
					}
				}
				if (toUpdate.getSpeeches()==null || !toUpdate.getSpeeches().equals(pMP.getSpeeches()))
				{ //this will keep old speeches EVEN if they are not in the new MP.
					//add all new speeches and update the speeches already here).
					toUpdate.getSpeeches().putAll(pMP.getSpeeches());
				}
			}
		}
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
		return this.aName == ((UserProfile)pOther).aName;
	}
	
	@Override
	public int hashCode()
	{
		return HASH_CONSTANT*this.aName.hashCode();
	}
	
	@Override
	public String toString()
	{
		return "UserProfile[Name="+this.aName+"]";
	}
	
}
