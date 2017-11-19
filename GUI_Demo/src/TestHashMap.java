import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class TestHashMap 
{

	private static String indexDataDirectory = "indexed\\uDataIndex";
	private static String indexItemDirectory = "indexed\\uItemIndex";
	private static String testDirectory = "indexed\\testing1310";
	
	public static void main(String[] args) throws IOException, ParseException
	{
		//Parse provided index directory
		//String indexDataDirectory = "indexed\\uDataIndex";
		//Parse provided query string
		String queryString = "275";

		search(testDirectory, queryString);
	}
	
	public static void search(String indexDirectory, String queryString) throws IOException, ParseException {
		File file = new File(indexDirectory);
		
		//Open index
		Directory directory = FSDirectory.open(file.toPath()); //3
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		    
		//Parse query
		//QueryParser parser = new QueryParser( "dataUserID", new StandardAnalyzer());
		//String[] fields = {"dataItemID", "dataRating","dataUserID"};
		String[] fields = {"dataUserID"};
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
		Query query = parser.parse(queryString);               
		     
		//long start = System.currentTimeMillis();
		
		//Search index
		TopDocs hits = indexSearcher.search(query, 1000);		
		
		//long end = System.currentTimeMillis();

		//Write search stats
		//System.err.println("Found " + hits.totalHits + " topics(s) (in " + (end - start) + " milliseconds) that matched query '" + queryString + "':");
		
		//Counting variable
		int i =1;
		
		for(ScoreDoc scoreDoc : hits.scoreDocs) {
			//Retrieve matching document
			Document document = indexSearcher.doc(scoreDoc.doc);                    
			//Display
			System.out.println();
			System.out.println(i);
			System.out.print(document.get("dataUserID") + " ");
			System.out.print(document.get("dataItemID") + " ");
		   
			String itemQueryString = document.get("dataItemID");
			File itemFile = new File(indexItemDirectory);
			//titleSearch(itemQueryString,itemFile);
		      
			System.out.print(document.get("dataRating") + " ");
			System.out.print(document.get("dataTimestamp") + " ");
			System.out.println();
			i++;
		}
		
		//Close IndexSearcher
		indexReader.close();                        
		 
	}
	
	public static void titleSearch(String itemQueryString, File itemFile) throws IOException, ParseException{
		//Open index
		Directory itemDirectory = FSDirectory.open(itemFile.toPath()); //3
		IndexReader itemIndexReader = DirectoryReader.open(itemDirectory);
		IndexSearcher itemIndexSearcher = new IndexSearcher(itemIndexReader);
		    
		//Parse query
		//QueryParser parser2 = new QueryParser( "id", new StandardAnalyzer());
		String[] itemFields = {"id", "genre"};
		MultiFieldQueryParser itemParser = new MultiFieldQueryParser( itemFields, new StandardAnalyzer());
		Query itemQuery = itemParser.parse(itemQueryString);               
		    
		//Search index
		TopDocs itemHits = itemIndexSearcher.search(itemQuery, 30);
		for(ScoreDoc itemScoreDoc : itemHits.scoreDocs) {
			//Retrieve matching document
			Document itemDocument = itemIndexSearcher.doc(itemScoreDoc.doc);                    
		    //Display
			//System.out.print(itemDocument.get("id") + " ");
			System.out.print(itemDocument.get("title") + " ");
			System.out.print(itemDocument.get("releaseDate") + " ");
			//System.out.print(itemDocument.get("imdbURL") + " ");
			System.out.print(itemDocument.get("genre") + " ");
		}
		   
		//Close Index Reader
		itemIndexReader.close();                        
		 
	
	}
}
