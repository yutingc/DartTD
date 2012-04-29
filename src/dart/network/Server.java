package dart.network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A chat server, listening for incoming connections and passing them
 * off to {@link ClientHandler}s.
 */
public class Server extends Thread {

	private int _port;
	private ServerSocket _socket;
	private ClientPool _clients;
	private boolean _running;
	private int _count=1;
	private Scanner _input;
	private PrintWriter _output;
	private ArrayList<Socket> _socketList;
	private boolean stopAcceptingClients;
	/**
	 * Initialize a server on the given port. This server will not listen until
	 * it is launched with the start() method.
	 * 
	 * @param port
	 * @throws IOException
	 */
	public Server(int port) throws IOException {
		if (port <= 1024) {
			throw new IllegalArgumentException("Ports under 1024 are reserved!");
		}

		_port = port;
		_clients = new ClientPool();
		_socket = new ServerSocket(_port);
		stopAcceptingClients = false;
		_socketList = new ArrayList<Socket>();
	}

	/**
	 * Wait for and handle connections indefinitely.
	 */
	public void run() {
		_running = true;
		
		ArrayList<Scanner>  inputList = new ArrayList<Scanner>();
		ArrayList<PrintWriter> outputList = new ArrayList<PrintWriter>();
		
		while(true)
		{
			try{
				Socket clientConnection = _socket.accept();
				//increment the number of connected clients

				
				
				PrintWriter writer = new PrintWriter(clientConnection.getOutputStream());
				inputList.add(new Scanner(clientConnection.getInputStream()));
				outputList.add(new PrintWriter(clientConnection.getOutputStream()));
				
				//add the socket to the socketList
				_socketList.add(clientConnection);
				//add the host itself as a client to the network
				ClientHandler newThread = new ClientHandler(_clients,clientConnection,_count,_socketList,inputList,outputList);

				_clients.add(newThread);
				System.out.println("newClientthread");

				newThread.start();
				//inform the new player of its own index
//				if(_count!=1){
//					writer.println(""+_count);
//					writer.flush();
//				}
				_count++;

				//when the game starts, stop waiting for more clients to join the game
				if(stopAcceptingClients)
				{
					break;
				}
			}


			catch(Exception e)
			{
				System.out.println("client connection error");

			}

		}

	}

	/**
	 * Stop waiting for connections, close all connected clients, and close
	 * this server's {@link ServerSocket}.
	 * 
	 * @throws IOException if any socket is invalid.
	 */
	public void kill() throws IOException {
		_running = false;
		_clients.killall();
		_socket.close();
	}

	//find server's ip
	public String getiP() {
		String ip="";
		try {

			ip = InetAddress.getLocalHost().getHostAddress();


		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return ip;

	}
}

