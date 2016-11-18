package lucene;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

/**
 * Simple test of the Indexing process, requires user to declare file path of the data
 * they want to index and file path of where the index will be stored.
 */
public class LuceneTester {

	String indexDir = "C:\\Users\\Ryan\\Desktop\\Index";
	String dataDir = "C:\\Users\\Ryan\\Desktop\\test directory";
	FileIndexer indexer;
	FileSearcher searcher;

	public static void main(String[] args) {
		LuceneTester tester;
		try {
			tester = new LuceneTester();
			//tester.createIndex();
			tester.search("running");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void createIndex(ArrayList<String> filenames) throws IOException{
		indexer = new FileIndexer(indexDir);
		int numIndexed;
		long startTime = System.currentTimeMillis();	
		numIndexed = indexer.createIndex(filenames, new TextFileFilter());
		long endTime = System.currentTimeMillis();
		indexer.closeIndex();
		System.out.println(numIndexed+" file(s) indexed, time taken: "
				+(endTime-startTime)+" ms");		
	}

	private void search(String searchQuery) throws IOException, ParseException {
		searcher = new FileSearcher(indexDir);
		long startTime = System.currentTimeMillis();

		TopDocs hits = searcher.search(searchQuery);
		long endTime = System.currentTimeMillis();

		System.out.println(hits.totalHits +
				" documents found. Time :" + (endTime - startTime) +" ms");//
		ScoreDoc[] scoreDoc = hits.scoreDocs;

		for(int i = 0; i < scoreDoc.length; i++){
			int docId = scoreDoc[i].doc;
			Document doc = searcher.getDocument(docId);
			System.out.println("File: "+ doc.get(LuceneConstants.FILE_PATH));
		}
	}
}