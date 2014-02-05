package ca.mcgill.cs.comp303.capone.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

import ca.mcgill.cs.comp303.capone.loaders.PropertyLoader;

/**
 * The help menu.
 * @author Jeff
 */
public class HelpMenu extends JMenu
{
	private static final long serialVersionUID = -8204675374592998318L;

	/**
	 * Constructor.
	 * @param pMessages - I18N
	 */
	public HelpMenu(final ResourceBundle pMessages)
	{
		init(pMessages.getString("Help"), null);
		JMenuItem aboutMenuItem = new JMenuItem(pMessages.getString("About"));
		aboutMenuItem.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				JOptionPane.showMessageDialog(null, pMessages.getString("CreatedBy"));
			}
		});
		
		ButtonGroup group = new ButtonGroup();
		JRadioButtonMenuItem englishSelectionButton = new JRadioButtonMenuItem(pMessages.getString("EN"));
		englishSelectionButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pEvent)
			{
				try
				{
					PropertyLoader.getInstance().load();
					PropertyLoader.getInstance().setProperty("Language", "EN");
					PropertyLoader.getInstance().save();
					JOptionPane.showMessageDialog(null, pMessages.getString("Restart"));
				}
				catch (IOException pE)
				{
					//Problem loading property.
					//Problem saving property.
					pE.printStackTrace();
				}
			}
		});
		JRadioButtonMenuItem frenchSelectionButton = new JRadioButtonMenuItem(pMessages.getString("FR"));
		frenchSelectionButton.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent pEvent)
			{
				try
				{
					PropertyLoader.getInstance().load();
					PropertyLoader.getInstance().setProperty("Language", "FR");
					PropertyLoader.getInstance().save();
					JOptionPane.showMessageDialog(null, pMessages.getString("Restart"));
				}
				catch (IOException pE)
				{
					//Problem loading property.
					//Problem saving property.
					pE.printStackTrace();
				}
			}
		});
		
		try
		{
			PropertyLoader.getInstance().load();
			if (PropertyLoader.getInstance().getProperty("Language").compareTo("EN") == 0)
			{
				englishSelectionButton.setSelected(true);
				frenchSelectionButton.setSelected(false);
			} 
			else 
			{
				englishSelectionButton.setSelected(false);
				frenchSelectionButton.setSelected(true);
			}
		}
		catch (IOException e)
		{
			//Problem loading property.
			e.printStackTrace();
		}
		
		group.add(englishSelectionButton);
		group.add(frenchSelectionButton);
		
		this.add(englishSelectionButton);
		this.add(frenchSelectionButton);
		this.add(aboutMenuItem);
	}

}
