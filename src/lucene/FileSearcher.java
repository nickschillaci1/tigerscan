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

public class FileSearcher {
	QueryParser queryParser;
	IndexSearcher indexSearcher;
	Query query;

	public static void main(String args[]) throws ParseException
	{
		try {
			FileSearcher testorino = new FileSearcher("C:\\Users\\Ryan\\Desktop\\Index");
			System.out.println(testorino.queryParser.parse("amenities"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public FileSearcher(String indexDirPath) throws IOException {
		queryParser = new QueryParser(LuceneConstants.CONTENTS, new EnglishAnalyzer());

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexDirPath)));
		indexSearcher = new IndexSearcher(reader);
	}

	public TopDocs search( String searchQuery) throws IOException, ParseException {
		query = queryParser.parse(searchQuery);
		return indexSearcher.search(query, LuceneConstants.MAX_SEARCH);
	}

	public Document getDocument(ScoreDoc scoreDoc) throws CorruptIndexException, IOException {
		return indexSearcher.doc(scoreDoc.doc);
	}
	
	public String stemTerm (String term) {
		PorterStemmer stemmer = new PorterStemmer();
		stemmer.setCurrent(term);
		stemmer.stem();
		return stemmer.getCurrent();
	}
}
