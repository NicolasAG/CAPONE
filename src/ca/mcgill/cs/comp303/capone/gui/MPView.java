package ca.mcgill.cs.comp303.capone.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import ca.mcgill.cs.comp303.capone.model.Capone;
import ca.mcgill.cs.comp303.capone.model.MP;
import ca.mcgill.cs.comp303.capone.model.Party;
import ca.mcgill.cs.comp303.capone.model.UserProfile;

/**
 * The MP view.
 * @author Nicolas A.G.
 */
public class MPView extends JComponent
{
	private static final long serialVersionUID = 5916973598543842855L;

	private final int aColumnWidthBig = 80;
	private final int aColumnWidthSmall = 10;
	private final int aFontSize = 15;
	
	private JTable aMPsTable = null;
	private JPanel aFilters = null;
	private ResourceBundle aBundle = null;
	
	/**
	 * Constructor.
	 * @param pMessages - I18N.
	 */
	public MPView(final ResourceBundle pMessages)
	{
		this.aBundle =  pMessages;
		/*********************************************
		 * This is the NORTH component of our MPView *
		 *********************************************/
		JTextField title = new JTextField(pMessages.getString("TipTabMP")); //create a title.
		title.setHorizontalAlignment(SwingConstants.CENTER); //center text.
		title.setFont(new Font("Verdana", Font.BOLD, this.aFontSize)); //set the font.
		title.setEditable(false); //can't change it.

		/**********************************************
		 * @author Nicolas                            *
		 * This is the CENTER component of our MPView *
		 **********************************************/
		String[] columnNames = {
				pMessages.getString("Name"),
				pMessages.getString("Party"),
				pMessages.getString("Riding"),
				pMessages.getString("Province"),
				pMessages.getString("Added")};
		List<MP> mpList = Capone.getParliament().getMPs();
		
		this.aMPsTable = new JTable(new MyMPTableModel(mpList, columnNames)); //table created!
		JScrollPane scrollPane = new JScrollPane(this.aMPsTable);
		
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(this.aMPsTable.getModel()); //create a sorter.
		this.aMPsTable.setRowSorter(sorter); //add sorter to table.
		
		this.setTable(); //do all settings for the table.
		
		/*************************************************************
		 * This is the SOUTH component of our MPView                 *
		 * form (grid 2,1) with:                                     *
		 * 	2 pairs of (JLabel, ComboBox) (grid 1,2) for our filters *
		 * 	a JButton to add MPs                                     *
		 *************************************************************/
		JPanel form = new JPanel(new GridLayout(2, 1)); //new grid that will have 1 grid and 1 button.
		
		this.aFilters = new JPanel(new GridLayout(1, 2)); //inner grid with the filters properties.
		this.setFilters(this.aMPsTable); //fill the inner grid.
		JButton addButton = new JButton(pMessages.getString("AddSelection"));
		MPView.setButton(addButton, this.aMPsTable, pMessages);
		
		form.add(this.aFilters);
		form.add(addButton);
		
		this.setLayout(new BorderLayout());
		this.add(title, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(form, BorderLayout.SOUTH);
	}
	
	/**
	 * Do all the settings for the table.
	 */
	private void setTable()
	{
		TableColumn column = null;
		for (int i = 0; i < this.aMPsTable.getColumnCount() ; i++)
		{
			column = this.aMPsTable.getColumnModel().getColumn(i);
			/**set size of each column**/
			if (i == 4)
			{
		        column.setPreferredWidth(this.aColumnWidthSmall);
		    }
			else
			{
				column.setPreferredWidth(this.aColumnWidthBig);
			}
			/**can resize the columns.**/
			column.setResizable(true);
		    /**Center text in the first 3 columns:**/
			if (i != 4)
			{
				DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
				dtcr.setHorizontalAlignment(SwingConstants.CENTER);
		        column.setCellRenderer(dtcr);
			}
		}
		this.aMPsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.aMPsTable.getTableHeader().setReorderingAllowed(false); //can't move the columns.
		this.aMPsTable.setFillsViewportHeight(true); //ensures that table never smaller than the viewport.
		this.aMPsTable.setAutoCreateRowSorter(true); //enable sorting each columns in alphabetical order.
		
		List <RowSorter.SortKey> sortKeys = new ArrayList<>();
		sortKeys.add( new RowSorter.SortKey(0, SortOrder.ASCENDING) ); //sort by MPs name in ascending order by default.
		this.aMPsTable.getRowSorter().setSortKeys(sortKeys); //set sorter.
		
		this.aMPsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //can only select 1 row at a time!
	}

	/**
	 * Create 2 JLabels and 2 JComboBox to filter the table.
	 * @param pTable - the table to filter.
	 */
	private void setFilters(final JTable pTable)
	{
		JLabel filterPartyLabel = new JLabel(this.aBundle.getString("FilterParty")+": ");
		JLabel filterProvinceLabel = new JLabel(this.aBundle.getString("FilterProvince")+": ");
		
		ArrayList<String> parties = new ArrayList<>(); //list of UNIQUE parties.
		ArrayList<String> provinces = new ArrayList<>(); //list of UNIQUE provinces.
		parties.add(this.aBundle.getString("All")); //default is all.
		provinces.add(this.aBundle.getString("All")); //default is all.
//		for (int i = 0; i < this.aMPsTable.getRowCount(); i++) //for every row (MP), fill up the 2 lists.
//		{
//			MP currentMP = (MP) this.aMPsTable.getValueAt(i, 0);
//			Party currentParty = currentMP.getCurrentMembership().getParty();
//			if (!parties.contains(currentParty.getShortName()))
//			{
//				parties.add(currentParty.getShortName());
//			}
//			String currentProvince = currentMP.getCurrentMembership().getRiding().getProvince();
//			if (!provinces.contains(currentProvince))
//			{
//				provinces.add(currentProvince);
//			}
//		}
		for (MP m: Capone.getParliament().getMPs())
		{
			Party currentParty = m.getCurrentMembership().getParty();
			if (!parties.contains(currentParty.getShortName()))
				{
					parties.add(currentParty.getShortName());
				}
				String currentProvince = m.getCurrentMembership().getRiding().getProvince();
				if (!provinces.contains(currentProvince))
				{
					provinces.add(currentProvince);
				}
		}
		
		//create the combo boxes!
		final JComboBox<Object> lPartyFilter = new JComboBox<>(parties.toArray());
		final JComboBox<Object> lProvinceFilter = new JComboBox<>(provinces.toArray());
		
		/**
		 * The action listener for BOTH ComboBoxes.
		 * @author Nicolas A.G.
		 */
		class MyActionListener implements ActionListener
		{
			@Override
			@SuppressWarnings("unchecked")
			public void actionPerformed(ActionEvent pE)
			{
				String selection1 = (String) lPartyFilter.getSelectedItem(); //get selection1.
				String selection2 = (String) lProvinceFilter.getSelectedItem(); //get selection2.
				List<RowFilter<TableModel, Object>> filters = new ArrayList<>();
				RowFilter<TableModel, Object> compoundRowFilter = null; //the COMBINED filter.
				if (selection1.equals("- - -") && selection2.equals("- - -"))
				{ //if both selections are "- - -", then filter nothing.
					((TableRowSorter<TableModel>) pTable.getRowSorter()).setRowFilter(null);
				}
				else if (!selection1.equals("- - -") && selection2.equals("- - -"))
				{ //if only selection1 is valid, then set row filter to filter1.
					try
					{
						RowFilter<TableModel, Object> firstFilter = RowFilter.regexFilter(selection1, 1); //get filter1.
						compoundRowFilter = firstFilter; //create the combined filter.
					}
					catch (java.util.regex.PatternSyntaxException e)
					{
						return;
					}
					((TableRowSorter<TableModel>) pTable.getRowSorter()).setRowFilter(compoundRowFilter); //set filter.
				}
				else if (selection1.equals("- - -") && !selection2.equals("- - -"))
				{ //if only selection2 is valid, then set row filter to filter2.
					try
					{
						RowFilter<TableModel, Object> secondFilter = RowFilter.regexFilter(selection2, 3); //get filter2.
						compoundRowFilter = secondFilter; //create the combined filter.
					}
					catch (java.util.regex.PatternSyntaxException e)
					{
						return;
					}
					((TableRowSorter<TableModel>) pTable.getRowSorter()).setRowFilter(compoundRowFilter); //set filter.
				}
				else
				{ //if BOTH selections are valid, then set row filter to combined filter.
					try
					{
						RowFilter<TableModel, Object> firstFilter = RowFilter.regexFilter(selection1, 1); //get filter1.
						RowFilter<TableModel, Object> secondFilter = RowFilter.regexFilter(selection2, 3); //get filter2.
						filters.add(firstFilter);
						filters.add(secondFilter);
						compoundRowFilter = RowFilter.andFilter(filters); //create the combined filter.
					}
					catch (java.util.regex.PatternSyntaxException e)
					{
						return;
					}
					((TableRowSorter<TableModel>) pTable.getRowSorter()).setRowFilter(compoundRowFilter); //set filter.
				}
			}
		}
		
		MyActionListener al = new MyActionListener(); //create an action listener for BOTH combo boxes
		lPartyFilter.addActionListener(al); //add it to cb1.
		lProvinceFilter.addActionListener(al); //add it to cb2.
				
		JPanel flow1 = new JPanel(new FlowLayout()); //flow 1 has party filter.
		flow1.add(filterPartyLabel);
		flow1.add(lPartyFilter);
		
		JPanel flow2 = new JPanel(new FlowLayout()); //flow 2 has province filter.
		flow2.add(filterProvinceLabel);
		flow2.add(lProvinceFilter);
		
		this.aFilters.removeAll();
		this.aFilters.add(flow1); //add flow 1 to the grid.
		this.aFilters.add(flow2); //add flow 2 to the grid.
	}
	
	/**
	 * Do all the settings for the button.
	 * @param pButton - the button to set.
	 * @param pTable - the table linked to the button.
	 * @param pMessage - I18N.
	 */
	private static void setButton(JButton pButton, final JTable pTable, final ResourceBundle pMessages)
	{
		pButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				try
				{
					int selection = pTable.getSelectedRow();
					//add selection to the user profile.
					if ((boolean) pTable.getValueAt(selection, 4))
					{
						JOptionPane.showMessageDialog(null, pMessages.getString("AlreadyHere"));
					}
					else
					{
						UserProfile.getInstance().addMP( (MP)pTable.getValueAt(selection, 0) );
						UserProfile.getInstance().refresh();
					}
				}
				catch(IndexOutOfBoundsException e)
				{
					//Nothing selected!
				}
			}
		});
	}

	/**
	 * Update the data in the table model.
	 */
	public void trigerDataChanges()
	{
		((MyMPTableModel) this.aMPsTable.getModel()).updateModel();
		setFilters(this.aMPsTable); //fill the inner grid.
		this.revalidate();
		this.repaint();
	}
}

