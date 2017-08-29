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


public class UserIndexer
{
	
	public static void main(String[] args) throws IOException
	{
		//Επιλογή φακέλου και αρχείου για δημιουργία του ευρετηρίου
		FileInputStream stream = new FileInputStream("ml-100k\\u.user");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get("indexed\\uUserIndex"));	//Σε περίπτωση που υπάρχει ήδη το αρχείο κάνει append όχι overwrite
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		writer.commit();
		
		String line;				
		String[] user;
		String userID, userAge, userGender, userOccupation, userZipCode;
		
		//Προσωρινή μεταβλητή για αποθήκευση του id
		int temp;
		
		//Μεταβλητή για τον υπολογισμό χρόνου(αρχή ευρετηριοποίησης)
		long start = System.currentTimeMillis();
		//Διάβασμα γραμμή προς γραμμή
		while (((line  = reader.readLine()) != null))
		{
			//Διαχωρισμός γραμμής σε πεδία
			user=line.split("[|]");
			temp = Integer.parseInt(user[0]);
			userID = String.format("%03d", temp);
			userAge = user[1];
			userGender = user[2];
			userOccupation = user[3];
			userZipCode = user[4];
			
			
			//Εκτύπωση ευρετηριοποιημένω στοιχείων
			System.out.println("id: " + userID);
			System.out.println("age: " + userAge);
			System.out.println("gender: " + userGender);
			System.out.println("occupation: " + userOccupation);
			System.out.println("zipcode: " + userZipCode);
		
			System.out.println();
			
			//Προσθήκη στο ευρετήριο
			addDoc(writer,userID, userAge, userGender, userOccupation, userZipCode);
			System.out.println("Ευρετηριοποιείται το αρχείο: " + userID);
			}
			
			//Μεταβλητή για τον υπολογισμό χρόνου(τέλος ευρετηριοποίησης)
			long end = System.currentTimeMillis();
			System.out.println("\nΗ ευρετηριοποίηση διήρκεσε " + (end - start) + " milliseconds");
			
			reader.close();
		
	}

	//Ευρετηριοποίηση
	public static void addDoc(IndexWriter writer, String id, String age, String gender, String occupation, String zipCode)  throws IOException
	{
		Document document = new Document();
		
		document.add(new StringField("userID", id, Field.Store.YES));
		document.add(new StringField("userAge", age, Field.Store.YES));
		document.add(new StringField("userGender", gender, Field.Store.YES));
		document.add(new StringField("userOccupation", occupation, Field.Store.YES));
		document.add(new StringField("userZipCode", zipCode, Field.Store.YES));
		
		
		writer.addDocument(document);
		writer.commit();
	}
}


