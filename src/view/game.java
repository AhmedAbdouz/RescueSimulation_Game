package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import controller.CommandCenter;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.units.Unit;
import simulation.Simulatable;

public class game extends JFrame {
	
	private Image icon ;
	public game() {
		super();
		icon =Toolkit.getDefaultToolkit().getImage("back.jpg");
		setIconImage(icon);		
		setTitle("Rescue The Island");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(null);
		setBackground(Color.DARK_GRAY);
		///////////////////////////
	}	
	
	public static void main(String[] args) {
		game a=new game();
	}
}
