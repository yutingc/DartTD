package dart.network;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import dart.Consts;
import dart.Consts.TowerType;
import dart.Consts.*;

//sets the attributes in the packets. Also responsible for sending the packets
//Note: all the methods need to take in the packet!!
public class Handler {

	private Socket _socket;
	private PrintWriter _output;
	private Scanner _input;
	private networkPacket _sendingPacket,_receivedPacket;


	public Handler(Socket socket) throws IOException
	{
		_socket = socket;
		_output = new PrintWriter(socket.getOutputStream());
		_input = new Scanner(socket.getInputStream());
		_sendingPacket = new networkPacket();//initialize the packet and set the values to default
		_receivedPacket = new networkPacket();
		reset(_sendingPacket);
		reset(_receivedPacket);
	}


	public void resetPacket(networkPacket packet,int playerIndex, long currentTime,int x,int y, AttrType towerUpgrade, TowerType towerPurchase,
			RunnerType sendRunner)
	{
		synchronized(this){
			packet.setPlayerIndex(playerIndex);
			packet.setCurrentTime(currentTime);
			packet.setX(x);
			packet.setY(y);
			packet.setTowerUpgrade(towerUpgrade);
			packet.setTowerPurchase(towerPurchase);
			packet.setSendRunner(sendRunner);
//			packet.setAttribute1Change(attribute1Change);
//			packet.setAttribute2Change(attribute2Change);
//			packet.setAttribute3Change(attribute3Change);
		}
	}

	public void reset(networkPacket packet){
		synchronized(this){
			packet.setCurrentTime(-1);
			packet.setX(-500);
			packet.setY(-500);
			packet.setTowerUpgrade(AttrType.NONE);
			packet.setTowerPurchase(TowerType.NONE);
			packet.setSendRunner(RunnerType.NONE);
			packet.setAttribute1Change(false);
			packet.setAttribute2Change(false);
			packet.setAttribute3Change(false);
		}
	}

	public void setPlayerIndex(int index)
	{
		synchronized(this){
			_sendingPacket.setPlayerIndex(index);
		}
	}
	public int getPlayerIndex()
	{
		return _receivedPacket.getPlayerIndex();
	}
	public void sendRunner(RunnerType sendRunner)
	{
		synchronized(this){
			_sendingPacket.setSendRunner(sendRunner);
		}
	}

	public void upgradeTower(AttrType upgradeTower)
	{
		synchronized(this){
			_sendingPacket.setTowerUpgrade(upgradeTower);
		}
	}

	public void buyTower(TowerType tt)
	{
		synchronized(this){
			_sendingPacket.setTowerPurchase(tt);
		}
	}

	public RunnerType isSendingRunner()
	{
		return _receivedPacket.getSendRunner();
	}

	public TowerType isBuyingTower()
	{
		return _receivedPacket.getTowerPurchase();
	}
	public AttrType isUpgradingTower()
	{
		return _receivedPacket.getTowerUpgrade();
	}

	public String translatePacketToString()
	{
		synchronized(this){
			String message = "";
			message += Integer.toString(_sendingPacket.getPlayerIndex())+"/"+"";
			message += Long.toString(_sendingPacket.getCurrentTime())+"/"+"";
			message += Integer.toString(_sendingPacket.getX())+"/"+"";
			message += Integer.toString(_sendingPacket.getY())+"/"+"";
			message += _sendingPacket.getTowerUpgrade().name() + "/" + "";
			message += _sendingPacket.getTowerPurchase().name()+"/"+"";
			message += _sendingPacket.getSendRunner().name()+"/"+"";
			//			Boolean.toString(_sendingPacket.getSendRunner())+"/"+"";
//			message += Boolean.toString(_sendingPacket.getAttribute1Change())+"/"+"";
//			message += Boolean.toString(_sendingPacket.getAttribute2Change())+"/"+"";
//			message += Boolean.toString(_sendingPacket.getAttribute3Change());

			return message;
		}
	}

	public void translateStringToPacket(String packetReceived)
	{
		
		String[] str = packetReceived.split("/");
		Integer playerIndex = Integer.parseInt(str[0]);
		Long currentTime = Long.parseLong(str[1]);
		Integer x = Integer.parseInt(str[2]);
		Integer y = Integer.parseInt(str[3]);
		AttrType towerUpgrade = Consts.AttrType.valueOf(str[4]);
		TowerType towerPurchase = Consts.TowerType.valueOf(str[5]);

		RunnerType sendRunner = Consts.RunnerType.valueOf(str[6]);

//		Boolean attribute1Change = Boolean.parseBoolean(str[7]);
//		Boolean attribute2Change = Boolean.parseBoolean(str[8]);
//		Boolean attribute3Change = Boolean.parseBoolean(str[9]);

		this.resetPacket(_receivedPacket,playerIndex, currentTime, x, y, towerUpgrade, towerPurchase, sendRunner);
		


	}

	//translate the network packet into the string and send it over the connection
	//while sending and resetting the packet, lock the packet
	public void sendPacket()
	{
		synchronized(this){

			String message = translatePacketToString();

			_output.println(message);
			_output.flush();
			reset(_sendingPacket);
		}
	}
	
	public void receivePacket()
	{
		//System.out.println("received");
		String packetReceived = _input.nextLine();
		translateStringToPacket(packetReceived);
	}


	public void setTowerLocation(int findBlockX, int findBlockY) {
		synchronized(this){
			_sendingPacket.setX(findBlockX);
			_sendingPacket.setY(findBlockY);
		}

	}

	public int getTowerLocationX() {
		return _receivedPacket.getX();


	}
	public int getTowerLocationY() {
		return _receivedPacket.getY();


	}


	public void setBuyTower(TowerType tt, int findBlockX, int findBlockY) {
		// TODO Auto-generated method stub
		synchronized(this)
		{
			_sendingPacket.setTowerPurchase(tt);
			_sendingPacket.setX(findBlockX);
			_sendingPacket.setY(findBlockY);

		}
	}
	
	public void setUpgradeTower(AttrType at, int x, int y) {
		synchronized(this)
		{
			_sendingPacket.setTowerUpgrade(at);
			_sendingPacket.setX(x);
			_sendingPacket.setY(y);
		}
	}


	public void setCurrentTime(long currentTimeMillis) {
		// TODO Auto-generated method stub
		synchronized(this)
		{
			_sendingPacket.setCurrentTime(currentTimeMillis);
		}
	}
	
	public long getCurrentTime()
	{
		return _receivedPacket.getCurrentTime();
	}

}
