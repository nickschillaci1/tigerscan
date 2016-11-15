package v1;

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
	private int confidentialityScore;
	private FileIndexer indexer;
	private FileSearcher searcher;

	String indexDir = "data/index/";

	public ContentScanner(DatabaseManager db) {
		this.db = db;
	}

	public int scanFiles(ArrayList<String> importedFileNames) {
		confidentialityScore = 0;
		HashMap<String,Integer> queryWords = new HashMap<String,Integer>();
		try {
			this.createIndex(importedFileNames);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		queryWords = db.getTerms();
		System.out.println("queryWords.keySet() = " + queryWords.keySet());

		for(String term : queryWords.keySet()){
			System.out.println("we GOT IN THE LOOP");
			try {
				System.out.println("We bout to search");
				search(term);
				System.out.println("Oh shit we searched i think");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("IOException " + e);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				System.out.println("ParseException " + e);
			}
		}
		System.out.println("Woa we made it to the end");
		return confidentialityScore;
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
	private void foundSensitiveTerm(String term) {
		try {
			confidentialityScore += db.getScore(term);
		} catch (DatabaseNoSuchTermException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Confidentiality score: " + confidentialityScore);
	}
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
			System.out.println("File: "+ doc.get(LuceneConstants.FILE_PATH));	//Bugged, should be spitting out filepath
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
