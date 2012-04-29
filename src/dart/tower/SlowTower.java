package dart.tower;

import java.io.IOException;

import dart.Consts;
import dart.Player;
import dart.Runner;

public class SlowTower extends Tower{

//--------------------------------
//Fields
//--------------------------------
	
	//Percent of the runner's initial speed the tower will slow it down to.
	//NOTE: Range is from 0.0 to 1.0
	private double slowPercent=1.10;
	
	//purchaseCost = 10.0;
	
//--------------------------------
//Constructors
//--------------------------------
	
	public SlowTower(int x, int y, Player p) throws IOException
	{
		super(x, y, Consts.TextureType.LASERTEX, Consts.TowerType.SLOWTOWER);
	}
//--------------------------------
//Methods
//--------------------------------
	
//Setter methods

	
	//Attack a runner
	public boolean attackRunner(Runner r,long time)
	{
		//Subtract damage, just like in the superclass
		boolean died = super.attackRunner(r,time);
		
		//Set the runner's speed to the lowest value between what this tower would slow it to
		//from its initial speed and what it's actual speed is now. This is done so that if a
		//better slowing tower attacked a runner, we don't want to speed up the runner and delete
		//the effects of that better tower.
		r.setSpeed((int) Math.max(slowPercent*(double)r.getInitialSpeed(),(double)r.getCurrentSpeed()));
		
		//Still return whether the Runner died or not.
		return died;
	}
	
}