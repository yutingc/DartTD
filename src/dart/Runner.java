package dart;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Runner {

//--------------------------------
//Fields
//--------------------------------
	
	//Position of the runner on the map
	int posX, posY;
	
	//Starting health and movement speed of the runner
	double initialHealth=100;
	int initialSpeed=400;
	
	//Health and movement speed of the runner
	double currentHealth=100;
	int currentSpeed=400;
	
	//Time the runner last moved
	long lastMoved;
	
	//Map that the runner follows to get to the checkpoint
	Map vectorMap;
	
	//Coordinates of its checkpoint
	int destinationX,destinationY;
	
	//Cost of the runner
	int cost;
	
	//Person who purchased the runner
	Player owner;
	
	//Money awarded to a user who kills this runner
	int rewardMoney;
	
//--------------------------------
//Constructors
//--------------------------------

	public Runner(int startX, int startY, Player owner)
	{
		this.posX	= startX;
		this.posY	= startY;
		this.owner	= owner;
	}
	
	// debugging constructor
	public Runner() {
		posX = 0;
		posY = 0;
	}
	
//--------------------------------
//Methods
//--------------------------------


//Getter Methods
	
	public int getPosX()			{ return this.posX; }
	public int getPosY()			{ return this.posY; }
	public int getCurrentSpeed()	{ return this.currentSpeed; }
	public int getInitialSpeed() { return this.initialSpeed; }
	public int getCost()			{ return this.cost;			}
	public int getRewardMoney()		{ return this.rewardMoney;	}
	public int getTeam()			{ return owner.getTeam();	}
	public Direction getDirection()	{ return vectorMap.getDirection(posX, posY); }
	
	public Player getPlayer()		{ return this.owner;		}
	
	
//Setter Methods
	
	public void setPosX(int posX)		{ this.posX = posX;   }
	public void setPosY(int posY)		{ this.posY = posY;	  }
	public void setSpeed(int speed)		{ this.currentSpeed = speed; }
	public void setCheckPointMap(Map m)	{ this.vectorMap = m; destinationX=m.getCheckPointX(); destinationY=m.getCheckPointY();}
	
	
		//--------------------------------//
	
//Movement methods	
	
	//Return whether the runner can move again based on when it last moved and
	//the time passed to the method
	public boolean canMove(long time)
	{
		//If the minimum amount of time has passed, move the runner
		if(time-lastMoved>currentSpeed)
			{
				return true;
			}
		return false;
	}
	
	//Method that returns whether the Runner has reached its current CheckPoint
	public boolean move(long time, Board b)
	{
		Direction d = b.getDirection(posX, posY, destinationX, destinationY);
		//Get the vector associated with the point on the map
		//Direction d = vectorMap.getDirection(posX, posY);
		
		//Move the runner
		posX+=d.getX();
		posY+=d.getY();
		
		//Set the last moved time
		lastMoved=time;
		
		//Say whether the runner reached its checkpoint
		return vectorMap.isCheckPoint(posX, posY); 
	}
	
//Interaction Methods
	
	//Method that deducts the damage passed to a runner from its health points
	//The return value is true if the runner has died and false if it hasn't
	public boolean takeDamage(double damage)
	{
		//Subtract damage from the runner's health
		currentHealth-=damage;
		
		//If the health is zero or less, return true, otherwise return false.
		if(currentHealth<=0) return true;
		else return false;
	}
	
	
//Paint Method
	
	public void paint(Graphics2D g, int x, int y, int w, int h) {
		Ellipse2D circle = new Ellipse2D.Double(x,y,w,h);
		g.setPaint(Color.YELLOW);
		g.draw(circle);
		g.fill(circle);
		
		Rectangle2D rect = new Rectangle2D.Double(x+1,y-10,(w - 1) * (currentHealth / initialHealth),5);
		g.setPaint(Color.GREEN);
		g.draw(rect);
		g.fill(rect);
	}
}
