package v1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Configuration medium to interface with a config file that contains the current database file name and total number of e-mails the program has scanned
 * @author Nick Schillaci
 *
 */
public class Config {

	private static final String configFilename = "data/config.cfg";
	private static final String defaultDatabaseFilename = "data/database.db";
	private static int numEmailsScanned;
	private static File configFile;
	private static String databaseFilename;
	
	/**
	 * Write current attributes to the config file
	 * @throws IOException
	 */
	private static void updateConfigFile() throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(configFilename));
		bw.write(Integer.toString(numEmailsScanned));
		bw.newLine();
		bw.write(databaseFilename);
		bw.newLine();
		bw.close();
	}
	
	/**
	 * Initialize the config attributes, reading from the config file or creating one and utilizing default values
	 * @throws IOException
	 */
	public static void initConfig() {
		BufferedReader br;
		configFile = new File(configFilename);
		try {
			if (configFile.createNewFile()) { //returns true if file did not exist and it needed to be created
				numEmailsScanned = 0;
				databaseFilename = defaultDatabaseFilename;
				updateConfigFile();
			}
			else { //executes if file already existed
				br = new BufferedReader(new FileReader(configFilename));
				ArrayList<String> lines = new ArrayList<String>();
				String line = null;
				while((line = br.readLine()) != null) {
					lines.add(line);
				}
				numEmailsScanned = Integer.parseInt(lines.get(0));
				databaseFilename = lines.get(1);
				br.close();
				FileHandler.getStringFromFile(configFilename);
			}
		} catch (NumberFormatException | IOException e) {
		}
	}
	
	/**
	 * Called when an e-mail is scanned, this method will simply increment the number of e-mails scanned
	 * @throws IOException
	 */
	public static void emailScanned() throws IOException {
		numEmailsScanned++;
		updateConfigFile();
	}
	
	/**
	 * @return total number of e-mails scanned
	 */
	public static int getEmailsScanned() {
		return numEmailsScanned;
	}
	
	/**
	 * Sets a new value for the database file name
	 * @param filename
	 * @throws IOException
	 */
	public static void setDatabaseFilename(String filename) throws IOException {
		databaseFilename = filename;
		updateConfigFile();
	}
	
	/**
	 * @return String current database file name
	 */
	public static String getDatabaseFilename() {
		return databaseFilename;
	}
	
}
