package db;

import java.util.ArrayList;

/**
 * This is an error class the Database class uses.
 * @author Brandon Dixon
 *
 */

public class DatabaseAddTermException extends Exception {

    private ArrayList<String> term;

    public DatabaseAddTermException(String t) {
		super("The term '"+t+"' has already been added.");
		term = new ArrayList<String>();
		term.add(t);
    }

    //add better functionality in the future
    public DatabaseAddTermException(ArrayList<String> inputTerm) {
		super("Some or all of these terms have already been added.  You can call the getTerms() method to find which ones.");
	
		term = inputTerm;
    }

    public ArrayList<String> getTerms() {
		return term;
    }

}
