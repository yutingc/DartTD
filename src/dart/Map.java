package dart;

import dart.exceptions.InvalidPathException;
import dart.tower.Square;
import dart.tower.Tower;

public class Map {

//--------------------------------	
//Fields
//--------------------------------
	
	//X and Y dimensions of the vector map
	private int dimensionX,dimensionY;
	
	//X and Y coordinates of the checkpoint square
	private int checkpointX,checkpointY;

	
	//Vector map indicating the shortest path to the starting X and Y
	private Direction[][] vector;

//--------------------------------
//Constructors
//--------------------------------
	
	//Create a map with dimensions X and Y	(used for debugging)
	public Map(int dimensionX, int dimensionY)
	{
		//Set the dimensions of the Map
		this.dimensionX=dimensionX;
		this.dimensionY=dimensionY;
		//Instantiate the Map's Direction field
		this.vector = new Direction[dimensionY][dimensionX];
		
	}
	
	//Create a Map given a board, checkpointX, and checkpointY
	public Map(Square[][] board, int checkpointX, int checkpointY) throws NullPointerException, InvalidPathException
	{
		//Check for a bad board
		if(board==null || !(board.length >=1) || !(board[0].length >= 1)) 
			throw new NullPointerException("Board dimensions must be at least 1x1.");
		
		//Check for a bad path, if the checkpoint is out of bounds or is not walkable, throw a 
		if(checkpointX-1 > board[0].length || checkpointY-1 > board.length || !board[checkpointY][checkpointX].isWalkable())
			throw new InvalidPathException("Checkpoint is out of bounds or not walkable");
			
		//Assign the variables passed.
		this.dimensionX=board[0].length;
		this.dimensionY=board.length;
		this.checkpointX=checkpointX;
		this.checkpointY=checkpointY;
		this.vector = new Direction[dimensionY][dimensionX];
		
		
		//Create the path (map) for the given board
		updatePath(board);
	}

//--------------------------------
//Methods
//--------------------------------	

//Getter Methods
	
	public Direction getDirection(int x, int y)
	{
		return vector[y][x];
	}
	
	public int getDestinationX()	{ return checkpointX; }
	public int getDestinationY()	{ return checkpointY; }
	
	public int getSizeX()			{ return vector[0].length;	}
	public int getSizeY()			{ return vector.length;		}	
	
	public int getCheckPointX()		{ return checkpointX;	}
	public int getCheckPointY()		{ return checkpointY;	}
	
		//--------------------------------//

	//Say whether a given (x,y) position is the destination point of this vector field
	public boolean isCheckPoint(int x,int y)
	{
		if(checkpointX==x && checkpointY==y) return true;
		return false;
	}
	
	
	//Update the path. The board is changed when new towers are built, so no new information needs to be passed
	public void updatePath(Square[][] board)
	{
		//Run the FIFO algorithm on the Map
		int tX,tY;
		int nX = 0,nY = 0;
		int fifr,fifw;
		int[] fifx = new int[dimensionX*dimensionY];
		int[] fify = new int[dimensionX*dimensionY];
		
		//shorthand for x and y positions of the original waypoint
		tX=checkpointX;
		tY=checkpointY;
		
		//fifo spawn location, start at waypoint
		fifx[0]=tX;
		fify[0]=tY;
		
		//Create a new vector map
		vector = new Direction[dimensionY][dimensionX];
		
		//last x and y (map vector back to starting position)
		//given a point on the board, return a vector pointing
		//along the shortest path to the waypoint
		//For this one, state that it is a waypoint
		this.vector[tX][tY]= new Direction(0,0,true);
		
		//Initialize the read and write buffers
		fifr=0; fifw=1;
		while(fifr<fifw)
		{
			//shorthand for gridpoint spawn
			tX=fifx[fifr];
			tY=fify[fifr];
			
			for(int j=0; j<4; j++)
			{
				if(j==0) {nX=tX+1; nY=tY  ;}  //check all 4 directions around the spawnpoint
				if(j==1) {nX=tX-1; nY=tY  ;}
				if(j==2) {nX=tX  ; nY=tY+1;}
				if(j==3) {nX=tX  ; nY=tY-1;}
				//if the next square is on the board and available to walk on and NOT already found
				if( (nX<vector[0].length) && (nY<vector.length) && (nX>=0) && (nY>=0) && board[nY][nX].isWalkable() && vector[nY][nX]==null)
				{
					
					//the vector for the square points to the spawn
					vector[nY][nX] = new Direction(tX-nX,tY-nY);
					//the next spawn on the list is this square
					fifx[fifw] = nX;
					fify[fifw] = nY;					
					//increase the write pointer
					fifw++;
//					print();				//Debugging print to show how the map
//					System.out.println();	//is built in each step.
				}
			}
			//move to the next space on the list
			fifr++;
			
		}//endwhile
	}
	
