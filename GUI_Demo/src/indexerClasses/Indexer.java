package indexerClasses;

import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.FSDirectory;

import searcherClasses.Searcher;

public class Indexer {

	private String indexDataDirectory = "indexed\\uDataIndex";
	// private static String indexItemDirectory = "indexed\\uItemIndex";
	private String indexUsersItemsNoDirectory = "indexed\\Users&Items";

	private int users;
	private int items;
	// private static User user;
	private static Searcher dataSearcher = new Searcher();

	public Indexer() throws IOException {
		users = dataSearcher.getUsersNo();
		items = dataSearcher.getItemsNo();
		// user = Indexer.user;
	}

	// Rates Item using UserID and ItemID
	public void rateDataItem(String userID, String itemID, String rating) throws IOException, ParseException {
		if (dataSearcher.searchDataRating(userID, itemID) != null)
			deleteDataDoc(userID, itemID);
		addDataDoc(userID, itemID, rating);
	}

	// Adds new or updates existing Item Rating
	public void addDataDoc(String userID, String itemID, String rating) throws IOException {

		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get(indexDataDirectory));
		// Σε περίπτωση που υπάρχει ήδη το αρχείο κάνει append όχι overwrite
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		writer.commit();
		Document document = new Document();

		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

		document.add(new StringField("dataUserID", userID, Field.Store.YES));
		document.add(new StringField("dataItemID", itemID, Field.Store.YES));
		document.add(new StringField("dataRating", rating, Field.Store.YES));
		document.add(new StringField("dataTimestamp", timestamp, Field.Store.YES));

		writer.addDocument(document);
		writer.commit();
		writer.close();
	}

	// Deletes existing Item Rating given UserID and ItemID
	public void deleteDataDoc(String userID, String itemID) throws IOException {
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get(indexDataDirectory));
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		writer.commit();

		BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		Query userQuery = new TermQuery(new Term("dataUserID", userID));
		Query itemQuery = new TermQuery(new Term("dataItemID", itemID));

		queryBuilder.add(userQuery, BooleanClause.Occur.MUST);
		queryBuilder.add(itemQuery, BooleanClause.Occur.MUST);

		writer.deleteDocuments(queryBuilder.build());
		writer.close();
	}

	// Add Rating (Index new Document)
	public void addUserDoc(String id, String age, String gender, String occupation, String zipCode) throws IOException {
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get(indexDataDirectory));
		// Σε περίπτωση που υπάρχει ήδη το αρχείο κάνει append όχι overwrite
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		writer.commit();
		Document document = new Document();

		document.add(new StringField("userID", id, Field.Store.YES));
		document.add(new StringField("userAge", age, Field.Store.YES));
		document.add(new StringField("userGender", gender, Field.Store.YES));
		document.add(new StringField("userOccupation", occupation, Field.Store.YES));
		document.add(new StringField("userZipCode", zipCode, Field.Store.YES));

		writer.addDocument(document);
		writer.commit();
		writer.close();

		users++;
		// MainFrame mainFrame = new MainFrame();
		// usernameNoSULabel.setText("Your UserID will be: " + (users + 1));

		indexUsersItemsNo(users, items);
	}

	// Indexes Number of Users & Items
	public void indexUsersItemsNo(int users, int items) throws IOException {

		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get(indexUsersItemsNoDirectory));
		// Σε περίπτωση που υπάρχει ήδη το αρχείο κάνει append όχι overwrite
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		// writer.commit();
		writer.deleteAll();
		Document document = new Document();

		String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());

		document.add(new StringField("UsersNo", String.format("%03d", users), Field.Store.YES));
		System.out.println("\nUsers: " + users);

		document.add(new StringField("ItemsNo", String.format("%04d", items), Field.Store.YES));
		System.out.println("Items: " + items);

		document.add(new StringField("Timestamp", timestamp, Field.Store.YES));
		System.out.println("Date: " + timestamp);

		writer.addDocument(document);
		// writer.commit();
		writer.close();
	}
}
