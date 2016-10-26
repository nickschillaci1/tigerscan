package lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class FileSearcher {
	QueryParser queryParser;
	IndexSearcher indexSearcher;
	Query query;

	public FileSearcher(String indexDirPath) throws IOException {
		queryParser = new QueryParser(LuceneConstants.CONTENTS, new StandardAnalyzer());

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
}
