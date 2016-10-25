

package db;
import java.util.ArrayList;

/**
 * This is an error class the Database class uses.
 * @author Brandon Dixon
 *
 */

public class DatabaseNoSuchTermException extends Exception {

    private String term;

    public DatabaseNoSuchTermException(String inputTerm) {
		super(inputTerm);
		this.term = inputTerm;
    }

    public String getTerm() {
		return term;
    }

}
