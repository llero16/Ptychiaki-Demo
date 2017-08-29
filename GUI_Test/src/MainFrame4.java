import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MainFrame4 extends JFrame implements ActionListener {

	private JButton addButton;
	private JButton subtractButton;
	private JButton clearButton;
	private JButton paneButton;
	private JTextField ioField;
	private double result = 0;
	private ImageIcon image = new ImageIcon("logout.png");
	
	
	public MainFrame4() {
		
		addButton = new JButton("+");
		subtractButton = new JButton("-");
		clearButton = new JButton("Clear");
		clearButton.setIcon(image);
		paneButton = new JButton("Dialog");
		
		addButton.addActionListener(this);
		subtractButton.addActionListener(this);
		clearButton.addActionListener(this);
		paneButton.addActionListener(this);
		
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout());
		ioField = new JTextField("Enter no here",30);
		textPanel.add(ioField);
		
		this.setLayout(new BorderLayout());
		add(textPanel,BorderLayout.NORTH);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.BLUE);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(addButton);
		buttonPanel.add(subtractButton);
		buttonPanel.add(clearButton);
		buttonPanel.add(paneButton);
		
		this.add(buttonPanel,BorderLayout.CENTER);
		
		this.setSize(400,200);
		this.setTitle("GUI Test");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	public void actionPerformed(ActionEvent e) {
		try{
			assumingCorrectNumberFormats(e);
		}
		catch(NumberFormatException e2){
			ioField.setText("Error");
		}
		
	}
	
	public void assumingCorrectNumberFormats(ActionEvent e)
	{
		String actionCommand = e.getActionCommand();
		
		if(actionCommand.equals("+")){
			result = result + stringToDouble(ioField.getText());
			ioField.setText(String.valueOf(result));
		}
		else if(actionCommand.equals("-")){
			result = result - stringToDouble(ioField.getText());
			ioField.setText(String.valueOf(result));
		}
		else if(actionCommand.equals("Clear")){
			result = 0.0;
			ioField.setText("0.0");
		}
		else if(actionCommand.equals("Dialog"))
		{
			String stringNumber = JOptionPane.showInputDialog("Number: ");
			double number = stringToDouble(stringNumber);
			ioField.setText(stringNumber);
		}
		else
			ioField.setText("Error");
	}

	private static double stringToDouble(String stringObject)
	{
		return Double.parseDouble(stringObject.trim());
	}
}
