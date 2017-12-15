package searcherClasses;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {

	// Read Number of Users
	public int getUsersNo(String indexDirectory) throws IOException {
		File file = new File(indexDirectory);
		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		int usersNo = 0;
		for (int i = 0; i < indexReader.maxDoc(); i++) {
			Document document = indexReader.document(i);
			usersNo = Integer.valueOf(document.get("UsersNo"));
		}
		indexReader.close();
		return usersNo;
	}

	// Read Number of Items
	public int getItemsNo(String indexDirectory) throws IOException {
		File file = new File(indexDirectory);
		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		int itemsNo = 0;
		for (int i = 0; i < indexReader.maxDoc(); i++) {
			Document document = indexReader.document(i);
			itemsNo = Integer.valueOf(document.get("ItemsNo"));
		}
		indexReader.close();
		return itemsNo;
	}

	// Searches and returns HashMap with found Movie Titles and the respective
	// Movie Documents containing Movie ID, Title, Release Date and Genre
	public HashMap<String, Document> searchItemDocs(String indexDirectory, String itemIDQuery, String itemTitleQuery,
			String itemReleaseQuery, String itemGenreQuery) throws IOException, ParseException {
		// ArrayList to Contain FieldNames
		ArrayList<String> fields = new ArrayList<String>();
		// HashMap to Contain Movie Title and the Respective Document it Belongs
		// to
		HashMap<String, Document> documentMap = new HashMap<String, Document>();
		String queryString = " ";
		// Conditions to form final QueryString(AND)
		// Tried queryBuilder, got tired, dropped it
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

		// Checks if there is at least one actual written TextField
		if (!fields.isEmpty()) {
			File file = new File(indexDirectory);

			// Open index
			Directory directory = FSDirectory.open(file.toPath());
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);

			// Multi-Parse the final Query
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]),
					new StandardAnalyzer());
			Query query = parser.parse(queryString);
			// Query query = parser.parse(queryBuilder.toString());

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
				documentMap.put(document.get("title"), document);
			}
			// Close IndexSearcher
			indexReader.close();
		}
		return documentMap;
	}

	// Matches and returns HashMap with recommended Movie Titles and the
	// respective Movie Documents containing Movie ID, Title, Release Date and
	// Genre
	public HashMap<String, Document> recommendedItemDocs(String indexDirectory, ArrayList<String> recommendedMovieIDs)
			throws IOException, ParseException {
		// ArrayList to Contain FieldNames
		ArrayList<String> fields = new ArrayList<String>();
		// HashMap to Contain Movie Title and the Respective Document it Belongs
		// to
		LinkedHashMap<String, Document> documentMap = new LinkedHashMap<String, Document>();

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
			TopDocs hits = indexSearcher.search(query, 1);

			// long end = System.currentTimeMillis();

			// Write search stats
			// System.err.println("Found " + hits.totalHits + " topics(s) (in "
			// + (end - start) + " milliseconds) that matched query '" +
			// queryString + "':");
			Document document = null;

			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				// Retrieve matching document
				document = indexSearcher.doc(scoreDoc.doc);
				// Put Movie Title and Document to HashMap
				documentMap.put(document.get("title"), document);

			}

			// Close IndexSearcher
			indexReader.close();
		}

		return documentMap;
	}

	// Given UserID returns the respective User Document with ID, Age, Gender,
	// Occupation, ZipCode
	public Document searchUserDoc(String indexDirectory, String userID) throws IOException, ParseException {
		File file = new File(indexDirectory);
		// Open index
		Directory directory = FSDirectory.open(file.toPath());
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);

		// Parse query
		QueryParser parser = new QueryParser("userID", new StandardAnalyzer());
		Query query = parser.parse(userID);

		TopDocs hits = searcher.search(query, 1);
		Document document = null;

		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			// Retrieve matching document
			document = searcher.doc(scoreDoc.doc);
		}
		reader.close();

		return document;
	}

	// Given UserID & ItemID returns the respective Rating if this exists
	public String searchDataRating(String indexDirectory, String userQuery, String itemQuery)
			throws IOException, ParseException {
		File file = new File(indexDirectory);
		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		String queryString = userQuery + " AND " + itemQuery;
		String rating = " - ";
		String[] fields = { "dataUserID", "dataItemID" };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
		Query query = parser.parse(queryString);

		TopDocs hits = indexSearcher.search(query, 1);
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			rating = document.get("dataRating");
		}

		indexReader.close();
		return rating;
	}

	// Given UserID it calculates and returns his/hers Average Rating
	public HashMap<String, String> getAverageUserRating(String indexDirectory, String userQuery)
			throws IOException, ParseException {

		File file = new File(indexDirectory);
		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		String queryString = userQuery;
		String[] fields = { "dataUserID" };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
		Query query = parser.parse(queryString);
		TopDocs hits = indexSearcher.search(query, 1682);

		HashMap<String, String> averageRatingMap = new HashMap<String, String>();
		double averageRating, sum;
		String stringSum;
		int count;
		sum = 0;
		count = 0;
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			stringSum = document.get("dataRating");
			sum += Double.valueOf(stringSum);
			count++;
		}

		averageRating = sum / count;
		averageRatingMap.put("averageRating", String.format("%2.1f", averageRating));
		averageRatingMap.put("itemsRated", Integer.toString(count));

		indexReader.close();
		return averageRatingMap;
	}

	// Matches Item IDs with Titles and returns HashMap IDs as keys and Titles
	// as values
	public HashMap<String, String> matchingIDsTitlesMap(String indexDirectory, ArrayList<String> highlyRatedMovies)
			throws IOException, ParseException {
		String titleTemp;
		HashMap<String, String> titlesList = new HashMap<String, String>();
		for (String suggestion : highlyRatedMovies) {
			String itemQueryString = suggestion;
			titleTemp = searchItemTitle(indexDirectory, itemQueryString);
			titlesList.put(suggestion, titleTemp);
		}

		return titlesList;
	}

	// Given ItemID returns the respective Title if this exists
	public String searchItemTitle(String indexDirectory, String itemIDQuery) throws IOException, ParseException {
		File file = new File(indexDirectory);
		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		String queryString = itemIDQuery;
		String title = " - ";
		String[] fields = { "id" };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
		Query query = parser.parse(queryString);

		TopDocs hits = indexSearcher.search(query, 1);
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			title = document.get("title");
		}

		indexReader.close();
		return title;
	}

	/*
	 * // Given the Item ID it returns the respective Average Rating public
	 * String searchItemRating(String itemQueryString, File itemFile) throws
	 * IOException, ParseException { // Open index Directory itemDirectory =
	 * FSDirectory.open(itemFile.toPath()); // 3 IndexReader itemIndexReader =
	 * DirectoryReader.open(itemDirectory); IndexSearcher itemIndexSearcher =
	 * new IndexSearcher(itemIndexReader);
	 * 
	 * String[] itemFields = { "itemID" }; MultiFieldQueryParser itemParser =
	 * new MultiFieldQueryParser(itemFields, new StandardAnalyzer()); Query
	 * itemQuery = itemParser.parse(itemQueryString); String rating = null;
	 * 
	 * // Search index TopDocs itemHits = itemIndexSearcher.search(itemQuery,
	 * 30); for (ScoreDoc itemScoreDoc : itemHits.scoreDocs) { // Retrieve
	 * matching document Document itemDocument =
	 * itemIndexSearcher.doc(itemScoreDoc.doc); rating =
	 * itemDocument.get("itemRating"); }
	 * 
	 * // Close Index Reader itemIndexReader.close(); return rating; }
	 */
}
