package db;

import java.util.ArrayList;

/**
 * This is an error class the Database class uses.
 * @author Brandon Dixon
 *
 */

public class DatabaseAddTermException extends Exception {

    private ArrayList<Integer> term;

    public DatabaseAddTermException(int inputTerm) {
		super("The term '"+inputTerm+"' has already been added.");
		term = new ArrayList<Integer>();
		term.add(inputTerm);
    }

    //add better functionality in the future
    public DatabaseAddTermException(ArrayList<Integer> inputTerm) {
		super("Some or all of these terms have already been added.  You can call the getTerms() method to find which ones.");
	
		term = inputTerm;
    }

    public ArrayList<Integer> getTerms() {
		return term;
    }

}
