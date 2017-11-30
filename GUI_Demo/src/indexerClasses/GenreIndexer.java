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
		//������� ������� ��� ������� ��� ���������� ��� ����������
		FileInputStream stream = new FileInputStream("ml-100k\\u.genre");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get("indexed\\uGenreIndex"));	//�� ��������� ��� ������� ��� �� ������ ����� append ��� overwrite
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		writer.commit();
		
		String line;				
		String[] occupation;
		String genreName, genreID;
		
		//��������� ��������� ��� ���������� ��� id
		int temp;
		
		//��������� ��� ��� ���������� ������(���� ����������������)
		long start = System.currentTimeMillis();
		//�������� ������ ���� ������
		while (((line  = reader.readLine()) != null))
		{
			//����������� ������� �� �����
			occupation=line.split("[|]");
			genreName = occupation[0];
			temp = Integer.parseInt(occupation[1]);
			genreID = String.format("%02d", temp);
			genreName = occupation[0];
			
			
			
			//�������� ������������������ ���������
			System.out.println("occupation: " + genreName);
			System.out.println("id: " + genreID);
			System.out.println();
			
			//�������� ��� ���������
			addDoc(writer,genreName, genreID);
			//System.out.println("����������������� �� ������: " + genreName);
			}
			
			//��������� ��� ��� ���������� ������(����� ����������������)
			long end = System.currentTimeMillis();
			System.out.println("\n� ��������������� �������� " + (end - start) + " milliseconds");
			
			reader.close();
		
	}

	//���������������
	public static void addDoc(IndexWriter writer, String name, String id)  throws IOException
	{
		Document document = new Document();
		
		document.add(new StringField("genreName", name, Field.Store.YES));
		document.add(new StringField("genreID", id, Field.Store.YES));
		
		writer.addDocument(document);
		writer.commit();
	}
}



