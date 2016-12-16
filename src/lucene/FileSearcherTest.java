package lucene;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.junit.Test;

public class FileSearcherTest {

	String indexDir = "test_data/test_index/";
	String dataDir = "test_data/test_emails/";
	ArrayList<String> filenames = new ArrayList<String>();
	FileIndexer indexer;
	FileSearcher searcher;

	@Test
	public void testSearchIndex() throws IOException {
		
		String[] queryTerms = {"run","wake","inside","banana","orange","email","string"};
		String[] testEmails = new String[5];
		testEmails[0] = "These strings will simulate emails that will be indexed and searched through for this test";
		testEmails[1] = "Five of these strings will be put into a String array and then added as the first line of a text file";
		testEmails[2] = "Each of these files will be created when the test is ran, you can add more of these by adding a new string to the array";
		testEmails[3] = "Thats enough explaining, heres some random words: apple banana fruit grapes pear mango dog cat bird mouse giraffe";
		testEmails[4] = "How can you see into my eyes, like open doors"
				+ "Leading you down into my core"
				+ "Where I've become so numb, without a soul"
				+ "My spirit's sleeping somewhere cold"
				+ "Until you find it there and lead it back home"
				+ "Wake me up, wake me up inside I can't wake up,"
				+ "Wake me up inside, save me,"
				+ "Call my name and save me from the dark, wake me up"
				+ "Bid my blood to run, I can't wake up"
				+ "Before I come undone, save me"
				+ "Save me from the nothing I've become"; 
		for(int i = 0; i < testEmails.length; i++) {
			List<String> lines = Arrays.asList(testEmails[i]);
			Path file = Paths.get(dataDir + "test_"+ i +".txt");
			Files.write(file, lines, Charset.forName("UTF-8"));
		}
		
		File f = new File(dataDir);
		ArrayList<String> files = new ArrayList<String>(Arrays.asList(f.list()));
		for(int i = 0; i < files.size(); i++){
			String current = files.get(i);
			files.set(i,dataDir+current);
		}

		
		
		try {
			createIndex(files);
			for(int i = 0; i< queryTerms.length; i++)
			search(queryTerms[i]);
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

		System.out.println();
		System.out.println("Term " + searchQuery+": " +hits.totalHits +
				" documents found. Time :" + (endTime - startTime) +" ms");//
		ScoreDoc[] scoreDoc = hits.scoreDocs;

		for(int i = 0; i < scoreDoc.length; i++){
			int docId = scoreDoc[i].doc;
			Document doc = searcher.getDocument(docId);
			System.out.println("File: "+ doc.get(LuceneConstants.FILE_PATH));
		}
	}

	@Test
	public void testStemWord() throws IOException{
		String[] word = {"running","apples", "seeing","kittens","puppies","setting"};
		String[] stemOfWord = {"run","appl","see","kitten","puppi","set"};
		String wordStemmed;
		
		for(int i = 0; i < word.length; i++) {
			wordStemmed = FileSearcher.stemTerm(word[i]);
			//System.out.println("stemOfWord[i]: " + stemOfWord[i]);
			//System.out.println("wordStemmed: " + wordStemmed);
			assertEquals(stemOfWord[i],wordStemmed);
		}

	}
}
