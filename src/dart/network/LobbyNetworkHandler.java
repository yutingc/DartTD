package dart.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class LobbyNetworkHandler {
	private Socket _socket;
	private PrintWriter _output;
	private Scanner _input;
	private networkPacket _sendingPacket,_receivedPacket;
	
	public LobbyNetworkHandler(Socket socket) throws IOException
	{
		_output = new PrintWriter(socket.getOutputStream());
		_input = new Scanner(socket.getInputStream());

//		_sendingPacket = new networkPacket();//initialize the packet and set the values to default
//		_receivedPacket = new networkPacket();
//		reset(_sendingPacket);
//		reset(_receivedPacket);
	}
	
	
	//only the host would trigger this call
	public void sendGameStartSignal()
	{
		_output.println("GAME START");
		_output.flush();
	}
}
