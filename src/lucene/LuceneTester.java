package lucene;

import java.io.IOException;

/**
 * Simple test of the Indexing process, requires user to declare file path of the data
 * they want to index and file path of where the index will be stored.
 */
public class LuceneTester {
	
   String indexDir = "C:\\Users\\Ryan\\Desktop\\Index";
   String dataDir = "C:\\Users\\Ryan\\Desktop\\test directory";
   FileIndexer indexer;
   
   public static void main(String[] args) {
      LuceneTester tester;
      try {
         tester = new LuceneTester();
         tester.createIndex();
      } catch (IOException e) {
         e.printStackTrace();
      } 
   }

   private void createIndex() throws IOException{
      indexer = new FileIndexer(indexDir);
      int numIndexed;
      long startTime = System.currentTimeMillis();	
      numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
      long endTime = System.currentTimeMillis();
      indexer.closeIndex();
      System.out.println(numIndexed+" File indexed, time taken: "
         +(endTime-startTime)+" ms");		
   }
}