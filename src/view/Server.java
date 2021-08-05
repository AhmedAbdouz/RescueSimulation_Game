package view;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Server extends JFrame{

	private JTextField usertext;
	private JTextArea chat;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public Server() {
		super("ahmed");
		usertext=new JTextField();
		usertext.setEditable(false);
		usertext.addActionListener(
				new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						sendmes(e.getActionCommand());
						usertext.setText("");
						
					}
				}
		);
		add(usertext,BorderLayout.NORTH);
		chat=new JTextArea();
		add(new JScrollPane(chat));
		setSize(300,150);
		setVisible(true);
		
	}
	public void startrunning() {
		try {
			server = new ServerSocket( 6789 , 100);
			while(true) {
				try {
					waitforconnection();
					setupstream();
					whilechatting();
				}catch(EOFException e) {
					showmes("\n Server ended the connection ");
				}finally {
					closecrap();
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void waitforconnection() throws IOException {
		showmes(" waiting for some one to connect.. \n");
		connection= server.accept();
		showmes(" now connected to "+connection.getInetAddress().getHostName());
	}
	
	private void setupstream() throws IOException {
		output =new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input=new ObjectInputStream(connection.getInputStream());
		showmes("\n Streams are now setup! \n");
	}
	
	private void whilechatting() throws IOException{
		String mes ="You are now connected ! ";
		sendmes(mes);
		abletotype (true);
		do {
			try {
				mes = (String) input.readObject();
				showmes("  \n"+mes);
			}catch (ClassNotFoundException e) {
				// TODO: handle exception
				showmes("\n dsbfkdj");
			}
		}while(! mes.equals("client - end"));
	}
	
	private void closecrap() {
		showmes("\n closing connections... \n");
		abletotype(false);
		try {
			output.close();
			input.close();
			connection.close();
		}catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private void sendmes(String s) {
		try {
			output.writeObject("Srever - "+s);
			output.flush();
			showmes("\n Server - " + s);
		}catch (IOException e) {
			// TODO: handle exception
			chat.append("\n ERROR: lsnv.skk svszddz");
		}
		
	}
	
	
	
	
	public void showmes(final String s) {
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						chat.append(s);
					}
				}								
		);
	}
	
	private void abletotype(final boolean f) {
		SwingUtilities.invokeLater(
				new Runnable() {
					@Override
					public void run() {
						usertext.setEditable(f);
					}
				}								
		);
	}
	public static void main(String[] args) {
		Server s=new Server();
		s.setDefaultCloseOperation(EXIT_ON_CLOSE);
		s.startrunning();
	}
}
