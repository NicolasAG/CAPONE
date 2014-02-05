package ca.mcgill.cs.comp303.capone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import ca.mcgill.cs.comp303.capone.model.MP;
import ca.mcgill.cs.comp303.capone.model.UserProfile;

/**
 * The Profile view.
 * @author Nicolas
 */
public class ProfileView extends JComponent 
{
	private static final long serialVersionUID = 4377431835764542602L;
	
	private final int aBigFontSize = 15;
	private final int aSmallFontSize = 12;
	private final int aExpressionTableSize = 300;
	private final int aMPTableSize = 700;
	
	private JTable aExpressionsTable = null;
	private JTable aMPsTable = null;
	private ResourceBundle aBundle = null;
	
	/**
	 * Constructor.
	 * @param pMessages - I18N.
	 */
	public ProfileView(final ResourceBundle pMessages)
	{
		this.aBundle = pMessages;
		/**************************************************
		 * This is the NORTH component of our ProfileView *
		 **************************************************/
		JTextField title = new JTextField(this.aBundle.getString("TipTabProfile")); //create a title.
		title.setHorizontalAlignment(SwingConstants.CENTER); //center text.
		title.setFont(new Font("Verdana", Font.BOLD, this.aBigFontSize)); //set the font.
		title.setEditable(false); //can't change it.
		
		/*************************************************
		 * This is the WEST component of our ProfileView *
		 * 	expressions table							 *
		 * 	and grid with 1text field, and 2 buttons	 *
		 *************************************************/
		JPanel panel1 = new JPanel(new BorderLayout());
		
		List<String> expressions = UserProfile.getInstance().getExpressions();
		
		this.aExpressionsTable = new JTable(new MyTableModel1(expressions, this.aBundle)); //table created!
		JScrollPane exprScrollPane = new JScrollPane(this.aExpressionsTable);
		TableRowSorter<TableModel> exprSorter = new TableRowSorter<>(this.aExpressionsTable.getModel()); //create a sorter.
		this.aExpressionsTable.setRowSorter(exprSorter); //add sorter to table.
		ProfileView.setTable(this.aExpressionsTable); //do all settings for the table.
		
		JPanel exprProperties = new JPanel(new GridLayout(1, 2)); //grid that contains a inner grid and a button.
		
		JPanel inner = new JPanel(new GridLayout(1, 2)); //inner grid that contains the add property.
		JTextField newExpr = new JTextField(); //Text filed for expression to add.
		JButton add = new JButton(this.aBundle.getString("Add")); //button that add the text.
		ProfileView.setAddExpressionButton(add, newExpr, this.aExpressionsTable); //do the setting for the button.
		inner.add(newExpr); //add text field to inner grid.
		inner.add(add); //add button to inner grid.
		
		JButton delete = new JButton(this.aBundle.getString("Delete"));
		ProfileView.setDeleteExpressionButton(delete, this.aExpressionsTable); //do the settings for the button.
		
		exprProperties.add(inner); //add inner grid to properties.
		exprProperties.add(delete); //add button to properties.
		
		panel1.add(exprScrollPane, BorderLayout.CENTER); //add table to our WEST component.
		panel1.add(exprProperties, BorderLayout.SOUTH); //add properties to our WEST component.
		panel1.setMinimumSize(new Dimension(this.aExpressionTableSize, this.aExpressionTableSize));
		panel1.revalidate();
		
		/***************************************************
		 * This is the CENTER component of our ProfileView *
		 ***************************************************/
		JPanel panel2 = new JPanel(new BorderLayout());
		
		JTextField subTitle = new JTextField(pMessages.getString("MPFollowed")); //create a title.
		subTitle.setHorizontalAlignment(SwingConstants.CENTER); //center text.
		subTitle.setFont(new Font("Verdana", Font.BOLD, this.aSmallFontSize)); //set the font.
		subTitle.setEditable(false); //can't change it.

		List<MP> mpList = UserProfile.getInstance().getMPs();
		String[] columns = {
				this.aBundle.getString("Name"),
				this.aBundle.getString("Party"),
				this.aBundle.getString("Riding"),
				this.aBundle.getString("Province")};
		
		this.aMPsTable = new JTable(new MyTableModel2(mpList, columns));
		JScrollPane mpScrollPane = new JScrollPane(this.aMPsTable);
		TableRowSorter<TableModel> mpSorter = new TableRowSorter<>(this.aMPsTable.getModel()); //create a sorter.
		this.aMPsTable.setRowSorter(mpSorter); //add sorter to table.
		ProfileView.setTable(this.aMPsTable); //do all settings for the table.

		JButton deleteMP = new JButton(this.aBundle.getString("Delete"));
		ProfileView.setDeleteMPButton(deleteMP, this.aMPsTable);
		
		panel2.add(subTitle, BorderLayout.NORTH);
		panel2.add(mpScrollPane, BorderLayout.CENTER);
		panel2.add(deleteMP, BorderLayout.SOUTH);
		panel2.setMinimumSize(new Dimension(this.aMPTableSize, this.aMPTableSize));
		panel2.revalidate();
		
		JSplitPane resizableFrame = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		resizableFrame.add(panel1);
		resizableFrame.add(panel2);
		
		this.setLayout(new BorderLayout(2, 2));
		this.add(title, BorderLayout.NORTH);
		this.add(resizableFrame, BorderLayout.CENTER);
	}


