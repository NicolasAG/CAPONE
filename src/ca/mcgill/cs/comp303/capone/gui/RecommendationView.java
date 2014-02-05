package ca.mcgill.cs.comp303.capone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import ca.mcgill.cs.comp303.capone.loaders.PropertyLoader;
import ca.mcgill.cs.comp303.capone.model.Capone;
import ca.mcgill.cs.comp303.capone.model.MP;
import ca.mcgill.cs.comp303.capone.model.Speech;
import ca.mcgill.cs.comp303.capone.model.UserProfile;
import ca.mcgill.cs.comp303.capone.model.UserProfile.RecommenderStrategy;

/**
 * The Recommendation view.
 * @author Nicolas
 */
public class RecommendationView extends JComponent
{
	private static final long serialVersionUID = -6190217432346775864L;

	private final int aBigFontSize = 15;
	private final int aTextAreaSize = 200;
	private final int aSpeechTableSize = 800;
	
	private JTable aSpeechesTable = null;
	private JTextArea aDescription = null;
	private ResourceBundle aBundle = null;
	
	/**
	 * Constructor.
	 * @param pMessages - I18N
	 */
	public RecommendationView(final ResourceBundle pMessages)
	{
		this.aBundle = pMessages;
		/**************************************************
		 * This is the NORTH component of our ProfileView *
		 **************************************************/
		JPanel top = new JPanel(); //this will contain the title and the combo box (with label).
		top.setLayout(new BoxLayout(top, BoxLayout.PAGE_AXIS));
		
		JTextField title = new JTextField(this.aBundle.getString("TipTabRecommendations")); //create a title.
		title.setHorizontalAlignment(SwingConstants.CENTER); //center text.
		title.setFont(new Font("Verdana", Font.BOLD, this.aBigFontSize)); //set the font.
		title.setEditable(false); //can't change it.
		
		JPanel selectAlgo = new JPanel(new FlowLayout()); //this will contain the label and the combo box.
		JLabel select = new JLabel(this.aBundle.getString("SelectAlgo")+":"); //create a label.
		JComboBox<RecommenderStrategy> algoType = new JComboBox<>(UserProfile.RecommenderStrategy.values());
		algoType.setSelectedItem(UserProfile.getInstance().getRecommenderStrategy()); //create a combo box.
		selectAlgo.add(select);
		selectAlgo.add(algoType);
		
		top.add(title);
		top.add(selectAlgo);
		
		/***********************************************************
		 * This is the CENTER component of our Recommendation View *
		 ***********************************************************/
		this.aDescription = new JTextArea();
		JScrollPane panel2 = new JScrollPane(this.aDescription);
		panel2.setMinimumSize(new Dimension(this.aTextAreaSize, this.aTextAreaSize));
		panel2.repaint();
		this.setTextArea();
		
		/*********************************************************
		 * This is the WEST component of our Recommendation View *
		 *********************************************************/
		String[] columns = { this.aBundle.getString("Speech"),
							this.aBundle.getString("MPname"),
							this.aBundle.getString("MPparty"),
							this.aBundle.getString("MPriding"),
							this.aBundle.getString("MPprovince"),
							this.aBundle.getString("Date"),
							this.aBundle.getString("Keywords"),
							this.aBundle.getString("FirstSentence")};
		this.aSpeechesTable = new JTable(new MyTableModel(UserProfile.getInstance().getRecommendedSpeeches(), columns));
		JScrollPane panel1 = new JScrollPane(this.aSpeechesTable);
		panel1.setMinimumSize(new Dimension(this.aSpeechTableSize, this.aSpeechTableSize));
		panel1.repaint();
		TableRowSorter<TableModel> mpSorter = new TableRowSorter<>(this.aSpeechesTable.getModel()); //create a sorter.
		this.aSpeechesTable.setRowSorter(mpSorter); //add sorter to table.
		RecommendationView.setTable(this.aSpeechesTable, this.aDescription); //do all settings for the table.
		RecommendationView.setComboBox(algoType, this.aSpeechesTable); //do all settings for the combo box.
		
		JSplitPane resizableFrame = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		resizableFrame.add(panel1);
		resizableFrame.add(panel2);
		
		this.setLayout(new BorderLayout(2, 2));
		this.add(top, BorderLayout.NORTH);
		this.add(resizableFrame, BorderLayout.CENTER);
	}

	/**
	 * Do all the settings for the Text Area.
	 */
	private void setTextArea()
	{
		this.aDescription.setEditable(false);
		this.aDescription.setMargin(new Insets(2, 2, 2, 2));
		this.aDescription.setLineWrap(true);
		this.aDescription.setWrapStyleWord(true);
	}

	/**
	 * Do all the settings for both Expressions table AND MP table.
	 * @param pTable - the table to set.
	 * @param pArea - the area to fill.
	 */
	private static void setTable(final JTable pTable, final JTextArea pArea)
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
		
		pTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent pE)
			{
				int selection = pTable.getSelectedRow();
				if (selection >= 0)
				{
					int actualRow = pTable.convertRowIndexToModel(selection);
					Speech speech = (Speech) pTable.getValueAt(actualRow, 0);
					UserProfile.getInstance().addReadSpeech(speech);
					String content = speech.getContent();
					content = content.split(">")[1];
					content = content.substring(4); //remove the first 4 characters (ie: "Mr. ").
					content = content.replaceAll("\"</p|</p", ""); //remove all "</p and all </p
					content = content.replaceAll("<a .* title=", ""); //replace all references by their title.
					if (PropertyLoader.getInstance().getProperty("Language").equals("FR"))
					{
						pArea.setText("Mons"+ content);
					}
					else
					{
						pArea.setText("Mr. "+content);
					}
				}
			}
		});
	}
	
	/**
	 * Do all the settings for the combo box.
	 * @param pComboBox - the box to set.
	 * @param pTable - the table.
	 */
	private static void setComboBox(final JComboBox<RecommenderStrategy> pComboBox, final JTable pTable)
	{
		pComboBox.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent pE)
			{
				if (pComboBox.getSelectedItem().equals(RecommenderStrategy.CONTENT))
				{
					//recommend by content!
					UserProfile.getInstance().setRecommenderStrategy(RecommenderStrategy.CONTENT);
					for (int i = 0; i < pTable.getRowCount(); i++)
					{
						((MyTableModel) pTable.getModel()).delRow(i);
					}
					for (Speech s : UserProfile.getInstance().getRecommendedSpeeches())
					{
						((MyTableModel) pTable.getModel()).addRow(s);
					}
				}
				else if (pComboBox.getSelectedItem().equals(UserProfile.RecommenderStrategy.SIMILARITY))
				{
					//recommend by strategy!
					UserProfile.getInstance().setRecommenderStrategy(RecommenderStrategy.SIMILARITY);
					for (int i = 0; i < pTable.getRowCount(); i++)
					{
						((MyTableModel) pTable.getModel()).delRow(i);
					}
					for (Speech s : UserProfile.getInstance().getRecommendedSpeeches())
					{
						((MyTableModel) pTable.getModel()).addRow(s);
					}
				}
			}
		});
	}
	
	/**
	 * Update the data in the table model.
	 */
	public void trigerDataChanges()
	{
		this.aSpeechesTable.clearSelection();
		this.aDescription.setText("");
		((MyTableModel) this.aSpeechesTable.getModel()).updateModel();
	}
}

/**
 * The Speech table model.
 * @author Nicolas A.G.
 */
class MyTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 3684616585802379202L;
	private String[] aColumns = new String[8];
	private List<Speech> aSpeeches = null;
	
	public MyTableModel(List<Speech> pSpeeches, String[] pColumns)
	{
		this.aSpeeches = pSpeeches;
		this.aColumns = pColumns;
	}
	@Override
	public int getRowCount()
	{
		return this.aSpeeches.size();
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
		switch (pCol)
		{
		case 0:
			return Speech.class;
		case 1:
			return MP.class;
		case 5:
			return java.util.Date.class;
		default:
			return String.class;
		}
    }
	@Override
	public Object getValueAt(int pRowIndex, int pColumnIndex)
	{
		switch (pColumnIndex)
		{
		case 0:
			return this.aSpeeches.get(pRowIndex);
		case 1:
			return Capone.getParliament().getMP(this.aSpeeches.get(pRowIndex).getMPKey());
		case 2:
			return Capone.getParliament().getMP(this.aSpeeches.get(pRowIndex).getMPKey()).getCurrentMembership().getParty().getShortName();
		case 3:
			return Capone.getParliament().getMP(this.aSpeeches.get(pRowIndex).getMPKey()).getCurrentMembership().getRiding().getName();
		case 4:
			return Capone.getParliament().getMP(this.aSpeeches.get(pRowIndex).getMPKey()).getCurrentMembership().getRiding().getProvince();
		case 5:
			return this.aSpeeches.get(pRowIndex).getTime();
		case 6:
			String toReturn = "";
			for (String expr : UserProfile.getInstance().getExpressions())
			{
				if (this.aSpeeches.get(pRowIndex).getContent().toLowerCase().contains(expr.toLowerCase()))
				{
					toReturn = toReturn+expr+", ";
				}
			}
			return toReturn;
		default:
			String firstSentence = this.aSpeeches.get(pRowIndex).getContent().split(">")[1];
			firstSentence = firstSentence.substring(4); //remove the first 4 characters (ie: "Mr. ").
			firstSentence = firstSentence.split("\\.")[0]; //split around "." and take the 1st sentence.
			if (PropertyLoader.getInstance().getProperty("Language").equals("FR"))
			{
				return "Mons"+firstSentence+".";
			}
			return "Mr. "+firstSentence+".";
		}
	}
	public void addRow(Speech pRow)
	{
		if (!this.aSpeeches.contains(pRow))
		{
			this.aSpeeches.add(pRow);
			this.fireTableDataChanged();
		}
	}
	public void delRow(int pRow)
	{
		this.aSpeeches.remove(pRow);
		this.fireTableDataChanged();
	}
	public void updateModel()
	{
		this.aSpeeches = UserProfile.getInstance().getRecommendedSpeeches();
		this.fireTableDataChanged();
	}
}
