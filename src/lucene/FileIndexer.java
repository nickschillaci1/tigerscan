package lucene;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


/**
 * This class takes text files and creates indexes from them that can be searched
 * @author Ryan Hudson
 * @version 1.0
 */
public class FileIndexer {

	IndexWriter writer;
	
	/**
	 * Constructor takes a filepath
	 * @param indexDirectoryPath
	 * @throws IOException
	 */
	public FileIndexer(String indexDirectoryPath) throws IOException {
		//this directory will contain the indexes
		Directory docDir = FSDirectory.open(Paths.get(indexDirectoryPath));

		StandardAnalyzer analyzer = new StandardAnalyzer();

		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(docDir, iwc);


		writer.close();
	}

	/**
	 * Method that creates a document that can be analyzed
	 * @param f - The text file that is being indexed
	 * @return The document
	 * @throws IOException
	 */
	public Document addDoc(File f) throws IOException
	{
		Document doc = new Document();
		FieldType ft = new FieldType();
		ft.setStored(true);

		doc.add(new Field(LuceneConstants.CONTENTS, new FileReader(f), ft));
		doc.add(new Field(LuceneConstants.FILE_NAME, f.getName(), ft));
		doc.add(new Field(LuceneConstants.FILE_PATH, f.getCanonicalPath(), ft));

		return doc;
	}

	/**
	 * Closes the IndexWrtier
	 */
	public void closeIndex() { 
		try {  
			writer.close(); 
		} catch (Exception e) { 
			System.out.println("Got an Exception: " + e.getMessage()); 
		} 
	}

}
