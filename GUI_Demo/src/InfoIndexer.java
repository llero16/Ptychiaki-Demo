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


public class InfoIndexer
{
	
	public static void main(String[] args) throws IOException
	{
		//Επιλογή φακέλου και αρχείου για δημιουργία του ευρετηρίου
		FileInputStream stream = new FileInputStream("ml-100k\\u.info");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get("indexed\\uInfoIndex"));	//Σε περίπτωση που υπάρχει ήδη το αρχείο κάνει append όχι overwrite
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		writer.commit();
		
		String line;				
		String[] info;
		String infoUsers = null, infoItems = null, infoRatings = null;
		
	
		//Μεταβλητή για τον υπολογισμό χρόνου(αρχή ευρετηριοποίησης)
		long start = System.currentTimeMillis();
		
		int i = 0;
		while (((line  = reader.readLine()) != null) && i < 3)
		{
			info = line.split("[ ]");
			switch(i)
			{
			case 0:
				infoUsers = info[0];
				addDoc(writer, "users", infoUsers);
				break;
			case 1:
				infoItems = info[0];
				addDoc(writer, "items", infoItems);
				break;
			case 2:
				infoRatings = info[0];
				addDoc(writer, "ratings", infoRatings);
				break;
			default:
				break;
			}
			i++;
		}
			
		//Εκτύπωση ευρετηριοποιημένων στοιχείων
		System.out.println("users: " + infoUsers);
		System.out.println("items: " + infoItems);
		System.out.println("ratings: " + infoRatings);
			
		//Προσθήκη στο ευρετήριο
		//addDoc(writer,infoUsers, infoItems, infoRatings);
		//System.out.println("Ευρετηριοποιείται το αρχείο: " + genreName);
		
			
			//Μεταβλητή για τον υπολογισμό χρόνου(τέλος ευρετηριοποίησης)
			long end = System.currentTimeMillis();
			System.out.println("\nΗ ευρετηριοποίηση διήρκεσε " + (end - start) + " milliseconds");
			
			reader.close();
		
	}

	//Ευρετηριοποίηση
	public static void addDoc(IndexWriter writer, String infoName, String infoAmount)  throws IOException
	{
		Document document = new Document();
		
		document.add(new StringField("infoName", infoName, Field.Store.YES));
		document.add(new StringField("infoAmount", infoAmount, Field.Store.YES));
		
		writer.addDocument(document);
		writer.commit();
	}
}