	/**
	 * Do all the settings for both Expressions table AND MP table.
	 * @param pTable - the table to set.
	 */
	private static void setTable(JTable pTable)
	{
		TableColumn column = null;
		for (int i = 0; i <pTable.getColumnCount() ; i++)
		{
			column = pTable.getColumnModel().getColumn(i);
			/**can resize the columns**/
			column.setResizable(true);
			/**Center text**/
			DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
			dtcr.setHorizontalAlignment(SwingConstants.CENTER);
			column.setCellRenderer(dtcr);		
		}
		pTable.getTableHeader().setReorderingAllowed(false); //can't move the columns.
		pTable.setFillsViewportHeight(true); //ensures that table never smaller than the viewport.
		pTable.setAutoCreateRowSorter(true); //enable sorting column in alphabetical order.
		
		pTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //can only select 1 row at a time!
	}
	
	/**
	 * Do all the settings for the add expression button.
	 * @param pButton - the button with an action listener.
	 * @param pNewExpr - the new expression to add.
	 * @param pExpressionsTable - the table where to add the new expression.
	 */
	private static void setAddExpressionButton(JButton pButton, final JTextField pNewExpr, final JTable pExpressionsTable)
	{
		pButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				UserProfile.getInstance().addExpression(pNewExpr.getText());
				((MyTableModel1) pExpressionsTable.getModel()).addRow(pNewExpr.getText());
			}
		});
	}
	
	/**
	 * Do all the settings for the delete expression button.
	 * @param pButton - the button with an action listener.
	 * @param pExpressionsTable - the table where we remove from.
	 */
	private static void setDeleteExpressionButton(JButton pButton, final JTable pExpressionsTable)
	{
		pButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				int selection = pExpressionsTable.getSelectedRow();
				try
				{
					int actualRow = pExpressionsTable.convertRowIndexToModel(selection);
					//delete selection from the user profile.
					UserProfile.getInstance().removeExpression( (String) pExpressionsTable.getValueAt(actualRow, 0) );
					((MyTableModel1) pExpressionsTable.getModel()).delRow(actualRow);
					UserProfile.getInstance().refresh();
				}
				catch (IndexOutOfBoundsException e)
				{
					//Nothing selected!
				}
			}
		});
	}
	
	/**
	 * Do all the settings for the delete MP button.
	 * @param pButton - the button.
	 * @param pMPsTable - the table where we remove from.
	 */
	private static void setDeleteMPButton(JButton pButton, final JTable pMPsTable)
	{
		pButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				int selection = pMPsTable.getSelectedRow();
				try
				{
					int actualRow = pMPsTable.convertRowIndexToModel(selection);
					UserProfile.getInstance().removeMP( (MP) pMPsTable.getValueAt(actualRow, 0) );
					((MyTableModel2) pMPsTable.getModel()).delRow(actualRow);
					UserProfile.getInstance().refresh();
				}
				catch(IndexOutOfBoundsException e)
				{
					//Nothing selected!
				}
			}
		});
	}
	
	/**
	 * Update the data in both table models.
	 */
	public void trigerDataChanges()
	{
		((MyTableModel1) this.aExpressionsTable.getModel()).updateModel();
		((MyTableModel2) this.aMPsTable.getModel()).updateModel();
	}
	
}

