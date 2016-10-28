package v1;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

/**
 * The table for the settings menu in the GUI was built in this separate class for legibility
 * @author Nick Schillaci
 *
 */
public class CustomTableModel extends AbstractTableModel {

	private String[] columnNames = {"Term", "Classification" };
	private HashMap<Integer,Integer> terms;
	private int selectedRowIndex;
	
	public CustomTableModel(HashMap<Integer,Integer> terms) {
		this.terms = terms;
	}
	
	public int getRowCount() {
		return terms.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}
	
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	//only implemented if we want to use the table itself to change classification score
	/*public boolean isCellEditable(int row, int col) {
		return col == 1;
	}*/

	public Object getValueAt(int row, int col) {
		String value = null;
		ArrayList<Integer> keys = new ArrayList<Integer>(terms.keySet());
		if (col == 0) { //if getting a term
			value = keys.get(row).toString();
		}
		else { //if getting a term's score
			value = terms.get(keys.get(row)).toString();
		}
		return value;
	}
	
	public int getSelectedRowIndex() {
		return selectedRowIndex;
	}
	
	public void setSelectedRowIndex(int row) {
		selectedRowIndex = row;
		return;
	}

}
