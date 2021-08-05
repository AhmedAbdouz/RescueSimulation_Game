package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class checkGameover extends JFrame{
	JLabel l;
	JLabel b;
	private Image icon;
	public checkGameover(int sc) {
		super();
		icon =Toolkit.getDefaultToolkit().getImage("back.jpg");
		setIconImage(icon);
		setBounds(700,380,660,400);
		setVisible(true);
		setLayout(null);
		
		l=new JLabel("Game Over");
		Font f=new Font("Arial", Font.BOLD, 40);
		l.setFont(f);
		l.setBounds(200,20,400,150);
		
		b=new JLabel("Your Score :-     " +sc);
		b.setFont(new Font("Arial", Font.BOLD,30));
		b.setBounds(200,110,400,100);
		add(l);
		add(b);
		
		
	}
	public static void main(String[] args) {
		checkGameover c=new checkGameover(50);
	}
}
