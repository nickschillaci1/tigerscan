

package v1;
import java.util.ArrayList;

/**
 * This is an error class the Database class uses.
 * @author Brandon Dixon
 *
 */

public class DatabaseRemoveTermException extends Exception {

    private ArrayList<String> term;

    public DatabaseRemoveTermException(String inputTerm) {
		super("The term '"+inputTerm+"' does not exist in the database and therefore cannot be deleted.");
		term = new ArrayList<String>();
		term.add(inputTerm);
    }

    //add better functionality in the future
    public DatabaseRemoveTermException(ArrayList<String> inputTerm) {
		super("Some or all of the input terms do not exist in the database and therefore cannot be deleted.  Call the getTerms() method to find out which ones.");
	
		term = inputTerm;
    }

    public ArrayList<String> getTerms() {
		return term;
    }

}
