import Java.IO.File;

/**
 * This class will maintain the database, handle database encryption, and handles modifications to the database.
 * @author Brandon Dixon
 * @version 10/4/16
 **/
public class Database {

    private ArrayList<String> terms;
    private final String fileName = "/info.info";

    /**
     * This will initalize the database and load in terms if there are any to load
     */
    public Databse() {
	File textFile = new File(filename);
	if (!textFile.exists()) {
	    
	}
    }
  
}
