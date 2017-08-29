import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class BorderLayoutJFrame extends JFrame{

	public static final int WIDTH = 500;
	public static final int HEIGHT = 400;
	
	public BorderLayoutJFrame() {
		super("BorderLayout Demo");
		setSize(WIDTH,HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setLayout(new BorderLayout());
		
		JLabel label1 = new JLabel("First");
		add(label1,BorderLayout.NORTH);
		
		JLabel label2 = new JLabel("Second");
		add(label2,BorderLayout.SOUTH);
		
		JLabel label3 = new JLabel("Third");
		add(label3,BorderLayout.CENTER);
		
		
		setVisible(true);
	}

}
