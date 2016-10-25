

package db;
import java.util.ArrayList;

/**
 * This is an error class the Database class uses.
 * @author Brandon Dixon
 *
 */

public class DatabaseRemoveTermException extends Exception {

    private ArrayList<Integer> term;

    public DatabaseRemoveTermException(int inputTerm) {
		super("The term '"+inputTerm+"' does not exist in the database and therefore cannot be deleted.");
		term = new ArrayList<Integer>();
		term.add(inputTerm);
    }

    //add better functionality in the future
    public DatabaseRemoveTermException(ArrayList<Integer> inputTerm) {
		super("Some or all of the input terms do not exist in the database and therefore cannot be deleted.  Call the getTerms() method to find out which ones.");
	
		term = inputTerm;
    }

    public ArrayList<Integer> getTerms() {
		return term;
    }

}
