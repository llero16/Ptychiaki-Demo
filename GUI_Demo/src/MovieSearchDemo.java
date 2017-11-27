import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

public class MovieSearchDemo extends JFrame {

	// Movie Search Labels
	private JLabel usernameMSLabel;
	private JLabel idMSLabel;
	private JLabel titleMSLabel;
	private JLabel releaseMSLabel;
	private JLabel genreMSLabel;
	// Movie Rate Labels
	private JLabel movieMRLabel;
	private JLabel titleTextMRLabel;
	private JLabel titleMRLabel;
	private JLabel idTextMRLabel;
	private JLabel idMRLabel;
	private JLabel releaseTextMRLabel;
	private JLabel releaseMRLabel;
	private JLabel imdbTextMRLabel;
	private JLabel imdbMRLabel;
	private JLabel genreTextMRLabel;
	private JLabel genreMRLabel;
	private JLabel ratedMRLabel;
	private JLabel ratingMRLabel;
	private JLabel averageRatingMRLabel;

	// Movie Search TextFields
	private JTextField idMSTF;
	private JTextField titleMSTF;
	private JTextField releaseMSTF;
	private JTextField genreMSTF;

	// Movie Search Buttons
	private JButton searchMSButton;
	private JButton signInOutMSButton;
	private JButton recsMSButton;
	// Movie Rate Buttons
	private JButton rateMRButton;
	private JButton searchMRButton;
	private JButton recsMRButton;
	private JButton signInOutMRButton;

	// Panels
	private JPanel mainPanel;
	// Movie Search Panels
	private JPanel movieSearchPanel;
	private JPanel infoSearchPanel;
	private JPanel buttonSearchPanel;
	// Movie Rate Panels
	private JPanel movieRatePanel;
	private JPanel infoRatePanel;
	private JPanel buttonRatePanel;
	private JPanel ratingsRatePanel;

	// Movie Search JList
	private JList<String> movieList;

	// MovieSearch JScroll Pane
	private JScrollPane movieListScrollPane;

	// Movie Search ArrayLists -> JList
	private ArrayList<String> movieTitles;

	// Movie Search HashMap
	private static HashMap<String, Document> movieHash;

	// Movie List ListModel
	private DefaultListModel<String> movieListModel;

	// Test String
	// private static String userID;
	private static User user;

	// CardLayout
	CardLayout cardLayout;
	private String queryString;

	private static String indexDataDirectory = "indexed\\uDataIndex";
	private static String indexItemDirectory = "indexed\\uItemIndex";
	private static String indexItemDirectoryUpdated = "indexed\\uItemIndexUpdated";
	private static String indexUsersItemsNoDirectory = "indexed\\Users&Items";

