import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class MainFrame extends JFrame implements ActionListener
{	
	//Labels
	//Sign In/Up Labels
	private JLabel usernameSILabel;
	private JLabel usernameSULabel;
	private JLabel ageLabel;
	private JLabel genderLabel;
	private JLabel occupationLabel;
	private JLabel zipCodeLabel;
	private JLabel imageLabel;
	//Movie Search Labels
	private JLabel usernameMSLabel;
	private JLabel idLabel;	
	private JLabel titleMSLabel;
	private JLabel releaseMSLabel;
	private JLabel genreMSLabel;
	//Movie Rate Labels
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
	
	//TextFields
	//Sign In/Up TextFields
	private JTextField usernameSITF;
	private JTextField usernameSUTF;
	private JTextField ageSUTF;
	private JTextField genderSUTF;
	private JTextField occupationSUTF;
	private JTextField zipCodeSUTF;
	//Movie Search TextFields
	private JTextField idTF;
	private JTextField titleTF;
	private JTextField releaseTF;
	private JTextField genreTF;
	
	
	//Buttons
	//Sign In/Up Buttons
	private JButton signInButton;
	private JButton signUpButton;
	//Movie Search Buttons
	private JButton searchButton;
	private JButton signOutButton;
	private JButton recommendButton;
	//Movie Rate Buttons
	private JButton rateButton;
	private JButton searchMRButton;
	private JButton signOutMRButton;
	//Recommendations Buttons
	private JButton jaccardButton;
	private JButton roundedJaccardButton;
	private JButton pearsonButton;
	private JButton cosineButton;
	private JButton euclideanButton;
	private JButton chebyshevButton;
	private JButton signOutRecButton;
	private JButton searchRecButton;
	
	//Panels
	//Sign In/Up Panels
	private JPanel mainPanel;
	private JPanel signInPanel;
	private JPanel signUpPanel;
	//Movie Search Panels
	private JPanel movieSearchPanel;
	private JPanel infoSearchPanel;
	private JPanel buttonSearchPanel;
	//Movie Rate Panels
	private JPanel movieRatePanel;
	private JPanel infoRatePanel;
	private JPanel buttonRatePanel;
	//Recommendations Panel
	private JPanel recommendationsPanel;
	private JPanel buttonRecommendationsPanel;
	private JPanel searchRecommendationsPanel;
	
	//Menu, MenuBar & MenuItems
	private JMenu mainMenu;
	private JMenuBar mainMB;
	private JMenuItem homeMI;
	private JMenuItem signInMI;
	private JMenuItem signUpMI;
	private JMenuItem movieSearchMI;
	private JMenuItem movieRateMI;
	private JMenuItem movieRecMI;
	private JMenuItem exitMI;
	
	
	//Movie Search JList
	private JList<String> movieList;
	//Recommendations JList
	private JList<String> recommendationList;
	
	//Movie Search ArrayLists -> JList
	private ArrayList<String> movieTitles;
	private ArrayList<Document> movieDocs;
	//Recommendations ArrayLists -> JList
	private ArrayList<String> recommendationTitles;
	private ArrayList<Document> recommendationDocs;
	
	//Movie Search HashMap
	private static HashMap<String,Document> movieHash;
	//Recommendations HashMap
	private static HashMap<String,Document> recommendationHash;
	
	//Movie List ListModel
	private DefaultListModel<String> movieListModel;
	//Recommendation List ListModel
	private DefaultListModel<String> recommendationListModel;
	
	//MovieSearch JScroll Pane
	private JScrollPane movieListScrollPane;
	//Recommendations List JScroll Pane
	private JScrollPane recommendationListScrollPane;
	
	//ImageIcon
	private ImageIcon scotlandImage;
	
	//CardLayout
	CardLayout cardLayout;
	private String queryString;
	
	//Test String
	private static String userID;
	private static User user;
	
	private static String indexDataDirectory = "indexed\\uDataIndex";
	private static String indexItemDirectory = "indexed\\uItemIndex";
	
	private static int users = 943; //943 //5
	private static int items = 1682; //1682 //4
	
	private static int userRating = 5;
	private static int similarUsersNo = 5;
	
	public MainFrame() throws IOException, ParseException
	{
		//Menu Items
		homeMI = new JMenuItem("Home");
		signInMI = new JMenuItem("Sign In");
		signUpMI = new JMenuItem("Sign Up");
		movieSearchMI = new JMenuItem("Movie Search");
		movieRateMI = new JMenuItem("Movie Rate");
		movieRecMI = new JMenuItem("Movie Recommendations");
		exitMI = new JMenuItem("Exit");
		
		//MenuListener, MenuItems: Add Listener
		//MenuListener listener = new MenuListener();
		homeMI.addActionListener(this);
		signInMI.addActionListener(this);
		signUpMI.addActionListener(this);
		movieSearchMI.addActionListener(this);
		movieRateMI.addActionListener(this);
		movieRecMI.addActionListener(this);
		exitMI.addActionListener(this);
		
		
		//Main Menu: Add Home, Sign In, Sign Up, Exit MenuItems
		mainMenu = new JMenu("What to do ?");
		mainMenu.add(homeMI);
		mainMenu.add(signInMI);
		mainMenu.add(signUpMI);
		mainMenu.add(movieSearchMI);
		mainMenu.add(movieRateMI);
		mainMenu.add(movieRecMI);
		mainMenu.add(exitMI);
		
		//Main Menu Bar: Add Main Menu
		mainMB = new JMenuBar();
		mainMB.add(mainMenu);
		
		//Labels
		//Sign In Panel Label
		usernameSILabel = new JLabel("Username");
		//Sign Up Panel Labels
		usernameSULabel = new JLabel("Username/User ID");
		ageLabel = new JLabel("Age");
		genderLabel = new JLabel("Gender");
		occupationLabel = new JLabel("Occupation");
		zipCodeLabel = new JLabel("Zip Code");
		//Movie Search Labels
		usernameMSLabel = new JLabel("Username");
		idLabel = new JLabel("Movie ID");
		titleMSLabel = new JLabel("Title");
		releaseMSLabel = new JLabel("Release");
		genreMSLabel = new JLabel("Genre");
		//Movie Rate Labels
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
		ratedMRLabel = new JLabel("Unrated");
		
		//TextFields
		//Sign In Panel TextField
		usernameSITF = new JTextField();
		//Sign Up Panel TextFields
		usernameSUTF = new JTextField();
		ageSUTF = new JTextField();
		genderSUTF = new JTextField();
		occupationSUTF = new JTextField();
		zipCodeSUTF = new JTextField();
		//Movie Search TextField
		idTF = new JTextField();
		titleTF = new JTextField();
		releaseTF = new JTextField();
		genreTF = new JTextField();
			
	
		//Buttons
		//Sign In Panel Button
		signInButton = new JButton("Sign In");
		//Sign Up Panel Button
		signUpButton = new JButton("Sign Up");
		//Movie Search Buttons
		signOutButton = new JButton("Sign Out");
		searchButton = new JButton("Search Movie");
		recommendButton = new JButton("Get Recommendation");
		//Movie Rate Buttons
		searchMRButton = new JButton("Search");
		signOutMRButton = new JButton("Sing Out");
		rateButton = new JButton("Rate");
		//Movie Recommendations Buttons
		searchRecButton = new JButton("Search");
		signOutRecButton = new JButton("Sign Out");
		jaccardButton = new JButton("Jaccard Similarity");
		roundedJaccardButton = new JButton("Rounded Jaccard Similarity");
		pearsonButton = new JButton("Pearson Correlation");
		cosineButton = new JButton("Cosine Similarity");
		euclideanButton = new JButton("Euclidean Distance");
		chebyshevButton = new JButton("Chebyshev");
		
		//ButtonListener
		ButtonListener listener = new ButtonListener();
		signInButton.addActionListener(listener);
		signUpButton.addActionListener(listener);
		signOutButton.addActionListener(listener);
		rateButton.addActionListener(listener);
		searchButton.addActionListener(listener);
		recommendButton.addActionListener(listener);
		searchMRButton.addActionListener(listener);
		signOutMRButton.addActionListener(listener);
		searchRecButton.addActionListener(listener);
		signOutRecButton.addActionListener(listener);
		jaccardButton.addActionListener(listener);
		roundedJaccardButton.addActionListener(listener);
		pearsonButton.addActionListener(listener);
		cosineButton.addActionListener(listener);
		euclideanButton.addActionListener(listener);
		chebyshevButton.addActionListener(listener);
		
		//ArrayLists
		movieTitles = new ArrayList<String>();
	    movieDocs = new ArrayList<Document>();
	    
	    recommendationTitles = new ArrayList<String>();
	    recommendationDocs = new ArrayList<Document>();
		//HashMap
	    movieHash = new HashMap<String,Document>();	 
		recommendationHash = new HashMap<String,Document>();
		
		//MovieSearch: MovieList List Model
	    movieListModel = new DefaultListModel<String>();
	    if(movieListModel.isEmpty())
	    	System.out.println("ListModel Empty At Constructor");
	    //ArrayList Implementation
	    /*for(String title: movieTitles)
	    	listModel.addElement(title);
		*/
	   //Hash Implementation
	   for(String title: movieHash.keySet())
	    	movieListModel.addElement(title);
	    
	    //MovieSearch JList
	    movieList = new JList<String>(movieListModel);
	    movieList.setVisibleRowCount(4);
	    movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    //MovieList Selection Listener
		movieList.addListSelectionListener(
				new ListSelectionListener()
				{
					public void valueChanged(ListSelectionEvent event)
					{
		
						queryString = movieList.getSelectedValue().toString();
	
						//ArrayList Implementation
						/*for(Document movieDoc: movieDocs)
						 * {
							if(movieDoc.get("title").equals(queryString))
							{
								titleMRLabel.setText(movieDoc.get("title"));
								idMRLabel.setText(movieDoc.get("id"));
								releaseMRLabel.setText(movieDoc.get("releaseDate"));
								imdbMRLabel.setText(movieDoc.get("imdbURL"));
								genreMRLabel.setText(movieDoc.get("genre"));
								cardLayout.show(mainPanel,"5");
								break;
							}
						}*/
						
						//HashMap Implementation
						for(String title: movieHash.keySet())
						{
							if(title.equals(queryString))
							{
								titleMRLabel.setText(movieHash.get(title).get("title"));
								idMRLabel.setText(movieHash.get(title).get("id"));
								releaseMRLabel.setText(movieHash.get(title).get("releaseDate"));
								imdbMRLabel.setText(movieHash.get(title).get("imdbURL"));
								genreMRLabel.setText(movieHash.get(title).get("genre"));
								cardLayout.show(mainPanel,"5");
								break;
							}
						}
					}
				}
				);
			 

		//Sign In Panel: Set Layout, Add Labels, TextFields & Buttons
		signInPanel = new JPanel();
		signInPanel.setLayout(new GridLayout(3,1,20,10));
		signInPanel.add(usernameSILabel);
		signInPanel.add(usernameSITF);
		signInPanel.add(signInButton);
			
		
		//Sign Up Panel: Set Layout, Add Labels, TextFields & Buttons
		signUpPanel = new JPanel();
		signUpPanel.setLayout(new GridLayout(6,2,20,10));
		signUpPanel.add(usernameSULabel);
		signUpPanel.add(usernameSUTF);
		signUpPanel.add(ageLabel);
		signUpPanel.add(ageSUTF);
		signUpPanel.add(genderLabel);
		signUpPanel.add(genderSUTF);
		signUpPanel.add(occupationLabel);
		signUpPanel.add(occupationSUTF);
		signUpPanel.add(zipCodeLabel);
		signUpPanel.add(zipCodeSUTF);
		signUpPanel.add(signUpButton);
		
		
		//Movie Search InfoSearch Panel: Set Layout, Add Labels, TextFields & Button
		infoSearchPanel = new JPanel();
		infoSearchPanel.setLayout(new GridLayout(5,2,20,10));
		infoSearchPanel.setBorder(new TitledBorder("Movie Search Info"));
		infoSearchPanel.add(usernameMSLabel);
		infoSearchPanel.add(signOutButton);
		infoSearchPanel.add(idLabel);
		infoSearchPanel.add(idTF);
		infoSearchPanel.add(titleMSLabel);
		infoSearchPanel.add(titleTF);
		infoSearchPanel.add(releaseMSLabel);
		infoSearchPanel.add(releaseTF);
		infoSearchPanel.add(genreMSLabel);
		infoSearchPanel.add(genreTF);
		
		//Movie Search ButtonSearch Panel: Set Layout, Add Labels, Buttons & ListView
		buttonSearchPanel = new JPanel();
		buttonSearchPanel.setLayout(new GridLayout(1,1,20,10));
		buttonSearchPanel.setBorder(new TitledBorder("Search Results & Recommendations"));
		buttonSearchPanel.add(searchButton);
		//buttonSearchPanel.add(resultsMSLabel);
		buttonSearchPanel.add(recommendButton);
		//buttonSearchPanel.add(movieList);
		
		//Movie Search JScroll Pane: Set Size, Border
		movieListScrollPane = new JScrollPane(movieList);
		movieListScrollPane.setPreferredSize(new Dimension(200, 200));
		movieListScrollPane.setBorder(new TitledBorder("Movie List"));
		/*sp.getVerticalScrollBar().setUI(new MyScrollBarUI());
		UIManager.put("ScrollBarUI", "my.package.MyScrollBarUI");*/
		
		//Movie Search Panel: Set Layout, Add Panels
		movieSearchPanel = new JPanel(new BorderLayout());
		//movieSearchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		movieSearchPanel.add(infoSearchPanel, BorderLayout.NORTH);
		movieSearchPanel.add(movieListScrollPane, BorderLayout.CENTER);
		movieSearchPanel.add(buttonSearchPanel, BorderLayout.SOUTH);
		
		
		
		//Movie Rate InfoRate Panel: Set Layout, Add Labels, TextFields & Button
		infoRatePanel= new JPanel();
		infoRatePanel.setLayout(new GridLayout(6,2,20,10));
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
		
		
		
		//Movie Rate ButtonRate Panel: Set Layout, Add Labels, TextFields & Button
		buttonRatePanel = new JPanel();
		buttonRatePanel.setLayout(new FlowLayout());
		buttonRatePanel.setBorder(new TitledBorder("Rate or Search for a Movie"));
		buttonRatePanel.add(rateButton);
		buttonRatePanel.add(searchMRButton);
		buttonRatePanel.add(signOutMRButton);
		
		//Movie Rate Panel: Set Layout, Add Panels
		movieRatePanel = new JPanel();
		//movieRatePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		movieRatePanel.setLayout(new BorderLayout());
		movieRatePanel.add(movieMRLabel, BorderLayout.NORTH);
		movieRatePanel.add(infoRatePanel, BorderLayout.CENTER);
		movieRatePanel.add(buttonRatePanel, BorderLayout.SOUTH);
		
		//Recommendations ButtonRecommendations Panel: Set Layout, Buttons
		searchRecommendationsPanel = new JPanel();
		searchRecommendationsPanel.setLayout(new GridLayout(3,2,20,10));
		searchRecommendationsPanel.setBorder(new TitledBorder("Similarity Measures"));
		searchRecommendationsPanel.add(jaccardButton);
		searchRecommendationsPanel.add(roundedJaccardButton);
		searchRecommendationsPanel.add(pearsonButton);
		searchRecommendationsPanel.add(cosineButton);
		searchRecommendationsPanel.add(euclideanButton);
		searchRecommendationsPanel.add(chebyshevButton);
		
		
		//MovieSearch: MovieList List Model
	    recommendationListModel = new DefaultListModel<String>();
	    if(recommendationListModel.isEmpty())
	    	System.out.println("ListModel Empty At Constructor");
	    //ArrayList Implementation
	    /*for(String title: movieTitles)
	    	listModel.addElement(title);
		*/
	   //Hash Implementation
	   for(String title: recommendationHash.keySet())
	    	recommendationListModel.addElement(title);
		
		//Recommendation JList
		recommendationList = new JList<String>(recommendationListModel);
		recommendationList.setVisibleRowCount(4);
		recommendationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    //MovieList Selection Listener
		recommendationList.addListSelectionListener(
				new ListSelectionListener()
				{
					public void valueChanged(ListSelectionEvent event)
					{
		
						queryString = recommendationList.getSelectedValue().toString();
	
						//ArrayList Implementation
						/*for(Document movieDoc: movieDocs)
						 * {
							if(movieDoc.get("title").equals(queryString))
							{
								titleMRLabel.setText(movieDoc.get("title"));
								idMRLabel.setText(movieDoc.get("id"));
								releaseMRLabel.setText(movieDoc.get("releaseDate"));
								imdbMRLabel.setText(movieDoc.get("imdbURL"));
								genreMRLabel.setText(movieDoc.get("genre"));
								cardLayout.show(mainPanel,"5");
								break;
							}
						}*/
						
						//HashMap Implementation
						for(String title: recommendationHash.keySet())
						{
							if(title.equals(queryString))
							{
								titleMRLabel.setText(recommendationHash.get(title).get("title"));
								idMRLabel.setText(recommendationHash.get(title).get("id"));
								releaseMRLabel.setText(recommendationHash.get(title).get("releaseDate"));
								imdbMRLabel.setText(recommendationHash.get(title).get("imdbURL"));
								genreMRLabel.setText(recommendationHash.get(title).get("genre"));
								cardLayout.show(mainPanel,"5");
								break;
							}
						}
					}
				}
				);
		
		//Recommendations JScroll Pane: Set Layout, AddPanels
		recommendationListScrollPane = new JScrollPane(recommendationList);
		recommendationListScrollPane.setPreferredSize(new Dimension(200, 200));
		recommendationListScrollPane.setBorder(new TitledBorder("Recommendations List"));
		
		//Recommendations Button Recommendations Panel: Set Layout, Add Buttons
		buttonRecommendationsPanel = new JPanel();
		buttonRecommendationsPanel.setLayout(new FlowLayout());
		buttonRecommendationsPanel.setBorder(new TitledBorder("Search for a Movie or Sign Out"));
		//buttonRecommendationsPanel.add(rateButton);
		buttonRecommendationsPanel.add(searchRecButton);
		buttonRecommendationsPanel.add(signOutRecButton);
		
		//Recommendations Panel: Set Layout, Add Panels
		recommendationsPanel = new JPanel();
		//recommendationsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		recommendationsPanel.setLayout(new BorderLayout());
		recommendationsPanel.add(searchRecommendationsPanel, BorderLayout.NORTH);
		recommendationsPanel.add(recommendationListScrollPane, BorderLayout.CENTER);
		recommendationsPanel.add(buttonRecommendationsPanel, BorderLayout.SOUTH);
		
		//ImageIcon & Label
		scotlandImage = new ImageIcon("scotland.jpg");
		imageLabel = new JLabel(scotlandImage);
		
		//Main Panel: Set Layout, Add SignIn,SignUp Panels
		mainPanel = new JPanel();
		cardLayout = new CardLayout();
		mainPanel.setLayout(cardLayout);
		mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		mainPanel.add(imageLabel, "3");
		mainPanel.add(signInPanel, "1");
		mainPanel.add(signUpPanel, "2");
		mainPanel.add(movieSearchPanel,"4");
		mainPanel.add(movieRatePanel,"5");
		mainPanel.add(recommendationsPanel,"6");
		cardLayout.show(mainPanel, "3");
		
		
		//Frame Staff
		this.setContentPane(mainPanel);
		this.setJMenuBar(mainMB);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setSize(350, 700);
		this.setVisible(true);
	}
	
	
	public void actionPerformed(ActionEvent event)
	{
		String buttonString = event.getActionCommand();
			
		if(buttonString.equals("Home"))
		{
			this.setTitle("Home");
			cardLayout.show(mainPanel,"3");
		}
		else if(buttonString.equals("Sign In"))
		{
			this.setTitle("Sign In");
			cardLayout.show(mainPanel,"1");
		}
		else if(buttonString.equals("Sign Up"))
		{
			this.setTitle("Sign Up");
			cardLayout.show(mainPanel,"2");
		}
		else if(buttonString.equals("Movie Search"))
		{
			this.setTitle("Movie Search");
			cardLayout.show(mainPanel,"4");
		}
		else if(buttonString.equals("Movie Rate"))
		{
			this.setTitle("Movie Rate");
			cardLayout.show(mainPanel,"5");
		}
		else if(buttonString.equals("Movie Recommendations"))
		{
			this.setTitle("Movie Recommendations");
			cardLayout.show(mainPanel, "6");
		}
		else if(buttonString.equals("Exit"))
			System.exit(0);
		else
			System.out.println("Unexpected Error");
	}

	//Add Rating (Index new Document)
	public static void addDoc(String userID, String itemID, String rating)  throws IOException
	{
		
		Analyzer analyzer = new StandardAnalyzer();
		FSDirectory index = FSDirectory.open(Paths.get("indexed\\uDataIndex"));	//Σε περίπτωση που υπάρχει ήδη το αρχείο κάνει append όχι overwrite
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
	
	//Matches and returns HashMap with recommended Movie Titles and the respective Movie Documents containing Movie ID, Title, Release Date and Genre
	public HashMap<String,Document> recommendedMovieHash(String indexDirectory, String idField, ArrayList<String> recommendedMovieIDs) throws IOException, ParseException
		{
			//ArrayList to Contain FieldNames
			ArrayList<String> fields = new ArrayList<String>();
			//HashMap to Contain Movie Title and the Respective Document it Belongs to
			HashMap<String,Document> documentHash = new HashMap<String,Document>();
			
			//Add wanted field to searched fields
			fields.add(idField);
			
			//Match every Recommended Movie ID with the Respective Title and Document
			for(String queryString:recommendedMovieIDs)
			{
				//Checks if there is at least one actual written TextField
				File file = new File(indexDirectory);
					
				//Open index
				Directory directory = FSDirectory.open(file.toPath()); //3
				IndexReader indexReader = DirectoryReader.open(directory);
				IndexSearcher indexSearcher = new IndexSearcher(indexReader);
					
				//Multi-Parse the final Query
				MultiFieldQueryParser parser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), new StandardAnalyzer());
				Query query = parser.parse(queryString);
					
				//Calculating Search Time
				//long start = System.currentTimeMillis();
				
				//Search index
				TopDocs hits = indexSearcher.search(query, 25);		
				
				//long end = System.currentTimeMillis();
			
				//Write search stats
				//System.err.println("Found " + hits.totalHits + " topics(s) (in " + (end - start) + " milliseconds) that matched query '" + queryString + "':");
					
				for(ScoreDoc scoreDoc : hits.scoreDocs)
				{
					//Retrieve matching document
					Document document = indexSearcher.doc(scoreDoc.doc);
					//Put Movie Title and Document to HashMap
					documentHash.put(document.get("title"), document);
				}
				
				//Close IndexSearcher
				indexReader.close();
			}
			return documentHash;               		 
		}
	
	//Searches and returns HashMap with found Movie Titles and the respective Movie Documents containing Movie ID, Title, Release Date and Genre
	public HashMap<String,Document> searchMovieHash(String indexDirectory, String fieldName1, String queryString1, String fieldName2, String queryString2, String fieldName3, String queryString3, String fieldName4, String queryString4) throws IOException, ParseException
	{
		//ArrayList to Contain FieldNames
		ArrayList<String> fields = new ArrayList<String>();
		//HashMap to Contain Movie Title and the Respective Document it Belongs to
		HashMap<String,Document> documentHash = new HashMap<String,Document>();
		
		
		//Conditions to form final QueryString(AND)
		if(!queryString1.equals("")){
			queryString = queryString1;
			fields.add(fieldName1);
		}
		if(!queryString2.equals("")){
			if(!fields.isEmpty())
				queryString += " AND " + queryString2;
			else
				queryString = queryString2;
			fields.add(fieldName2);
		}
		if(!queryString3.equals("")){
			if(!fields.isEmpty())
				queryString += " AND " + queryString3;
			else
				queryString = queryString3;
			fields.add(fieldName3);
		}
		if(!queryString4.equals("")){
			if(!fields.isEmpty())
				queryString += " AND " + queryString4;
			else
				queryString = queryString4;
			fields.add(fieldName4);
		}
		
		
		//Checks if there is at least one actual written TextField
		if(!fields.isEmpty())
		{
			File file = new File(indexDirectory);
			
			//Open index
			Directory directory = FSDirectory.open(file.toPath()); //3
			IndexReader indexReader = DirectoryReader.open(directory);
			IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			
			//Multi-Parse the final Query
			MultiFieldQueryParser parser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), new StandardAnalyzer());
			Query query = parser.parse(queryString);
			
			//Calculating Search Time
			//long start = System.currentTimeMillis();
		
			//Search index
			TopDocs hits = indexSearcher.search(query, 25);		
		
			//long end = System.currentTimeMillis();
	
			//Write search stats
			//System.err.println("Found " + hits.totalHits + " topics(s) (in " + (end - start) + " milliseconds) that matched query '" + queryString + "':");
			
			
			
			for(ScoreDoc scoreDoc : hits.scoreDocs)
			{
				//Retrieve matching document
				Document document = indexSearcher.doc(scoreDoc.doc);
				//Put Movie Title and Document to HashMap
				documentHash.put(document.get("title"), document);
	
			}
		
			//Close IndexSearcher
			indexReader.close();
			
		}
	
		return documentHash;               		 
	}
	
	//Return Array of Row Averages
	public static double[] rowAverages(int utilityMatrix[][])
	{
		double temp;
		double [] rowAverages = new double[users+1];
		for(int i = 1; i <= users; i++)
		{
			temp = 0;
			//System.out.print("User" + i + ". ");
			for(int j = 1; j <= items; j++)
			{
				temp += utilityMatrix[i][j];
				//System.out.print(utilityMatrix[i][j] + " ");
			}
			//System.out.println();
			rowAverages[i] = temp / items;
			
		}
		
		return rowAverages;
	}
	
	//Returns Array with the Pearson Similarities between the given User and the rest of the Users
	public static double[] pearsonSimilarityMatrix(int currentUser, int utilityMatrix[][])
	{
		double [] pearsonSimMatrix = new double [users+1];
		//double [] averages = new double[users+1];
		double [] userAverages = rowAverages(utilityMatrix);
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
		
		System.out.println("Similarity to User " + currentUser);
		for(int i = 1; i <= users; i++)
		{
			product = 1;
			productSum = 0;
			rxSum = 0;
			rySum = 0;
			
			for(int j = 1; j <= items; j++)
			{
				
				//Differences
				rxs_rx = utilityMatrix[currentUser][j] - userAverages[currentUser];
				rys_ry = utilityMatrix[i][j] - userAverages[i];
				
				product = rxs_rx * rys_ry;
				
				//Sum of Products of Differences
				productSum += product;
				//productSum += rxs_rx * rys_ry;
				
				//Sum of Squares of Differences
				rxs_rx2 = Math.pow(rxs_rx,2);
				rxSum += rxs_rx2;
				rys_ry2 = Math.pow(rys_ry, 2);
				rySum += rys_ry2;
			}
			
			
			//Root Sum of Squares of Differences
			rxRoot = Math.sqrt(rxSum);
			ryRoot = Math.sqrt(rySum);
			
			pearsonSimMatrix[i] = productSum / (rxRoot * ryRoot);
			//System.out.println(i + ". " + pearsonSimMatrix[i]);
		}
		return pearsonSimMatrix;
	}
	
	//Prints Reccomended Movies along with Current User ID, # of Similar Users we set and the Rating we set as standard
	public static void printRecommendedMovies(int currentUserID, int similarUsersNo, int userRating, ArrayList<String> recommendedMovies)
	{
		int i = 1;
		System.out.println("Recommended Movies");
		for(String movie: recommendedMovies)
		{
			System.out.println(i +". " + movie);
			i++;
		}
	}
	
	//Returns Array with the Chebyshev Similarities between the given User and the rest of the Users
	public static double[] chebyshevSimilarityMatrix(int currentUser, int utilityMatrix[][])
	{
		double[] chebSimMatrix = new double[users+1];
		double sum, distance;
		double dif = 0;
		double difInf = 0;
		for(int i = 1; i <= users; i++)
		{
			sum =0;
			
			for(int j = 1; j <= items; j++)
			{
				dif = utilityMatrix[currentUser][j] - utilityMatrix[i][j];
				difInf = Math.pow(dif, Double.POSITIVE_INFINITY);
				sum += difInf;
			}
			
			//Calculating the Euclidean Distance
			distance = Math.pow(sum, 1/Double.POSITIVE_INFINITY);
			
			//Calculating and Storing the Euclidean Similarity by subtracting the Euclidean Distance from 1
			chebSimMatrix[i] = 1 - distance;
				
		}
			
		return chebSimMatrix;
	}
	
	//Returns Array with the Euclidean Similarities between the given User and the rest of the Users
	public static double[] euclideanSimilarityMatrix(int currentUser, int utilityMatrix[][])
	{
		double[] euclSimMatrix = new double[users+1];
		double sum, euclideanDistance;
		double dif = 0;
		double dif2 = 0;
		for(int i = 1; i <= users; i++)
		{
			sum =0;
			
			for(int j = 1; j <= items; j++)
			{
				dif = utilityMatrix[currentUser][j] - utilityMatrix[i][j];
				dif2 = Math.pow(dif, 2);
				sum += dif2;
			}
			
			//Calculating the Euclidean Distance
			euclideanDistance = Math.sqrt(sum);
			
			//Calculating and Storing the Euclidean Similarity by subtracting the Euclidean Distance from 1
			euclSimMatrix[i] = 1 - euclideanDistance;
				
		}
			
		return euclSimMatrix;
	}
	
	//Returns Array with the Jaccard Similarities between the given User and the rest of the Users
	public static double[] jaccardSimilarityMatrix(int currentUser, int utilityMatrix[][], ArrayList<String> currentUserMovies)
	{
		double[] jacSimMatrix = new double[users+1];
		
		ArrayList<String> tempList = new ArrayList<String>();
		ArrayList<String> intersectionList = new ArrayList<String>();
		ArrayList<String> unionList = new ArrayList<String>();
		
		for(int i = 1; i <= users; i++)
		{
			for(int j = 1; j <= items; j++)
			{
				//Condition for Union
				if(utilityMatrix[i][j] != 0)
				{
					tempList.add(String.format("%04d", j));
					//Condition for Intersection
					if(currentUserMovies.contains(String.format("%04d", j)))
						intersectionList.add(String.format("%04d", j));
				}
			}
			
			//Getting the Union between Searching User's Movies and the Rest 
			unionList = union(currentUserMovies, tempList);
			
			//Calculating and Storing the Jaccard Coefficient in the jacSimMatrix 
			jacSimMatrix[i] = (double) intersectionList.size() / unionList.size();
			
			intersectionList.clear();
			tempList.clear();
		}
		
		return jacSimMatrix;
	}
	
	//Return the Union(ArrayList) of the two given ArrayLists
	public static ArrayList<String> union(ArrayList<String> List1,ArrayList<String> List2)
	{
		HashSet<String> set = new HashSet<String>();
		
		set.addAll(List1);
		set.addAll(List2);
		
		return new ArrayList<String>(set);
	}
	
	
	//Returns ArrayList of Movies with #userRated rating and higher by similar Users
	public static ArrayList<String> recommendedMovies(ArrayList<Integer> similarUsers, ArrayList<String> currentUserMovies, int userRating, int[][] utilityMatrix) throws IOException, ParseException
	{
		ArrayList<String> suggestions = new ArrayList<String>();
		String itemID;
        for(int user: similarUsers)
        {
        	for(int j = 1; j <= users; j++)
        	{
        		itemID = String.format("%04d", j);
        		if(utilityMatrix[user][j] >= userRating && !suggestions.contains(itemID))
        			suggestions.add(itemID);
        	}
        }
        
        suggestions = removeMoviesInCommon(suggestions, currentUserMovies);
        //suggestions = replaceIdWithTitle(suggestions);
		return suggestions;
	}
	
	//Removes Items' IDs from higlyRatedMovies ArrayList the movies it has in common with the currentUserMovies ArrayLists
	public static ArrayList<String> removeMoviesInCommon(ArrayList<String> highlyRatedMovies, ArrayList<String> currentUserMovies)
	{
		
        for(String movie: currentUserMovies)
        	if(highlyRatedMovies.contains(movie))
        		highlyRatedMovies.remove(movie);
        
		return highlyRatedMovies;
	}
	
	//Returns ArrayList with Titles instead of Item IDs
	public static ArrayList<String> replaceIdWithTitle(ArrayList<String> highlyRatedMovies) throws IOException, ParseException
	{
		String titleTemp;
		ArrayList<String> titlesList = new ArrayList<String>();
		for(String suggestion: highlyRatedMovies)
		{
			String itemQueryString = suggestion;
			File itemFile = new File(indexItemDirectory);
			titleTemp = titleSearch(itemQueryString,itemFile);
			
			//highlyRatedMovies.remove(suggestion);
			titlesList.add(titleTemp);
		}
		
		return titlesList;
	}
	
	//Returns ArrayList with the #similarUsersNo most similar users to the user given as parameter
	@SuppressWarnings({ "unchecked", "rawtypes"})
	public static ArrayList<Integer> similarUsers(int currentUser, int similarUsersNo, double[] cosineSimilarityMatrix)
	{
		//Putting the similarities in a map with respective users as keys
		Map<Integer, Double> cosineSimilarityMap = new HashMap<Integer, Double>();
        for(int i = 1; i <= users; i++)
        	cosineSimilarityMap.put(i, cosineSimilarityMatrix[i]);
        cosineSimilarityMap.remove(currentUser);
        
        //Sorting the map by values(similarity)
        Map<Integer, Double> map;
        map = sortByValues(cosineSimilarityMap);
        Set set = map.entrySet();
        Iterator iterator = set.iterator();
        
        //Selecting 5 most similar users and store the in ArratList similarUsers;
        //ArrayList<String> similarUsers = new ArrayList<String>();
        ArrayList<Integer> similarUsers = new ArrayList<Integer>();
        
        int counter = 0;
        while(iterator.hasNext() && counter<5)
        //while(counter < similarUsersNo)
        {
        	Map.Entry entry = (Map.Entry) iterator.next();
            /*System.out.print(entry.getKey() + ":\t");
            System.out.println(entry.getValue());*/
            //similarUsers.add(String.format("%03d", entry.getKey()));
            similarUsers.add((Integer) entry.getKey());
            counter ++;
        }
        
		return similarUsers;
	}
	
	//Returns ArrayList of Item IDs of the Movies the given User has rated
	public static ArrayList<String> currentUserMovies(int currentUser, int[][] utilityMatrix)
	{
		ArrayList<String> movies = new ArrayList<String>();
        for(int j=1; j<=items; j++)
        {
        	if(utilityMatrix[currentUser][j] !=0)
        		movies.add(String.format("%04d", j));
        	
        }
        
        return movies;
	}
	
	//Returns Array with the Cosine Similarities between the given User and the rest of the Users
	public static double[] cosineSimilarityMatrix(int currentUser, int utilityMatrix[][], double rssMatrix[])
	{
		double[] cosSimMatrix = new double[users+1];
		double productSum;
		System.out.println("Similarity to User " + 1);
		for(int j = 1; j <= users; j++)
		{
			productSum = 0;
			for(int k = 1; k <= items; k++)
			{
				
				//productSum += utilityMatrix[currentUser][k] * utilityMatrix[j][k];
				if(utilityMatrix[currentUser][k]!=0 && utilityMatrix[j][k]!=0)
				{
					productSum += utilityMatrix[currentUser][k] * utilityMatrix[j][k];
					
				}
			}
			
			
			cosSimMatrix[j] = productSum / (rssMatrix[currentUser]*rssMatrix[j]);
			//System.out.println(j + ". " + cosSimMatrix[j]);
		}
		return cosSimMatrix;
	}
	
	//Returns Array consisting the whole Utility Matrix
	public static int[][] utilityMatrixPopulator(String directoryString) throws IOException, ParseException
	{
		File file = new File(directoryString);

		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		
		String userString, itemString, ratingString, queryString;
		int itemTemp, ratingTemp;
		Query query;
		TopDocs hits;
		int[][] utilityMatrix = new int[users+5][items+5];
		
		String[] fields = {"dataUserID"};
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
				
		
		for(int i = 1; i <= users; i++)
		{
			userString = String.format("%03d", i);
			queryString = userString;
			query = parser.parse(queryString);    
			
			//We get the matches to User i
			hits = indexSearcher.search(query, items);		
			
			//For each match to User i we get the Item ID and Item Rating
			//Using i (User ID) and itemTemp (Item ID) as indexes we populate
			//the Utility Matrix
			for(ScoreDoc scoreDoc : hits.scoreDocs) 
			{
				Document document = indexSearcher.doc(scoreDoc.doc);                    
				
				itemString = document.get("dataItemID");
				itemTemp = Integer.valueOf(itemString);
				
				ratingString = document.get("dataRating");
				ratingTemp = Integer.valueOf(ratingString);
				
				utilityMatrix[i][itemTemp] = ratingTemp;
				//System.out.println(i + " " + itemTemp + ": " + utilityMatrix[i][itemTemp]);
			}
		}
		
		indexReader.close(); 
		
		return utilityMatrix;
	}
	
	//Returns Array consisting the whole Utility Matrix with 1s in place of 3s,4s and 5s, and 0s in place of no rating, 1s and 2s
	public static int[][] utilityMatrixPopulatorRounded(String directoryString) throws IOException, ParseException
	{
		File file = new File(directoryString);

		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
			
		String userString, itemString, ratingString, queryString;
		int itemTemp, ratingTemp;
		Query query;
		TopDocs hits;
		int[][] utilityMatrix = new int[users+5][items+5];
			
		String[] fields = {"dataUserID"};
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
					
			
		for(int i = 1; i <= users; i++)
		{
			userString = String.format("%03d", i);
			queryString = userString;
			query = parser.parse(queryString);    
				
			//We get the matches to User i
			hits = indexSearcher.search(query, items);		
			
			//For each match to User i we get the Item ID and Item Rating
			//Using i (User ID) and itemTemp (Item ID) as indexes we populate
			//the Utility Matrix
			for(ScoreDoc scoreDoc : hits.scoreDocs) 
			{
				Document document = indexSearcher.doc(scoreDoc.doc);                    
					
				itemString = document.get("dataItemID");
				itemTemp = Integer.valueOf(itemString);
				
				ratingString = document.get("dataRating");
				ratingTemp = Integer.valueOf(ratingString);
				
				//Redundant Condition - Utility Matrix element already equals 0
				/*if(ratingTemp <= 2)
					utilityMatrix[i][itemTemp] = 0;*/
				if(ratingTemp > 3)
					utilityMatrix[i][itemTemp] = 1;
				//System.out.println(i + " " + itemTemp + ": " + utilityMatrix[i][itemTemp]);
			}
		}
			
		indexReader.close(); 
			
		return utilityMatrix;
	}
		
	
	//Returns Array with the Root Sum of Squares of the Users' Ratings
	public static double[] rootSumSquaresMatrix(String directoryString) throws IOException, ParseException
	{
		File file = new File(directoryString);

		Directory directory = FSDirectory.open(file.toPath());
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		
		String userString, ratingString, queryString;
		int ratingTemp, sumOfSquares;
		Query query;
		TopDocs hits;
		
		double[] rssMatrix = new double[users+5];
		
		String[] fields = {"dataUserID"};
		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, new StandardAnalyzer());
					
		for(int i = 1; i <= users; i++)
		{
			userString = String.format("%03d", i);
			queryString = userString;
			query = parser.parse(queryString);    
			
			//We get the matches to User i
			hits = indexSearcher.search(query, items);		
			
			sumOfSquares = 0;
			
			//For each match to User i we get the Item ID and Item Rating
			//Using i (User ID) and itemTemp (Item ID) as indexes we populate
			//the Utility Matrix
			for(ScoreDoc scoreDoc : hits.scoreDocs)
			{
				Document document = indexSearcher.doc(scoreDoc.doc);                    

				ratingString = document.get("dataRating");
				ratingTemp = Integer.valueOf(ratingString);
				
				//We calculate the Sum of Squares of each User
				sumOfSquares += Math.pow(ratingTemp,2);
			}
			
			//We store each User's Root of the Sum of Squares for later use in the Cosine Similarity
			rssMatrix[i] =  Math.sqrt(sumOfSquares);
		}
		
		indexReader.close(); 
		
		return rssMatrix;
	}
	
	//Returns a sorted by values HashMap of Users as keys and the respective Cosine Similarities as values
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static HashMap sortByValues(Map<Integer, Double> cosineSimilarityMap) { 
	       List list = new LinkedList(cosineSimilarityMap.entrySet());
	       // Defined Custom Comparator here
	       Collections.sort(list, new Comparator(){
	            public int compare(Object o1, Object o2)
	            {
	               return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
	            }
	       }.reversed());

	       // Here I am copying the sorted list in HashMap
	       // using LinkedHashMap to preserve the insertion order
	       HashMap sortedHashMap = new LinkedHashMap();
	       for (Iterator it = list.iterator(); it.hasNext();) {
	              Map.Entry entry = (Map.Entry) it.next();
	              sortedHashMap.put(entry.getKey(), entry.getValue());
	       } 
	       return sortedHashMap;
	}
	
	//Returns Title(String) of the given Item ID
	public static String titleSearch(String itemQueryString, File itemFile) throws IOException, ParseException{
			//Open index
			String title = "";
			Directory itemDirectory = FSDirectory.open(itemFile.toPath()); //3
			IndexReader itemIndexReader = DirectoryReader.open(itemDirectory);
			IndexSearcher itemIndexSearcher = new IndexSearcher(itemIndexReader);
			    
			//Parse query
			//QueryParser parser2 = new QueryParser( "id", new StandardAnalyzer());
			String[] itemFields = {"id", "genre"};
			MultiFieldQueryParser itemParser = new MultiFieldQueryParser( itemFields, new StandardAnalyzer());
			Query itemQuery = itemParser.parse(itemQueryString);               
			    
			//Search index
			TopDocs itemHits = itemIndexSearcher.search(itemQuery, 30);
			for(ScoreDoc itemScoreDoc : itemHits.scoreDocs) {
				//Retrieve matching document
				Document itemDocument = itemIndexSearcher.doc(itemScoreDoc.doc);                    
			    //Display
				//System.out.print(itemDocument.get("id") + " ");
				//System.out.print(itemDocument.get("title") + " ");
				//System.out.print(itemDocument.get("releaseDate") + " ");
				//System.out.println();
				//System.out.print(itemDocument.get("imdbURL") + " ");
				//System.out.print(itemDocument.get("genre") + " ");
				
				title = itemDocument.get("title");
			}
			   
			//Close Index Reader
			itemIndexReader.close();                        
			 
			return title;
		}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource() == signInButton){
				userID = usernameSITF.getText();
				
				user = new OldUser(userID);
				JOptionPane.showMessageDialog(null,"Signed In " + user.getId());
				if(user!=null)
					usernameMSLabel.setText(user.getId());
			}
			if(event.getSource() == signUpButton)
			{
				user = new NewUser(usernameSUTF.getText(), ageSUTF.getText(), genderSUTF.getText(), occupationSUTF.getText(), zipCodeSUTF.getText());
				System.out.println(usernameSUTF.getText() + " " + user.getId());
				JOptionPane.showMessageDialog(null,"Now Signed IN " + user.getId());
				if(user!=null)
					usernameMSLabel.setText(user.getId());
			}
			if(event.getSource() == signOutButton){
				userID = null;
				JOptionPane.showMessageDialog(null,"Signed Out");
			}
			if(event.getSource() == rateButton){
				
				String rating = JOptionPane.showInputDialog("Rate " + titleMRLabel.getText());
				System.out.println(userID);
				if ((userID != null) && (userID.length() > 0) && (rating != null) && (rating.length() > 0)) {
					ratedMRLabel.setText(rating);
					try {
						addDoc(userID,idMRLabel.getText(),rating);
						System.out.println(userID);
					} catch (IOException e){
						e.printStackTrace();
					}
				    return;
				}
				
			}
			if(event.getSource() == searchButton){
				System.out.println();
				
				//ArrayList Implementation
				/*try {		
					if(!movieTitles.isEmpty())
						movieTitles.clear();
					movieTitles = searchMovieTitles("indexed\\uItemIndex", "id", idTF.getText(), "title", titleTF.getText(), "releaseDate", releaseTF.getText(), "genre", genreTF.getText());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				try {
					if(!movieDocs.isEmpty())
						movieDocs.clear();
					movieDocs = searchMovieDocs("indexed\\uItemIndex", "id", idTF.getText(), "title", titleTF.getText(), "releaseDate", releaseTF.getText(), "genre", genreTF.getText());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}*/
				
				//HashMap Implementation
				try {		
					if(!movieTitles.isEmpty())
						movieTitles.clear();
					movieHash = searchMovieHash("indexed\\uItemIndex", "id", idTF.getText(), "title", titleTF.getText(), "releaseDate", releaseTF.getText(), "genre", genreTF.getText());
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				//ArrayList Implementation
				/*listModel.removeAllElements();
				for(String movieTitle : movieTitles)
					listModel.addElement(movieTitle);
				if(listModel.isEmpty())
					System.out.println("ListModel Empty After Search");
				movieList.setModel(listModel);*/
				
				//HashMap Implementation
				movieListModel.removeAllElements();
				for(String movieTitle : movieHash.keySet())
					movieListModel.addElement(movieTitle);
				if(movieListModel.isEmpty())
					System.out.println("ListModel Empty After Search");
				movieList.setModel(movieListModel);
				
				System.out.println("Movies In List After Search");
				
				//ArrayList Implementation
				/*for(String movieTitle : movieTitles)
					System.out.println(movieTitle);*/
				
				//HashMap Implementation
				for(String title : movieHash.keySet())
					System.out.println(title);
				
				movieListScrollPane.revalidate();
					
				
			}
			if(event.getSource() == recommendButton)
			{
				setTitle("Movie Recommendations");
				cardLayout.show(mainPanel, "6");
			}
			if(event.getSource() == searchMRButton)
			{
				setTitle("Movie Search");
				cardLayout.show(mainPanel, "4");
			}
			if(event.getSource() == signOutMRButton){
				userID = null;
				JOptionPane.showMessageDialog(null,"Signed Out");
			}
			if(event.getSource() == searchRecButton)
			{
				setTitle("Movie Search");
				cardLayout.show(mainPanel, "4");
			}
			if(event.getSource() == signOutRecButton){
				userID = null;
				JOptionPane.showMessageDialog(null,"Signed Out");
			}
			if((event.getSource() == jaccardButton) || (event.getSource() == pearsonButton) || (event.getSource() == cosineButton) || (event.getSource() == euclideanButton) || (event.getSource() == chebyshevButton))
			{
				if(user==null)
				{
					setTitle("Sign In");
					cardLayout.show(mainPanel, "1");
					JOptionPane.showMessageDialog(null,"You need to Sign In or Sing Up first...");
				}
				else
				{
					int[][] um = null;
					try {
						um = utilityMatrixPopulator(indexDataDirectory);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
						
					ArrayList<String> cum = currentUserMovies(Integer.valueOf(userID),um);
					
					//Jaccard Similarity
					if(event.getSource() == jaccardButton)
					{
						double [] jsm = jaccardSimilarityMatrix(Integer.valueOf(userID),um,cum);
						ArrayList<Integer> jsu = similarUsers(Integer.valueOf(userID),similarUsersNo,jsm);
						try {
							ArrayList<String> jrm = recommendedMovies(jsu, cum, userRating, um);
							recommendationHash = recommendedMovieHash("indexed\\uItemIndex", "id", jrm);
						} catch (IOException e){
							e.printStackTrace();
						} catch (ParseException e){
							e.printStackTrace();
						}
					}
					
					//Pearson Correlation
					if(event.getSource() == pearsonButton)
					{
						double[] psm = pearsonSimilarityMatrix(Integer.valueOf(userID),um);
						ArrayList<Integer> psu = similarUsers(Integer.valueOf(userID), similarUsersNo, psm);
						try {
							ArrayList<String> prm = recommendedMovies(psu, cum, userRating, um);
							recommendationHash = recommendedMovieHash("indexed\\uItemIndex", "id", prm);
						} catch (IOException e){
							e.printStackTrace();
						} catch (ParseException e){
							e.printStackTrace();
						}
					}
					
					//Cosine Similarity
					if(event.getSource() == cosineButton)
					{
						double[] rssm = null;
						try {
							rssm = rootSumSquaresMatrix(indexDataDirectory);
						} catch (IOException e1) {
							e1.printStackTrace();
						} catch (ParseException e1) {
							e1.printStackTrace();
						}
						double[] csm = cosineSimilarityMatrix(Integer.valueOf(userID), um, rssm);
						ArrayList<Integer> csu = similarUsers(Integer.valueOf(userID), similarUsersNo, csm);
						try {
							ArrayList<String> crm = recommendedMovies(csu, cum, userRating, um);
							recommendationHash = recommendedMovieHash("indexed\\uItemIndex", "id", crm);
						} catch (IOException e){
							e.printStackTrace();
						} catch (ParseException e){
							e.printStackTrace();
						}
					}
					if(event.getSource() == euclideanButton)
					{
						double[] esm = euclideanSimilarityMatrix(Integer.valueOf(userID),um);
						ArrayList<Integer> esu = similarUsers(Integer.valueOf(userID), similarUsersNo, esm);
						try {
							ArrayList<String> erm = recommendedMovies(esu, cum, userRating, um);
							recommendationHash = recommendedMovieHash("indexed\\uItemIndex", "id", erm);
						} catch (IOException e){
							e.printStackTrace();
						} catch (ParseException e){
							e.printStackTrace();
						}
					}
					if(event.getSource() == chebyshevButton)
					{
						double[] chsm = chebyshevSimilarityMatrix(Integer.valueOf(userID),um);
						ArrayList<Integer> chsu = similarUsers(Integer.valueOf(userID), similarUsersNo, chsm);
						try {
							ArrayList<String> chrm = recommendedMovies(chsu, cum, userRating, um);
							recommendationHash = recommendedMovieHash("indexed\\uItemIndex", "id", chrm);
						} catch (IOException e){
							e.printStackTrace();
						} catch (ParseException e){
							e.printStackTrace();
						}	
					}
				}
				
				/*//HashMap Implementation
				recommendationListModel.removeAllElements();
				for(String recommendationTitle : recommendationHash.keySet())
					recommendationListModel.addElement(recommendationTitle);
				if(recommendationListModel.isEmpty())
					System.out.println("ListModel Empty After Search");
				recommendationList.setModel(recommendationListModel);
				
				//HashMap Implementation
				for(String title : recommendationHash.keySet())
					System.out.println(title);
				
				recommendationListScrollPane.revalidate();*/
				
			}
				
				
			if(event.getSource() == roundedJaccardButton)
			{
				if(user==null)
				{
					setTitle("Sign In");
					cardLayout.show(mainPanel, "1");
					JOptionPane.showMessageDialog(null,"You need to Sign In or Sing Up first...");
				}
				else
				{

					
					
					
					int[][] um = null;
					int[][] rum = null;
					try {
						um = utilityMatrixPopulator(indexDataDirectory);
						rum = utilityMatrixPopulatorRounded(indexDataDirectory);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}
					ArrayList<String> cum = currentUserMovies(Integer.valueOf(userID),um);
					
					double [] jrsm = jaccardSimilarityMatrix(Integer.valueOf(userID),rum,cum);
					ArrayList<Integer> jrsu = similarUsers(Integer.valueOf(userID),similarUsersNo,jrsm);
					try {
						ArrayList<String> jrrm = recommendedMovies(jrsu, cum, userRating, um);;
						recommendationHash = recommendedMovieHash("indexed\\uItemIndex", "id", jrrm);
					} catch (IOException e){
						e.printStackTrace();
					} catch (ParseException e){
						e.printStackTrace();
					}
				}
			}
				
			//HashMap Implementation
			recommendationListModel.removeAllElements();
			for(String recommendationTitle : recommendationHash.keySet())
				recommendationListModel.addElement(recommendationTitle);
			if(recommendationListModel.isEmpty())
				System.out.println("ListModel Empty After Search");
			recommendationList.setModel(recommendationListModel);
			
			//HashMap Implementation
			for(String title : recommendationHash.keySet())
				System.out.println(title);
			
			recommendationListScrollPane.revalidate();
				
			
		}
	}

	
	
	// ScrollBar Customizer Method(Incomplete)
	/*public class MyScrollBarUI extends BasicScrollBarUI {

	    @Override
	    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
	        // your code
	    }

	    @Override
	    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
	        // your code
	    }
	}*/
	
	//Searches and returns ArrayList with found Movie Titles
	/*public ArrayList<String> searchMovieTitles(String indexDirectory, String fieldName1, String queryString1, String fieldName2, String queryString2, String fieldName3, String queryString3, String fieldName4, String queryString4) throws IOException, ParseException
		{
			//Parse provided index directory
			//String indexDir = "indexed\\uItemIndex";				
			
			ArrayList<String> fields = new ArrayList<String>();
			ArrayList<String> documentsNames = new ArrayList<String>();
			queryString = "";
			
			if(!queryString1.equals("")){
				queryString = queryString1;
				fields.add(fieldName1);
			}
			if(!queryString2.equals("")){
				if(!fields.isEmpty())
					queryString += " AND " + queryString2;
				else
					queryString += queryString2;
				fields.add(fieldName2);
			}
			if(!queryString3.equals("")){
				if(!fields.isEmpty())
					queryString += " AND " + queryString3;
				else
					queryString += queryString3;
				fields.add(fieldName3);
			}
			if(!queryString4.equals("")){
				if(!fields.isEmpty())
					queryString += " AND " + queryString4;
				else
					queryString += queryString4;
				fields.add(fieldName4);
			}
			
			if(!fields.isEmpty())
			{
				File file = new File(indexDirectory);
				
				//Open index
				Directory directory = FSDirectory.open(file.toPath()); //3
				IndexReader indexReader = DirectoryReader.open(directory);
				IndexSearcher indexSearcher = new IndexSearcher(indexReader);
				
				//Parse query
				//QueryParser parser = new QueryParser( "dataUserID", new StandardAnalyzer());
				//String[] fields = {"dataItemID", "dataRating"};
				//String[] fields = {fieldName1, fieldName2, fieldName3, fieldName4};
				MultiFieldQueryParser parser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), new StandardAnalyzer());
				Query query = parser.parse(queryString);
				//long start = System.currentTimeMillis();
			
				//Search index
				TopDocs hits = indexSearcher.search(query, 25);		
			
				//long end = System.currentTimeMillis();
		
				//Write search stats
				//System.err.println("Found " + hits.totalHits + " topics(s) (in " + (end - start) + " milliseconds) that matched query '" + queryString + "':");
				
				
				for(ScoreDoc scoreDoc : hits.scoreDocs)
				{
					//Retrieve matching document
					Document document = indexSearcher.doc(scoreDoc.doc);                    
					documentsNames.add(document.get("title"));
		
				}
			
				//Close IndexSearcher
				indexReader.close();
				
			}
		
			return documentsNames;               		 
		}
	*/		
		//Searches and returns ArrayList with found Movie Documents containing Movie ID, Title, Release Date and Genre
		/*public ArrayList<Document> searchMovieDocs(String indexDirectory, String fieldName1, String queryString1, String fieldName2, String queryString2, String fieldName3, String queryString3, String fieldName4, String queryString4) throws IOException, ParseException
		{
			//ArrayList to Contain FieldNames
			ArrayList<String> fields = new ArrayList<String>();
			//ArrayList to Contain Documents
			ArrayList<Document> documents = new ArrayList<Document>();
			queryString = " ";
			System.out.println("Searched Fields");
			
			//Sub-QueryStrings formed to OR QueryString
			if(!queryString1.equals("")){
				System.out.println("ID Search: " + queryString1);
				queryString = queryString1;
				fields.add(fieldName1);
			}
			if(!queryString2.equals("")){
				System.out.println("Title Search: " + queryString2);
				queryString += " " + queryString2;
				fields.add(fieldName2);
			}
			if(!queryString3.equals("")){
				System.out.println("ReleaseDate Search: " + queryString3);
				queryString += " " + queryString3;
				fields.add(fieldName3);
			}
			if(!queryString4.equals("")){
				System.out.println("Genre Search: " + queryString4);
				queryString += " " + queryString4;
				fields.add(fieldName4);
			}
			
			//Sub-QueryStrings formed to AND QueryString
			if(!queryString1.equals("")){
				queryString = queryString1;
				fields.add(fieldName1);
			}
			if(!queryString2.equals("")){
				if(!fields.isEmpty())
					queryString += " AND " + queryString2;
				else
					queryString += queryString2;
				fields.add(fieldName2);
			}
			if(!queryString3.equals("")){
				if(!fields.isEmpty())
					queryString += " AND " + queryString3;
				else
					queryString += queryString3;
				fields.add(fieldName3);
			}
			if(!queryString4.equals("")){
				if(!fields.isEmpty())
					queryString += " AND " + queryString4;
				else
					queryString += queryString4;
				fields.add(fieldName4);
			}
			
			
			if(!fields.isEmpty())
			{
				File file = new File(indexDirectory);
				
				//Open index
				Directory directory = FSDirectory.open(file.toPath());
				IndexReader indexReader = DirectoryReader.open(directory);
				IndexSearcher indexSearcher = new IndexSearcher(indexReader);
						
						
				//Parse query
				//QueryParser parser = new QueryParser( "dataUserID", new StandardAnalyzer());
				//String[] fields = {"dataItemID", "dataRating"};
				//String[] fields = {fieldName1, fieldName2, fieldName3, fieldName4};
				MultiFieldQueryParser parser = new MultiFieldQueryParser(fields.toArray(new String[fields.size()]), new StandardAnalyzer());
				Query query = parser.parse(queryString);
				
				//Calculating Searching Time
				//long start = System.currentTimeMillis();
					
				//Search index
				TopDocs hits = indexSearcher.search(query, 25);		
					
				//long end = System.currentTimeMillis();
		
				
				//System.err.println("Found " + hits.totalHits + " topics(s) (in " + (end - start) + " milliseconds) that matched query '" + queryString + "':");
					
				//Counting variable
				int counter = 1;
					
				for(ScoreDoc scoreDoc : hits.scoreDocs)
				{
					//Retrieve matching document
					Document document = indexSearcher.doc(scoreDoc.doc);                    
					//Display
					System.out.println();
					System.out.println("Result " + counter);
					System.out.print("ID: "			+ document.get("id") + " ");
					System.out.print("Title: "		+ document.get("title") + " ");
					System.out.print("Release: " 	+ document.get("releaseDate") + " ");
					System.out.print("Genre: " 		+ document.get("genre") + " ");
					documents.add(document);
					System.out.println();
					counter++;
				}
				
				//Close IndexSearcher
				indexReader.close();
			}
			
			return documents;                        
			 
		}
		*/
	
}
