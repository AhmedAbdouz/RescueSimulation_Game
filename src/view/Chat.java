package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Chat extends JFrame{

	private JTextField f;
	private JLabel l;
	private Client c;
	
	public Chat() {
		super("chat");
		setVisible(true);
		setSize(600, 300);
		setLocation(600, 350);
		setLayout(null);
		Font newFont = new Font("Arial", Font.BOLD, 24);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		l=new JLabel("Please enter the IP Address ");
		l.setBounds(80,30,400,80);
		l.setFont(newFont);
		add(l);
		
		f=new JTextField();
		f.addActionListener(
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						dispose();
						callmethod(e.getActionCommand());
						
						
					}
				}		
		);
		f.setBounds(80,130,480,60);
		f.setFont(newFont);
		add(f);
		
		f.updateUI();
	}
	
	public void callmethod(String s) {
		c=new Client(s);
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.startrunning();
	}
	
	public static void main(String [] args) {
		Chat c=new Chat();
	}
	
}
