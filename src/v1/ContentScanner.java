package v1;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * 
 * This class handles the scanning of a given file's text.
 * 
 * @author Zackary Flake
 * @author Nick Schillaci
 * @author Ryan Hudson
 */

public class ContentScanner {

	private Database db;
	private int confidentialityScore;
	
	public ContentScanner() {
		db = new Database();
		//additional testing can happen heres
	}
	
	public void scanFiles(ArrayList<String> importedFileNames) {
		confidentialityScore = 0;
		for(String fileName : importedFileNames) {
			checkForSensitiveTerm(getContentFromFile(fileName));
			//System.out.println(getContentFromFile(fileName));
			
		}	
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
		System.out.println("getting content from " + filename);
		return content;
	}
	
	private void foundSensitiveTerm() {
		System.out.println("Sensitive term " + confidentialityScore + " has been found");
		confidentialityScore++;
		
	}
	
	private void checkForSensitiveTerm(String text) {
		String words[] = text.split(" ");
		for(int i=0; i<words.length; i++) {
			if(db.hasTerm(words[i]))
				foundSensitiveTerm();
		}
	}
	
}
