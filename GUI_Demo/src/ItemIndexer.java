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
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;


public class ItemIndexer
{
	
	public static void main(String[] args) throws IOException
	{
		//Επιλογή φακέλου και αρχείου για δημιουργία του ευρετηρίου
		FileInputStream stream = new FileInputStream("ml-100k\\u.item");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get("indexed\\uItemIndex"));	//Σε περίπτωση που υπάρχει ήδη το αρχείο κάνει append όχι overwrite
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		writer.commit();
		
		String line, genre = "-";				
		String[] item;
		String id, title, releaseDate, imdbURL;
		String[] genreTitles = {"Uknown", "Action","Adventure","Animation","Childrens","Comedy", "Crime", "Documentary", "Drama", "Fantasy", "Film-Noir", "Horror", "Musical", "Mystery", "Romance", "Sci-Fi", "Thriller", "War", "Western"};
		
		//Genre: uknown, action, adventure, animation, childrens, comedy, crime, documentary, drama,fantasy, noir, horror, musical, mystery, romance, sciFi, thriller, war, western;
		//Προσωρινή μεταβλητή για αποθήκευση του id
		int temp;
		
		//Μεταβλητή για τον υπολογισμό χρόνου(αρχή ευρετηριοποίησης)
		long start = System.currentTimeMillis();
		//Διάβασμα γραμμή προς γραμμή
		while (((line  = reader.readLine()) != null))
		{
			//Διαχωρισμός γραμμής σε πεδία
			item=line.split("[|]");
			temp = Integer.parseInt(item[0]);
			id = String.format("%04d", temp);
			title = item[1];
			releaseDate = item[2];
			// item[3] is empty
			imdbURL = item[4];
			/*uknown = item[5];
			action = item[6];
			adventure = item[7];
			animation = item[8];
			childrens = item[9];
			comedy = item[10];
			crime = item[11];
			documentary = item[12];
			drama = item[13];
			fantasy = item[14];
			noir = item[15];
			horror = item[16];
			musical = item[17];
			mystery = item[18];
			romance = item[19];
			sciFi = item[20];
			thriller = item[21];
			war = item[22];
			western = item[23];*/
			for(int i= 0; i < genreTitles.length; i++)
			{
				if(item[i+5].equals("1"))
					genre += genreTitles[i] + "-";
			}
			
			//Εκτύπωση ευρετηριοποιημένω στοιχείων
			System.out.println("id: " + id);
			System.out.println("title: " + title);
			System.out.println("release date: " + releaseDate);
			System.out.println("imdbURL: " + imdbURL);
			System.out.println("genre: " + genre);
			
			
			//Προσθήκη στο ευρετήριο
			addDoc(writer,id, title, releaseDate, imdbURL, genre);
			genre = "-";
			System.out.println("Ευρετηριοποιείται το αρχείο: " + id);
			}
			
			//Μεταβλητή για τον υπολογισμό χρόνου(τέλος ευρετηριοποίησης)
			long end = System.currentTimeMillis();
			System.out.println("\nΗ ευρετηριοποίηση διήρκεσε " + (end - start) + " milliseconds");
			
			reader.close();
		
	}

	//Ευρετηριοποίηση
	public static void addDoc(IndexWriter writer, String id, String title, String releaseDate, String imdbURL, String genre)  throws IOException
	{
		Document document = new Document();
		
		document.add(new StringField("id", id, Field.Store.YES));
		document.add(new TextField("title", title, Field.Store.YES));
		document.add(new TextField("releaseDate", releaseDate, Field.Store.YES));
		document.add(new StringField("imdbURL", imdbURL, Field.Store.YES));
		document.add(new TextField("genre", genre, Field.Store.YES));
		
		writer.addDocument(document);
		writer.commit();
	}
}