	public MovieSearchDemo() {
		// Movie Search Labels
		usernameMSLabel = new JLabel("Username");
		idMSLabel = new JLabel("Movie ID");
		titleMSLabel = new JLabel("Title");
		releaseMSLabel = new JLabel("Release");
		genreMSLabel = new JLabel("Genre");

		// Movie Rate Labels
		movieMRLabel = new JLabel("MOVIE INFO", SwingConstants.CENTER);
		titleTextMRLabel = new JLabel("Title");
		titleMRLabel = new JLabel("Title");
		idTextMRLabel = new JLabel("Movie ID");
		idMRLabel = new JLabel("Movie ID");
		releaseTextMRLabel = new JLabel("Realease Date");
		releaseMRLabel = new JLabel("Realease Date");
		imdbTextMRLabel = new JLabel("IMDb URL");
		imdbMRLabel = new JLabel("IMDB URL");
		genreTextMRLabel = new JLabel("Genre");
		genreMRLabel = new JLabel("Genre");
		ratedMRLabel = new JLabel("Rated/Unrated");
		ratingMRLabel = new JLabel("  ", SwingConstants.CENTER);
		averageRatingMRLabel = new JLabel(" ", SwingConstants.CENTER);

		// Movie Search TextField
		idMSTF = new JTextField();
		titleMSTF = new JTextField();
		releaseMSTF = new JTextField();
		genreMSTF = new JTextField();

		// Movie Search Buttons
		signInOutMSButton = new JButton("Sign In/Out");
		searchMSButton = new JButton("Search Movie");
		recsMSButton = new JButton("Get Recommendation");
		// Movie Rate Buttons
		rateMRButton = new JButton("Rate");
		searchMRButton = new JButton("Search");
		recsMRButton = new JButton("Recommendations");
		signInOutMRButton = new JButton("Sign In/Out");

		// ButtonListeners
		ButtonListener listener = new ButtonListener();
		// Movie Search Listeners
		// signInOutMSButton.addActionListener(listener);
		searchMSButton.addActionListener(listener);
		// recsMSButton.addActionListener(listener);
		// Movie Rate Listeners
		// rateMRButton.addActionListener(listener);
		searchMRButton.addActionListener(listener);
		// recsMRButton.addActionListener(listener);
		// signInOutMRButton.addActionListener(listener);

		// ArrayLists
		movieTitles = new ArrayList<String>();

		// HashMap
		movieHash = new HashMap<String, Document>();

		// MovieSearch: MovieList List Model
		movieListModel = new DefaultListModel<String>();
		if (movieListModel.isEmpty())
			System.out.println("ListModel Empty At Constructor");
		// ArrayList Implementation
		/*
		 * for(String title: movieTitles) listModel.addElement(title);
		 */
		// Hash Implementation
		for (String title : movieHash.keySet())
			movieListModel.addElement(title);

		// MovieSearch JList
		movieList = new JList<String>(movieListModel);
		movieList.setVisibleRowCount(4);
		movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// MovieList Selection Listener
		movieList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {

				queryString = movieList.getSelectedValue().toString();

				// ArrayList Implementation
				/*
				 * for(Document movieDoc: movieDocs) {
				 * if(movieDoc.get("title").equals(queryString)) {
				 * titleMRLabel.setText(movieDoc.get("title"));
				 * idMRLabel.setText(movieDoc.get("id"));
				 * releaseMRLabel.setText(movieDoc.get("releaseDate"));
				 * imdbMRLabel.setText(movieDoc.get("imdbURL"));
				 * genreMRLabel.setText(movieDoc.get("genre"));
				 * cardLayout.show(mainPanel,"5"); break; } }
				 */

				// HashMap Implementation
				for (String title : movieHash.keySet()) {
					if (title.equals(queryString)) {
						titleMRLabel.setText(movieHash.get(title).get("title"));
						idMRLabel.setText(movieHash.get(title).get("id"));
						releaseMRLabel.setText(movieHash.get(title).get("releaseDate"));
						imdbMRLabel.setText(movieHash.get(title).get("imdbURL"));
						genreMRLabel.setText(movieHash.get(title).get("genre"));
						averageRatingMRLabel.setText(movieHash.get(title).get("averageRating"));
						if ((movieHash.get(title).get("averageRating") == null)
								|| movieHash.get(title).get("averageRating").equals(" "))
							System.out.println(movieHash.get(title).get("averageRating"));
						String tempRating;
						if (user != null) {
							try {
								tempRating = searchRating(user.getId(), movieHash.get(title).get("id"));
								ratingMRLabel.setText(tempRating);
								if (tempRating.equals(" - "))
									ratedMRLabel.setText("Unrated");
								else
									ratedMRLabel.setText("Rated");
							} catch (IOException e) {
								e.printStackTrace();
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}
						changePage(4);
						break;
					}
				}
			}
		});

		// Movie Search InfoSearch Panel: Set Layout, Add Labels, TextFields &
		// Button
		infoSearchPanel = new JPanel();
		infoSearchPanel.setLayout(new GridLayout(5, 2, 20, 10));
		infoSearchPanel.setBorder(new TitledBorder("Movie Search Info"));
		infoSearchPanel.add(usernameMSLabel);
		infoSearchPanel.add(signInOutMSButton);
		infoSearchPanel.add(idMSLabel);
		infoSearchPanel.add(idMSTF);
		infoSearchPanel.add(titleMSLabel);
		infoSearchPanel.add(titleMSTF);
		infoSearchPanel.add(releaseMSLabel);
		infoSearchPanel.add(releaseMSTF);
		infoSearchPanel.add(genreMSLabel);
		infoSearchPanel.add(genreMSTF);

