import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import searcherClasses.Searcher;

public class Similarity {

	private String indexUsersItemsNoDirectory = "indexed\\Users&Items";

	// private User user;
	private int users;
	private int items;

	private static Searcher dataSearcher = new Searcher();

	public Similarity() throws IOException {
		users = dataSearcher.getUsersNo(indexUsersItemsNoDirectory);
		items = dataSearcher.getItemsNo(indexUsersItemsNoDirectory);
		// user = Indexer.user;
	}

	// Returns Array consisting the whole Utility Matrix
	public int[][] utilityMatrixPopulator(String directoryString) throws IOException, ParseException {
		File file = new File(directoryString);

		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		String userString, itemString, ratingString, queryString;
		int itemTemp, ratingTemp;
		Query query;
		TopDocs hits;
		int[][] utilityMatrix = new int[users + 5][items + 5];

		String[] fields = { "dataUserID" };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());

		for (int i = 1; i <= users; i++) {
			userString = String.format("%03d", i);
			queryString = userString;
			query = parser.parse(queryString);

			// We get the matches to User i
			hits = indexSearcher.search(query, items);

			// For each match to User i we get the Item ID and Item Rating
			// Using i (User ID) and itemTemp (Item ID) as indexes we populate
			// the Utility Matrix
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document document = indexSearcher.doc(scoreDoc.doc);

				itemString = document.get("dataItemID");
				itemTemp = Integer.valueOf(itemString);

				ratingString = document.get("dataRating");
				ratingTemp = Integer.valueOf(ratingString);

				utilityMatrix[i][itemTemp] = ratingTemp;

			}
		}

		indexReader.close();

		return utilityMatrix;
	}

	// Returns Array consisting the whole Utility Matrix with 1s in place of
	// 3s,4s and 5s, and 0s in place of no rating, 1s and 2s
	public int[][] utilityMatrixPopulatorRounded(String directoryString) throws IOException, ParseException {
		File file = new File(directoryString);

		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		String userString, itemString, ratingString, queryString;
		int itemTemp, ratingTemp;
		Query query;
		TopDocs hits;
		int[][] utilityMatrix = new int[users + 5][items + 5];

		String[] fields = { "dataUserID" };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());

		for (int i = 1; i <= users; i++) {
			userString = String.format("%03d", i);
			queryString = userString;
			query = parser.parse(queryString);

			// We get the matches to User i
			hits = indexSearcher.search(query, items);

			// For each match to User i we get the Item ID and Item Rating
			// Using i (User ID) and itemTemp (Item ID) as indexes we populate
			// the Utility Matrix
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document document = indexSearcher.doc(scoreDoc.doc);

				itemString = document.get("dataItemID");
				itemTemp = Integer.valueOf(itemString);

				ratingString = document.get("dataRating");
				ratingTemp = Integer.valueOf(ratingString);

				// Redundant Condition - Utility Matrix element already equals 0
				/*
				 * if(ratingTemp <= 2) utilityMatrix[i][itemTemp] = 0;
				 */
				if (ratingTemp > 3)
					utilityMatrix[i][itemTemp] = 1;
			}
		}

		indexReader.close();

		return utilityMatrix;
	}

	// Returns ArrayList of Item IDs of the Movies the given User has rated
	public ArrayList<String> currentUserMovies(int currentUser, int[][] utilityMatrix) {
		ArrayList<String> movies = new ArrayList<String>();
		for (int j = 1; j <= items; j++) {
			if (utilityMatrix[currentUser][j] != 0) {
				movies.add(String.format("%04d", j));
			}

		}

		return movies;
	}

	// If we are to include Users with the movies we should change the ArrayList
	// to HashMap
	// If we are to choose based on Average Rating instead of Similar Users'
	// Rating we should change userRating to averageRating

	// Returns ArrayList of Movies. The more Similar the User the higher the
	// movie on the list
	public ArrayList<String> recommendedMovies(ArrayList<Integer> similarUsers, ArrayList<String> currentUserMovies,
			int userRating, int[][] utilityMatrix) throws IOException, ParseException {
		ArrayList<String> suggestions = new ArrayList<String>();
		String itemID;
		for (int user : similarUsers) {
			for (int j = 1; j <= users; j++) {
				itemID = String.format("%04d", j);
				if (utilityMatrix[user][j] >= userRating && !suggestions.contains(itemID))
					suggestions.add(itemID);
			}
		}

		suggestions = removeCommons(suggestions, currentUserMovies);
		// suggestions = replaceIdWithTitle(suggestions);
		return suggestions;
	}

	// Removes Items' IDs from higlyRatedMovies ArrayList the movies it has in
	// common with the currentUserMovies ArrayLists
	public ArrayList<String> removeCommons(ArrayList<String> highlyRatedMovies, ArrayList<String> currentUserMovies) {

		for (String movie : currentUserMovies)
			if (highlyRatedMovies.contains(movie))
				highlyRatedMovies.remove(movie);

		return highlyRatedMovies;
	}

	// Returns ArrayList with the #similarUsersNo most similar users to the user
	// given as parameter
	@SuppressWarnings("rawtypes")
	public ArrayList<Integer> similarUsers(int currentUser, int similarUsersNo, double similarityMatrix[]) {
		// Putting the similarities in a map with respective users as keys
		Map<Integer, Double> similarityMap = new HashMap<Integer, Double>();
		for (int i = 1; i <= users; i++)
			similarityMap.put(i, similarityMatrix[i]);
		similarityMap.remove(currentUser);

		// Sorting the map by values(similarity)
		Map<Integer, Double> map;
		map = sortByValues(similarityMap);
		Set set = map.entrySet();
		Iterator iterator = set.iterator();

		// Selecting 5 most similar users and store the in ArratList
		// similarUsers;
		ArrayList<Integer> similarUsers = new ArrayList<Integer>();
		int counter = 0;
		while (iterator.hasNext() && counter < similarUsersNo)
		// while(counter < similarUsersNo)
		{
			Map.Entry entry = (Map.Entry) iterator.next();
			similarUsers.add((Integer) entry.getKey());
			counter++;
		}

		return similarUsers;
	}

	// Returns a sorted by values HashMap of Users as keys and the respective
	// Similarities as values
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap<Integer, Double> sortByValues(Map<Integer, Double> similarityMap) {
		List list = new LinkedList(similarityMap.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
			}
		}.reversed());

		// Here I am copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator iterator = list.iterator(); iterator.hasNext();) {
			Map.Entry entry = (Map.Entry) iterator.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}

	// Returns Array with the Cosine Similarities between the given User and the
	// rest of the Users
	public double[] cosineSimilarityMatrix(int currentUser, int utilityMatrix[][], double rssMatrix[]) {
		double[] cosSimMatrix = new double[users + 1];
		double productSum;
		for (int j = 1; j <= users; j++) {
			productSum = 0;
			for (int k = 1; k <= items; k++) {
				if (utilityMatrix[currentUser][k] != 0 && utilityMatrix[j][k] != 0) {
					productSum += utilityMatrix[currentUser][k] * utilityMatrix[j][k];

				}
			}

			cosSimMatrix[j] = productSum / (rssMatrix[currentUser] * rssMatrix[j]);
		}
		return cosSimMatrix;
	}

	// Returns Array with the Root Sum of Squares of the Users' Ratings
	public double[] rootSumSquaresMatrix(String directoryString) throws IOException, ParseException {
		File file = new File(directoryString);

		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		String userString, ratingString, queryString;
		int ratingTemp, sumOfSquares;
		Query query;
		TopDocs hits;

		double[] rssMatrix = new double[users + 5];

		String[] fields = { "dataUserID" };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());

		for (int i = 1; i <= users; i++) {
			userString = String.format("%03d", i);
			queryString = userString;
			query = parser.parse(queryString);

			// We get the matches to User i
			hits = indexSearcher.search(query, items);

			sumOfSquares = 0;

			// For each match to User i we get the Item ID and Item Rating
			// Using i (User ID) and itemTemp (Item ID) as indexes we populate
			// the Utility Matrix
			for (ScoreDoc scoreDoc : hits.scoreDocs) {
				Document document = indexSearcher.doc(scoreDoc.doc);

				ratingString = document.get("dataRating");
				ratingTemp = Integer.valueOf(ratingString);

				// We calculate the Sum of Squares of each User
				sumOfSquares += Math.pow(ratingTemp, 2);
			}

			// We store each User's Root of the Sum of Squares for later use in
			// the Cosine Similarity
			rssMatrix[i] = Math.sqrt(sumOfSquares);
		}

		indexReader.close();

		return rssMatrix;
	}

	// Returns Array with the Jaccard Similarities between the given User and
	// the rest of the Users
	public double[] jaccardSimilarityMatrix(int currentUser, int utilityMatrix[][],
			ArrayList<String> currentUserMovies) {

		double[] jacSimMatrix = new double[users + 1];
		ArrayList<String> tempList = new ArrayList<String>();
		ArrayList<String> intersectionList = new ArrayList<String>();
		ArrayList<String> unionList = new ArrayList<String>();

		for (int i = 1; i <= users; i++) {
			for (int j = 1; j <= items; j++) {
				// Condition for Union
				if (utilityMatrix[i][j] != 0) {
					tempList.add(String.format("%04d", j));
					// Condition for Intersection
					if (currentUserMovies.contains(String.format("%04d", j)))
						intersectionList.add(String.format("%04d", j));
				}
			}

			// Getting the Union between Searching User's Movies and the Rest
			unionList = union(currentUserMovies, tempList);

			// Calculating and Storing the Jaccard Coefficient in the
			// jacSimMatrix
			jacSimMatrix[i] = (double) intersectionList.size() / unionList.size();
			intersectionList.clear();
			tempList.clear();
		}

		return jacSimMatrix;
	}

	// Return the Union(ArrayList) of the two given ArrayLists
	public ArrayList<String> union(ArrayList<String> List1, ArrayList<String> List2) {

		HashSet<String> set = new HashSet<String>();
		set.addAll(List1);
		set.addAll(List2);

		return new ArrayList<String>(set);
	}

	// Returns Array with the Chebyshev Similarities between the given User and
	// the rest of the Users
	public double[] chebyshevSimilarityMatrix(int currentUser, int utilityMatrix[][]) {
		double[] chebSimMatrix = new double[users + 1];
		double sum, distance;
		double dif = 0;
		double difInf = 0;
		for (int i = 1; i <= users; i++) {
			sum = 0;

			for (int j = 1; j <= items; j++) {
				dif = utilityMatrix[currentUser][j] - utilityMatrix[i][j];
				// System.out.println(dif);
				difInf = Math.pow(dif, Double.POSITIVE_INFINITY);
				// System.out.println(difInf);
				sum += difInf;
			}

			// Calculating the Euclidean Distance
			distance = Math.pow(sum, 1 / Double.POSITIVE_INFINITY);
			// System.out.println(distance);
			// Calculating and Storing the Euclidean Similarity by subtracting
			// the Euclidean Distance from 1
			chebSimMatrix[i] = 1 - distance;
			// System.out.println(chebSimMatrix);

		}

		return chebSimMatrix;
	}

	// Returns Array with the Euclidean Similarities between the given User and
	// the rest of the Users
	public double[] euclideanSimilarityMatrix(int currentUser, int utilityMatrix[][]) {
		double[] euclSimMatrix = new double[users + 1];
		double sum, euclideanDistance;
		double dif = 0;
		double dif2 = 0;
		for (int i = 1; i <= users; i++) {
			sum = 0;
			for (int j = 1; j <= items; j++) {
				dif = utilityMatrix[currentUser][j] - utilityMatrix[i][j];
				dif2 = Math.pow(dif, 2);
				sum += dif2;
			}
			// Calculating the Euclidean Distance
			euclideanDistance = Math.sqrt(sum);

			// Calculating and Storing the Euclidean Similarity by subtracting
			// the Euclidean Distance from 1
			euclSimMatrix[i] = 1 - euclideanDistance;
		}

		return euclSimMatrix;
	}

	// Returns Array with the Pearson Similarities between the given User and
	// the rest of the Users
	public double[] pearsonSimilarityMatrix(int currentUser, int utilityMatrix[][]) {
		double[] pearsonSimMatrix = new double[users + 1];
		double[] userAverages = rowAverages(utilityMatrix);
		double rxs_rx;
		double rys_ry;
		double product;
		double productSum;
		double rxs_rx2;
		double rys_ry2;
		double rxRoot;
		double ryRoot;
		double rxSum;
		double rySum;

		for (int i = 1; i <= users; i++) {
			product = 1;
			productSum = 0;
			rxSum = 0;
			rySum = 0;

			for (int j = 1; j <= items; j++) {

				// Differences
				rxs_rx = utilityMatrix[currentUser][j] - userAverages[currentUser];
				rys_ry = utilityMatrix[i][j] - userAverages[i];

				product = rxs_rx * rys_ry;

				// Sum of Products of Differences
				productSum += product;
				// productSum += rxs_rx * rys_ry;

				// Sum of Squares of Differences
				rxs_rx2 = Math.pow(rxs_rx, 2);
				rxSum += rxs_rx2;
				rys_ry2 = Math.pow(rys_ry, 2);
				rySum += rys_ry2;
			}

			// Root Sum of Squares of Differences
			rxRoot = Math.sqrt(rxSum);
			ryRoot = Math.sqrt(rySum);

			pearsonSimMatrix[i] = productSum / (rxRoot * ryRoot);
		}
		return pearsonSimMatrix;
	}

	// Return Array of Row Averages
	public double[] rowAverages(int utilityMatrix[][]) {
		double temp;
		double[] rowAverages = new double[users + 1];
		for (int i = 1; i <= users; i++) {
			temp = 0;
			for (int j = 1; j <= items; j++) {
				temp += utilityMatrix[i][j];
			}
			rowAverages[i] = temp / items;

		}

		return rowAverages;
	}

	// Given an ArrayList of Similar Users and the respective similarities,
	// stores and returns Item IDs along with Predicted Ratings
	public HashMap<String, String> predictionsHash(double[] similarityMatrix, ArrayList<Integer> similarUsers,
			int[][] utilityMatrix, ArrayList<String> recommendedMovies) {
		HashMap<String, String> predictionHash = new HashMap<String, String>();
		double prediction;

		for (String item : recommendedMovies) {
			prediction = predictedRating(Integer.valueOf(item), similarityMatrix, similarUsers, utilityMatrix);
			predictionHash.put(item, String.format("%2.1f", prediction));
		}

		return predictionHash;
	}

	// Same method with predictionHashRounded above. Takes into account rounded
	// Utility Matrices
	// Given an ArrayList of Similar Users and the respective similarities,
	// stores and returns Item IDs along with Predicted Ratings
	public HashMap<String, String> predictionsHashRounded(ArrayList<Integer> similarUsers, int[][] utilityMatrix,
			ArrayList<String> recommendedMovies) {
		HashMap<String, String> predictionHash = new HashMap<String, String>();
		double prediction;

		for (String item : recommendedMovies) {
			prediction = predictedRatingRounded(Integer.valueOf(item), similarUsers, utilityMatrix);
			predictionHash.put(item, String.format("%2.1f", prediction));
		}

		return predictionHash;
	}

	// Given an ArrayList of Similar Users and the respective similarities,
	// calculates and returns Predicted Rating for a particular Item
	public double predictedRating(int itemID, double[] similarityMatrix, ArrayList<Integer> similarUsers,
			int[][] utilityMatrix) {
		double product, a, b, predictedRating;
		a = 0;
		b = 0;

		for (int user : similarUsers) {
			product = similarityMatrix[user] * utilityMatrix[user][itemID];
			// We do not take into account Similarity with a User that hasn't
			// rated the given item
			// utilityMatrix[user][itemID] = 0 when unrated by the user
			if (product != 0) {
				a += product;
				b += similarityMatrix[user];
			}
		}

		predictedRating = a / b;
		return predictedRating;
	}

	// Different prediction function used than the predictedRatingRounded above.
	// Takes into account rounded Utility Matrices
	// Given an ArrayList of Similar Users, calculates and returns Predicted
	// Rating for a particular Item
	public double predictedRatingRounded(int itemID, ArrayList<Integer> similarUsers, int[][] utilityMatrix) {
		double sum, predictedRating;
		sum = 0;

		for (int user : similarUsers) {

			// We do not take into account Similarity with a User that hasn't
			// rated the given item
			// utilityMatrix[user][itemID] = 0 when unrated by the user
			sum += utilityMatrix[user][itemID];

		}

		predictedRating = sum / (double) similarUsers.size();
		return predictedRating;
	}
}
