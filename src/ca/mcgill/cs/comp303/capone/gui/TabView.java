package ca.mcgill.cs.comp303.capone.gui;

import java.awt.GridLayout;
import java.util.ResourceBundle;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import ca.mcgill.cs.comp303.capone.model.UserProfile;

/**
 * The 3 tabs for the different views (MP, Profile, Recommendation) in the app.
 * @author Nicolas A.G.
 */
public class TabView extends JPanel
{
	private static final long serialVersionUID = 7229821238598380114L;
	private JTabbedPane aTabView;
	
	/**
	 * Constructor.
	 * @param pMessages - I18N.
	 */
	public TabView(ResourceBundle pMessages)
	{
		super(new GridLayout(1, 1));
		this.aTabView = new JTabbedPane();
		
		UserProfile current = UserProfile.getInstance();
		
		JComponent mpView = new MPView(pMessages);
		current.addView(mpView);
		this.aTabView.addTab(pMessages.getString("MP"), null, mpView, pMessages.getString("TipTabMP"));
		
		JComponent profileView = new ProfileView(pMessages);
		current.addView(profileView);
		this.aTabView.addTab(pMessages.getString("Profile"), null, profileView, pMessages.getString("TipTabProfile"));
		
		JComponent recommendationView = new RecommendationView(pMessages);
		current.addView(recommendationView);
		this.aTabView.addTab(pMessages.getString("Recommendations"), null, recommendationView, pMessages.getString("TipTabRecommendations"));
		
		this.aTabView.setTabPlacement(SwingConstants.TOP);
		add(this.aTabView);
	}
}