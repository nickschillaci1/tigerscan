package main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

	/**
	 * Initializes the the ContentScanner given a database of confidential term	
	 * @param db - data base of confidential terms
	 */
	public ContentScanner(DatabaseManager db) {
		this.db = db;
	}

	/**
	 * Scans an ArrayList of filenames, calling for the creation of a Lucene index based of the contents of the files.
	 * After the index is created, it calls a search query on the index from the list of database terms, an APattern report for the
	 * confidentiality score of the email is created based off the results of the query.
	 * @param importedFileNames - A String ArrayList of filenames that will be indexed
	 * @return A HashMap of the filename and the confidentiality score of the file.
	 */
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
		HashMap<String,Double> queryWords = new HashMap<String,Double>();
		try {
			this.createIndex(importedFileNames);
		} catch (IOException e) {
			e.printStackTrace();
		}
		queryWords = db.getTerms();

		for(String term : queryWords.keySet()){
			try {
				search(term);
			} catch (IOException e) {
				System.out.println("IOException " + e);
			} catch (ParseException e) {
				System.out.println("ParseException " + e);
			}
		}
		
		//get the APatternReports and save proper word values
		ArrayList<String> wordsFound = new ArrayList<String>();
		for (int i=0; i<size; i++) {
			String fName = importedFileNames.get(i);
			APatternReport r = emailAP.get(fName).calculateProbability();
			
			emailValues.put(fName,r.getConfidentialityScoreOfThisEmail());
			EventLog.writeScannedScore(fName,r.getConfidentialityScoreOfThisEmail());
			
			//save the necessary values
			int sizeWords = r.getNumbWords();
			for (int j=0; j<sizeWords; j++) {
				String rWord = r.getWord(j);
				db.setAverageProbability(rWord,r.getAverageProbabilityConfidential(j));
				db.setProbabilityAny(rWord,r.getProbabilityConfidentialPerWord(j));
				db.incrementNumbEmailsIn(rWord);
				if (!wordsFound.contains(rWord)) {
					wordsFound.add(rWord);
				}
			}
		}
		
		//increment number of emails word not in for all values
		String[] allTerms = queryWords.keySet().toArray(new String[0]);
		int numbTerms = allTerms.length;
		for (int i=0; i<numbTerms; i++) {
			String currentTerm = allTerms[i];
			if (!wordsFound.contains(currentTerm)) {
				db.incrementNumbEmailsNotIn(currentTerm);
			}
		}
		
		//EventLog.writeScanned(importedFileNames);
		//return emailAPR;
		return emailValues;
		//stop email and alert user is confidentiality score is above threshold
	}

	/**
	 * Queries a database term to be searched by calling the FileSearcher class
	 * @param term - A term from the database
	 * @throws IOException
	 * @throws ParseException
	 */
	private void search(String term) throws IOException, ParseException {
		searcher = new FileSearcher(indexDir);
		TopDocs hits = searcher.search(""+term);
		ScoreDoc[] scoreDoc = hits.scoreDocs;

		for(int i = 0; i < scoreDoc.length; i++){
			int docId = scoreDoc[i].doc;
			Document doc = searcher.getDocument(docId);
			String fileName = doc.get(LuceneConstants.FILE_PATH);
			
			//add the term
			try {
				emailAP.get(fileName).addWord(term,db.getScore(term),db.getAverageProbability(term),db.getNumbEmailsIn(term),db.getNumbEmailsNotIn(term),db.getProbabilityAny(term));
			} catch (APatternException | DatabaseNoSuchTermException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Calls the FileIndexer class to begin indexing the email
	 * @param filenames - A String ArrayList of filenames to be indexed
	 * @throws IOException
	 */
	private void createIndex(ArrayList<String> filenames) throws IOException{
		indexer = new FileIndexer(indexDir);
		int numIndexed = filenames.size();
		long startTime = System.currentTimeMillis();
		indexer.createIndex(filenames, new TextFileFilter());
		long endTime = System.currentTimeMillis();
		indexer.closeIndex();
		EventLog.writeFileIndexed(numIndexed, startTime, endTime);	
	}
}
