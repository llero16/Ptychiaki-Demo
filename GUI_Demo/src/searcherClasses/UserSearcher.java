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
		String q = "946";

		search(indexDir, q);
	}
	
	public static void search(String indexDir, String q)
		    throws IOException, ParseException {
		    File ff=new File(indexDir);
		    //#3 Open index
		    Directory dir = FSDirectory.open(ff.toPath()); //3
		    IndexReader reader = DirectoryReader.open(dir);
		    IndexSearcher is = new IndexSearcher(reader);
		    
		    //Parse query
		    QueryParser parser = new QueryParser( "userID", new StandardAnalyzer());
		     Query query = parser.parse(q);               
		    long start = System.currentTimeMillis();
		    //Search index
		    TopDocs hits = is.search(query, 30);		
		    long end = System.currentTimeMillis();


		    //Write search stats
		    System.err.println("Found " + hits.totalHits + " topics(s) (in " + (end - start) + " milliseconds) that matched query '" + q + "':");
		    
		    for(ScoreDoc scoreDoc : hits.scoreDocs) {
		    	//Retrieve matching document
		      Document doc = is.doc(scoreDoc.doc);                    
		      //#8 Display filename
		      System.out.print(doc.get("userID") + " ");
		      System.out.print(doc.get("userAge") + " ");
		      System.out.print(doc.get("userGender") + " ");
		      System.out.print(doc.get("userOccupation") + " ");
		      System.out.print(doc.get("userZipCode") + " ");
		    }
		    //Close IndexSearcher
		   reader.close();                        
		 
		  }
}
