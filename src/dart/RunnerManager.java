package dart;

import java.util.ArrayList;

public class RunnerManager {

	//An Array of ArrayLists holding runners for each team
	//Each team has an ArrayList of runners
	private ArrayList<ArrayList<Runner>> Runners;
		
//--------------------------------
//Constructors
//--------------------------------

	//Create n lists of lists of Runners for n teams 
	public RunnerManager(int teams)
	{
		//Set up ArrayLists to track runners
		Runners = new ArrayList<ArrayList<Runner>>();
		for(int i=0;i<teams;i++)
		{
			Runners.add(new ArrayList<Runner>());
		}
		//Set the board reference
	}
	
//--------------------------------
//Methods
//--------------------------------

//Getter Methods
	
	public ArrayList<ArrayList<Runner>> getAllRunners() {
		return Runners;
	}
	
	//Get the list of Runners from the team opposite the player
	public ArrayList<Runner> getEnemyRunners(Player player)
	{
		//Currently only works with 2 teams, although it could be modified easily enough to work with more
		return Runners.get(player.getTeam());
	}
	
//Setter Methods
	
	public void setRunners(int team, ArrayList<Runner> r)
	{
		Runners.set(team, r);
	}
	
		//--------------------------------//
	
	
	
	//Add a runner to a team's list of runners
	public void addRunner(Runner r)
	{
		Runners.get(r.getTeam()).add(r);
	}
	//No need to manually remove them because board does that
	
	//Move the runners by one time-step along the map and
	public ArrayList<Runner> moveRunners(long time,Board board)
	{
		
		ArrayList<Runner> r = new ArrayList<Runner>();
		Runner current;
		Map m;
		
		//Loop through each team's list of runners
		for(int i=0;i<Runners.size();i++)
		{
			//Loop through each runner for that team
			for(int j=0;j<Runners.get(i).size();j++)
			{
				current=Runners.get(i).get(j);
				//If a runner can move, move him
				if(current.canMove(time))
					//If movement results in a Runner reaching the checkpoint, reroute his checkpoint
					if(current.move(time,board))
					{
						//We'll have to make this look nicer
						//If there's no checkpoint past the one the runner has gotten to, it has gotten to an endpoint.
						//So remove it from the RunnerManager list and add it to the list of runners to be returned.
						if(null==((CheckPoint)board.getSquare(current.getPosX(),current.getPosY())).getNextCheckPoint(current.getTeam()))
						{
							
							r.add(current);
							Runners.get(i).remove(j);
							continue;
						}
						
						//If it's not the end, go find the next waypoint.
						m = ( (CheckPoint)board.getSquare(current.getPosX(),current.getPosY())).getNextCheckPoint(current.getTeam()).getMap();
						current.setCheckPointMap( m );
					}
			}
		}	
		return r;
	}
	
}
