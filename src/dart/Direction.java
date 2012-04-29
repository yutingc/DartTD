package dart;

public class Direction {
	
//--------------------------------
//Fields
//--------------------------------
	
	int dx,dy;
	
	boolean isCheckpoint;

//--------------------------------
//Constructors
//--------------------------------
	
	//Constructor that initializes the checkpoint to false
	public Direction(int dx, int dy)
	{
		this.dx=dx;
		this.dy=dy;
		this.isCheckpoint=false;
	}
	//Constructor that allows you to set the checkpoint boolean
	public Direction(int dx, int dy, boolean isCheckpoint)
	{
		this.dx=dx;
		this.dy=dy;
		this.isCheckpoint=isCheckpoint;
	}
	
//--------------------------------
//Methods
//--------------------------------
	
//Getter Methods
	
	public int getX() {return dx;}
	public int getY() {return dy;}
	
	
		//--------------------------------//
	
	
	
}