	//Path-finding Algorithm used for quick debugging
											//update to Field class
	public void updatePath(int startX, int startY, Square[][] field)
	{
		checkpointX = startX;
		checkpointY = startY;
	//Check bounds on the start positions
		if(startX < 0 || startX > field[0].length || startY < 0 || startY > field.length)
		{
			//Throw a Point Out of Bounds Exception
		}
	//Check to see that the waypoint is reachable
		if(!field[startY][startX].isWalkable())
		{
			//Throw a Point Not Reachable Exception
		}
		
		//Set the dimensions of the Map to equal those of the passed Square
		this.dimensionX=field[0].length;
		this.dimensionY=field.length;
		
	//Run the FIFO algorithm on the Map
		int tX,tY;
		int nX = 0,nY = 0;
		int fifr,fifw;
		int[] fifx = new int[this.dimensionX*this.dimensionY];
		int[] fify = new int[this.dimensionX*this.dimensionY];
		
		//shorthand for x and y positions of the original waypoint
		tX=startX;
		tY=startY;
		
		//fifo spawn location, start at waypoint
		fifx[0]=tX;
		fify[0]=tY;
		
		//last x and y (map vector back to starting position)
		//given a point on the board, return a vector pointing
		//along the shortest path to the waypoint
		//For this one, state that it is a waypoint
		this.vector[tX][tY]= new Direction(0,0,true);
		
		//Initialize the read and write buffers
		fifr=0; fifw=1;
		while(fifr<fifw)
		{
			//shorthand for gridpoint spawn
			tX=fifx[fifr];
			tY=fify[fifr];
			
			for(int j=0; j<4; j++)
			{
				if(j==0) {nX=tX+1; nY=tY  ;}  //check all 4 directions around the spawnpoint
				if(j==1) {nX=tX-1; nY=tY  ;}
				if(j==2) {nX=tX  ; nY=tY+1;}
				if(j==3) {nX=tX  ; nY=tY-1;}
				//if the next square is on the board and available to walk on and NOT already found
				if( (nX<this.vector[0].length) && (nY<this.vector.length) && (nX>=0) && (nY>=0) && field[nY][nX].isWalkable() && this.vector[nY][nX]==null)
				{
					
					//the vector for the square points to the spawn
					this.vector[nY][nX] = new Direction(tX-nX,tY-nY);
					//the next spawn on the list is this square
					fifx[fifw] = nX;
					fify[fifw] = nY;					
					//increase the write pointer
					fifw++;
//					print();				//Debugging print to show how the map
//					System.out.println();	//is built in each step.
				}
			}
			//move to the next space on the list
			fifr++;
			
		}//endwhile
	}

	//Print the vectors of the map
	public void print()
	{
		if (this.vector.length == 0 || this.vector[0].length == 0) return;
		for(int i=0; i<vector[0].length;i++)
		{
			for(int j=0; j<vector.length;j++)
				if(vector[i][j]==null) System.out.printf("( 0, 0) ");
				else System.out.printf("(%2d,%2d) ",vector[i][j].getX(),vector[i][j].getY());
			System.out.printf("\n");
		}
	}
	
}
