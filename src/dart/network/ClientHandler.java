package dart.network;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Encapsulate IO for the given client {@link Socket}, with a group of
 * other clients in the given {@link ClientPool}.
 */
public class ClientHandler extends Thread {
	private ClientPool _pool;
	private Socket _client;
	private Scanner _input;
	private PrintWriter _output;
	//	private ObjectOutputStream _out;
	private Handler _networkHandler;
	private int _index;
	private ArrayList<Socket> _socketList;
	private ArrayList<Scanner> _inputList;
	private ArrayList<PrintWriter> _outputList;
	/**
	 * Constructs a {@link ClientHandler} on the given client with the given pool.
	 * 
	 * @param pool a group of other clients to chat with
	 * @param client the client to handle
	 * @throws IOException if the client socket is invalid
	 * @throws IllegalArgumentException if pool or client is null
	 */
	public ClientHandler(ClientPool pool, Socket client, int index, ArrayList<Socket> socketList, ArrayList<Scanner> inputList, ArrayList<PrintWriter> outputList) throws IOException {
		if (pool == null || client == null) {
			throw new IllegalArgumentException("Cannot accept null arguments.");
		}

		_pool = pool;
		_client = client;
		_input = new Scanner(client.getInputStream());
		_output = new PrintWriter(client.getOutputStream());
		_index = index;
		_socketList  = socketList;
		_inputList = inputList;
		_outputList = outputList;
	}

	/**
	 * Send and receive data from the client. 
	 */
	public void run() {
		try{
			
			if(_index==1){ //if it's the host client thread

				System.out.println("hi");
				while(_input.hasNext())
				{
					System.out.println("hi");
					String line = _input.nextLine();
					if(line.equals("GAME START"))
					{
						//start the game thread and server sends the message to all the clients
						_pool.broadcast("GAME START");
						
						//new runGameThread(_socketList,_pool).runThread();
						break;
					}
				
				}
//				System.out.println("enter here");
				int packetReceived = 0;
				
//				ArrayList<Scanner>  inputList = new ArrayList<Scanner>();
//				ArrayList<PrintWriter> outputList = new ArrayList<PrintWriter>();
				ArrayList<String> packetList = new ArrayList<String>();
				
//				for(int i=0;i<_socketList.size();i++)
//				{
//					inputList.add(new Scanner(_socketList.get(i).getInputStream()));
//					outputList.add(new PrintWriter(_socketList.get(i).getOutputStream()));
//					//System.out.println("hi333");
//				}
				
				while (true) {
//					long curTime = System.currentTimeMillis();
//					System.out.println("enter while loop"+curTime);
					long curTime; 
					while(packetReceived<_socketList.size())
					{
						
						for(int j=0;j<_socketList.size();j++)
						{
							String packet = _inputList.get(j).nextLine();
							if(packet.contains("SLOW"))
							{
								curTime = System.currentTimeMillis();
								System.out.println("recevied"+packet);
							}
							packetList.add(packet);
							packetReceived++;
						}

					}
					for(int k=0;k<_socketList.size();k++)
					{
						String packet = packetList.get(k);
						//System.out.println("get here");
						_pool.broadcast(packet);
					
						//System.out.println("successful");
					}
					
					packetReceived = 0;
					packetList = new ArrayList<String>();
					
//					System.out.println("leave while loop"+""+(int)(System.currentTimeMillis()-curTime));
				}
			
			}
			//else 
			else
			{
//				while(true)
//				{
//					System.out.println("enter here");	
//					int count = 0;
//					boolean breakOuterLoop = false;
//					while(_input.hasNext())
//					{
//						String lineReceived= _input.nextLine();
//						System.out.println(lineReceived);
//						//if it receives the GAME START signal, start the game and break this loop
//						if(lineReceived.equals("GAME START"))
//						{
//							breakOuterLoop = true;
//							break;
//						}
//						
//						//receives the line indicate its own player index and current number of players
//						if(count==0)
//						{
//							System.out.println(lineReceived);
//							//Starts the new lobby screen
//							
//						}
//						
//						//receives the instructions (eg. change the team, someone join the game,etc)
//						else
//						{
//							
//						}
//						count++;
//					}
//					if(breakOuterLoop)
//					{
//						break;
//					}
//				}
//			
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Send a string to the client via the socket
	 * 
	 * @param message text to send
	 */
	public void send(String message) {
		try {

			_output.println(new String(message.getBytes(), "US-ASCII"));
			
		} catch (UnsupportedEncodingException e) {
			_output.println(message);
			
		}
		_output.flush();
	}

	/**
	 * Close this socket and its related streams.
	 * 
	 * @throws IOException Passed up from socket
	 */
	public void kill() throws IOException {
		_output.close();
		_client.close();
	}
}
//
//
//class gameStartThread {
//	private int packetReceived = 0;
//	private ArrayList<Socket> _socketList;
//	private Thread t;
//	//	private ArrayList<String> _packetList;
//
//	public gameStartThread(ArrayList<Socket> socketList) throws IOException {
//		_socketList = socketList;
//		ArrayList<Scanner>  inputList = new ArrayList<Scanner>();
//		ArrayList<PrintWriter> outputList = new ArrayList<PrintWriter>();
//
//		for(int i=0;i<_socketList.size();i++)
//		{
//			inputList.add(new Scanner(_socketList.get(i).getInputStream()));
//			outputList.add(new PrintWriter(_socketList.get(i).getOutputStream()));
//		}
//
//		t = new Thread(inputList,outputList) {
//
//			public void run() {
//				while (true) {
//					while(packetReceived<_socketList.size())
//					{
//						for(int j=0;j<_socketList.size();j++)
//						{
//							String packet = inputList.get(j).nextLine();
//						}
//					}
//
//				}
//			}
//
//
//		};
//		t.start();
//	}
//}



//A separate method to run some code as a thread:

class runGameThread {
	private int packetReceived = 0;
	private ArrayList<Socket> _socketList;
	private Thread t;
	private ArrayList<Scanner>  inputList = new ArrayList<Scanner>();
	private ArrayList<PrintWriter> outputList = new ArrayList<PrintWriter>();
	private ArrayList<String> packetList = new ArrayList<String>();
	private ClientPool _pool;


	public runGameThread(ArrayList<Socket> socketList, ClientPool pool) throws IOException {
		_socketList = socketList;
		_pool = pool;
		//System.out.println(_socketList.size());
		for(int i=0;i<_socketList.size();i++)
		{
			inputList.add(new Scanner(_socketList.get(i).getInputStream()));
			outputList.add(new PrintWriter(_socketList.get(i).getOutputStream()));
		}

	}

	public void runThread() {
		if (t == null) {
			t = new Thread() {
				public void run() {
					while (true) {
						//System.out.println("hi444");
						while(packetReceived<_socketList.size())
						{
							for(int j=0;j<_socketList.size();j++)
							{
								String packet = inputList.get(j).nextLine();
								packetList.add(packet);
								packetReceived++;
							}

						}
						for(int k=0;k<_socketList.size();k++)
						{
							String packet = packetList.get(k);
							_pool.broadcast(packet);
						}

					}
				}
			};
			t.start();
		}
	}
}