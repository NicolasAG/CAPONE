package ca.mcgill.cs.comp303.capone.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import ca.mcgill.cs.comp303.capone.Driver;
import ca.mcgill.cs.comp303.capone.loaders.ParliamentLoader;
import ca.mcgill.cs.comp303.capone.loaders.PropertyLoader;
import ca.mcgill.cs.comp303.capone.loaders.op.OpenParliamentFileLoader;
import ca.mcgill.cs.comp303.capone.model.BinaryFormat;
import ca.mcgill.cs.comp303.capone.model.UserProfile;

/**
 * The menu bar of the Capone.
 * @author Jeff
 */
public class DataMenu extends JMenu
{
	private static final long serialVersionUID = 8691673449569767267L;

	/**
	 * Constructor.
	 * @param pMessages - I18N.
	 */
	public DataMenu(ResourceBundle pMessages)
	{
		init(pMessages.getString("Data"), null);
		JMenuItem loadFromDisk = new JMenuItem(pMessages.getString("LoadDisk"));
		loadFromDisk.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				try
				{
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					
					PropertyLoader.getInstance().load();
					
					fc.setSelectedFile(new File(PropertyLoader.getInstance().getProperty("FilePath")));
					ParliamentLoader loader = new OpenParliamentFileLoader();
					int returnVal = fc.showSaveDialog(null);
					//set default to one if exist in property file
					
					if (returnVal == JFileChooser.APPROVE_OPTION) 
					{
						try
						{
							PropertyLoader.getInstance().setProperty("FilePath", fc.getSelectedFile().getAbsolutePath());
							PropertyLoader.getInstance().save();
							
							loader.loadFromDisk(PropertyLoader.getInstance().getProperty("FilePath"));
							UserProfile.getInstance().load(new BinaryFormat());
							UserProfile.getInstance().triggerSpeechRecommendation();
							
							Driver.getMainFrameInstance().revalidate();
							Driver.getMainFrameInstance().repaint();
						}
						catch (IOException e)
						{
							System.out.println("Problem saving property");
						}
					
					} 
					else if (returnVal == JFileChooser.CANCEL_OPTION) 
					{
						//handle cancel
					}
				}
				catch (IOException e)
				{
					//Problem loading property. 
				}
			}
		});
		
		JMenuItem loadFromWeb = new JMenuItem(pMessages.getString("LoadWeb"));
		loadFromWeb.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				try
				{
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					
					PropertyLoader.getInstance().load();
					
					fc.setSelectedFile(new File(PropertyLoader.getInstance().getProperty("FilePath")));
					ParliamentLoader loader = new OpenParliamentFileLoader();
					
					int returnVal = fc.showSaveDialog(null);
					//set default to one if exist in property file
					
					if (returnVal == JFileChooser.APPROVE_OPTION) 
					{
						PropertyLoader.getInstance().setProperty("FilePath", fc.getSelectedFile().getAbsolutePath());
						PropertyLoader.getInstance().save();
						loader.downloadFromWeb(PropertyLoader.getInstance().getProperty("FilePath"));
						loader.loadFromDisk(PropertyLoader.getInstance().getProperty("FilePath"));
						UserProfile.getInstance().load(new BinaryFormat());
						UserProfile.getInstance().triggerSpeechRecommendation();
						
						Driver.getMainFrameInstance().revalidate();
						Driver.getMainFrameInstance().repaint();

					} 
					else if (returnVal == JFileChooser.CANCEL_OPTION) 
					{
						//handle cancel
					}
				}
				catch (IOException e)
				{
					//Problem loading property.
					//Problem saving property.
				}
			}
		});
		
		JCheckBoxMenuItem autoLoad = new JCheckBoxMenuItem(pMessages.getString("AutoLoad"));
		autoLoad.setState(Boolean.valueOf(PropertyLoader.getInstance().getProperty("AutoLoad")));
		autoLoad.addActionListener(new ActionListener()
		{		
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				try
				{
					PropertyLoader.getInstance().load();
					boolean loadState = Boolean.valueOf(PropertyLoader.getInstance().getProperty("AutoLoad")); 
					PropertyLoader.getInstance().setProperty("AutoLoad", String.valueOf(!loadState));
					PropertyLoader.getInstance().save();
				}
				catch (IOException e)
				{
					//Problem loading property.
					//Problem saving property.
				}
				
			}
		});
		
		JMenuItem updateSpeechs = new JMenuItem(pMessages.getString("UpdateSpeeches"));
		updateSpeechs.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				try
				{
					PropertyLoader.getInstance().load();
					ParliamentLoader loader = new OpenParliamentFileLoader();
					loader.updateSpeeches(PropertyLoader.getInstance().getProperty("FilePath"));
					UserProfile.getInstance().triggerSpeechRecommendation();

				}
				catch (IOException ef)
				{
					//Problem loading property.
				}
			}
		});
		
		this.add(loadFromDisk);
		this.add(loadFromWeb);
		this.add(autoLoad);
		this.add(updateSpeechs);
		
	}

}
