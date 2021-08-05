package view;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class background{
	
	
	public static void main(String[] args) throws IOException {
		JFrame f=new JFrame("jabms");
		f.setSize(600, 400);
		f.setLayout(null);
		JPanel ba=new JPanel();
		ba.setBounds(0,0,300,300);ba.setBackground(Color.RED);
		JPanel b=new JPanel();
		b.setBounds(0,0,200,200);b.setBackground(Color.BLACK);
		f.add(ba);
		f.add(b);
		f.setVisible(true);
		

//		JLabel a=new JLabel();
//		JButton b=new JButton();
//		f.add(b);
//		
//		JTextArea t =new JTextArea();
//
////		t.setBorder(null);
//		t.setText("asdad");
//		t.setEditable(false);
//		t.setBackground(new Color(0,0,0,0));
//		f.add(t);
		
	}

}
