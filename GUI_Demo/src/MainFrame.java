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
	private static JLabel usernameNoSULabel; // Replaced the usernameSUTF TextField
	private JLabel ageSULabel;
	private JLabel genderSULabel;
	private JLabel occupationSULabel;
	private JLabel zipCodeSULabel;
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
	private JLabel predictedRatingMRLabel;
	private JLabel ratingMRLabel;
	private JLabel averageRatingMRLabel;

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
	private JButton searchMSButton;
	private JButton signInOutMSButton;
	private JButton recsMSButton;
	// Movie Rate Buttons
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
	private JMenuItem exitMI;

	// Movie Search JList
	private JList<String> movieList;
	// Recommendations JList
	private JList<String> recommendationList;

	// Movie Search ArrayLists -> JList
	private ArrayList<String> movieTitles;

	// Movie Search HashMap
	private static HashMap<String, Document> movieHash;
	// Recommendations HashMap
	private static HashMap<String, Document> recommendationHash;
	// Predictions HashMap
	private static HashMap<String, String> predictionsHash;

	// Movie List ListModel
	private DefaultListModel<String> movieListModel;
	// Recommendation List ListModel
	private DefaultListModel<String> recommendationListModel;

	// CardLayout
	CardLayout cardLayout;
	private String queryString;

	// Test String
	private static User user;

	private static String indexDataDirectory = "indexed\\uDataIndex";
	// private static String indexItemDirectory = "indexed\\uItemIndex";
	private static String indexItemDirectoryUpdated = "indexed\\uItemIndexUpdated";
	// private static String indexUsersItemsNoDirectory =
	// "indexed\\Users&Items";

	private static int users; // 943
	private static int items; // 1682

	private static int userRating = 5;
	private static int similarUsersNo = 5;
	private static int maxRating = 5;
	private static int minRating = 1;

	private static Searcher searcher;
	private static Indexer indexer;
	private static Similarity similarity;

	@SuppressWarnings("unchecked")
	public MainFrame() throws IOException, ParseException {
		// Number of Users and Items

		searcher = new Searcher();
		indexer = new Indexer();
		similarity = new Similarity();
		
		users = searcher.getUsersNo();
		items = searcher.getItemsNo();
		System.out.println("Users: " + users);
		System.out.println("Items: " + items);
		

		// Menu Items
		homeMI = new JMenuItem("Home");
		signInMI = new JMenuItem("Sign In");
		signUpMI = new JMenuItem("Sign Up");
		movieSearchMI = new JMenuItem("Movie Search");
		movieRateMI = new JMenuItem("Movie Rate");
		movieRecMI = new JMenuItem("Movie Recommendations");
		exitMI = new JMenuItem("Exit");

		// MenuListener, MenuItems: Add Listener
		// MenuListener listener = new MenuListener();
		homeMI.addActionListener(this);
		signInMI.addActionListener(this);
		signUpMI.addActionListener(this);
		movieSearchMI.addActionListener(this);
		movieRateMI.addActionListener(this);
		movieRecMI.addActionListener(this);
		exitMI.addActionListener(this);

		// Main Menu: Add Home, Sign In, Sign Up, Exit MenuItems
		mainMenu = new JMenu("What to do ?");
		mainMenu.add(homeMI);
		mainMenu.add(signInMI);
		mainMenu.add(signUpMI);
		mainMenu.add(movieSearchMI);
		mainMenu.add(movieRateMI);
		mainMenu.add(movieRecMI);
		mainMenu.add(exitMI);

		// Main Menu Bar: Add Main Menu
		mainMB = new JMenuBar();
		mainMB.add(mainMenu);

		// Labels
		// Sign In Panel Label
		usernameSILabel = new JLabel("Username");
		// Sign Up Panel Labels
		usernameSULabel = new JLabel("Username/User ID");
		usernameNoSULabel = new JLabel("Your UserID will be: " + (users + 1)); // Replaced usernameSUTF TextField
		ageSULabel = new JLabel("Age");
		genderSULabel = new JLabel("Gender");
		occupationSULabel = new JLabel("Occupation");
		zipCodeSULabel = new JLabel("Zip Code");
		// Movie Search Labels
		usernameMSLabel = new JLabel("Anonymous User");
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
		ratedMRLabel = new JLabel("Un-/Rated");
		predictedRatingMRLabel = new JLabel("Prediction");
		ratingMRLabel = new JLabel("User's", SwingConstants.CENTER);
		averageRatingMRLabel = new JLabel("Average", SwingConstants.CENTER);

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
		// Sign Up Panel Button
		signUpSUpButton = new JButton("Sign Up");
		signInSUpButton = new JButton("Sign In");
		// Movie Search Buttons
		signInOutMSButton = new JButton("Sign In/Out");
		searchMSButton = new JButton("Search Movie");
		recsMSButton = new JButton("Recommendations");
		// Movie Rate Buttons
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
		signInOutMSButton.addActionListener(buttonListener);
		searchMSButton.addActionListener(buttonListener);
		recsMSButton.addActionListener(buttonListener);
		// Movie Rate Listeners
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

		// ArrayLists
		movieTitles = new ArrayList<String>();

		// HashMap
		movieHash = new HashMap<String, Document>();
		recommendationHash = new HashMap<String, Document>();
		predictionsHash = new HashMap<String,String>();

		// MovieSearch: MovieList List Model
		movieListModel = new DefaultListModel<String>();
		/*if (movieListModel.isEmpty())
			System.out.println("Search ListModel Empty At Constructor");*/

		// Hash Implementation
		for (String title : movieHash.keySet())
			movieListModel.addElement(title);

		// MovieSearch JList
		movieList = new JList<String>(movieListModel);
		movieList.setVisibleRowCount(4);
		movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// MovieList Selection Listener
		movieList.addListSelectionListener(listListener);

		// Sign In Panel: Set Layout, Add Labels, TextFields & Buttons
		signInPanel = new JPanel();
		// signInPanel.setLayout(new GridLayout(4,1,20,10));
		signInPanel.setLayout(new GridLayout(8, 1, 20, 10));
		signInPanel.setBorder(new TitledBorder("Sign In using your UserID"));

		signInPanel.add(usernameSILabel);
		signInPanel.add(usernameSITF);
		signInPanel.add(signInSInButton);
		signInPanel.add(signUpSInButton);

		signUpSInButton.setBorderPainted(false);
		signUpSInButton.setOpaque(false);
		signUpSInButton.setBackground(Color.WHITE);
		Font font = signUpSInButton.getFont();
		@SuppressWarnings("rawtypes")
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		signUpSInButton.setFont(font.deriveFont(attributes));

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
		ratingsRatePanel.setBorder(new TitledBorder("Ratings"));
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
		//infoRatePanel.add(ratedMRLabel);
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
		movieRatePanel.add(movieMRLabel, BorderLayout.NORTH);
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
		/*if (recommendationListModel.isEmpty())
			System.out.println("Recommendations ListModel Empty At Constructor");*/

		// HashMap Implementation
		for (String title : recommendationHash.keySet())
			recommendationListModel.addElement(title);

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
		default:
			setTitle("Home/Sign In ");
			cardLayout.show(mainPanel, "1");
			break;
		}
	}
	
	public void movieRatePanelSetText(String titleLabel, String idLabel, String releaseLabel, String imdbLabel, String genreLabel, String averageRatingLabel)
	{
		titleMRLabel.setText(titleLabel);
		idMRLabel.setText(idLabel);
		releaseMRLabel.setText(releaseLabel);
		imdbMRLabel.setText(imdbLabel);
		genreMRLabel.setText(genreLabel);
		averageRatingMRLabel.setText(averageRatingLabel);
	}

	class ListListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (event.getSource() == movieList) {

				if (movieList.getSelectedValue() == null)
					return;

				queryString = movieList.getSelectedValue().toString();

				// HashMap Implementation
				for (String title : movieHash.keySet()) {
					if (title.equals(queryString)) {
						movieRatePanelSetText(movieHash.get(title).get("title"), movieHash.get(title).get("id"), movieHash.get(title).get("releaseDate"), movieHash.get(title).get("imdbURL"), movieHash.get(title).get("genre"), movieHash.get(title).get("averageRating"));
						String tempRating;
						if (user != null) {
							try {
								tempRating = searcher.searchDataRating(user.getId(), movieHash.get(title).get("id")); 
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
				queryString = recommendationList.getSelectedValue().toString();

				// HashMap Implementation
				for (String title : recommendationHash.keySet()) {
					if (title.equals(queryString)) {
						movieRatePanelSetText(recommendationHash.get(title).get("title"), recommendationHash.get(title).get("id"), recommendationHash.get(title).get("releaseDate"), recommendationHash.get(title).get("imdbURL"), recommendationHash.get(title).get("genre"), recommendationHash.get(title).get("averageRating"));
						predictedRatingMRLabel.setText(predictionsHash.get(recommendationHash.get(title).get("id")));
						String tempRating;
						if (user != null) {
							try {
								tempRating = searcher.searchDataRating(user.getId(), recommendationHash.get(title).get("id"));
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
					usernameMSLabel.setText("User " + user.getId());
			}
			if (event.getSource() == signUpSInButton) {
				changePage(2);
			}
			// Sign Up Page
			if (event.getSource() == signUpSUpButton) {
				user = new NewUser(String.format("%03d", users + 1), ageSUTF.getText(), genderSUTF.getText(), occupationSUTF.getText(), zipCodeSUTF.getText());
				try {
					indexer.addUserDoc(user.getId(), ageSUTF.getText(), genderSUTF.getText(), occupationSUTF.getText(), zipCodeSUTF.getText());
				} catch (IOException e) {
					e.printStackTrace();
				}

				JOptionPane.showMessageDialog(null, "Signed In as User " + user.getId());
				if (user != null) {
					usernameMSLabel.setText("User "+ user.getId());
				}
				signInOutMSButton.setText("Sign Out");
				signInOutMRButton.setText("Sign Out");
				recsListScrollPane.setBorder(new TitledBorder("User " + user.getId() + " - Recommendations List"));
				changePage(3);
			}
			if (event.getSource() == signInSUpButton) {
				changePage(1);
			}
			// Movie Search Page
			if (event.getSource() == searchMSButton) {

				// HashMap Implementation
				try {
					if (!movieTitles.isEmpty())
						movieTitles.clear();
					movieHash = searcher.searchItemDocs(indexItemDirectoryUpdated, idMSTF.getText(),titleMSTF.getText(), releaseMSTF.getText(), genreMSTF.getText());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}

				// HashMap Implementation
				movieListModel = new DefaultListModel<String>();
				// movieListModel.removeAllElements();
				for (String movieTitle : movieHash.keySet())
					movieListModel.addElement(movieTitle);
				if (movieListModel.isEmpty()) {
					JOptionPane.showMessageDialog(null, "No results found.");
				}

				movieList.setModel(movieListModel);

				/*System.out.println("\nMovies In List After Search");
				// HashMap Implementation
				for (String title : movieHash.keySet())
					System.out.println(title);*/

				movieListScrollPane.revalidate();

			}
			if (event.getSource() == recsMSButton) {
				changePage(5);
			}
			if (event.getSource() == signInOutMSButton) {
				if (user != null) {
					JOptionPane.showMessageDialog(null, "Signed Out User " + user.getId());
					user = null;
					usernameMSLabel.setText("Anonymous User");
					signInOutMSButton.setText("Sign In");
				} else {
					changePage(1);
					signInOutMSButton.setText("Sign Out");
				}
				// changePage(1);
			}
			// Movie Rate Page
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
							JOptionPane.showMessageDialog(null, "The number you chose is " + intRating + ".");
							ratedMRLabel.setText("Rated");
							ratingMRLabel.setText(stringRating);
							try {
								indexer.rateDataItem(user.getId(), idMRLabel.getText(), stringRating);
							} catch (IOException e) {
								e.printStackTrace();
							} catch (ParseException e) {
								e.printStackTrace();
							}
							return;
						}
					} catch (NumberFormatException e) {
						JOptionPane.showMessageDialog(null, "The text you typed is not an integer.");
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
				} else {
					changePage(1);
					signInOutMRButton.setText("Sign Out");
				}
			}
			// Recommendations Page
			if (event.getSource() == searchRecButton) {
				changePage(3);
			}
			if (event.getSource() == signOutRecButton) {
				JOptionPane.showMessageDialog(null, "Signed Out User " + user.getId());
				user = null;
				signInOutMSButton.setText("Sign In");
				changePage(1);
			}
			if ((event.getSource() == jaccardButton) || (event.getSource() == pearsonButton)
					|| (event.getSource() == cosineButton) || (event.getSource() == euclideanButton)
					|| (event.getSource() == chebyshevButton)) {
				if (user == null) {
					changePage(1);
					JOptionPane.showMessageDialog(null, "You need to Sign In or Sing Up first...");
				} else {
					int[][] um = null;
					try {
						um = similarity.utilityMatrixPopulator(indexDataDirectory);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ArrayList<String> cum = similarity.currentUserMovies(Integer.valueOf(user.getId()), um);
					// Jaccard Similarity
					if (event.getSource() == jaccardButton) {
						double[] jsm = similarity.jaccardSimilarityMatrix(Integer.valueOf(user.getId()), um, cum);
						ArrayList<Integer> jsu = similarity.similarUsers(Integer.valueOf(user.getId()), similarUsersNo, jsm);
						try {
							ArrayList<String> jrm = similarity.recommendedMovies(jsu, cum, userRating, um);
							recommendationHash = searcher.recommendedItemDocs(indexItemDirectoryUpdated, jrm);
							// Predicted Rating
							predictionsHash = similarity.predictionsHash(jsm, jsu, um, jrm);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					// Pearson Correlation
					if (event.getSource() == pearsonButton) {
						double[] psm = similarity.pearsonSimilarityMatrix(Integer.valueOf(user.getId()), um);
						ArrayList<Integer> psu = similarity.similarUsers(Integer.valueOf(user.getId()),	similarUsersNo, psm);
						try {
							ArrayList<String> prm = similarity.recommendedMovies(psu, cum, userRating, um);
							recommendationHash = searcher.recommendedItemDocs(indexItemDirectoryUpdated, prm);
							// Predicted Rating
							predictionsHash = similarity.predictionsHash(psm, psu, um, prm);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					// Cosine Similarity
					if (event.getSource() == cosineButton) {
						double[] rssm = null;
						try {
							rssm = similarity.rootSumSquaresMatrix(indexDataDirectory);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
						double[] csm = similarity.cosineSimilarityMatrix(Integer.valueOf(user.getId()), um, rssm);
						ArrayList<Integer> csu = similarity.similarUsers(Integer.valueOf(user.getId()), similarUsersNo, csm);
						try {
							ArrayList<String> crm = similarity.recommendedMovies(csu, cum, userRating, um);
							recommendationHash = searcher.recommendedItemDocs(indexItemDirectoryUpdated, crm);
							// Predicted Rating
							predictionsHash = similarity.predictionsHash(csm, csu, um, crm);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					// Euclidean Distance
					if (event.getSource() == euclideanButton) {
						double[] esm = similarity.euclideanSimilarityMatrix(Integer.valueOf(user.getId()), um);
						ArrayList<Integer> esu = similarity.similarUsers(Integer.valueOf(user.getId()),	similarUsersNo, esm);
						try {
							ArrayList<String> erm = similarity.recommendedMovies(esu, cum, userRating, um);
							recommendationHash = searcher.recommendedItemDocs(indexItemDirectoryUpdated, erm);
							// Predicted Rating
							predictionsHash = similarity.predictionsHash(esm, esu, um, erm);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					// Chebyshev Similarity
					if (event.getSource() == chebyshevButton) {
						double[] chsm = similarity.chebyshevSimilarityMatrix(Integer.valueOf(user.getId()), um);
						ArrayList<Integer> chsu = similarity.similarUsers(Integer.valueOf(user.getId()), similarUsersNo, chsm);
						try {
							ArrayList<String> chrm = similarity.recommendedMovies(chsu, cum, userRating, um);
							recommendationHash = searcher.recommendedItemDocs(indexItemDirectoryUpdated, chrm);
							// Predicted Rating
							predictionsHash = similarity.predictionsHash(chsm, chsu, um, chrm);
						} catch (IOException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					
					
				}
			}

			// Rounded Jaccard Similarity
			if (event.getSource() == roundedJaccardButton) {
				if (user == null) {
					changePage(1);
					JOptionPane.showMessageDialog(null, "You need to Sign In or Sing Up first...");
				} else {
					int[][] um = null;
					int[][] rum = null;
					try {
						um = similarity.utilityMatrixPopulator(indexDataDirectory);
						rum = similarity.utilityMatrixPopulatorRounded(indexDataDirectory);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ArrayList<String> cum = similarity.currentUserMovies(Integer.valueOf(user.getId()), um);
					double[] jrsm = similarity.jaccardSimilarityMatrix(Integer.valueOf(user.getId()), rum, cum);
					ArrayList<Integer> jrsu = similarity.similarUsers(Integer.valueOf(user.getId()), similarUsersNo, jrsm);
					try {
						ArrayList<String> jrrm = similarity.recommendedMovies(jrsu, cum, userRating, um);
						recommendationHash = searcher.recommendedItemDocs(indexItemDirectoryUpdated, jrrm);
						// Predicted Rating
						predictionsHash = similarity.predictionsHashRounded(jrsu, rum, jrrm);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}

			// HashMap Implementation
			recommendationListModel = new DefaultListModel<String>();
			// recommendationListModel.removeAllElements();
			for (String recommendationTitle : recommendationHash.keySet())
				recommendationListModel.addElement(recommendationTitle);
			/*if (recommendationListModel.isEmpty())
				System.out.println("ListModel Empty After Search");*/
			recommendationList.setModel(recommendationListModel);

			/*System.out.println("\nMovies In List After Recommendation");
			// HashMap Implementation
			for (String title : recommendationHash.keySet())
				System.out.println(title);*/

			recsListScrollPane.revalidate();

		}
	}
}
