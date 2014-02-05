package ca.mcgill.cs.comp303.capone.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import ca.mcgill.cs.comp303.capone.loaders.PropertyLoader;
import ca.mcgill.cs.comp303.capone.model.JSONFormat;
import ca.mcgill.cs.comp303.capone.model.UserProfile;

/**
 * The profile menu.
 * @author Jeff.
 */
public class ProfileMenu extends JMenu
{
	private static final long serialVersionUID = -3337976290056962369L;

	private ResourceBundle aMessages = null;
	
	/**
	 * Constructor.
	 * @param pMessages - I18N.
	 */
	public ProfileMenu(ResourceBundle pMessages)
	{
		this.aMessages = pMessages;
		
		init(this.aMessages.getString("Profile"), null);
		JMenuItem exportAsJSONItem = new JMenuItem(this.aMessages.getString("ExportJSON"));
		exportAsJSONItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				try
				{
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					
					PropertyLoader.getInstance().load();
					
					fc.setSelectedFile(new File(PropertyLoader.getInstance().getProperty("ExportProfilePath")));
					int returnVal = fc.showSaveDialog(null);
					//set default to one if exist in property file
					
					if (returnVal == JFileChooser.APPROVE_OPTION) 
					{
						try
						{
							PropertyLoader.getInstance().setProperty("ExportProfilePath", fc.getSelectedFile().getAbsolutePath());
							PropertyLoader.getInstance().save();
							UserProfile.getInstance().save(new JSONFormat(), PropertyLoader.getInstance().getProperty("ExportProfilePath"));
							System.out.println(fc.getSelectedFile().getAbsolutePath());
						}
						catch (IOException e)
						{
							//Problem saving property.
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
		
		JMenuItem forgetReadSpeeches = new JMenuItem(this.aMessages.getString("ForgetSpeeches"));
		forgetReadSpeeches.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				UserProfile.getInstance().resetReadSpeeches();
			}
		});
		
		this.add(exportAsJSONItem);
		this.add(forgetReadSpeeches);
	}
}