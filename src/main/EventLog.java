package main;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.lucene.search.TopDocs;

/**
 * This class is meant to write information to a log file for runtime documentation
 * @author Nick Schillaci
 *
 */
public class EventLog {
	
	private static final String logFilename = "data/log.log";
	private static BufferedWriter bw;
	
	/**
	 * Write one or more lines of text to the log file
	 * @param lines of text
	 */
	private static void write(String[] lines) {
		try {
			bw = new BufferedWriter(new FileWriter(logFilename, true)); //true to append
			for(int i=0; i<lines.length; i++) {
				bw.write(CryptoUtility.encryptString(lines[i]));
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			System.err.println("Error accessing log file.");
		}
	}
	
	/**
	 * Clear the log file
	 */
	public static void clear() {
		String[] text = {""};
		write(text);
	}
	
	/**
	 * Write to the log file to reflect that the program has scanned a file
	 * @param scannedFile
	 */
	public static void writeScanned(ArrayList<String> scannedFiles) {
		ArrayList<String> lines = new ArrayList<String>();
		for(String filename : scannedFiles) {
			lines.add(new Date().toString() + " -- File scanned -- " + filename);
		}
		String[] text = lines.toArray(new String[0]);
		write(text);
	}
	
	public static void writeScannedScore(String filepath, double score) {
		String[] text = {
				new Date().toString() + " -- File scanned -- " + filepath + " -- Score: " + score
		};
		write(text);
	}
	
	
	/**
	 * Write to the log file to reflect that a term has been added to the database
	 * @param dbFilename
	 */
	public static void writeTermAdded(String dbFilename) {
		String[] text = {
				new Date().toString() + " -- Term/word added -- " + dbFilename
		};
		write(text);
	}
	
	/**
	 * Write to the log file to reflect that a term has been renamed from the database
	 * @param dbFilename
	 */
	public static void writeTermRenamed(String dbFilename) {
		String[] text = {
				new Date().toString() + " -- Term/word renamed -- " + dbFilename
		};
		write(text);
	}
	
	/**
	 * Write to the log file to reflect that a term has been removed from the database
	 * @param dbFilename
	 */
	public static void writeTermRemoved(String dbFilename) {
		String[] text = {
				new Date().toString() + " -- Term/word removed -- " + dbFilename
		};
		write(text);
	}
	
	/**
	 * Write to the log file to reflect that all terms have been removed from the database
	 * @param dbFilename
	 */
	public static void writeTermRemovedAll(String dbFilename) {
		String[] text = {
				new Date().toString() + " -- All terms have been removed -- " + dbFilename
		};
		write(text);
	}
	
	/**
	 * Write to the log file to reflect that a term's classification has been changed
	 * @param dbFilename
	 */
	public static void writeTermClassificationChanged(String dbFilename) {
		String[] text = {
				new Date().toString() + " -- Term/word score changed -- " + dbFilename
		};
		write(text);
	}
	
	/**
	 * Write to the log file to reflect that terms have been imported to the database
	 * @param dbFilename
	 */
	public static void writeTermImported(String dbFilename, String importedFilename) {
		String[] text = {
				new Date().toString() + " -- Terms/words have been imported -- " + importedFilename + " >> " + dbFilename
		};
		write(text);
	}
	
	/**
	 * Write to the log file to reflect that the database has been renamed
	 * @param dbFilenameOld
	 * @param dbFilenameNew
	 */
	public static void writeDatabaseRenamed(String dbFilenameOld, String dbFilenameNew) {
		String[] text = {
				new Date().toString() + " -- The associated database has been renamed -- " + dbFilenameOld + " >> " + dbFilenameNew
		};
		write(text);
	}
	
	/**
	 * Write to the log file to reflect that a new database has been selected
	 * @param dbFilenameOld
	 * @param dbFilenameNew
	 */
	public static void writeDatabaseChanged(String dbFilenameOld, String dbFilenameNew) {
		String[] text = {
				new Date().toString() + " -- A new database has been selected -- " + dbFilenameOld + " >> " + dbFilenameNew
		};
		write(text);
	}
	
	public static void writeFileIndexed(int numIndexed, long startTime, long endTime) {
		String[] text = {
				new Date().toString() + " -- Files indexed -- " + numIndexed + " file(s), time taken: " + (endTime - startTime)+" ms"
		};
		write(text);
	}
	
	public static void writeDocHits(String searchQuery, TopDocs hits) {
		String[] text = {
				new Date().toString() + " -- Total document hits for the term: " + searchQuery + " -- " + hits.totalHits + " document(s)"
		};
		write(text);
	}
	
	public static void writeAdminCredentialsChanged(String username) {
		String[] text = {
				new Date().toString() + " -- Administrator credentials changed -- " + username
		};
		write(text);
	}
	
	public static void writeUserLoggedIn(String username) {
		String[] text = {
				new Date().toString() + " -- Successful account log-in -- " + username
		};
		write(text);
	}
	
	public static String getLogFilename(){
		return logFilename;
	}
}
