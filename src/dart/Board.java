package dart;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;


import dart.exceptions.InvalidPathException;
import dart.tower.SlowTower;
import dart.tower.Square;
import dart.tower.Tower;

public class Board {

//--------------------------------
//Fields	
//--------------------------------

	//Dimensions of the Board
	int dimensionX,dimensionY;
		
	//Need fields for checkpoints
	
	//Maps for each waypoint
	ArrayList<Map> checkpointMap = new ArrayList<Map>();
	
	//The game board of Squares
	Square[][] board;
	
	//A group of towers per player
	ArrayList<ArrayList<Tower>> playerTowers = new ArrayList<ArrayList<Tower>>();
	//ArrayList<Tower>[] playerTowers;
	
//--------------------------------
//Constructors	
//--------------------------------
	
	//Construct a board from a square array and the number of players in the game
	public Board(Square[][] board, int playerNumber) throws NullPointerException, InvalidPathException
	{
		//Make the board equal the passed in Square array
		this.board=board;
		
		this.dimensionX = board[0].length;
		this.dimensionY = board.length;
		
		//Make an ArrayList of towers for each player
		for(int i=0;i<playerNumber;i++)
			playerTowers.add(new ArrayList<Tower>());
		
		//Find the checkpoints on the board
		findCheckPoints();
	}
	
	//Construct a blank board 
	public Board(int dimensionX, int dimensionY)
	{
		this.dimensionX = dimensionX;
		this.dimensionY = dimensionY;
		board = new Square[dimensionY][dimensionX];
		for(int y=0;y<dimensionY;y++)
			for(int x=0;x<dimensionX;x++)
				board[y][x]=new Square(x,y,null);
	}
	
//--------------------------------
//Methods	
//--------------------------------	
	
	
//Getter Methods
	
	public int getWidth()	{ return dimensionX; }
	public int getHeight()	{ return dimensionY; }
	
	public Square[][] getSquares() { return board; }
	
	//Get the image associated with a given x,y square
	public Consts.TextureType getTile(int x, int y)	{ return board[y][x].getTile(); }
	public Square getSquare(int x, int y)		{ return board[y][x];			}
	public Map		getMap(int x, int y)		
	{ 
		for(int i=0;i<checkpointMap.size();i++)
			if(checkpointMap.get(i).getCheckPointX()==x && checkpointMap.get(i).getCheckPointY()==y) return checkpointMap.get(i); 
		return null;
		//return ((CheckPoint) board[y][x]).getMap();
	}
	
	//Say whether the board location at (x,y) is a Tower
	public boolean isTower(int x, int y)
	{
		return (board[y][x] instanceof Tower);
	}

	
//Setter Methods
	
	
	public void setTileScale(int width, int height) {
		ImageManager.resizeImages(width, height);
	}
	
	
	
		//--------------------------------//
		
//Board operations	
	
	//Find all of the checkpoints on the board
	public void findCheckPoints()
	{
		checkpointMap.clear();
		for(int y=0;y<dimensionY;y++)
			for(int x=0;x<dimensionX;x++)
				if(board[y][x] instanceof CheckPoint) checkpointMap.add(((CheckPoint) board[y][x]).getMap());
	}
	
	
	//Method that builds a tower at a given (x,y) position if permitted.
	//Returns true if the build is successful and false if not 
	//ie, if the square isn't buildable or the player doesn't have enough money.
	public boolean buildTower(int x, int y, Player p, Consts.TowerType type)
	{
		if(board[y][x].isBuildable())
		{
			//Build a tower depending on what tower is being build
			switch(type)
			{
				case SLOWTOWER:
					//Return false if the player can't afford the tower
					//if(! (p.getMoney() > SlowTower.getPurchaseCost())) return false;
					
					//Set the board location to be a new SlowTower
					try { board[y][x] = new SlowTower(x, y, p);	} 
					catch (IOException e) { e.printStackTrace();}
					
					//Subtract the cost from the player
					p.setMoney(p.getMoney() - SlowTower.getPurchaseCost());
					
					//Add the tower to the board's list of towers by player 
					playerTowers.get(p.getIndex()).add((SlowTower) board[y][x]);
					
					//Update the paths
					updatePath();
					
					//Return true for a successful purchase
					return true;
			}
			return true;
		}
		return false;
	}
	
	//Change a square on the map to something else and update the path
	public void changeSquare(int x, int y, Square s)
	{
		//Set the square to the proper position on the board
		board[y][x] = s;
		//Update the path
		updatePath();
		
		//If the Square is a Tower, update the tower List array
		if(s instanceof Tower)
		{
			//Add the tower to the player's list of towers
			playerTowers.get(( (Tower) s).getOwnerIndex()).add( (Tower) s);
		}
		
		//Re-calculate the paths
		updatePath();
	}
	
	//Given an x,y and attribute, upgrade the attribute of the tower at that location
	//public void upgradeTower(int x, int y, String ATTRIBUTE, )
	
	//Have the towers attack the runners passed to this function
	//The ArrayList<Runner> that's returned is a list of all the runners that were killed
	public ArrayList<Runner> turretsAttackRunners(int playerIndex, long time, ArrayList<Runner> runners)
	{	
		//List of runners to return that were died
		ArrayList<Runner> returnList = new ArrayList<Runner>();
		
		//Boolean to hold whether a given runner has died
		boolean died;
		//List<Runner> argument could be replaced by a KD-Tree which could be searched for 
		//nearest neighbor. This search here is being used until that happens
		
		//Run through every tower in the indicated player's list of towers
		for(int i=0;i<playerTowers.get(playerIndex).size();i++)
		{
			//Run through every runner
			for(int j=0;j<runners.size();j++)
			{
				//If the runner is within the range of the tower and the tower can shoot
				if(playerTowers.get(playerIndex).get(i).runnerIsWithinRange(runners.get(j)))
				{
					playerTowers.get(playerIndex).get(i).setTurretAngle(runners.get(j));
					if( playerTowers.get(playerIndex).get(i).canAttack(time))
					{
					//Attack the runner
						died = playerTowers.get(playerIndex).get(i).attackRunner(runners.get(j),time);
						//If the runner died, remove them from the list and add them to the return list
						if(died)
						{
							returnList.add(runners.remove(j));
						}
					}
				}
			}
		}
		
		return returnList;
	}
	
	public Direction getDirection(int posX, int posY, int destX, int destY)
	{
		for(int i=0;i<checkpointMap.size();i++)
			if(destX==checkpointMap.get(i).getCheckPointX() && destY==checkpointMap.get(i).getCheckPointY()) return checkpointMap.get(i).getDirection(posX, posY);
		return null;
//		return ((CheckPoint)board[destY][destX]).getMap().getDirection(posX, posY);
	}
	
	//Update the map (wrapping Map's update function)
	public void updatePath()
	{
		for(int i=0;i<checkpointMap.size();i++)
			checkpointMap.get(i).updatePath(board);
	}
	
	public void print()
	{
		for(int y=0;y<128;y++)
		{
			System.out.printf("%3d",y);
			for(int x=0;x<128;x++)
			{
				switch(board[y][x].getTile())
				{
					case GRAVEL: System.out.printf(" ");	break;
					case GRASS: System.out.printf(" ");		break;
					case DIRT: System.out.printf(" ");		break;
					case LASERTEX:	System.out.printf("0");	break;
				}
			}
			System.out.printf("\n");
		}
	}
}