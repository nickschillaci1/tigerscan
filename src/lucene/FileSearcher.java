package lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.tartarus.snowball.ext.PorterStemmer;

/**
 * This class searches on the Index Directory created by FileIndexer
 * @author Ryan Hudson
 * @version 1.0
 */
public class FileSearcher {
	QueryParser queryParser;
	IndexSearcher indexSearcher;
	Query query;
	
	/**
	 * Creates a Query Parser from one of lucene's analyzer's, opens the index directory,
	 * then creates an indexSearcher from the index.
	 * @param indexDirPath - File path of the directory created by FileIndexer
	 * @throws IOException
	 */
	public FileSearcher(String indexDirPath) throws IOException {
		queryParser = new QueryParser(LuceneConstants.CONTENTS, new EnglishAnalyzer());

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDirPath)));
		indexSearcher = new IndexSearcher(reader);
	}
	
	/**
	 * Performs a search on the specified query through all documents in the index.
	 * @param searchQuery - Term or word that the user wants to search for in the documents.
	 * @return - Returns the search results as TopDocs (represents hits returned by 
	 * IndexSearcher.search)
	 * @throws IOException
	 * @throws ParseException
	 */
	public TopDocs search(String searchQuery) throws IOException, ParseException {
		query = queryParser.parse(searchQuery);
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
	}

	/**
	 * Grabs documents that had hits for the search query.
	 * @param scoreDoc - A document that has a hit from the search query.
	 * @return - The document.
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
		return indexSearcher.doc(scoreDoc.doc);
	}
}
