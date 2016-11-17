package v1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import db.DatabaseManager;
import db.DatabaseNoSuchTermException;
import lucene.FileIndexer;
import lucene.FileSearcher;
import lucene.LuceneConstants;
import lucene.TextFileFilter;
import scoring.*;

/**
 * This class handles the scanning of a given file's text.
 * 
 * @author Zackary Flake
 * @author Nick Schillaci
 * @author Ryan Hudson
 * @author Brandon Dixon
 */

public class ContentScanner {

	private DatabaseManager db;
	//private int confidentialityScore;
	private FileIndexer indexer;
	private FileSearcher searcher;
	
	//for scanning
	private HashMap<String,APattern> emailAP;
	//private HashMap<String,APatternReport> emailAPR;
	private HashMap<String,Double> emailValues;

	String indexDir = "data/index/";

	public ContentScanner(DatabaseManager db) {
		this.db = db;
	}

	public HashMap<String,Double> scanFiles(ArrayList<String> importedFileNames) {
		//confidentialityScore = 0;
		
		//create APattern reports for each email
		emailAP = new HashMap<String,APattern>();
		int size = importedFileNames.size();
		for (int i=0; i<size; i++) {
			emailAP.put(importedFileNames.get(i),new APattern());
		}
		//emailAPR = new HashMap<String,APatternReport>();
		emailValues = new HashMap<String,Double>();
		
		//create queryWords
		HashMap<String,Integer> queryWords = new HashMap<String,Integer>();
		try {
			this.createIndex(importedFileNames);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		queryWords = db.getTerms();

		for(String term : queryWords.keySet()){
			try {
				search(term);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("IOException " + e);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("ParseException " + e);
			}
		}
		
		//get the APatternReports and save proper word values
		HashMap<Integer,String> wordsFound = new HashMap<Integer,String>();
		int value = 0;
		for (int i=0; i<size; i++) {
			String fName = importedFileNames.get(i);
			APatternReport r = emailAP.get(fName).calculateProbability();
			
			emailValues.put(fName,r.getConfidentialityScoreOfThisEmail());
			
			//save the necessary values
			int sizeWords = r.getNumbWords();
			for (int j=0; j<sizeWords; j++) {
				String rWord = r.getWord(j);
				db.setAverageProbability(rWord,r.getAverageProbabilityConfidential(j));
				db.setProbabilityAny(rWord,r.getProbabilityConfidentialPerWord(sizeWords));
				db.incrementNumbEmailsIn(rWord);
				if (!wordsFound.containsKey(value)) {
					wordsFound.put(value,rWord);
					value++;
				}
			}
			
		}
		
		//increment number of emails word not in for all values
		String[] allTerms = (String[]) queryWords.keySet().toArray();
		int numbTerms = allTerms.length;
		for (int i=0; i<numbTerms; i++) {
			String currentTerm = allTerms[i];
			if (!wordsFound.containsKey(currentTerm)) {
				db.incrementNumbEmailsNotIn(currentTerm);
			}
		}
		
		//return emailAPR;
		return emailValues;
		//stop email and alert user is confidentiality score is above threshold
	}


	/*	
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
	 */	
	/*private void foundSensitiveTerm(String fileName, String term) {
		try {
			emailAP.get(fileName).addWord(term.hashCode(),db.getScore(term),db.getAverageProbability(term),db.getNumbEmailsIn(term),db.getNumbEmailsNotIn(term),db.getProbabilityAny(term));
		} catch (APatternException | DatabaseNoSuchTermException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		/*try {
			confidentialityScore += db.getScore(term);
		} catch (DatabaseNoSuchTermException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Confidentiality score: " + confidentialityScore);
	}*/
	/*
	private void checkForSensitiveTerm(String text) {
		//delete punctuation, convert words to lowercase and split based on spaces
		String words[] = text.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");

		for(int i=0; i<words.length; i++) {
			if(db.hasTerm(words[i]))
				foundSensitiveTerm(words[i]);
		}
	}
	 */

	private void search(String searchQuery) throws IOException, ParseException {
		searcher = new FileSearcher(indexDir);
		long startTime = System.currentTimeMillis();

		TopDocs hits = searcher.search(searchQuery);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits +
				" documents found. Time :" + (endTime - startTime) +" ms");
		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.getDocument(scoreDoc);
			String fileName = doc.get(LuceneConstants.FILE_PATH);
			
			//add the term
			try {
				emailAP.get(fileName).addWord(searchQuery,db.getScore(searchQuery),db.getAverageProbability(searchQuery),db.getNumbEmailsIn(searchQuery),db.getNumbEmailsNotIn(searchQuery),db.getProbabilityAny(searchQuery));
			} catch (APatternException | DatabaseNoSuchTermException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println(fileName);
			//System.out.println("File: "+ doc.get(LuceneConstants.FILE_PATH));	//Bugged, should be spitting out filepath
		}
	}

	private void createIndex(ArrayList<String> filenames) throws IOException{
		indexer = new FileIndexer(indexDir);
		int numIndexed = filenames.size();
		long startTime = System.currentTimeMillis();
		indexer.createIndex(filenames, new TextFileFilter());
		long endTime = System.currentTimeMillis();
		indexer.closeIndex();
		System.out.println(numIndexed+" file(s) indexed, time taken: "
				+(endTime-startTime)+" ms");		
	}
}
