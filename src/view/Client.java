package view;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{

	private String IP;
	private JTextField usertext;
	private JTextArea chat;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	public Client(String host) {
		super(" Client ");
		IP=host;
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
		add(new JScrollPane(chat),BorderLayout.CENTER);
		setSize(300,150);
		setVisible(true);
	}
	
	//connect to server 
	public void startrunning () {
		try {
			connecttoserver();
			setupstreams();
			whilechatting();
		}catch (EOFException e) {
			showmes("\n Clint terminated the connection ");
		}catch(IOException e) {
			e.printStackTrace();
		}finally {
			closecrap();
		}
	}
	
	private void connecttoserver() throws IOException{
		showmes(" ATTEMPTING CONNECTION... \n");
		connection=new Socket(InetAddress.getByName(IP), 6789);
		showmes(" You are connected to: "+connection.getInetAddress().getHostName());
	}
	
	// set up stream to send and receive messages
	private void setupstreams() throws IOException{
		output =new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input=new ObjectInputStream(connection.getInputStream());
		showmes("\n Streams are now setup! \n");
	}
	
	// while chatting with the server 
	private void whilechatting() throws IOException{
		abletotype(true);
		String mes ="You are now connected ! ";
		sendmes(mes);
		do {
			try {
				mes=(String)input.readObject();
				showmes("\n"+mes);
			}catch (ClassNotFoundException e) {
				// TODO: handle exception
				showmes("\n i do not know that");
			}
		}while(! mes.equals("client - end"));
	}
	
	//send messages to thr server
	private void sendmes(String s) {
		try {
			output.writeObject(" Client - "+  s);
			output.flush();
			showmes("\n Client - "+ s);
		}catch (IOException e) {
			chat.append("\n something went wrong");
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
}
