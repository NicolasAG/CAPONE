package ca.mcgill.cs.comp303.capone.gui;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import ca.mcgill.cs.comp303.capone.model.BinaryFormat;
import ca.mcgill.cs.comp303.capone.model.UserProfile;

/**
 * This is the main frame for the application.
 * Other panels will be added here.
 * @author Jeffrey Ng
 */
public class AppFrame extends JFrame
{
	protected static final String HOME_DIR_FILE =  System.getProperty("user.home") + File.separator;
	protected static final Logger LOGGER = Logger.getLogger(AppFrame.class.getName());

	private static final long serialVersionUID = 5340794078045132452L;
	
	private static final int WIDTH_SIZE = 1300;
	private static final int HEIGHT_SIZE = 750;
	private static final int MIN_SIZE_WIDTH = 1000;
	private static final int MIN_SIZE_HEIGHT = 500;
	
	private static JPanel aMainPanel;
	
	/**
	 * Main window frame for Capone Application.
	 * @param pMessages - I18N.
	 */
	public AppFrame(ResourceBundle pMessages)
	{
		aMainPanel = new JPanel(new CardLayout());
		this.add(aMainPanel);
		this.setSize(WIDTH_SIZE, HEIGHT_SIZE);
		this.setMinimumSize(new Dimension(MIN_SIZE_WIDTH, MIN_SIZE_HEIGHT));
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();
		
		int xPos = (dim.width / 2) - (this.getWidth() / 2);
		int yPos = (dim.height / 2) - (this.getHeight() / 2);
		
		this.setJMenuBar(new MenuBar(pMessages)); //add the menu bar to the frame.
		this.add(new TabView(pMessages)); //add the tabs to the frame.
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle(pMessages.getString("Capone"));
		this.setResizable(true);
		this.setLocation(xPos, yPos);
		this.setVisible(true);
		
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent pE) 
		    {
		        //Save the user profile when the window is closing.
		    	UserProfile.getInstance().save(new BinaryFormat(), HOME_DIR_FILE + UserProfile.getInstance().getName() + ".dat");
		    	LOGGER.info("Window closing");
		    }
		});
		
	}
	

}
