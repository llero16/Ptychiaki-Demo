package indexerClasses;
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


public class GenreIndexer
{
	
	public static void main(String[] args) throws IOException
	{
		//Επιλογή φακέλου και αρχείου για δημιουργία του ευρετηρίου
		FileInputStream stream = new FileInputStream("ml-100k\\u.genre");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get("indexed\\uGenreIndex"));	//Σε περίπτωση που υπάρχει ήδη το αρχείο κάνει append όχι overwrite
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		writer.commit();
		
		String line;				
		String[] occupation;
		String genreName, genreID;
		
		//Προσωρινή μεταβλητή για αποθήκευση του id
		int temp;
		
		//Μεταβλητή για τον υπολογισμό χρόνου(αρχή ευρετηριοποίησης)
		long start = System.currentTimeMillis();
		//Διάβασμα γραμμή προς γραμμή
		while (((line  = reader.readLine()) != null))
		{
			//Διαχωρισμός γραμμής σε πεδία
			occupation=line.split("[|]");
			genreName = occupation[0];
			temp = Integer.parseInt(occupation[1]);
			genreID = String.format("%02d", temp);
			genreName = occupation[0];
			
			
			
			//Εκτύπωση ευρετηριοποιημένων στοιχείων
			System.out.println("occupation: " + genreName);
			System.out.println("id: " + genreID);
			System.out.println();
			
			//Προσθήκη στο ευρετήριο
			addDoc(writer,genreName, genreID);
			//System.out.println("Ευρετηριοποιείται το αρχείο: " + genreName);
			}
			
			//Μεταβλητή για τον υπολογισμό χρόνου(τέλος ευρετηριοποίησης)
			long end = System.currentTimeMillis();
			System.out.println("\nΗ ευρετηριοποίηση διήρκεσε " + (end - start) + " milliseconds");
			
			reader.close();
		
	}

	//Ευρετηριοποίηση
	public static void addDoc(IndexWriter writer, String name, String id)  throws IOException
	{
		Document document = new Document();
		
		document.add(new StringField("genreName", name, Field.Store.YES));
		document.add(new StringField("genreID", id, Field.Store.YES));
		
		writer.addDocument(document);
		writer.commit();
	}
}



