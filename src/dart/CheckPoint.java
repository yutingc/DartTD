package dart;

import dart.exceptions.InvalidPathException;
import dart.tower.Square;


public class CheckPoint extends Square{
	
//--------------------------------
//Fields
//--------------------------------
		
	//Array of checkpoints. The index of the array matches up with the team# of the runner
	//This allows multiple checkpoints at one position on the board. If a checkpoint of
	//index A is undefined, that means runners from team A are not to hit this checkpoint,
	//making there no need to store one from which they would run to from here
	private CheckPoint[] nextCheckPoint;
	
	//A vector map of Direction objects that points to this point
	private Map vectorMap;
	
	//Boolean to say whether this CheckPoint is an EndPoint
	private boolean isEndPoint; 
	
	
//--------------------------------
//Constructors
//--------------------------------
	
	//Create a checkpoint with a given (X,Y) position, a picture, and the number of teams that are in the game
	public CheckPoint(int x, int y, Consts.TextureType tile, int numTeams) throws NumberFormatException
	{
		//Create a square that is walkable
		super(x,y,tile);
		
		//Don't make this buildable
		setBuildable(false);
		
		if(numTeams < 1) throw new NumberFormatException("You need at least 1 team to play (by yourself).");
		
		nextCheckPoint = new CheckPoint[numTeams];
	}
	
	//Create a checkpoint from an existing Square
	public CheckPoint(Square s, int numTeams, Square[][] array) throws NumberFormatException
	{
		//Create a square from the existing one
		super(s);
		
		//Don't make the checkpoint buildable
		setBuildable(false);
		
		//Check to see if the team number is valid
		if(numTeams < 1) throw new NumberFormatException("You need at least 1 team to play (by yourself).");
		
		//Instantiate the map object for this checkpoint
		try {
			vectorMap = new Map(array,posX,posY);
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPathException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Create a new array of checkpoints that this one will point to
		nextCheckPoint = new CheckPoint[numTeams];
	}
	
	
//--------------------------------
//Methods
//--------------------------------
	
//Getter Methods
	
	//Get the next checkpoint for a given team
	public CheckPoint getNextCheckPoint(int team) {	return nextCheckPoint[team]; }
	
	public boolean isEndPoint()	{ return isEndPoint; }
	
	public Map getMap()			{ return vectorMap; }
	
	
//Setter Methods
	
	//Set the next checkpoint a runner should go to after reaching this one
	public void setNextCheckPoint(int team, CheckPoint next) throws NumberFormatException
	{
		//Detect for invalid checkpoint numbers
		if(team<0 || team > nextCheckPoint.length-1) throw new NumberFormatException("Invalid team number.");
		
		//Assign the next checkpoint.
		nextCheckPoint[team] = next;
	}
	
	public void setIsEndPoint(boolean isEndPoint) { this.isEndPoint=isEndPoint; }
}
