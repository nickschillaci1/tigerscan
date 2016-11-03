package lucene;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.tartarus.snowball.ext.PorterStemmer;


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

		EnglishAnalyzer analyzer = new EnglishAnalyzer();

		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		writer = new IndexWriter(docDir, iwc);
		
		//TODO Find a way to delete the last index when we create a new one.
	}

	/**
	 * Method that creates a document that can be analyzed
	 * @param f - The text file that is being indexed
	 * @return The document
	 * @throws IOException
	 */
	public Document addDoc(File f) throws IOException {
		Document doc = new Document();
		FileReader fileReader = new FileReader(f);
		FieldType ft = new FieldType();
		ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);

		doc.add(new Field(LuceneConstants.CONTENTS, fileReader, ft));
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


	/**
	 * Creates a Document then adds the Document to the IndexWriter
	 * @param f - File being indexed
	 * @throws IOException
	 */
	public void indexFile(File f) throws IOException {
		Document doc = addDoc(f);
		writer.addDocument(doc);
	}

	/**
	 * Creates the index using a file path of where the data is located and a file filter
	 * @param dataDirPath - Directory where the text files to be indexed are located
	 * @param filter - Filter for a specific file format
	 * @return The number of files that were indexed.
	 * @throws IOException
	 */
	public int createIndex(String dataDirPath, FileFilter filter) 
			throws IOException{
		//get all files in the data directory
		File[] files = new File(dataDirPath).listFiles();

		//a bunch of checks to see if the file can be used for indexing
		for (File file : files) {
			if(!file.isDirectory() && !file.isHidden() && file.exists() && file.canRead() && filter.accept(file)) {
				indexFile(file);
			}
		}
		return writer.numDocs();
	}
}

