package dart;

import java.io.IOException;

import dart.tower.Square;

public class BoardGenerator {

	
	public static Square[][] StandardBoard() throws IOException
	{
	//Set fields	
		
		//Set the dimensions of the board
		int dimensionX=128;
		int dimensionY=128;
		
		//Amount of space to pad the playing area with with unplayable terrain
		//int borderWidth=4;
				
		//Number of images used to draw the board
		int numImages=10;
		
		//Array to hold the images to be drawn
		Consts.TextureType[] images = new Consts.TextureType[numImages];
		images[0] = Consts.TextureType.GRASS;
		images[1] = Consts.TextureType.DIRT;
		images[2] = Consts.TextureType.GRAVEL;

			
			
		//Declare the board that is to be returned
		Square[][] board = new Square[dimensionY][dimensionX];
		
	//Use mathematics to geometrically define the board

		for(int y=0;y<dimensionY;y++)
			for(int x=0;x<dimensionX;x++)
			{
				//Set the border of the map
				if(y<4 || y>123 || x<4 || x>123 )
				{
					board[y][x]= new Square(x,y,images[0]);
					board[y][x].setWalkable(false);
					board[y][x].setBuildable(false);
				}
				//Set the middle area of the map here the runners can run but players can't build
				else if(y>58  && y<69 && x>=4 && x<=123)
				{
					board[y][x]= new Square(x,y,images[2]);
					board[y][x].setWalkable(true);
					board[y][x].setBuildable(false);
				}
				else
				{
					board[y][x] = new Square(x,y,images[1]);
					board[y][x].setWalkable(true);
					board[y][x].setBuildable(true);
				}
				if(y<59)
				{
					if(y>48 && x>27 && x<102)
					{
						board[y][x]= new Square(x,y,images[0]);
						board[y][x].setWalkable(false);
						board[y][x].setBuildable(false);						
					}
					if(y>17 && ((x>27 && x<49) || (x>80 && x<102)))
					{
						board[y][x]= new Square(x,y,images[0]);
						board[y][x].setWalkable(false);
						board[y][x].setBuildable(false);
					}
				}
				else if(y>68)
				{
					if(y<80 && x>27 && x<102)
					{
						board[y][x]= new Square(x,y,images[0]);
						board[y][x].setWalkable(false);
						board[y][x].setBuildable(false);
					}
					if(y<111 && ((x>27 && x<49) || (x>80 && x<102)))
					{
						board[y][x]= new Square(x,y,images[0]);
						board[y][x].setWalkable(false);
						board[y][x].setBuildable(false);
					}
				}

				if((x>4 && x<27) || (x>102 && x<123))
				{
					if(y==59 || y== 68)
					{
						board[y][x]= new Square(x,y,images[1]);
						board[y][x].setWalkable(true);
						board[y][x].setBuildable(true);
					} 
				}
				if((x>5 && x<26) || (x>103 && x<122))
				{
					if(y==60 || y==67)
					{
						board[y][x]= new Square(x,y,images[1]);
						board[y][x].setWalkable(true);
						board[y][x].setBuildable(true);
					}
				}
			}
		
			//Set the waypoints manually
		
			//left, middle, right x
			int lx = 14;	int mx = 64;	int rx =114;
			
			//top, middle, bottom y
			int ty = 12;
			int my = 64;
			int by =116;
			
			//end y coordinates on top team and bottom team's side
			int ey1= 48;
			int ey2= 80;
		 	
			//Set the board positions to be checkpoints
		 	board[ty][lx] = new CheckPoint(board[ty][lx], 2, board);	board[ty ][mx] = new CheckPoint(board[ty ][mx], 2, board);	board[ty][rx] = new CheckPoint(board[ty][rx], 2, board);
		 																board[ey1][mx] = new CheckPoint(board[ey1][mx], 2, board);
		 	board[my][lx] = new CheckPoint(board[my][lx], 2, board);	board[my ][mx] = new CheckPoint(board[my ][mx], 2, board);	board[my][rx] = new CheckPoint(board[my][rx], 2, board);
		 																board[ey2][mx] = new CheckPoint(board[ey2][mx], 2, board);
		 	board[by][lx] = new CheckPoint(board[by][lx], 2, board);	board[by ][mx] = new CheckPoint(board[by ][mx], 2, board);	board[by][rx] = new CheckPoint(board[by][rx], 2, board);

		 	//Set waypoints of each of the checkpoints
		 	
		 	((CheckPoint)board[ty][lx]).setNextCheckPoint(1,(CheckPoint) board[ty][mx]);	((CheckPoint)board[ty ][mx]).setNextCheckPoint(1,(CheckPoint) board[ey1][mx]);	((CheckPoint)board[ty][rx]).setNextCheckPoint(1,(CheckPoint) board[ty][mx]);
		 																					((CheckPoint)board[ey1][mx]).setIsEndPoint(true);
		 	((CheckPoint)board[my][lx]).setNextCheckPoint(1,(CheckPoint) board[ty][lx]);	/*The game will spawn two runners to go in either direction*/					((CheckPoint)board[my][rx]).setNextCheckPoint(1,(CheckPoint) board[ty][rx]);
		 	((CheckPoint)board[my][lx]).setNextCheckPoint(0,(CheckPoint) board[by][lx]);																					((CheckPoint)board[my][rx]).setNextCheckPoint(0,(CheckPoint) board[by][rx]);
		 																					((CheckPoint)board[ey2][mx]).setIsEndPoint(true);
		 	((CheckPoint)board[by][lx]).setNextCheckPoint(0,(CheckPoint) board[by][mx]);	((CheckPoint)board[by ][mx]).setNextCheckPoint(0,(CheckPoint) board[ey2][mx]);	((CheckPoint)board[by][rx]).setNextCheckPoint(0,(CheckPoint) board[by][mx]);
		 	
		return board;
	}
	
