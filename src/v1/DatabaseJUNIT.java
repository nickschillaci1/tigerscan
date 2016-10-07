package v1;

/*import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;*/
import java.util.ArrayList;

/**
 * The test that check whether the file is written correctly occured before encryption was added.
 * NOTE: this will be converted to a proper junit test at a later time
 * @author Brandon Dixon
 */

public class DatabaseJUNIT {

    public static void main(String[] args) {
	System.out.println("Testing Single Term: ");
	addSingleTerm();
	System.out.println("\nTesting Multiple Terms: ");
	addMultipleTerms();
	System.out.println("\nTesting File Write/Read: ");
	readInTerms();
	System.out.println("\nTesting Remove Single Term: ");
	removeSingleTerm();
	System.out.println("\nTesting Remove Multiple Terms: ");
	removeMultipleTerms();
    }

    //@Test
    public static void addSingleTerm() {
	String term = "nuclear";

	Database dbTest = new Database();
	dbTest.removeAllTerms();

	try {
	    dbTest.addTerm(term);
	} catch (DatabaseAddTermException e) {
	    System.out.println("Error in add; database does not already have term "+term);
	    System.out.println(e);
	}
	//assertTrue(dbTest.hasTerm(term));
	System.out.println("Check to see if the term is added: "+dbTest.hasTerm(term));
    }

    //@Test
    public static void addMultipleTerms() {
	ArrayList<String> terms = new ArrayList<String>();
	terms.add("nuclear");
	terms.add("maintenance");
	terms.add("brandon");
	terms.add("yellow");
	
	Database dbTest = new Database();
	dbTest.removeAllTerms();

	try {
	    dbTest.addTerm(terms) ;
	} catch (DatabaseAddTermException e) {
	    System.out.println("Error in add multiple test.");
	    System.out.println(e);
	}

	//assert true for all words
	System.out.println("Nuclear is added: "+dbTest.hasTerm("nuclear"));
	System.out.println("Maintenance is added: "+dbTest.hasTerm("maintenance"));
	System.out.println("Brandon is added: "+dbTest.hasTerm("brandon"));
	System.out.println("Yellow is added: "+dbTest.hasTerm("yellow"));
    }

    public static void readInTerms() {
	Database dbTest = new Database();

	System.out.println("Nuclear is added: "+dbTest.hasTerm("nuclear"));
        System.out.println("Maintenance is added: "+dbTest.hasTerm("maintenance"));
        System.out.println("Brandon is added: "+dbTest.hasTerm("brandon"));
        System.out.println("Yellow is added: "+dbTest.hasTerm("yellow"));
    }

    //@Test
    public static void removeSingleTerm() {
	Database dbTest = new Database();

	try {
	    dbTest.removeTerm("nuclear");
	} catch (DatabaseRemoveTermException e) {
	    System.out.println("Error in remove single test.");
	}

	System.out.println("Nuclear is removed: "+(!dbTest.hasTerm("nuclear")));
    }

    //@Test
    public static void removeMultipleTerms() {
	Database dbTest = new Database();
	ArrayList<String> rTerms = new ArrayList<String>();
	rTerms.add("maintenance");
	rTerms.add("brandon");
	rTerms.add("yellow");

	try {
	    dbTest.removeTerm(rTerms);
	} catch (DatabaseRemoveTermException e) {
	    System.out.println("Error in remove multiple test.");
	}
	
	System.out.println("Maintenance is removed: "+(!dbTest.hasTerm("maintenance")));
	System.out.println("Brandon is removed: "+(!dbTest.hasTerm("brandon")));
	System.out.println("Yellow is removed: "+(!dbTest.hasTerm("yellow")));
    }

}
