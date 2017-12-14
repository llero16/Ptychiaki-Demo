package testingClasses;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class SearchingAndIndexing {

	private String indexDataDirectory = "indexed\\uDataIndex";
	//private static  String indexItemDirectory = "indexed\\uItemIndex";
	private String indexUsersItemsNoDirectory = "indexed\\Users&Items";
	
	private int users;
	private int items;
	
	public SearchingAndIndexing(int users, int items) 
	{
		users = this.users;
		items = this.items;
	}
	
	// Read Number of Users
	public int getUsersNo() throws IOException {
		File file = new File(indexUsersItemsNoDirectory);
		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		int usersNo = 0;
		for (int i = 0; i < indexReader.maxDoc(); i++) {
			Document document = indexReader.document(i);
			System.out.println("Users: " + document.get("UsersNo"));
			usersNo = Integer.valueOf(document.get("UsersNo"));
		}
		indexReader.close();
		return usersNo;
	}

	// Read Number of Items
	public int getItemsNo() throws IOException {
		File file = new File(indexUsersItemsNoDirectory);
		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		int itemsNo = 0;
		for (int i = 0; i < indexReader.maxDoc(); i++) {
			Document document = indexReader.document(i);
			itemsNo = Integer.valueOf(document.get("ItemsNo"));
			System.out.println("Items: " + document.get("ItemsNo"));
		}
		indexReader.close();
		return itemsNo;
	}

/*	// Returns ArrayList with Titles instead of Item IDs
	public static ArrayList<String> replaceIdWithTitle(ArrayList<String> highlyRatedMovies)
			throws IOException, ParseException {
		String titleTemp;
		ArrayList<String> titlesList = new ArrayList<String>();
		for (String suggestion : highlyRatedMovies) {
			String itemQueryString = suggestion;
			File itemFile = new File(indexItemDirectory);
			titleTemp = searchItemTitle(itemQueryString, itemFile);
			titlesList.add(titleTemp);
		}

		return titlesList;
	}*/

/*	// Returns Title(String) of the given Item ID
	public static String searchItemTitle(String itemQueryString, File itemFile) throws IOException, ParseException {
		// Open index
		String title = "";
		Directory itemDirectory = FSDirectory.open(itemFile.toPath()); // 3
		IndexReader itemIndexReader = DirectoryReader.open(itemDirectory);
		IndexSearcher itemIndexSearcher = new IndexSearcher(itemIndexReader);

		// Parse query
		String[] itemFields = { "id", "genre" };
		MultiFieldQueryParser itemParser = new MultiFieldQueryParser(itemFields, new StandardAnalyzer());
		Query itemQuery = itemParser.parse(itemQueryString);

		// Search index
		TopDocs itemHits = itemIndexSearcher.search(itemQuery, 30);
		for (ScoreDoc itemScoreDoc : itemHits.scoreDocs) {
			// Retrieve matching document
			Document itemDocument = itemIndexSearcher.doc(itemScoreDoc.doc);

			title = itemDocument.get("title");
		}

		// Close Index Reader
		itemIndexReader.close();

		return title;
	}*/

	// Searches and returns HashMap with found Movie Titles and the respective
	// Movie Documents containing Movie ID, Title, Release Date and Genre
	public HashMap<String, Document> searchItemDocs(String indexDirectory, String itemIDQuery, String itemTitleQuery, String itemReleaseQuery, String itemGenreQuery) throws IOException, ParseException {
		// ArrayList to Contain FieldNames
		ArrayList<String> fields = new ArrayList<String>();
		// HashMap to Contain Movie Title and the Respective Document it Belongs to
		HashMap<String, Document> documentHash = new HashMap<String, Document>();
		String queryString = " ";
		// Conditions to form final QueryString(AND)
		if (!itemIDQuery.equals("")) {
			queryString = itemIDQuery;
			fields.add("id");
		}
		if (!itemTitleQuery.equals("")) {
			if (!fields.isEmpty())
				queryString += " AND " + itemTitleQuery;
			else
				queryString = itemTitleQuery;
			fields.add("title");
		}
		if (!itemReleaseQuery.equals("")) {
			if (!fields.isEmpty())
				queryString += " AND " + itemReleaseQuery;
			else
				queryString = itemReleaseQuery;
			fields.add("releaseDate");
		}
		if (!itemGenreQuery.equals("")) {
			if (!fields.isEmpty())
				queryString += " AND " + itemGenreQuery;
			else
				queryString = itemGenreQuery;
			fields.add("genre");
		}
		
		//QueryBuilder does not function correctly with Lucene - (or maybe I can't make it work) 
		/*BooleanQuery.Builder queryBuilder = new BooleanQuery.Builder();
		Query idQuery = new TermQuery(new Term("id", itemIDQuery));
		Query titleQuery = new TermQuery(new Term("title", itemTitleQuery));
		Query releaseQuery = new TermQuery(new Term("releaseDate", itemReleaseQuery));
		Query genreQuery = new TermQuery(new Term("genre", itemGenreQuery));
		
		queryBuilder.add(idQuery, BooleanClause.Occur.MUST);
		queryBuilder.add(titleQuery, BooleanClause.Occur.MUST);
		queryBuilder.add(releaseQuery, BooleanClause.Occur.MUST);
		queryBuilder.add(genreQuery, BooleanClause.Occur.MUST);*/
		
		// Checks if there is at least one actual written TextField
		if (!fields.isEmpty()) {
			File file = new File(indexDirectory);

			// Open index
			Directory directory = FSDirectory.open(file.toPath()); // 3
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);

			// Multi-Parse the final Query
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]),
					new StandardAnalyzer());
			Query query = parser.parse(queryString);
			//Query query = parser.parse(queryBuilder.toString());

			// Calculating Search Time
			// long start = System.currentTimeMillis();

			// Search index
			TopDocs hits = indexSearcher.search(query, 25);

			// long end = System.currentTimeMillis();

			// Write search stats
			// System.err.println("Found " + hits.totalHits + " topics(s) (in "
			// + (end - start) + " milliseconds) that matched query '" +
			// queryString + "':");

			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				// Retrieve matching document
				Document document = indexSearcher.doc(scoreDoc.doc);
				// Put Movie Title and Document to HashMap
				documentHash.put(document.get("title"), document);
			}
			// Close IndexSearcher
			indexReader.close();
		}
		return documentHash;
	}

	// Matches and returns HashMap with recommended Movie Titles and the
	// respective Movie Documents containing Movie ID, Title, Release Date and Genre
	public HashMap<String, Document> recommendedItemDocs(String indexDirectory, ArrayList<String> recommendedMovieIDs) throws IOException, ParseException {
		// ArrayList to Contain FieldNames
		ArrayList<String> fields = new ArrayList<String>();
		// HashMap to Contain Movie Title and the Respective Document it Belongs
		// to
		HashMap<String, Document> documentHash = new HashMap<String, Document>();

		// Add wanted field to searched fields
		fields.add("id");

		// Match every Recommended Movie ID with the Respective Title and
		// Document
		for (String queryString : recommendedMovieIDs) {
			// Checks if there is at least one actual written TextField
			File file = new File(indexDirectory);

			// Open index
			Directory directory = FSDirectory.open(file.toPath());
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);

			// Multi-Parse the final Query
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]),
					new StandardAnalyzer());
			Query query = parser.parse(queryString);

			// Calculating Search Time
			// long start = System.currentTimeMillis();

			// Search index
			TopDocs hits = indexSearcher.search(query, 25);

			// long end = System.currentTimeMillis();

			// Write search stats
			// System.err.println("Found " + hits.totalHits + " topics(s) (in "
			// + (end - start) + " milliseconds) that matched query '" +
			// queryString + "':");

			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				// Retrieve matching document
				Document document = indexSearcher.doc(scoreDoc.doc);
				// Put Movie Title and Document to HashMap
				documentHash.put(document.get("title"), document);
			}

			// Close IndexSearcher
			indexReader.close();
		}
		return documentHash;
	}
	
	/*// Given the Item ID it returns the respective Average Rating
	public String searchItemRating(String itemQueryString, File itemFile) throws IOException, ParseException {
		// Open index
		Directory itemDirectory = FSDirectory.open(itemFile.toPath()); // 3
		IndexReader itemIndexReader = DirectoryReader.open(itemDirectory);
		IndexSearcher itemIndexSearcher = new IndexSearcher(itemIndexReader);

		String[] itemFields = { "itemID" };
		MultiFieldQueryParser itemParser = new MultiFieldQueryParser(itemFields, new StandardAnalyzer());
		Query itemQuery = itemParser.parse(itemQueryString);
		String rating = null;

		// Search index
		TopDocs itemHits = itemIndexSearcher.search(itemQuery, 30);
		for (ScoreDoc itemScoreDoc : itemHits.scoreDocs) {
			// Retrieve matching document
			Document itemDocument = itemIndexSearcher.doc(itemScoreDoc.doc);
			rating = itemDocument.get("itemRating");
		}

		// Close Index Reader
		itemIndexReader.close();
		return rating;
	}
*/
	// Rates Item using UserID and ItemID
	public void rateDataItem(String userID, String itemID, String rating) throws IOException, ParseException {
		if (searchDataRating(userID, itemID) != null)
			deleteDataDoc(userID, itemID);
		addDataDoc(userID, itemID, rating);
	}
	
	// Given UserID & ItemID returns the respective Rating if this exists
	public String searchDataRating(String userQuery, String itemQuery) throws IOException, ParseException {

		File file = new File(indexDataDirectory);
		Directory directory = FSDirectory.open(file.toPath()); 
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		String queryString = userQuery + " AND " + itemQuery;
		String rating = " - ";
		String[] fields = { "dataUserID", "dataItemID" };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
		Query query = parser.parse(queryString);

		TopDocs hits = indexSearcher.search(query, 10);
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			rating = document.get("dataRating");
		}

		indexReader.close();
		// System.out.print(rating);
		return rating;
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
	public void addUserDoc(String id, String age, String gender, String occupation, String zipCode)
			throws IOException {
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get(indexDataDirectory));
		//Σε περίπτωση που υπάρχει ήδη το αρχείο κάνει append όχι overwrite
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
		//MainFrame mainFrame = new MainFrame();
		//usernameNoSULabel.setText("Your UserID will be: " + (users + 1));
		System.out.println("Users are now " + users);

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
		System.out.println("Users: " + users);

		document.add(new StringField("ItemsNo", String.format("%04d", items), Field.Store.YES));
		System.out.println("Items: " + items);

		document.add(new StringField("Timestamp", timestamp, Field.Store.YES));
		System.out.println("Date: " + timestamp);

		writer.addDocument(document);
		// writer.commit();
		writer.close();
	}
	


}
