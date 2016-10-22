package lucene;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class FileIndexer {

	private IndexWriter writer;


	public FileIndexer(String indexDirectoryPath) throws IOException {
		//this directory will contain the indexes
		Directory docDir = FSDirectory.open(Paths.get(indexDirectoryPath));

		StandardAnalyzer analyzer = new StandardAnalyzer();

		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(docDir, iwc);


		writer.close();
	}




}
