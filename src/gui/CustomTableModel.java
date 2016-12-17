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
	
	/**
	 * Constructor for the table model object, instantiates a local list of terms to display in the table
	 * @param hashMap
	 */
	public CustomTableModel(HashMap<String, Double> hashMap) {
		this.terms = hashMap;
	}
	
	/**
	 * Get the number of rows
	 */
	public int getRowCount() {
		return terms.size();
	}

	/**
	 * Get the number of columns
	 */
	public int getColumnCount() {
		return columnNames.length;
	}
	
	/**
	 * Get the name of a column
	 */
	public String getColumnName(int col) {
		return columnNames[col];
	}
	
	/*//only implemented if we want to use the table itself to change classification score
	//(currently buggy, will need to rewrite setValueAt if we do this)
	public boolean isCellEditable(int row, int col) {
		return col == 1;
	}*/

	/**
	 * Return the value at a specific location in the table
	 */
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
	
	/**
	 * Set the value at a specific location in the table
	 */
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
			terms.put(selectedKey, (Double)value);
		}
	}
	
	/**
	 * Clear all cells in the table
	 */
	public void clearAllCells() {
		terms.clear();
	}
	
	/**
	 * Return the index of the currently selected row
	 * @return
	 */
	public int getSelectedRowIndex() {
		return selectedRowIndex;
	}
	
	/**
	 * Sets the index of the selected row, thereby selecting a row
	 * @param row
	 */
	public void setSelectedRowIndex(int row) {
		selectedRowIndex = row;
		return;
	}

}
