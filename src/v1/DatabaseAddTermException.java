package v1;

import java.util.ArrayList;


public class DatabaseAddTermException extends Exception {

    private ArrayList<String> term;

    public DatabaseAddTermException(String inputTerm) {
	super("The term '"+inputTerm+"' has already been added.");
	term = new ArrayList<String>();
	term.add(inputTerm);
    }

    public DatabaseAddTermException(ArrayList<String> inputTerm) {
	super("Some or all of these terms have already been added.  You can call the getTerms() method to find which ones.");

	term = inputTerm;
    }

    public ArrayList<String> getTerms() {
	return term;
    }

}
