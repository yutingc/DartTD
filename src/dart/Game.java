package dart;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import dart.exceptions.InvalidPathException;
import dart.gui.GameScreen;
import dart.network.Handler;

public class Game {

//--------------------------------
//Fields
//--------------------------------
	
	//Int to hold how many players there are (redundant with players.length but this is easier to write)
	private int numPlayers;
	
	//Array of all the players in the game
	private Player[] players;
	
	//Make the game board
	private Board board;

	//Network handler
	private Handler handler;
	
//--------------------------------
//Constructors
//--------------------------------
	
	//Constructor that takes a number of players and a path to the game map
	public Game(Handler h)
	{
		handler = h;
		
		//Initialize the board for this game
		try
		{
			board = new Board(BoardGenerator.StandardBoard(),1);
			board.findCheckPoints();
		}
		catch (IOException e)
		{
			System.out.println("Error Loading Images");
			e.printStackTrace();
		} 
		catch (NullPointerException e) { e.printStackTrace(); }
		catch (InvalidPathException e) { e.printStackTrace(); }
		
	}
	
//--------------------------------
//Methods
//--------------------------------

	//Play a game
	public void play() throws IOException, InterruptedException
	{
	//Declare/Initialize fields	

		//Get number of players from the Server
		numPlayers=2;
		players = new Player[numPlayers];
		players[0] = new Player(0,0);
		players[1] = new Player(1,1);
		//Integer to say how many packets have been received so far
		int packetsReceived=0;
		
		//Integer holding which player's packet has just been read
		int from=0;	//initialized for debugging
			
		//Create a RunnerManager to hold and operate the Runners sent by players
		RunnerManager runnerManager = new RunnerManager(numPlayers);
		
		//ArrayList of Runners who were killed
		ArrayList<Runner> deadRunners = new ArrayList<Runner>();
				
		//Game window
		final GameScreen window = new GameScreen(board, runnerManager, handler);
		
		//Network Handler
		handler.setPlayerIndex(0);

	//Game loop
		while(true)
		{
			
			//1. Gather player input
				handler.setCurrentTime(System.currentTimeMillis());
			
			//2. Send the packet
				handler.sendPacket(); //(sending automatically resets the local information immediately after sending)

			//3. Loop to receive all the packets from the host, 1 from each player
			packetsReceived = 0;
			while(packetsReceived<numPlayers)
			{
				//1. Receive a packet from the server
					handler.receivePacket();
					//Set which player is being read (shorter than saying handler.getPlayerIndex() all the time).
					from = handler.getPlayerIndex();
					
					//Synchronize certain operations with the host so that they only happen once
					if(from==0)
					{
						//Set everyone's time to the host's
						for(int i=0;i<players.length;i++)
							players[i].setTime(handler.getCurrentTime());
						
					}
					
				//2. Execute commands for that player
					//i.	purchase towers
					//If the packet doesn't read NONE for the tower type being purchased
					if(!handler.isBuyingTower().equals(Consts.TowerType.NONE))
					{
						board.buildTower(handler.getTowerLocationX(),handler.getTowerLocationY(),players[from],handler.isBuyingTower());
						window.setTowerSelected(Consts.TowerType.NONE);
					}
					
					//ii.	purchase runners
					if(!handler.isSendingRunner().equals(Consts.RunnerType.NONE))
					{
//						System.out.println("Bought a runner");

						//Create two new runners
						Runner r1 = new Runner(64,64,players[from]);
						Runner r2 = new Runner(64,64,players[from]);
						
						//Have them run in either direction
						r1.setCheckPointMap(board.getMap( 14,64));
						r2.setCheckPointMap(board.getMap(114,64));
						
						//Add the runners to the runnerManager
						runnerManager.addRunner(r1);
						runnerManager.addRunner(r2);
						System.out.println("New Runners bought");

					}
					
						//
					//iii.	upgrade towers
						//
					//iv.	
				
				//3. Shoot towers
					//i.update money and runner lists accordingly.
					
					deadRunners = board.turretsAttackRunners(from, players[0].getTime(),runnerManager.getEnemyRunners(players[from]));
				//4. Get money for the runners killed by this player
					players[from].tallyRunners(deadRunners);
					
				//5. Move runners
					//i.check to see if any runners get to their checkpoints
					runnerManager.moveRunners(handler.getCurrentTime(),board);
					//ii.execute the move operation
				
				//Increase the counter for the number of packets that have been received.
				packetsReceived++;
			}
			
			//Draw the frame
			Runnable repaint = new Runnable() {
				public void run() {
					window.repaint();
				}
			};
			
			try {
				SwingUtilities.invokeAndWait(repaint);
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			window.repaint();
//			Thread.sleep(100000);
		}
	}
}