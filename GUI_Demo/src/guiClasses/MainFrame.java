package guiClasses;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import indexerClasses.Indexer;
import searcherClasses.Searcher;
import similarityClasses.Similarity;
import userClasses.NewUser;
import userClasses.OldUser;
import userClasses.User;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener {
	// Labels
	// Home/Sign In Labels
	private JLabel usernameSILabel;
	// Sign Up Labels
	private JLabel usernameSULabel;
	private static JLabel usernameNoSULabel; // Replaced the usernameSUTF
												// TextField
	private JLabel ageSULabel;
	private JLabel genderSULabel;
	private JLabel occupationSULabel;
	private JLabel zipCodeSULabel;
	// Movie Search Labels
	// private JLabel usernameMSLabel; // Replaced by usernameMSButton Button
	private JLabel idMSLabel;
	private JLabel titleMSLabel;
	private JLabel releaseMSLabel;
	private JLabel genreMSLabel;
	// Movie Rate Labels
	// private JLabel usernameMRLabel; // Replaced usernameMRButton Button
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
	private JLabel predictedRatingMRLabel;
	private JLabel ratingMRLabel;
	private JLabel averageRatingMRLabel;
	// User Profile Labels
	private JLabel usernameTextUPLabel;
	private JLabel usernameUPLabel;
	private JLabel ageTextUPLabel;
	private JLabel ageUPLabel;
	private JLabel genderTextUPLabel;
	private JLabel genderUPLabel;
	private JLabel occupationTextUPLabel;
	private JLabel occupationUPLabel;
	private JLabel zipCodeTextUPLabel;
	private JLabel zipCodeUPLabel;
	private JLabel averageRatingTextUPLabel;
	private JLabel averageRatingUPLabel;
	private JLabel itemsRatedTextUPLabel;
	private JLabel itemsRatedUPLabel;

	// TextFields
	// Home/Sign In TextFields
	private JTextField usernameSITF;
	// Sign Up TextFields
	// private JTextField usernameSUTF; //Replaced by the usernameNoSULabel -
	// Check in Labels
	private JTextField ageSUTF;
	private JTextField genderSUTF;
	private JTextField occupationSUTF;
	private JTextField zipCodeSUTF;
	// Movie Search TextFields
	private JTextField idMSTF;
	private JTextField titleMSTF;
	private JTextField releaseMSTF;
	private JTextField genreMSTF;

	// Buttons
	// Home/Sign In Buttons
	private JButton signInSInButton;
	private JButton signUpSInButton;
	// Sign Up Buttons
	private JButton signUpSUpButton;
	private JButton signInSUpButton;
	// Movie Search Buttons
	private JButton usernameMSButton; // Replaced usernameMSLabel Label
	private JButton searchMSButton;
	private JButton signInOutMSButton;
	private JButton recsMSButton;
	// Movie Rate Buttons
	private JButton usernameMRButton; // Replace usernameMRLabel Label
	private JButton rateMRButton;
	private JButton searchMRButton;
	private JButton recsMRButton;
	private JButton signInOutMRButton;
	// Recommendations Buttons
	private JButton jaccardButton;
	private JButton roundedJaccardButton;
	private JButton pearsonButton;
	private JButton cosineButton;
	private JButton euclideanButton;
	private JButton chebyshevButton;
	private JButton searchRecButton;
	private JButton signOutRecButton;
	// User Profile Button
	private JButton searchUPButton;
	private JButton recsUPButton;
	private JButton signOutUPButton;

	// Panels
	private JPanel mainPanel;
	// Home/Sign In Panel
	private JPanel signInPanel;
	// Sign Up Panels
	private JPanel signUpPanel;
	private JPanel infoSignUpPanel;
	private JPanel buttonSignUpPanel;
	// Movie Search Panels
	private JPanel movieSearchPanel;
	private JPanel infoSearchPanel;
	private JPanel buttonSearchPanel;
	// Movie Rate Panels
	private JPanel movieRatePanel;
	private JPanel infoRatePanel;
	private JPanel buttonRatePanel;
	private JPanel ratedRatePanel;
	private JPanel ratingsRatePanel;
	// Recommendations Panels
	private JPanel recommendationsPanel;
	private JPanel searchRecsPanel;
	private JPanel buttonRecsPanel;
	// User Profile Panels
	private JPanel userProfilePanel;
	private JPanel infoUPPanel;
	private JPanel buttonUPPanel;

	// JScroll Panes
	// MovieSearch JScroll Pane
	private JScrollPane movieListScrollPane;
	// Recommendations List JScroll Pane
	private JScrollPane recsListScrollPane;

	// Menu, MenuBar & MenuItems
	private JMenu mainMenu;
	private JMenuBar mainMB;
	private JMenuItem homeMI;
	private JMenuItem signInMI;
	private JMenuItem signUpMI;
	private JMenuItem movieSearchMI;
	private JMenuItem movieRateMI;
	private JMenuItem movieRecMI;
	private JMenuItem userProfileMI;
	private JMenuItem exitMI;

	// Movie Search JList
	private JList<String> searchList;
	// Recommendations JList
	private JList<String> recommendationList;

	// Movie Search ArrayLists -> JList
	private ArrayList<String> movieTitles;

	// Movie Search HashMap
	private static HashMap<String, Document> movieMap;
	// Recommendations HashMap
	private static HashMap<String, Document> recommendationMap;
	private static HashMap<String, String> recsUsersMap;
	// Predictions HashMap
	private static HashMap<String, Double> predictionMap;

	// Search List ListModel
	private DefaultListModel<String> searchListModel;
	// Recommendation List ListModel
	private DefaultListModel<String> recommendationListModel;

	// CardLayout
	CardLayout cardLayout;
	private String[] listSelection;
	private String queryString;

	private static String indexDataDirectory = "indexed\\uDataIndex";
	private static String indexUserDirectory = "indexed\\uUserIndex";
	private static String indexItemDirectoryUpdated = "indexed\\uItemIndexUpdated";
	private String indexUsersItemsNoDirectory = "indexed\\Users&Items";

	private static int users; // initially 943
	private static int items; // initially 1682

	private static int userRating = 1;
	private static int similarUsersNo = 5;
	private static int maxRating = 5;
	private static int minRating = 1;

	private static User user;
	private static Searcher searcher;
	private static Indexer indexer;
	private static Similarity similarity;

	public MainFrame() throws IOException, ParseException {

		searcher = new Searcher();
		indexer = new Indexer();
		similarity = new Similarity();

		// Number of Users and Items
		users = searcher.getUsersNo(indexUsersItemsNoDirectory);
		items = searcher.getItemsNo(indexUsersItemsNoDirectory);
		System.out.println("Users: " + users);
		System.out.println("Items: " + items);

		// Menu Items
		homeMI = new JMenuItem("Home");
		signInMI = new JMenuItem("Sign In");
		signUpMI = new JMenuItem("Sign Up");
		movieSearchMI = new JMenuItem("Movie Search");
		movieRateMI = new JMenuItem("Movie Rate");
		movieRecMI = new JMenuItem("Movie Recommendations");
		userProfileMI = new JMenuItem("User Profile");
		exitMI = new JMenuItem("Exit");

		// MenuListener, MenuItems: Add Listener
		// MenuListener listener = new MenuListener();
		homeMI.addActionListener(this);
		signInMI.addActionListener(this);
		signUpMI.addActionListener(this);
		movieSearchMI.addActionListener(this);
		movieRateMI.addActionListener(this);
		movieRecMI.addActionListener(this);
		userProfileMI.addActionListener(this);
		exitMI.addActionListener(this);

		// Main Menu: Add Home, Sign In, Sign Up, Exit MenuItems
		mainMenu = new JMenu("What to do ?");
		mainMenu.add(homeMI);
		mainMenu.add(signInMI);
		mainMenu.add(signUpMI);
		mainMenu.add(movieSearchMI);
		mainMenu.add(movieRateMI);
		mainMenu.add(movieRecMI);
		mainMenu.add(userProfileMI);
		mainMenu.add(exitMI);

		// Main Menu Bar: Add Main Menu
		mainMB = new JMenuBar();
		mainMB.add(mainMenu);

		// Labels
		// Sign In Panel Label
		usernameSILabel = new JLabel("Username");
		// Sign Up Panel Labels
		usernameSULabel = new JLabel("Username/User ID");
		usernameNoSULabel = new JLabel("Your UserID will be: " + (users + 1)); // Replaced
																				// usernameSUTF
																				// TextField
		ageSULabel = new JLabel("Age");
		genderSULabel = new JLabel("Gender");
		occupationSULabel = new JLabel("Occupation");
		zipCodeSULabel = new JLabel("Zip Code");
		// Movie Search Labels
		// usernameMSLabel = new JLabel("Anonymous User"); //Replace by
		// usernameMSButton Button
		// underlineLabel(usernameMSLabel);
		idMSLabel = new JLabel("Movie ID");
		titleMSLabel = new JLabel("Title");
		releaseMSLabel = new JLabel("Release");
		genreMSLabel = new JLabel("Genre");
		// Movie Rate Labels
		// usernameMRLabel = new JLabel("MOVIE INFO", SwingConstants.CENTER);
		titleTextMRLabel = new JLabel("Title");
		titleMRLabel = new JLabel("Title");
		idTextMRLabel = new JLabel("Movie ID");
		idMRLabel = new JLabel("-");
		releaseTextMRLabel = new JLabel("Realease Date");
		releaseMRLabel = new JLabel("-");
		imdbTextMRLabel = new JLabel("IMDb URL");
		imdbMRLabel = new JLabel("-");
		genreTextMRLabel = new JLabel("Genre");
		genreMRLabel = new JLabel("-");
		ratedMRLabel = new JLabel("Un-/Rated");
		predictedRatingMRLabel = new JLabel("-");
		predictedRatingMRLabel.setVisible(false);
		ratingMRLabel = new JLabel("User's", SwingConstants.CENTER);
		averageRatingMRLabel = new JLabel("Average", SwingConstants.CENTER);
		// User Profile Labels
		usernameTextUPLabel = new JLabel("User ID");
		usernameUPLabel = new JLabel("-");
		ageTextUPLabel = new JLabel("Age");
		ageUPLabel = new JLabel("-");
		genderTextUPLabel = new JLabel("Gender");
		genderUPLabel = new JLabel("-");
		occupationTextUPLabel = new JLabel("Occupation");
		occupationUPLabel = new JLabel("-");
		zipCodeTextUPLabel = new JLabel("Zip Code");
		zipCodeUPLabel = new JLabel("-");
		averageRatingTextUPLabel = new JLabel("Average User Rating");
		averageRatingUPLabel = new JLabel("-");
		itemsRatedTextUPLabel = new JLabel("Items Rated");
		itemsRatedUPLabel = new JLabel("-");

		// TextFields
		// Sign In Panel TextField
		usernameSITF = new JTextField();
		// Sign Up Panel TextFields
		// usernameSUTF = new JTextField(); //Replaced by usernameNoSULabel
		ageSUTF = new JTextField();
		genderSUTF = new JTextField();
		occupationSUTF = new JTextField();
		zipCodeSUTF = new JTextField();
		// Movie Search TextField
		idMSTF = new JTextField();
		titleMSTF = new JTextField();
		releaseMSTF = new JTextField();
		genreMSTF = new JTextField();

		// Buttons
		// Home/Sign In Panel Button
		signInSInButton = new JButton("Sign In");
		signUpSInButton = new JButton("Sign Up");
		transparentButton(signUpSInButton);
		// Sign Up Panel Button
		signUpSUpButton = new JButton("Sign Up");
		signInSUpButton = new JButton("Sign In");
		// Movie Search Buttons
		usernameMSButton = new JButton("Anonymous User");
		usernameMSButton.setEnabled(false);
		transparentButton(usernameMSButton);
		signInOutMSButton = new JButton("Sign In/Out");
		searchMSButton = new JButton("Search Movie");
		recsMSButton = new JButton("Recommendations");
		// Movie Rate Buttons
		usernameMRButton = new JButton();
		transparentButton(usernameMRButton);
		rateMRButton = new JButton("Rate");
		searchMRButton = new JButton("Search");
		recsMRButton = new JButton("Recommendations");
		signInOutMRButton = new JButton("Sign In/Out");
		// Movie Recommendations Buttons
		searchRecButton = new JButton("Search");
		signOutRecButton = new JButton("Sign Out");
		jaccardButton = new JButton("Jaccard Similarity");
		roundedJaccardButton = new JButton("Rounded Jaccard Similarity");
		pearsonButton = new JButton("Pearson Correlation");
		cosineButton = new JButton("Cosine Similarity");
		euclideanButton = new JButton("Euclidean Distance");
		chebyshevButton = new JButton("Chebyshev");
		// User Profile Buttons
		searchUPButton = new JButton("Search");
		recsUPButton = new JButton("Recommendations");
		signOutUPButton = new JButton("Sign Out");

		// ListListener
		ListListener listListener = new ListListener();
		// ButtonListener
		ButtonListener buttonListener = new ButtonListener();
		// Home/Sign In Listeners
		signInSInButton.addActionListener(buttonListener);
		signUpSInButton.addActionListener(buttonListener);
		// Sign Up Listeners
		signUpSUpButton.addActionListener(buttonListener);
		signInSUpButton.addActionListener(buttonListener);
		// Movie Search Listeners
		usernameMSButton.addActionListener(buttonListener);
		signInOutMSButton.addActionListener(buttonListener);
		searchMSButton.addActionListener(buttonListener);
		recsMSButton.addActionListener(buttonListener);
		// Movie Rate Listeners
		usernameMRButton.addActionListener(buttonListener);
		rateMRButton.addActionListener(buttonListener);
		searchMRButton.addActionListener(buttonListener);
		recsMRButton.addActionListener(buttonListener);
		signInOutMRButton.addActionListener(buttonListener);
		// Recommendations Listeners
		searchRecButton.addActionListener(buttonListener);
		signOutRecButton.addActionListener(buttonListener);
		jaccardButton.addActionListener(buttonListener);
		roundedJaccardButton.addActionListener(buttonListener);
		pearsonButton.addActionListener(buttonListener);
		cosineButton.addActionListener(buttonListener);
		euclideanButton.addActionListener(buttonListener);
		chebyshevButton.addActionListener(buttonListener);
		// User Profile Listeners
		searchUPButton.addActionListener(buttonListener);
		recsUPButton.addActionListener(buttonListener);
		signOutUPButton.addActionListener(buttonListener);

		// ArrayLists
		movieTitles = new ArrayList<String>();

		// HashMaps
		movieMap = new HashMap<String, Document>();
		recommendationMap = new HashMap<String, Document>();
		recsUsersMap = new HashMap<String, String>();
		predictionMap = new HashMap<String, Double>();

		// MovieSearch: SearchList List Model
		searchListModel = new DefaultListModel<String>();

		// Filling the Search ListModel
		for (String title : movieMap.keySet())
			searchListModel.addElement(title);

		// MovieSearch JList
		searchList = new JList<String>(searchListModel);
		searchList.setVisibleRowCount(4);
		searchList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// MovieList Selection Listener
		searchList.addListSelectionListener(listListener);

		// Sign In Panel: Set Layout, Add Labels, TextFields & Buttons
		signInPanel = new JPanel();
		// signInPanel.setLayout(new GridLayout(4,1,20,10));
		signInPanel.setLayout(new GridLayout(8, 1, 20, 10));
		signInPanel.setBorder(new TitledBorder("Sign In using your UserID"));

		signInPanel.add(usernameSILabel);
		signInPanel.add(usernameSITF);
		signInPanel.add(signInSInButton);
		signInPanel.add(signUpSInButton);

		// Sign Up Info Panel: Set Layout, Add Labels & TextFields
		infoSignUpPanel = new JPanel();
		infoSignUpPanel.setLayout(new GridLayout(6, 2, 20, 10));
		infoSignUpPanel.setBorder(new TitledBorder("State your Info to Sign Up"));
		infoSignUpPanel.add(usernameSULabel);
		// infoSignUpPanel.add(usernameSUTF); //Replaced by the Label bellow
		infoSignUpPanel.add(usernameNoSULabel);
		infoSignUpPanel.add(ageSULabel);
		infoSignUpPanel.add(ageSUTF);
		infoSignUpPanel.add(genderSULabel);
		infoSignUpPanel.add(genderSUTF);
		infoSignUpPanel.add(occupationSULabel);
		infoSignUpPanel.add(occupationSUTF);
		infoSignUpPanel.add(zipCodeSULabel);
		infoSignUpPanel.add(zipCodeSUTF);
		// Sign Up Button Panel: Set Layout, Add Labels & TextFields
		buttonSignUpPanel = new JPanel();
		buttonSignUpPanel.setBorder(new TitledBorder("Sign Up or Sign In"));
		buttonSignUpPanel.setLayout(new GridLayout(1, 1, 20, 10));
		buttonSignUpPanel.add(signUpSUpButton);
		buttonSignUpPanel.add(signInSUpButton);

		// Sign Up Panel: Set Layout, Add Labels, TextFields & Buttons
		signUpPanel = new JPanel();
		signUpPanel.setLayout(new BorderLayout());
		signUpPanel.add(infoSignUpPanel, BorderLayout.CENTER);
		signUpPanel.add(buttonSignUpPanel, BorderLayout.SOUTH);

		// Movie Search InfoSearch Panel: Set Layout, Add Labels, TextFields &
		// Button
		infoSearchPanel = new JPanel();
		infoSearchPanel.setLayout(new GridLayout(5, 2, 20, 10));
		infoSearchPanel.setBorder(new TitledBorder("Movie Search Info"));
		// infoSearchPanel.add(usernameMSLabel);
		infoSearchPanel.add(usernameMSButton);
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
		movieListScrollPane = new JScrollPane(searchList);
		movieListScrollPane.setPreferredSize(new Dimension(200, 200));
		movieListScrollPane.setBorder(new TitledBorder("Movie List"));

		// Movie Search Panel: Set Layout, Add Panels
		movieSearchPanel = new JPanel();
		movieSearchPanel.setLayout(new BorderLayout());
		// movieSearchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		movieSearchPanel.add(infoSearchPanel, BorderLayout.NORTH);
		movieSearchPanel.add(movieListScrollPane, BorderLayout.CENTER);
		movieSearchPanel.add(buttonSearchPanel, BorderLayout.SOUTH);

		// Movie Rate Rated/Unrated Panel: Set Layout, Add Labels
		ratedRatePanel = new JPanel();
		ratedRatePanel.setLayout(new GridLayout(1, 2, 20, 10));
		ratedRatePanel.setBorder(new TitledBorder("Rating Prediction"));
		ratedRatePanel.add(ratedMRLabel);
		ratedRatePanel.add(predictedRatingMRLabel);

		// Movie Rate RatingsRate Panel: Set Layout, Add Labels
		ratingsRatePanel = new JPanel();
		ratingsRatePanel.setLayout(new GridLayout(1, 2, 20, 10));
		ratingsRatePanel.setBorder(new TitledBorder("Average & User Rating"));
		ratingsRatePanel.add(averageRatingMRLabel);
		ratingsRatePanel.add(ratingMRLabel);

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
		// infoRatePanel.add(ratedMRLabel);
		// infoRatePanel.add(ratingMRLabel);
		infoRatePanel.add(ratedRatePanel);
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
		// movieRatePanel.add(usernameMRLabel, BorderLayout.NORTH);
		movieRatePanel.add(infoRatePanel, BorderLayout.CENTER);
		movieRatePanel.add(buttonRatePanel, BorderLayout.SOUTH);

		// Recommendations ButtonRecommendations Panel: Set Layout, Buttons
		searchRecsPanel = new JPanel();
		searchRecsPanel.setLayout(new GridLayout(3, 2, 20, 10));
		searchRecsPanel.setBorder(new TitledBorder("Similarity Measures"));
		searchRecsPanel.add(jaccardButton);
		searchRecsPanel.add(roundedJaccardButton);
		searchRecsPanel.add(pearsonButton);
		searchRecsPanel.add(cosineButton);
		searchRecsPanel.add(euclideanButton);
		searchRecsPanel.add(chebyshevButton);

		// Recommendations RecommendationsList: List Model
		recommendationListModel = new DefaultListModel<String>();

		// Filling the Recommendations ListModel
		for (String title : recommendationMap.keySet())
			recommendationListModel.addElement(recsUsersMap.get(title) + ". -> \t" + title);

		// Recommendation JList
		recommendationList = new JList<String>(recommendationListModel);
		recommendationList.setVisibleRowCount(4);
		recommendationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// MovieList Selection Listener
		recommendationList.addListSelectionListener(listListener);

		// Recommendations JScroll Pane: Set Layout, AddPanels
		recsListScrollPane = new JScrollPane(recommendationList);
		recsListScrollPane.setPreferredSize(new Dimension(200, 200));
		recsListScrollPane.setBorder(new TitledBorder("Recommendations List"));

		// Recommendations Button Recommendations Panel: Set Layout, Add Buttons
		buttonRecsPanel = new JPanel();
		buttonRecsPanel.setLayout(new GridLayout(1, 2, 20, 10));
		buttonRecsPanel.setBorder(new TitledBorder("Search for a Movie or Sign Out"));
		// buttonRecommendationsPanel.add(rateButton);
		buttonRecsPanel.add(searchRecButton);
		buttonRecsPanel.add(signOutRecButton);

		// Recommendations Panel: Set Layout, Add Panels
		recommendationsPanel = new JPanel();
		// recommendationsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		recommendationsPanel.setLayout(new BorderLayout());
		recommendationsPanel.add(searchRecsPanel, BorderLayout.NORTH);
		recommendationsPanel.add(recsListScrollPane, BorderLayout.CENTER);
		recommendationsPanel.add(buttonRecsPanel, BorderLayout.SOUTH);

		// User Profile InfoPanel: Set Layout, Add Panels
		infoUPPanel = new JPanel();
		infoUPPanel.setLayout(new GridLayout(7, 2, 20, 10));
		infoUPPanel.setBorder(new TitledBorder("User Info"));
		infoUPPanel.add(usernameTextUPLabel);
		infoUPPanel.add(usernameUPLabel);
		infoUPPanel.add(ageTextUPLabel);
		infoUPPanel.add(ageUPLabel);
		infoUPPanel.add(genderTextUPLabel);
		infoUPPanel.add(genderUPLabel);
		infoUPPanel.add(occupationTextUPLabel);
		infoUPPanel.add(occupationUPLabel);
		infoUPPanel.add(zipCodeTextUPLabel);
		infoUPPanel.add(zipCodeUPLabel);
		infoUPPanel.add(averageRatingTextUPLabel);
		infoUPPanel.add(averageRatingUPLabel);
		infoUPPanel.add(itemsRatedTextUPLabel);
		infoUPPanel.add(itemsRatedUPLabel);
		// User Profile Button Panel: Set Layout, Add Labels
		buttonUPPanel = new JPanel();
		buttonUPPanel.setLayout(new GridLayout(1, 3, 20, 10));
		buttonUPPanel.setBorder(new TitledBorder("Search or Get Recommendations"));
		buttonUPPanel.add(searchUPButton);
		buttonUPPanel.add(recsUPButton);
		buttonUPPanel.add(signOutUPButton);

		// User Profile Panel: Set Layout, Add Buttons
		userProfilePanel = new JPanel();
		userProfilePanel.setLayout(new BorderLayout());
		userProfilePanel.add(infoUPPanel, BorderLayout.CENTER);
		userProfilePanel.add(buttonUPPanel, BorderLayout.SOUTH);

		// Main Panel: Set Layout, Add SignIn,SignUp Panels
		mainPanel = new JPanel();
		cardLayout = new CardLayout();
		mainPanel.setLayout(cardLayout);
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		// mainPanel.add(imageLabel, "3");
		mainPanel.add(signInPanel, "1");
		mainPanel.add(signUpPanel, "2");
		mainPanel.add(movieSearchPanel, "3");
		mainPanel.add(movieRatePanel, "4");
		mainPanel.add(recommendationsPanel, "5");
		mainPanel.add(userProfilePanel, "6");
		cardLayout.show(mainPanel, "1");

		// Frame Staff
		this.setContentPane(mainPanel);
		this.setJMenuBar(mainMB);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setSize(350, 700);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent event) {
		String buttonString = event.getActionCommand();

		if (buttonString.equals("Home")) {
			changePage(1);
		} else if (buttonString.equals("Sign In")) {
			changePage(1);
		} else if (buttonString.equals("Sign Up")) {
			changePage(2);
		} else if (buttonString.equals("Movie Search")) {
			changePage(3);
		} else if (buttonString.equals("Movie Rate")) {
			changePage(4);
		} else if (buttonString.equals("Movie Recommendations")) {
			changePage(5);
		} else if (buttonString.equals("User Profile")) {
			changePage(6);
		} else if (buttonString.equals("Exit")) {
			System.exit(0);
		} else
			System.out.println("Unexpected Error");
	}

	// Changes Page(Panel)
	public void changePage(int panelNumber) {
		switch (panelNumber) {
		case 1:
			setTitle("Home/Sign In ");
			cardLayout.show(mainPanel, "1");
			break;
		case 2:
			setTitle("Sign Up ");
			cardLayout.show(mainPanel, "2");
			break;
		case 3:
			setTitle("Movie Search");
			cardLayout.show(mainPanel, "3");
			break;
		case 4:
			setTitle("Movie Rate");
			cardLayout.show(mainPanel, "4");
			break;
		case 5:
			setTitle("Movie Recommendations");
			cardLayout.show(mainPanel, "5");
			break;
		case 6:
			setTitle("User Profile");
			cardLayout.show(mainPanel, "6");
			break;
		default:
			setTitle("Home/Sign In ");
			cardLayout.show(mainPanel, "1");
			break;
		}
	}

	public void movieRatePanelSetText(String titleLabel, String idLabel, String releaseLabel, String imdbLabel,
			String genreLabel, String averageRatingLabel) {
		titleMRLabel.setText(titleLabel);
		idMRLabel.setText(idLabel);
		releaseMRLabel.setText(releaseLabel);
		imdbMRLabel.setText(imdbLabel);
		genreMRLabel.setText(genreLabel);
		averageRatingMRLabel.setText(averageRatingLabel);
	}

	public void userProfilePanelSetText(String userID, String age, String gender, String occupation, String zipCode,
			String averageRating, String itemsRated) {
		usernameUPLabel.setText(userID);
		ageUPLabel.setText(age);
		genderUPLabel.setText(gender);
		occupationUPLabel.setText(occupation);
		zipCodeUPLabel.setText(zipCode);
		averageRatingUPLabel.setText(averageRating);
		itemsRatedUPLabel.setText(itemsRated);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void transparentButton(JButton button) {
		button.setBorderPainted(false);
		button.setOpaque(false);
		button.setBackground(Color.WHITE);
		Font font = signUpSInButton.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		button.setFont(font.deriveFont(attributes));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void underlineLabel(JLabel label) {
		Font font = label.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		label.setFont(font.deriveFont(attributes));
	}

	public void disableUserButtons() {
		usernameMSButton.setText("Anonymous User");
		usernameMSButton.setEnabled(false);

		usernameMRButton.setText("Anonymous User");
		usernameMRButton.setEnabled(false);
		movieRatePanel.remove(usernameMRButton);
	}

	public void enableUserButtons() {
		usernameMSButton.setText("User " + user.getId());
		usernameMSButton.setEnabled(true);

		usernameMRButton.setText(("User" + user.getId()));
		usernameMRButton.setEnabled(true);
		movieRatePanel.add(usernameMRButton, BorderLayout.NORTH);
	}
	
	public void changeRecListTitle(int similarityMeasure)
	{
		switch (similarityMeasure) {
		case 1:
			recsListScrollPane.setBorder(new TitledBorder("User " + user.getId() + " - Recommendations List - jacSim"));
			break;
		case 2:
			recsListScrollPane.setBorder(new TitledBorder("User " + user.getId() + " - Recommendations List - jacSimRound"));
			break;
		case 3:
			recsListScrollPane.setBorder(new TitledBorder("User " + user.getId() + " - Recommendations List - pearCor"));
			break;
		case 4:
			recsListScrollPane.setBorder(new TitledBorder("User " + user.getId() + " - Recommendations List - cosSim"));
			break;
		case 5:
			recsListScrollPane.setBorder(new TitledBorder("User " + user.getId() + " - Recommendations List - eucDist"));
			break;
		case 6:
			recsListScrollPane.setBorder(new TitledBorder("User " + user.getId() + " - Recommendations List - chebSim"));
			break;
		default:
			recsListScrollPane.setBorder(new TitledBorder("User " + user.getId() + " - Recommendations List"));
			break;
		}
		
	}

	public void similarityStuff(String similarityType, int[][] utilityMatrix, int[][] utilityMatrixRounded,
			ArrayList<String> currentUserMovies) {
		double[] similarityMatrix = null;

		// Calculate Similarity Matrix
		switch (similarityType) {
		case "jaccardSimilarity":
			similarityMatrix = similarity.jaccardSimilarityMatrix(Integer.valueOf(user.getId()), utilityMatrix,
					currentUserMovies);
			break;
		case "pearsonCorrelation":
			similarityMatrix = similarity.pearsonSimilarityMatrix(Integer.valueOf(user.getId()), utilityMatrix);
			break;
		case "cosineSimilarity":
			double[] rootSumSquaresMatrix = null;
			try {
				rootSumSquaresMatrix = similarity.rootSumSquaresMatrix(indexDataDirectory);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			similarityMatrix = similarity.cosineSimilarityMatrix(Integer.valueOf(user.getId()), utilityMatrix,
					rootSumSquaresMatrix);
			break;
		case "euclideanDistance":
			similarityMatrix = similarity.euclideanSimilarityMatrix(Integer.valueOf(user.getId()), utilityMatrix);
			break;
		case "chebyshevSimilarity":
			similarityMatrix = similarity.chebyshevSimilarityMatrix(Integer.valueOf(user.getId()), utilityMatrix);
			break;
		case "jaccardRoundedSimilarity":
			similarityMatrix = similarity.jaccardSimilarityMatrix(Integer.valueOf(user.getId()), utilityMatrixRounded,
					currentUserMovies);
			break;
		default:
			System.out.println("Error Occured In Similarity Matrix Calculations!!!");
			break;
		}

		// Retrieve Similar Users
		ArrayList<Integer> similarUsers = similarity.similarUsers(Integer.valueOf(user.getId()), similarUsersNo,
				similarityMatrix);
		try {
			// Retrieve Recommended Movies
			ArrayList<String> recommendedMovies = similarity.recommendedMovies(similarUsers, currentUserMovies,
					userRating, utilityMatrix);
			// Predicted Rating
			predictionMap = similarity.predictionsMap(similarityMatrix, similarUsers, utilityMatrix, recommendedMovies);
			// Sorted by Predicted Rating
			ArrayList<String> recommendedMoviesSorted = similarity.sortedRecommendedMovies(predictionMap);
			// Recommendation Map Created
			recommendationMap = searcher.recommendedItemDocs(indexItemDirectoryUpdated, recommendedMoviesSorted);
			// Add User in JList alongside Suggested Item Title
			recsUsersMap = similarity.recommendationsUsersMap(indexItemDirectoryUpdated, similarUsers,
					recommendedMovies, utilityMatrix);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	class ListListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (event.getSource() == searchList) {

				if (searchList.getSelectedValue() == null)
					return;

				queryString = searchList.getSelectedValue().toString();

				// "Transfering" info from movieMap to movieRatePanel
				for (String title : movieMap.keySet()) {
					if (title.equals(queryString)) {
						movieRatePanelSetText(movieMap.get(title).get("title"), movieMap.get(title).get("id"),
								movieMap.get(title).get("releaseDate"), movieMap.get(title).get("imdbURL"),
								movieMap.get(title).get("genre"), movieMap.get(title).get("averageRating"));
						String tempRating;
						if (user != null) {
							try {
								tempRating = searcher.searchDataRating(indexDataDirectory, user.getId(),
										movieMap.get(title).get("id"));
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
			if (event.getSource() == recommendationList) {

				if (recommendationList.getSelectedValue() == null)
					return;
				listSelection = recommendationList.getSelectedValue().toString().split("[\t]");
				queryString = listSelection[1];

				// "Transfering" info from recommendationMap and predictionMap
				// to movieRatePanel
				for (String title : recommendationMap.keySet()) {
					if (title.equals(queryString)) {
						movieRatePanelSetText(recommendationMap.get(title).get("title"),
								recommendationMap.get(title).get("id"), recommendationMap.get(title).get("releaseDate"),
								recommendationMap.get(title).get("imdbURL"), recommendationMap.get(title).get("genre"),
								recommendationMap.get(title).get("averageRating"));
						predictedRatingMRLabel.setText(
								String.format("%2.1f", predictionMap.get(recommendationMap.get(title).get("id"))));
						predictedRatingMRLabel.setVisible(true);
						String tempRating;
						if (user != null) {
							try {
								tempRating = searcher.searchDataRating(indexDataDirectory, user.getId(),
										recommendationMap.get(title).get("id"));
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
		}
	}

	class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {

			// Home/Sign In Page
			if (event.getSource() == signInSInButton) {
				int intUserID = Integer.valueOf(usernameSITF.getText());
				if ((intUserID >= 1) && (intUserID <= users)) {
					user = new OldUser(String.format("%03d", intUserID));
					JOptionPane.showMessageDialog(null, "Signed In as User " + user.getId());
					signInOutMSButton.setText("Sign Out");
					signInOutMRButton.setText("Sign Out");
					recsListScrollPane.setBorder(new TitledBorder("User " + user.getId() + " - Recommendations List"));
					changePage(3);
				} else {
					JOptionPane.showMessageDialog(null, "User " + usernameSITF.getText() + " does not exist!");
				}
				if (user != null)
					enableUserButtons();
			}
			if (event.getSource() == signUpSInButton) {
				changePage(2);
			}
			// Sign Up Page
			if (event.getSource() == signUpSUpButton) {
				user = new NewUser(String.format("%03d", users + 1), ageSUTF.getText(), genderSUTF.getText(),
						occupationSUTF.getText(), zipCodeSUTF.getText());
				try {
					indexer.addUserDoc(indexUserDirectory, user.getId(), ageSUTF.getText(), genderSUTF.getText(),
							occupationSUTF.getText(), zipCodeSUTF.getText());
				} catch (IOException e) {
					e.printStackTrace();
				}

				JOptionPane.showMessageDialog(null, "Signed In as User " + user.getId());
				if (user != null)
					enableUserButtons();

				signInOutMSButton.setText("Sign Out");
				signInOutMRButton.setText("Sign Out");
				recsListScrollPane.setBorder(new TitledBorder("User " + user.getId() + " - Recommendations List"));
				changePage(3);
			}
			if (event.getSource() == signInSUpButton) {
				changePage(1);
			}
			// Movie Search Page
			if (event.getSource() == usernameMSButton) {
				Document userDocument;
				HashMap<String, String> averageRatingMap;
				try {
					userDocument = searcher.searchUserDoc(indexUserDirectory, user.getId());
					averageRatingMap = searcher.getAverageUserRating(indexDataDirectory, user.getId());
					userProfilePanelSetText(userDocument.get("userID"), userDocument.get("userAge"),
							userDocument.get("userGender"), userDocument.get("userOccupation"),
							userDocument.get("userZipCode"), averageRatingMap.get("averageRating"),
							averageRatingMap.get("itemsRated"));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				changePage(6);
			}
			if (event.getSource() == searchMSButton) {

				try {
					if (!movieTitles.isEmpty())
						movieTitles.clear();
					movieMap = searcher.searchItemDocs(indexItemDirectoryUpdated, idMSTF.getText(), titleMSTF.getText(),
							releaseMSTF.getText(), genreMSTF.getText());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

				// Filling the Search ListModel
				searchListModel = new DefaultListModel<String>();
				for (String movieTitle : movieMap.keySet())
					searchListModel.addElement(movieTitle);
				if (searchListModel.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No results found.");
				}

				searchList.setModel(searchListModel);
				movieListScrollPane.revalidate();

			}
			if (event.getSource() == recsMSButton) {
				changePage(5);
			}
			if (event.getSource() == signInOutMSButton) {
				if (user != null) {
					JOptionPane.showMessageDialog(null, "Signed Out User " + user.getId());
					user = null;
					disableUserButtons();
					signInOutMSButton.setText("Sign In");

				} else {
					changePage(1);
					signInOutMSButton.setText("Sign Out");
				}
				// changePage(1);
			}
			// Movie Rate Page
			if (event.getSource() == usernameMRButton) {
				Document userDocument;
				HashMap<String, String> averageRatingMap;
				try {
					userDocument = searcher.searchUserDoc(indexUserDirectory, user.getId());
					averageRatingMap = searcher.getAverageUserRating(indexDataDirectory, user.getId());
					userProfilePanelSetText(userDocument.get("userID"), userDocument.get("userAge"),
							userDocument.get("userGender"), userDocument.get("userOccupation"),
							userDocument.get("userZipCode"), averageRatingMap.get("averageRating"),
							averageRatingMap.get("itemsRated"));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				changePage(6);
			}
			if (event.getSource() == rateMRButton) {

				if (user == null) {
					changePage(1);
					JOptionPane.showMessageDialog(null, "You need to Sign In or Sing Up first...");
				} else {
					String stringRating = JOptionPane.showInputDialog("Rate " + titleMRLabel.getText());

					try {
						int intRating = Integer.parseInt(stringRating);
						if (intRating > maxRating || intRating < minRating) {
							JOptionPane.showMessageDialog(null, "Your rating must be between 1 and 5.");
						} else {
							JOptionPane.showMessageDialog(null, "You rated this " + intRating + ".");
							ratedMRLabel.setText("Rated");
							ratingMRLabel.setText(stringRating);
							try {
								indexer.rateDataItem(indexDataDirectory, user.getId(), idMRLabel.getText(),
										stringRating);
							} catch (IOException e) {
								e.printStackTrace();
							} catch (ParseException e) {
								e.printStackTrace();
							}
							return;
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "You need to type an integer.");
					}
				}
			}
			if (event.getSource() == searchMRButton) {
				changePage(3);
			}
			if (event.getSource() == recsMRButton) {
				changePage(5);
			}
			if (event.getSource() == signInOutMRButton) {
				if (user != null) {
					JOptionPane.showMessageDialog(null, "Signed Out User " + user.getId());
					user = null;
					signInOutMRButton.setText("Sign In");

					disableUserButtons();
				} else {
					changePage(1);
					signInOutMRButton.setText("Sign Out");
				}
			}
			// User Profile Page
			if (event.getSource() == signOutUPButton) {
				JOptionPane.showMessageDialog(null, "Signed Out User " + user.getId());
				user = null;
				signInOutMSButton.setText("Sign In");
				changePage(1);
			}
			if (event.getSource() == searchUPButton) {
				changePage(3);
			}
			if (event.getSource() == recsUPButton) {
				changePage(5);
			}
			// Recommendations Page
			if (event.getSource() == searchRecButton) {
				changePage(3);
			}
			if (event.getSource() == signOutRecButton) {
				JOptionPane.showMessageDialog(null, "Signed Out User " + user.getId());
				user = null;
				signInOutMSButton.setText("Sign In");

				disableUserButtons();
				changePage(1);
			}
			if ((event.getSource() == jaccardButton) || (event.getSource() == pearsonButton)
					|| (event.getSource() == cosineButton) || (event.getSource() == euclideanButton)
					|| (event.getSource() == chebyshevButton)) {
				if (user == null) {
					changePage(1);
					JOptionPane.showMessageDialog(null, "You need to Sign In or Sing Up first...");
				} else {
					int[][] utilityMatrix = null;
					try {
						utilityMatrix = similarity.utilityMatrixPopulator(indexDataDirectory);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ArrayList<String> currentUserMovies = similarity.currentUserMovies(Integer.valueOf(user.getId()),
							utilityMatrix);

					// Jaccard Similarity
					if (event.getSource() == jaccardButton) {
						changeRecListTitle(1);
						similarityStuff("jaccardSimilarity", utilityMatrix, null, currentUserMovies);
					}

					// Pearson Correlation
					if (event.getSource() == pearsonButton) {
						changeRecListTitle(3);
						similarityStuff("pearsonCorrelation", utilityMatrix, null, currentUserMovies);
					}

					// Cosine Similarity
					if (event.getSource() == cosineButton) {
						changeRecListTitle(4);
						similarityStuff("cosineSimilarity", utilityMatrix, null, currentUserMovies);
					}

					// Euclidean Distance
					if (event.getSource() == euclideanButton) {
						changeRecListTitle(5);
						similarityStuff("euclideanDistance", utilityMatrix, null, currentUserMovies);
					}

					// Chebyshev Similarity
					if (event.getSource() == chebyshevButton) {
						changeRecListTitle(6);
						similarityStuff("chebyshevSimilarity", utilityMatrix, null, currentUserMovies);
					}

				}
			}

			// Rounded Jaccard Similarity
			if (event.getSource() == roundedJaccardButton) {
				if (user == null) {
					changePage(1);
					JOptionPane.showMessageDialog(null, "You need to Sign In or Sing Up first...");
				} else {
					int[][] utilityMatrix = null;
					int[][] utilityMatrixRounded = null;
					try {
						utilityMatrix = similarity.utilityMatrixPopulator(indexDataDirectory);
						utilityMatrixRounded = similarity.utilityMatrixPopulatorRounded(indexDataDirectory);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ArrayList<String> currentUserMovies = similarity.currentUserMovies(Integer.valueOf(user.getId()),
							utilityMatrix);
					changeRecListTitle(2);
					similarityStuff("jaccardRoundedSimilarity", utilityMatrix, utilityMatrixRounded, currentUserMovies);
				}
			}

			// Filling the Recommendations ListModel
			recommendationListModel = new DefaultListModel<String>();
			for (String recommendationTitle : recommendationMap.keySet()) {
				recommendationListModel
						.addElement(recsUsersMap.get(recommendationTitle) + ". -> \t" + recommendationTitle);
			}

			recommendationList.setModel(recommendationListModel);

			recsListScrollPane.revalidate();

		}
	}
}
