package searcherClasses;
import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
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

public class UserSearcher 
{

	public static void main(String[] args) throws IOException, ParseException
	{
		//Parse provided index directory
		String indexDir = "indexed\\uUserIndex";					
		//Parse provided query string
		String queryString = "946";

		search(indexDir, queryString);
	}
	
	public static void search(String indexDir, String q)
		    throws IOException, ParseException {
		    File file=new File(indexDir);
		    //#3 Open index
		    Directory index = FSDirectory.open(file.toPath()); //3
		    IndexReader reader = DirectoryReader.open(index);
		    IndexSearcher searcher = new IndexSearcher(reader);
		    
		    //Parse query
		    QueryParser parser = new QueryParser( "userID", new StandardAnalyzer());
		     Query query = parser.parse(q);               
		    long start = System.currentTimeMillis();
		    //Search index
		    TopDocs hits = searcher.search(query, 30);		
		    long end = System.currentTimeMillis();


		    //Write search stats
		    System.err.println("Found " + hits.totalHits + " topics(s) (in " + (end - start) + " milliseconds) that matched query '" + q + "':");
		    
		    for(ScoreDoc scoreDoc : hits.scoreDocs) {
		    	//Retrieve matching document
		      Document document = searcher.doc(scoreDoc.doc);                    
		      //#8 Display filename
		      System.out.print(document.get("userID") + " ");
		      System.out.print(document.get("userAge") + " ");
		      System.out.print(document.get("userGender") + " ");
		      System.out.print(document.get("userOccupation") + " ");
		      System.out.print(document.get("userZipCode") + " ");
		    }
		    //Close IndexSearcher
		   reader.close();                        
		 
		  }
}
