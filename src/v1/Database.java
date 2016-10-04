import Java.IO.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Exception;

/**
 * This class will maintain the database, handle database encryption, and handles modifications to the database.
 *
 * SO FAR: this file only creates the database locally in a non-encrypted file
 *
 * @author Brandon Dixon
 * @version 10/4/16
 **/
public class Database {

    private ArrayList<String> terms;
    private final String fileName = "/info.info";
    private File textFile = new File(filename);
    private FileWriter write = new FileWriter(textFile,false);
    private PrintWriter printToFile = new PrintWriter(fileWrite);

    /**
     * This will initalize the database and load in terms if there are any to load
     */
    public Databse() {
	//TODO: read the file in
    }
  
}
