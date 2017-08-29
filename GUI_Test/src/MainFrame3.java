import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame3 extends JFrame
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
	private JButton checkNameButton;
	
	private JTextField plateNameField;
	private JTextField plateWeightField;
	private JTextField imageGuyNameField;
	
	public MainFrame3() 
	{
		
		createPlateButton = new JButton("Δημιουργία Πιάτου");
		addOrderButton = new JButton("Προσθήκη Παραγγελίας");
		prepareOrderButton = new JButton("Ετοιμασία Παραγγελίας");
		calculateTimeButton = new JButton("Υπολογισμός Χρόνου");
		imageLabel = new JLabel(image);
		plateNameField = new JTextField("Όνομα Πιάτου");
		plateWeightField = new JTextField("Βάρος Πιάτου");
		checkNameButton = new JButton("Είναι...?");
		imageGuyNameField = new JTextField();
		
		closeButton = new JButton("close naoum");
		
		ButtonListener listener = new ButtonListener();
		
		createPlateButton.addActionListener(listener);
		addOrderButton.addActionListener(listener);
		prepareOrderButton.addActionListener(listener);
		calculateTimeButton.addActionListener(listener);
		closeButton.addActionListener(listener);
		
		checkNameButton.addActionListener(listener);
		
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(2,4));
		buttonPanel.setBackground(Color.YELLOW);
		buttonPanel.add(createPlateButton);
		buttonPanel.add(addOrderButton);
		buttonPanel.add(prepareOrderButton);
		buttonPanel.add(calculateTimeButton);
		buttonPanel.add(plateNameField);
		buttonPanel.add(plateWeightField);
		buttonPanel.add(closeButton);
		
		imagePanel = new JPanel(new BorderLayout());
		imagePanel.setBackground(Color.BLUE);
		imagePanel.add(imageLabel,BorderLayout.NORTH);
		imagePanel.add(imageGuyNameField, BorderLayout.CENTER);
		imagePanel.add(checkNameButton, BorderLayout.SOUTH);
		
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(buttonPanel,BorderLayout.NORTH);
		panel.add(imagePanel,BorderLayout.CENTER);
		
		
		this.setContentPane(panel);
		panel.setBackground(Color.MAGENTA);
		this.setSize(600,600);
		this.setTitle("GUI Test");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			if(event.getSource() == createPlateButton)
				JOptionPane.showMessageDialog(null,"Plate Made" + " " + plateNameField.getText() + " " + plateWeightField.getText());
			else if(event.getSource() == addOrderButton)
				JOptionPane.showMessageDialog(null,"Order Received");
			else if(event.getSource() == prepareOrderButton)
				JOptionPane.showMessageDialog(null,"Order Ready");
			else if(event.getSource() == calculateTimeButton)
				JOptionPane.showMessageDialog(null,"Time Calculated");
			else if(event.getSource() == checkNameButton)
			{
				if(!imageGuyNameField.getText().equals(""))
					if(imageGuyNameField.getText().toLowerCase().equals("degrasse") || 
						imageGuyNameField.getText().toLowerCase().equals("black science guy"))
						JOptionPane.showMessageDialog(null,"Damn right he is...");
					else
						JOptionPane.showMessageDialog(null,"You ignorant sob...");
			}
			else if(event.getSource() == closeButton)
				System.exit(0);
		}
	}

}
