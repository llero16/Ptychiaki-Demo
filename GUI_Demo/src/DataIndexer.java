import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;


public class DataIndexer
{
	
	public static void main(String[] args) throws IOException
	{
		//Επιλογή φακέλου και αρχείου για δημιουργία του ευρετηρίου
		FileInputStream stream = new FileInputStream("ml-100k\\u.data");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get("indexed\\uDataIndex"));	//Σε περίπτωση που υπάρχει ήδη το αρχείο κάνει append όχι overwrite
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		writer.commit();
		
		String line;				
		String[] data;
		String dataUserID, dataItemID, dataRating, dataTimestamp;
		
		//Προσωρινή μεταβλητή για αποθήκευση του id
		int tempUser, tempItem;
		
		//Μεταβλητή για τον υπολογισμό χρόνου(αρχή ευρετηριοποίησης)
		long start = System.currentTimeMillis();
		//Διάβασμα γραμμή προς γραμμή
		while (((line  = reader.readLine()) != null))
		{
			//Διαχωρισμός γραμμής σε πεδία
			data=line.split("\\t");
			tempUser = Integer.parseInt(data[0]);
			dataUserID = String.format("%03d", tempUser);
			tempItem = Integer.parseInt(data[1]);
			dataItemID = String.format("%04d", tempItem);
			dataRating = data[2];
			dataTimestamp = data[3];
			
			
			
			//Εκτύπωση ευρετηριοποιημένω στοιχείων
			System.out.println("user: " + dataUserID);
			System.out.println("item: " + dataItemID);
			System.out.println("rating: " + dataRating);
			System.out.println("timestamp: " + dataTimestamp);
			System.out.println();
			
			//Προσθήκη στο ευρετήριο
			addDoc(writer,dataUserID, dataItemID, dataRating, dataTimestamp);
			System.out.println("Ευρετηριοποιείται το αρχείο: " + dataUserID);
			}
			
			//Μεταβλητή για τον υπολογισμό χρόνου(τέλος ευρετηριοποίησης)
			long end = System.currentTimeMillis();
			System.out.println("\nΗ ευρετηριοποίηση διήρκεσε " + (end - start) + " milliseconds");
			
			reader.close();
		
	}

	//Ευρετηριοποίηση
	public static void addDoc(IndexWriter writer, String userID, String itemID, String rating, String timestamp)  throws IOException
	{
		Document document = new Document();
		
		document.add(new StringField("dataUserID", userID, Field.Store.YES));
		document.add(new StringField("dataItemID", itemID, Field.Store.YES));
		document.add(new StringField("dataRating", rating, Field.Store.YES));
		document.add(new StringField("dataTimestamp", timestamp, Field.Store.YES));
		
		
		writer.addDocument(document);
		writer.commit();
	}
}


