package dart.network;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import javax.swing.*;

import dart.Game;
import dart.Player;

class panelWindow extends JFrame {
	public panelWindow() {
		JPanel main = new JPanel();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(main);
		main.setSize(new Dimension(400,400));
		main.setPreferredSize(new Dimension(400,400));

		main.setLayout(new GridLayout(3,1));
		JLabel label = new JLabel();
		label.setText("Choose whether you want to be the host or join a game");
		label.setSize(new Dimension(400,60));
		JButton button1 = new JButton("Be a host");
		JButton button2 = new JButton("Join the game");	
		main.add(label);
		main.add(button1);
		main.add(button2);
		button1.addActionListener(new hostListener(this));
		button2.addActionListener(new joinListener(this));


		this.pack();
		this.setVisible(true);
	}

	private class hostListener implements ActionListener{
	
		private panelWindow _window;
		
		public hostListener(panelWindow window)
		{
			 _window = window;
			 
		}
		public void actionPerformed(ActionEvent arg0) {
			try {
				Server newServer = new Server(1337);
				newServer.start();

				//client client1 = new client(newServer.getiP());
				Socket mySocket = new Socket(newServer.getiP(),1337);
				//hardcoded for now.. need to change later

				Handler h = new Handler(mySocket);
				//h.sendPacket();
				//h.receivePacket();
//				Game g = new Game(h);
////				
//				g.play();
				_window.setVisible(false);
				
				//open the lobby screen
				//open the newLobbyHandler
				//new thread receiving the info from the server about the lobby screen interaction
				
				//new ThreadMethod(h).runThread();
				new lobbyThread(mySocket,h).runThread();
				
				JFrame tempWindow = new JFrame();
				tempWindow.setSize(new Dimension(200,200));
				JPanel newPanel = new JPanel();
				tempWindow.add(newPanel);
				JButton start = new JButton("start");
				start.addActionListener(new startListener(h,mySocket));
				newPanel.add(start);
				tempWindow.pack();
				tempWindow.setVisible(true);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("server socket establishing failed");
			}


		}
	}
	private class startListener implements ActionListener{
		
		private Handler handler;
		private Socket socket;
		public startListener( Handler h,Socket s)
		{
			
			handler = h;
			socket = s;
		}
		public void actionPerformed(ActionEvent arg0) {
			
			//send server Game Start//
			try {
				
				PrintWriter writer = new PrintWriter(socket.getOutputStream());
				writer.println("GAME START");
				writer.flush();
				System.out.println("send to server");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//hostThread.runThread();
		}
		
	
	}

	private class joinListener implements ActionListener{
	
		private panelWindow _window;
		
		public joinListener(panelWindow window)
		{
			_window = window;
		}
		public void actionPerformed(ActionEvent arg0) {
			JPanel newPanel = new JPanel();


			JFrame newFrame = new JFrame("Host IP address");
			newFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

			newFrame.add(newPanel);
			JLabel label = new JLabel();
			label.setText("Please enter the host's ip address:");
			JTextField text = new JTextField(30);
			newPanel.add(label);
			newPanel.add(text);
			newPanel.setSize(new Dimension(400,400));
			newPanel.setPreferredSize(new Dimension(400,400));
			newFrame.pack();
			newFrame.setVisible(true);

			JButton submit = new JButton("Submit");
			newPanel.add(submit);
			submit.addActionListener(new submitListener(text.getText()));
		}
	}

	private class submitListener implements ActionListener{
		private String _text;
		public submitListener(String text)
		{
			_text = text;
		}
		

		public void actionPerformed(ActionEvent e) {
			try {
				Socket mySocket = new Socket(_text,1337);
				Handler h = new Handler(mySocket);
				
				
				//LobbyNetworkHandler h2 = new LobbyNetworkHandler(mySocket);
				//open a new thread, asking the server to get the index.
				//while loop to receive info from the lobby screen
				new lobbyThread(mySocket,h).runThread();
				
//				new ThreadMethod(h).runThread();
				
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				System.out.println("host's ip invalid");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				System.out.println("Socket connection failed");
			}
			
		}

	}
	
	private class ThreadMethod {
		
		private Thread t;
		private Handler handler;
		
		public ThreadMethod(Handler h) {
			handler = h;
		}
		
		public void runThread() {
			if(t == null) {
				t = new Thread() {
					public void run() {
						Game g = new Game(handler);
						
						try {
							g.play();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				t.start();
			}
		}
	}
	
private class lobbyThread {
		
		private Thread t;
		private Socket socket;
		private PrintWriter _output;
		private Scanner _input;
		private Handler _handler;
		
		public lobbyThread(Socket s, Handler h) throws IOException {
			socket = s;
			_handler = h;
			_output = new PrintWriter(socket.getOutputStream());
			_input = new Scanner(socket.getInputStream());
		}
		
		public void runThread() {
			if(t == null) {
				t = new Thread() {
					public void run() {
						//asks the server to give the index first and then receives and sends info about the lobby info
						while(true)
						{
							int count = 0;
							boolean breakOuterLoop = false;
							while(_input.hasNext())
							{
								String lineReceived= _input.nextLine();
								
								//if it receives the GAME START signal, start the game and break this loop
								if(lineReceived.equals("GAME START"))
								{
									breakOuterLoop = true;
									break;
								}
								
								//receives the line indicate its own player index and current number of players
								if(count==0)
								{
									//System.out.println(lineReceived);
								}
								
								//receives the instructions (eg. change the team, someone join the game,etc)
								else
								{
									
								}
								count++;
							}
							if(breakOuterLoop)
							{
								System.out.println("enter here");
								break;
							}
						}
						new ThreadMethod(_handler).runThread();
						
					}
				};
				t.start();
			}
		}
	}

	public static void main(String[] args) {
		new panelWindow	();

	}


}