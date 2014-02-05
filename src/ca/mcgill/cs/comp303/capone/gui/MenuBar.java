package ca.mcgill.cs.comp303.capone.gui;

import java.util.ResourceBundle;

import javax.swing.JMenuBar;

/**
 * The menu bar that has a DataMenu, a ProfileMenu, and a HelpMenu.
 * @author Jeff.
 */
public class MenuBar extends JMenuBar
{
	private static final long serialVersionUID = -4827248579480812459L;

	/**
	 * Constructor.
	 * @param pMessages - I18N.
	 */
	public MenuBar(ResourceBundle pMessages)
	{
		this.add(new DataMenu(pMessages));
		this.add(new ProfileMenu(pMessages));
		this.add(new HelpMenu(pMessages));
		setVisible(true);
	}
}
