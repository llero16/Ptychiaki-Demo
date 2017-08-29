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
		//������� ������� ��� ������� ��� ���������� ��� ����������
		FileInputStream stream = new FileInputStream("ml-100k\\u.user");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get("indexed\\uUserIndex"));	//�� ��������� ��� ������� ��� �� ������ ����� append ��� overwrite
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		writer.commit();
		
		String line;				
		String[] user;
		String userID, userAge, userGender, userOccupation, userZipCode;
		
		//��������� ��������� ��� ���������� ��� id
		int temp;
		
		//��������� ��� ��� ���������� ������(���� ����������������)
		long start = System.currentTimeMillis();
		//�������� ������ ���� ������
		while (((line  = reader.readLine()) != null))
		{
			//����������� ������� �� �����
			user=line.split("[|]");
			temp = Integer.parseInt(user[0]);
			userID = String.format("%03d", temp);
			userAge = user[1];
			userGender = user[2];
			userOccupation = user[3];
			userZipCode = user[4];
			
			
			//�������� ����������������� ���������
			System.out.println("id: " + userID);
			System.out.println("age: " + userAge);
			System.out.println("gender: " + userGender);
			System.out.println("occupation: " + userOccupation);
			System.out.println("zipcode: " + userZipCode);
		
			System.out.println();
			
			//�������� ��� ���������
			addDoc(writer,userID, userAge, userGender, userOccupation, userZipCode);
			System.out.println("����������������� �� ������: " + userID);
			}
			
			//��������� ��� ��� ���������� ������(����� ����������������)
			long end = System.currentTimeMillis();
			System.out.println("\n� ��������������� �������� " + (end - start) + " milliseconds");
			
			reader.close();
		
	}

	//���������������
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


