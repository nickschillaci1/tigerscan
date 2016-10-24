package lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;

public class FileSearcher {
	QueryParser qp;
	
	public FileSearcher(String indexDirPath) {
		qp = new QueryParser(LuceneConstants.CONTENTS, new StandardAnalyzer());
		
	}
}
