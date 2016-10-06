/*import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;*/

/**
 * The test that check whether the file is written correctly occured before encryption was added.
 * NOTE: this will be converted to a proper junit test at a later time
 * @author Brandon Dixon
 */

public class DatabaseJUNIT {

    public static void main(String[] args) {
	System.out.println("Testing Single Term: ");
	addSingleTerm();
	System.out.println();
    }

    //@Test
    public static void addSingleTerm() {
	String term = "nuclear";

	Database dbTest = new Database();

	try {
	    dbTest.addTerm(term);
	} catch (DatabaseAddTermException e) {
	    System.out.println("Error in add; database does not already have term "+term);
	    System.out.println(e);
	}
	//assertTrue(dbTest.hasTerm(term));
	System.out.println("Check to see if the term is added: "+dbTest.hasTerm(term));
    }

}
