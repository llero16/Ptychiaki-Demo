import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame2 extends JFrame implements ActionListener
{

	private JButton createPlateButton;
	private JButton addOrderButton;
	private JButton prepareOrderButton;
	private JButton calculateTimeButton;
	private ImageIcon image = new ImageIcon("degrasse_tyson_woosh.gif");
	private JLabel imageLabel;
	private JPanel panel;
	private JPanel buttonPanel;
	private JPanel imagePanel;
	private JButton closeButton;
	private JMenu menu;
	
	JMenuItem createChoice;
	JMenuItem addOrderChoice;
	JMenuItem prepareOrderChoice;
	JMenuItem calculateTimeChoice;
	JMenuItem closeChoice;
	
	public MainFrame2() 
	{	
	
		menu = new JMenu("Ντου Σαμθνιγκ ρε Μαν");
		
		createChoice = new JMenuItem("Δημιουργία Πιάτου");
		addOrderChoice = new JMenuItem("Προσθήκη Παραγγελίας");
		prepareOrderChoice = new JMenuItem("Ετοιμασία Παραγγελίας");
		calculateTimeChoice = new JMenuItem("Υπολογισμός Χρόνου");
		closeChoice = new JMenuItem("close naoum");
		
		createChoice.addActionListener(this);
		addOrderChoice.addActionListener(this);
		prepareOrderChoice.addActionListener(this);
		calculateTimeChoice.addActionListener(this);
		closeChoice.addActionListener(this);
		
		menu.add(createChoice);
		menu.add(addOrderChoice);
		menu.add(prepareOrderChoice);
		menu.add(calculateTimeChoice);
		menu.add(closeChoice);
		
		JMenuBar bar = new JMenuBar();
		bar.add(menu);
		this.setJMenuBar(bar);
		
		imageLabel = new JLabel(image);
		
		imagePanel = new JPanel();
		imagePanel.setBackground(Color.BLUE);
		imagePanel.add(imageLabel);
		
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(imagePanel);
		
		
		this.setContentPane(panel);
		this.setSize(600,600);
		this.setTitle("GUI Test");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void actionPerformed(ActionEvent event)
	{
		String buttonString = event.getActionCommand();
			
		if(buttonString.equals("Δημιουργία Πιάτου"))
			JOptionPane.showMessageDialog(null,"Plate Made");
		else if(buttonString.equals("Προσθήκη Παραγγελίας"))
			JOptionPane.showMessageDialog(null,"Order Received");
		else if(buttonString.equals("Ετοιμασία Παραγγελίας"))
			JOptionPane.showMessageDialog(null,"Order Ready");
		else if(buttonString.equals("Υπολογισμός Χρόνου"))			
		{
			JOptionPane.showMessageDialog(null,"Time Calculated");
			calculateTimeChoice.removeActionListener(this);
		}
		else if(buttonString.equals("close naoum"))
			System.exit(0);
		else
			System.out.println("Unexpected Error");
	}
	

}
