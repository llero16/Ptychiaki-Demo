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
import java.util.HashMap;

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
	
	//Menu, MenuBar & MenuItems
	private JMenu mainMenu;
	private JMenuBar mainMB;
	private JMenuItem homeMI;
	private JMenuItem signInMI;
	private JMenuItem signUpMI;
	private JMenuItem movieSearchMI;
	private JMenuItem movieRateMI;
	private JMenuItem exitMI;
	
	
	//Movie Search JList
	private JList<String> movieList;

	//Movie Search ArrayLists -> JList
	private ArrayList<String> movieTitles;
	private ArrayList<Document> movieDocs;
	
	//Movie Search HashMap
	private static HashMap<String,Document> movieHash;
	
	//Movie List ListModel
	private DefaultListModel<String> listModel;
	
	//MovieSearch JScroll Pane
	private JScrollPane movieListScrollPane;
	
	//ImageIcon
	private ImageIcon scotlandImage;
	
	//CardLayout
	CardLayout cardLayout;
	private String queryString;
	
	//Test String
	private static String userID;
	private static User user;
	
	
	
	public MainFrame() throws IOException, ParseException
	{
		//Menu Items
		homeMI = new JMenuItem("Home");
		signInMI = new JMenuItem("Sign In");
		signUpMI = new JMenuItem("Sign Up");
		movieSearchMI = new JMenuItem("Movie Search");
		movieRateMI = new JMenuItem("Movie Rate");
		exitMI = new JMenuItem("Exit");
		
		//MenuListener, MenuItems: Add Listener
		//MenuListener listener = new MenuListener();
		homeMI.addActionListener(this);
		signInMI.addActionListener(this);
		signUpMI.addActionListener(this);
		movieSearchMI.addActionListener(this);
		movieRateMI.addActionListener(this);
		exitMI.addActionListener(this);
		
		
		//Main Menu: Add Home, Sign In, Sign Up, Exit MenuItems
		mainMenu = new JMenu("What to do ?");
		mainMenu.add(homeMI);
		mainMenu.add(signInMI);
		mainMenu.add(signUpMI);
		mainMenu.add(movieSearchMI);
		mainMenu.add(movieRateMI);
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
		
		//ButtonListener
		ButtonListener listener = new ButtonListener();
		signInButton.addActionListener(listener);
		signUpButton.addActionListener(listener);
		signOutButton.addActionListener(listener);
		rateButton.addActionListener(listener);
		searchButton.addActionListener(listener);

		//ArrayLists
		movieTitles = new ArrayList<String>();
	    movieDocs = new ArrayList<Document>();
		//HashMap
	    movieHash = new HashMap<String,Document>();	 
			
			
		
		//MovieSearch: MovieList List Model
	    listModel = new DefaultListModel<String>();
	    if(listModel.isEmpty())
	    	System.out.println("ListModel Empty At Constructor");
	    //ArrayList Implementation
	    /*for(String title: movieTitles)
	    	listModel.addElement(title);
		*/
	   //Hash Implementation
	   for(String title: movieHash.keySet())
	    	listModel.addElement(title);
	    
	    //MovieSearch JList
	    movieList = new JList<String>(listModel);
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
				listModel.removeAllElements();
				for(String movieTitle : movieHash.keySet())
					listModel.addElement(movieTitle);
				if(listModel.isEmpty())
					System.out.println("ListModel Empty After Search");
				movieList.setModel(listModel);
				
				System.out.println("Movies In List After Search");
				
				//ArrayList Implementation
				/*for(String movieTitle : movieTitles)
					System.out.println(movieTitle);*/
				
				//HashMap Implementation
				for(String title : movieHash.keySet())
					System.out.println(title);
				
				movieListScrollPane.revalidate();
					
				
			}
			
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