/**
 * The Table model for the MP table.
 * @author Nicolas
 */
class MyMPTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 7811142070143826076L;
	private List<MP> aMPlist = null;
	private String[] aColumns = new String[5];
	
	public MyMPTableModel(List<MP> pList, String[] pColumns)
	{
		this.aMPlist = pList;
		this.aColumns = pColumns;
	}
	@Override
	public int getRowCount()
	{
		return this.aMPlist.size();
	}
	@Override
	public int getColumnCount()
	{
		return this.aColumns.length;
	}
	@Override
	public String getColumnName(int pCol)
	{
        return this.aColumns[pCol];
    }
	@Override
	public Class<?> getColumnClass(int pCol)
	{
		if (pCol == 0)
		{
			return MP.class;
		}
		else if (pCol == 4)
		{
			return Boolean.class;
		}
		return String.class;
    }
	@Override
	public Object getValueAt(int pRowIndex, int pColumnIndex)
	{
		switch(pColumnIndex)
		{
		case 0:
			return this.aMPlist.get(pRowIndex); //value at name column = MP
		case 1:
			return this.aMPlist.get(pRowIndex).getCurrentMembership().getParty().getShortName(); //value at party column = Party
		case 2:
			return this.aMPlist.get(pRowIndex).getCurrentMembership().getRiding().getName(); //value at riding = Riding
		case 3:
			return this.aMPlist.get(pRowIndex).getCurrentMembership().getRiding().getProvince(); //value at province = Province
		default:
			return UserProfile.getInstance().getMPs().contains(this.aMPlist.get(pRowIndex));
		}
	}
	public void updateModel()
	{
		Capone.getInstance();
		this.aMPlist = Capone.getParliament().getMPs();
		this.fireTableDataChanged();
	}
}
