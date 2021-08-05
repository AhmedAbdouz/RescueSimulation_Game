package view;

import javax.swing.JFrame;

public class ClientTest {

	public static void main(String[] args) {
	
		
		Client c=new Client("192.168.43.59");
		c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c.startrunning();
	}
}




 