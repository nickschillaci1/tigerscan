package v1;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import db.DatabaseManager;
import db.DatabaseNoSuchTermException;

/**
 * 
 * This class handles the scanning of a given file's text.
 * 
 * @author Zackary Flake
 * @author Nick Schillaci
 * @author Ryan Hudson
 * @author Brandon Dixon
 */

public class ContentScanner {

	private DatabaseManager db;
	private int confidentialityScore;
	
	public ContentScanner(DatabaseManager db) {
		this.db = db;
	}
	
	public int scanFiles(ArrayList<String> importedFileNames) {
		confidentialityScore = 0;
		for(String fileName : importedFileNames) {
			checkForSensitiveTerm(getContentFromFile(fileName));
			
		}	
		return confidentialityScore;
		//stop email and alert user is confidentiality score is above threshold
	}
	
	private String getContentFromFile(String filename) {
		String content;
		try {
			content = FileHandler.getStringFromFile(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.print("The file \"" + filename + "\" was not found.");
			return null;
		}
		return content;
	}
	
	private void foundSensitiveTerm(String term) {
		try {
			confidentialityScore += db.getScore(term);
		} catch (DatabaseNoSuchTermException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Confidentiality score: " + confidentialityScore);
	}
	
	private void checkForSensitiveTerm(String text) {
		//delete punctuation, convert words to lowercase and split based on spaces
		String words[] = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		
		for(int i=0; i<words.length; i++) {
			if(db.hasTerm(words[i]))
				foundSensitiveTerm(words[i]);
		}
	}
	
}
