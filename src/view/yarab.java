package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class yarab extends JFrame {
	
	private Image icon ;
	public yarab() throws IOException {
		super();
		setVisible(true);
		setTitle("Rescue The Island");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		icon =Toolkit.getDefaultToolkit().getImage("back.jpg");
		setIconImage(icon);
//		setOpacity(0);
//		setBackground(new Color(0, 0, 0,0));
//		background=new JLabel();
//		background.setIcon(new ImageIcon("c://back.gpj"));
//		background.setBounds(0,0,getWidth(),getHeight());
//		add(background);
		
	}
	
	public static void main(String[] args) throws IOException {
		yarab a =new yarab();
	}
}
