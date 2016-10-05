import static org.junit.Assert.*;
import org.junit.*;

/**
 * The test that check whether the file is written correctly occured before encryption was added.
 * @author Brandon Dixon
 */

public class DatabaseJUNIT {

    public DatabaseJUNIT() {

    }

    @Test
    public void addSingleTerm() {
	String term = "nuclear";

	Database dbTest = new Database();

	dbTest.addTerm(term);
	assertTrue(dbTest.hasTerm(term));
    }

}
