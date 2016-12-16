package gui;

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
	private HashMap<String,Double> terms;
	private int selectedRowIndex;
	
	public CustomTableModel(HashMap<String, Double> hashMap) {
		this.terms = hashMap;
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
	
	/*//only implemented if we want to use the table itself to change classification score
	//(currently buggy, will need to rewrite setValueAt if we do this)
	public boolean isCellEditable(int row, int col) {
		return col == 1;
	}*/

	public Object getValueAt(int row, int col) {
		Object value;
		ArrayList<String> keys = new ArrayList<String>(terms.keySet());
		if (col == 0) { //if getting a term
			value = keys.get(row);
		}
		else { //if getting a term's score
			value = terms.get(keys.get(row));
		}
		return value;
	}
	
	public void setValueAt(Object value, int row, int col) {
		ArrayList<String> keys = new ArrayList<String>(terms.keySet());
		String selectedKey = keys.get(row);
		double selectedValue = terms.get(selectedKey);
		if (col == 0) { //if renaming a term
			terms.remove(selectedKey);
			terms.put((String) value, selectedValue);
		}
		else { //if changing a term's score
			terms.remove(selectedKey);
			terms.put(selectedKey, Double.parseDouble((String) value));
		}
	}
	
	public void clearAllCells() {
		terms.clear();
	}
	
	public int getSelectedRowIndex() {
		return selectedRowIndex;
	}
	
	public void setSelectedRowIndex(int row) {
		selectedRowIndex = row;
		return;
	}

}