		// Movie Search ButtonSearch Panel: Set Layout, Add Labels, Buttons &
		// ListView
		buttonSearchPanel = new JPanel();
		buttonSearchPanel.setLayout(new GridLayout(1, 1, 20, 10));
		buttonSearchPanel.setBorder(new TitledBorder("Search Results & Recommendations"));
		buttonSearchPanel.add(searchMSButton);
		// buttonSearchPanel.add(resultsMSLabel);
		buttonSearchPanel.add(recsMSButton);
		// buttonSearchPanel.add(movieList);

		// Movie Search JScroll Pane: Set Size, Border
		movieListScrollPane = new JScrollPane(movieList);
		movieListScrollPane.setPreferredSize(new Dimension(200, 200));
		movieListScrollPane.setBorder(new TitledBorder("Movie List"));
		movieListScrollPane.getViewport().setBackground(Color.BLACK);
		/*
		 * sp.getVerticalScrollBar().setUI(new MyScrollBarUI());
		 * UIManager.put("ScrollBarUI", "my.package.MyScrollBarUI");
		 */

		// Movie Search Panel: Set Layout, Add Panels
		movieSearchPanel = new JPanel();
		movieSearchPanel.setLayout(new BorderLayout());
		// movieSearchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		movieSearchPanel.add(infoSearchPanel, BorderLayout.NORTH);
		movieSearchPanel.add(movieListScrollPane, BorderLayout.CENTER);
		movieSearchPanel.add(buttonSearchPanel, BorderLayout.SOUTH);

		// Movie Rate RatingsRate Panel: Set Layout, Add Labels
		ratingsRatePanel = new JPanel();
		ratingsRatePanel.setLayout(new GridLayout(1, 2, 20, 10));
		ratingsRatePanel.setBorder(new TitledBorder("Ratings"));
		ratingsRatePanel.add(ratingMRLabel);
		ratingsRatePanel.add(averageRatingMRLabel);

		// Movie Rate InfoRate Panel: Set Layout, Add Labels, TextFields &
		// Button
		infoRatePanel = new JPanel();
		infoRatePanel.setLayout(new GridLayout(6, 2, 20, 10));
		infoRatePanel.setBorder(new TitledBorder("Movie Info"));
		infoRatePanel.add(titleTextMRLabel);
		infoRatePanel.add(titleMRLabel);
		infoRatePanel.add(idTextMRLabel);
		infoRatePanel.add(idMRLabel);
		infoRatePanel.add(releaseTextMRLabel);
		infoRatePanel.add(releaseMRLabel);
		infoRatePanel.add(imdbTextMRLabel);
		infoRatePanel.add(imdbMRLabel);
		infoRatePanel.add(genreTextMRLabel);
		infoRatePanel.add(genreMRLabel);
		infoRatePanel.add(ratedMRLabel);
		// infoRatePanel.add(ratingMRLabel);
		infoRatePanel.add(ratingsRatePanel);

		// Movie Rate ButtonRate Panel: Set Layout, Add Labels, TextFields &
		// Button
		buttonRatePanel = new JPanel();
		buttonRatePanel.setLayout(new GridLayout(2, 2, 20, 10));
		buttonRatePanel.setBorder(new TitledBorder("Rate or Search for a Movie"));
		buttonRatePanel.add(rateMRButton);
		buttonRatePanel.add(searchMRButton);
		buttonRatePanel.add(recsMRButton);
		buttonRatePanel.add(signInOutMRButton);

		// Movie Rate Panel: Set Layout, Add Panels
		movieRatePanel = new JPanel();
		// movieRatePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		movieRatePanel.setLayout(new BorderLayout());
		movieRatePanel.add(movieMRLabel, BorderLayout.NORTH);
		movieRatePanel.add(infoRatePanel, BorderLayout.CENTER);
		movieRatePanel.add(buttonRatePanel, BorderLayout.SOUTH);

