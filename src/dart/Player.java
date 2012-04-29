package dart;

import java.util.ArrayList;

public class Player {
	
//--------------------------------
//Fields
//--------------------------------

	//Int to hold the index (which player) this player is.
	private int index;
	
	//Int to hold which team the player belongs to
	private int team;
	
	//Player information
	private double money,score;
	
	private long time=0,dtim;
//--------------------------------
//Constructors
//--------------------------------

	public Player(int index, int team)
	{
		//Request player index from the host
		this.index	= index;
		this.team	= team;
	}

		
//--------------------------------
//Methods
//--------------------------------		
	
//Getter Methods
	
	public int getIndex()	{ return index;	}
	public int getTeam()	{ return team;	}
	public double getMoney()	{ return money; }
	public double getScore()	{ return score; }
	public long getTime()	{ return time;	}
	
//Setter Methods
	
	public void setMoney(double money)	{ this.money = money; }
	public void setScore(double score) { this.score = score; }
	public void setTime(long time)	{ this.time  = time;  }
	
		//--------------------------------//
	
	public void tallyRunners(ArrayList<Runner> deadRunners)
	{
		Runner r;
		//Loop through every runner
		while(deadRunners.size()>0)
		{
			//remove the lastmost runner on the list (quicker than the first and having to shift the whole list)
			r = deadRunners.remove(deadRunners.size()-1);
			
			//Increase the player's score
			money += r.getRewardMoney();
		}
	}
	
}