	//Not Finished! (modifier is private)
	@SuppressWarnings("unused")
	private static Square[][] XBoard() throws IOException
	{
	//Set fields	
		
		//Set the dimensions of the board
		int dimensionX=128;
		int dimensionY=128;
		
		//Amount of space to pad the playing area with with unplayable terrain
		int borderWidth=2;
		
		//Thickness of the X path from the center of it to its edge
		int xThickness = 4;
		
		//Number of images used to draw the board
		int numImages=10;
		
		//Array to hold the images to be drawn
		Consts.TextureType[] images = new Consts.TextureType[numImages];
		images[0] = Consts.TextureType.GRASS;
		images[1] = Consts.TextureType.DIRT;
		images[2] = Consts.TextureType.GRAVEL;
		
		//Declare the board that is to be returned
		Square[][] board = new Square[dimensionY][dimensionX];
		
	//Use mathematics to geometrically define the board
		
		//Clear the whole board to unwalkable areas
		for(int y=0;y<dimensionY;y++)
			for(int x=0;x<dimensionX;x++)
			{
				board[y][x] = new Square(x,y,images[0]);
				board[y][x].setWalkable(false);
			}
				
		//Create the X path for the runners
		for(int y=borderWidth+xThickness;y<dimensionY-borderWidth-xThickness;y++)
		{
			for(int j=-xThickness;j<xThickness;j++)
			{
				board[y][y+j] = new Square(y+j,y,images[1]);
				board[y][y+j].setWalkable(true);
			}
		}
		
		//Connect the top and bottom parts of the X
		for(int y=borderWidth;y<xThickness*2;y++)
		{
			for(int x=borderWidth;x<dimensionX-borderWidth;x++)
			{
				board[y][x] = new Square(x,y,images[1]);
				board[dimensionY-y][x] = new Square(x,dimensionY-y,images[1]);
				
				board[y][x].setWalkable(true);
				board[dimensionY-y][x].setWalkable(true);
				
			}
		}
		
		return board;
	}
	
}