		// Main Panel: Set Layout, Add SignIn,SignUp Panels
		mainPanel = new JPanel();
		cardLayout = new CardLayout();
		mainPanel.setLayout(cardLayout);
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		// mainPanel.add(imageLabel, "3");
		// mainPanel.add(signInPanel, "1");
		// mainPanel.add(signUpPanel, "2");
		mainPanel.add(movieSearchPanel, "3");
		mainPanel.add(movieRatePanel, "4");
		// mainPanel.add(recommendationsPanel,"5");
		// cardLayout.show(mainPanel, "1");

		// Frame Staff
		this.setContentPane(mainPanel);
		// this.setJMenuBar(mainMB);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setSize(350, 700);
		this.setVisible(true);
	}

	// Changes Page(Panel)
	public void changePage(int panelNumber) {
		switch (panelNumber) {
		case 3:
			setTitle("Movie Search");
			cardLayout.show(mainPanel, "3");
			break;
		case 4:
			setTitle("Movie Rate");
			cardLayout.show(mainPanel, "4");
			break;

		default:
			setTitle("Home/Sign In ");

			break;
		}
	}

	// Searches and returns HashMap with found Movie Titles and the respective
	// Movie Documents containing Movie ID, Title, Release Date and Genre
	public HashMap<String, Document> searchMovieHash(String indexDirectory, String fieldName1, String queryString1,
			String fieldName2, String queryString2, String fieldName3, String queryString3, String fieldName4,
			String queryString4) throws IOException, ParseException {
		// ArrayList to Contain FieldNames
		ArrayList<String> fields = new ArrayList<String>();
		// HashMap to Contain Movie Title and the Respective Document it Belongs
		// to
		HashMap<String, Document> documentHash = new HashMap<String, Document>();

		// Conditions to form final QueryString(AND)
		if (!queryString1.equals("")) {
			queryString = queryString1;
			fields.add(fieldName1);
		}
		if (!queryString2.equals("")) {
			if (!fields.isEmpty())
				queryString += " AND " + queryString2;
			else
				queryString = queryString2;
			fields.add(fieldName2);
		}
		if (!queryString3.equals("")) {
			if (!fields.isEmpty())
				queryString += " AND " + queryString3;
			else
				queryString = queryString3;
			fields.add(fieldName3);
		}
		if (!queryString4.equals("")) {
			if (!fields.isEmpty())
				queryString += " AND " + queryString4;
			else
				queryString = queryString4;
			fields.add(fieldName4);
		}

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
				System.out.println(document.get("title"));

			}

			// Close IndexSearcher
			indexReader.close();

		}

		return documentHash;
	}

	// Given UserID & ItemID returns the respective Rating if this exists
	public static String searchRating(String userQuery, String itemQuery) throws IOException, ParseException {

		File file = new File(indexDataDirectory);
		Directory directory = FSDirectory.open(file.toPath()); // 3
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
		System.out.print(rating);
		return rating;
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			// Movie Search Page
			if (event.getSource() == searchMSButton) {
				System.out.println();

				// HashMap Implementation
				try {
					if (!movieTitles.isEmpty())
						movieTitles.clear();
					movieHash = searchMovieHash(indexItemDirectoryUpdated, "id", idMSTF.getText(), "title",
							titleMSTF.getText(), "releaseDate", releaseMSTF.getText(), "genre", genreMSTF.getText());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

				// HashMap Implementation
				movieListModel.removeAllElements();
				for (String movieTitle : movieHash.keySet())
					movieListModel.addElement(movieTitle);
				if (movieListModel.isEmpty())
					System.out.println("ListModel Empty After Search");
				movieList.setModel(movieListModel);

				System.out.println("Movies In List After Search");

				// HashMap Implementation
				for (String title : movieHash.keySet())
					System.out.println(title);

				movieListScrollPane.revalidate();

			}

			// Movie Rate Page
			if (event.getSource() == searchMRButton) {
				changePage(3);
			}
		}
	}
}