/**
 * My Expression Table Model.
 * @author Nicolas A.G
 */
class MyTableModel1 extends AbstractTableModel
{
	private static final long serialVersionUID = 3684616585802379202L;
	private String[] aColumn = new String[1];
	private ResourceBundle aMessages = null;
	private List<String> aExpr = null;
	
	public MyTableModel1(List<String> pExpr, ResourceBundle pMessages)
	{
		this.aExpr = pExpr;
		this.aMessages = pMessages;
		this.aColumn[0] = pMessages.getString("Expressions");
	}
	@Override
	public int getRowCount()
	{
		return this.aExpr.size();
	}
	@Override
	public int getColumnCount()
	{
		return this.aColumn.length;
	}
	@Override
	public String getColumnName(int pCol)
	{
        return this.aColumn[pCol];
    }
	@Override
	public Class<?> getColumnClass(int pCol)
	{
		return String.class;
    }
	@Override
	public Object getValueAt(int pRowIndex, int pColumnIndex)
	{
		return this.aExpr.get(pRowIndex);
	}
	@Override
    public void setValueAt(Object pValue, int pRow, int pColumn)
	{
		if (!this.aExpr.contains(pValue))
		{
			this.aExpr.set(pRow, (String) pValue);
			this.fireTableCellUpdated(pRow, pColumn);
		}
		else
		{
			JOptionPane.showMessageDialog(null, this.aMessages.getString("AlreadyHere"));
		}
    }
	public void addRow(String pRow)
	{
		if (!this.aExpr.contains(pRow))
		{
			this.aExpr.add(pRow);
			this.fireTableDataChanged();
		}
	}
	public void delRow(int pRow)
	{
		this.aExpr.remove(pRow);
		this.fireTableDataChanged();
	}
	@Override
	public boolean isCellEditable(int pRow, int pColumn)
	{  
		return true;  
	}
	public void updateModel()
	{
		this.aExpr = UserProfile.getInstance().getExpressions();
		this.fireTableDataChanged();
	}
}

/**
 * My MP Table Model.
 * @author Nicolas A.G
 */
class MyTableModel2 extends AbstractTableModel
{
	private static final long serialVersionUID = 7228148160229632004L;
	private String[] aColumns = null;
	private List<MP> aMPs = null;
	
	public MyTableModel2(List<MP> pList, String[] pColumns)
	{
		this.aMPs = pList;
		this.aColumns = pColumns;
	}
	@Override
	public int getRowCount()
	{
		return this.aMPs.size();
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
		if(pCol == 0)
		{
			return MP.class;
		}
		return String.class;
    }
	@Override
	public Object getValueAt(int pRowIndex, int pColumnIndex)
	{
		try
		{
			switch(pColumnIndex)
			{
			case 0:
				return this.aMPs.get(pRowIndex); //value at name column = MP
			case 1:
				return this.aMPs.get(pRowIndex).getCurrentMembership().getParty().getShortName(); //value at party column = Party
			case 2:
				return this.aMPs.get(pRowIndex).getCurrentMembership().getRiding().getName(); //value at riding = Riding
			default:
				return this.aMPs.get(pRowIndex).getCurrentMembership().getRiding().getProvince(); //value at province = Province
			}
		}
		catch (NullPointerException e)
		{
			return null;
		}

	}
	public void delRow(int pRow)
	{
		this.aMPs.remove(pRow);
		this.fireTableDataChanged();
	}
	public void updateModel()
	{
		this.aMPs = UserProfile.getInstance().getMPs();
		this.fireTableDataChanged();
	}
}
