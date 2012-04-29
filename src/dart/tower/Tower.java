package dart.tower;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import dart.Consts;
import dart.ImageManager;
import dart.Player;
import dart.Runner;

public class Tower extends Square{
	
//--------------------------------
//Fields	
//--------------------------------

	//Inherits posx and posy from square
	
	//Integer index to hold which players owns this tower
	protected Player owner;
	
	//cost to purchase the tower
	protected static double purchaseCost;
	
	//Variables to specify the current stats of the tower
	protected double damage=10,range=4,armor;
	//Variables to specify the exponent by which the stats will upgrade
	protected double damageExp,rangeExp,reloadSpeedExp,armorExp;
	//Variables to specify the current cost to upgrade
	protected double damageCost,rangeCost,reloadSpeedCost,armorCost;
	//Variables to hold the exponent by which the cost is multiplied
	protected double damageCostExp,rangeCostExp,reloadSpeedCostExp,armorCostExp;
	
	
	//Long to hold how much time must elapse before the tower can attack again.
	protected long reloadSpeed=1000;
	//Long to hold the time the tower last fired
	protected long lastFired;
	
	//The (radian) angle at which the tower's turret will be drawn on top of it
	protected double turretAngle;
	
	//The image for the turret.
	private Consts.TowerType turret;
	
//--------------------------------
//Constructors	
//--------------------------------
	
	//Basic constructor for a tower
	public Tower(int x, int y, Consts.TextureType tile, Consts.TowerType turret)
	{
		//Create a square
		super(x, y, tile);
		//Specify that the tower is not walkable
		this.setWalkable(false);
		//Set the turret picture
		this.turret=turret;
		//Initialize the turretAngle to an arbitrary number
		this.turretAngle=0;
		
		this.tile = tile;
		this.turret = Consts.TowerType.SLOWTOWER;
	}
	
//--------------------------------
//Methods
//--------------------------------
	
	
//Getter Methods

	public Player getOwner() { return this.owner; }
	public int    getOwnerIndex()	{ return owner.getIndex(); }
	public int    getTeam()	 { return owner.getTeam(); }
	
	public static double getPurchaseCost()	{ return purchaseCost; }
	
	public double getDamage() { return this.damage; }
	public double  getRange() { return this. range; }
	public double  getArmor() { return this. armor; }
	public long   getSpeed() { return this. reloadSpeed; }
	
	public double getDamageCost() { return this.damageCost; }
	public double  getRangeCost() { return this.rangeCost;  }
	public double  getSpeedCost() { return this.reloadSpeedCost;  }
	public double  getArmorCost() { return this.armorCost;  }
	
	public double getTurretAngle() { return this.turretAngle; }
	
//Setter Methods

	//Takes in screen coordinates
	public void setTurretAngle(double x0, double y0, double x1, double y1)
	{
		this.turretAngle=Math.atan2(y0-y1,x0-x1);
	}
	
	//Set the player owner of this tower
	public void setOwner(Player owner)
	{
		this.owner = owner;
	}
	
	public void setTurretAngle(Runner r)
	{
		turretAngle=Math.PI+(Math.atan2((double) (posY-r.getPosY()), (double) (posX-r.getPosX())));
	}
	
		//--------------------------------//

//Runner methods

	//Determine whether the tower can attack a runner yet
	public boolean canAttack(long time)
	{
		//If the tower has waited a sufficient amount of time, say yes
		if(time - lastFired > reloadSpeed) return true;
		//Otherwise say no.
		return false;
	}
	
	//Attacks the runner and returns whether the runner has died or not
	public boolean attackRunner(Runner r,long time)
	{
		boolean died; 
		
		//Make the tower face the runner
		setTurretAngle(r);
				
		//Deal damage to the runner and find out if it died or not
		
		died = r.takeDamage(getDamage());
		
		//Reset the time the tower last fired
		lastFired=time;
		
		//Return whether the runner died or not
		return died;			
	}
	
	//Calculates the distance between a runner and a tower and returns whether the runner is in range
	public boolean runnerIsWithinRange(Runner r)
	{
		//Calculate the difference between the x and y coordinates
		int x = posX - r.getPosX();
		int y = posY - r.getPosY();
		
		//Check if the fields are within range
		if( Math.sqrt( (double) (x*x+y*y) ) <= range) return true;
		else return false;
	}
	
	
//Upgrade Methods
	
	public void upgradeDamage()
	{
		//Upgrade the damage
		this.damage=Math.floor(this.damage*this.damageExp);
		//Increase the cost to upgrade next time.
		this.damageCost=Math.floor(this.damageCost*this.damageCostExp);
	}
	public void upgradeRange()
	{
		//Upgrade the range
		this.range=Math.floor(this.range*this.rangeExp);
		//Increase the cost to upgrade next time.
		this.rangeCost=Math.floor(this.rangeCost*this.rangeCostExp);
	}
	public void upgradeSpeed()
	{
		//Upgrade the speed
		this.reloadSpeed=(long) Math.floor( ((double) this.reloadSpeed )*this.reloadSpeedExp);
		//Increase the cost to upgrade next time.
		this.reloadSpeedCost=Math.floor(this.reloadSpeedCost*this.reloadSpeedCostExp);
	}
	public void upgradeArmor()
	{
		//Upgrade the armor
		this.armor=Math.floor(this.armor*this.armorExp);
		//Increase the cost to upgrade next time.
		this.armorCost=Math.floor(this.armorCost*this.armorCostExp);
	}
	
//Paint Method
	
	@Override
	public void paint(Graphics2D g, int x, int y) {
		super.paint(g,x,y);
		
		BufferedImage resizedTurret = ImageManager.getResizedTurret(turret);
		AffineTransform at = new AffineTransform();
		at.rotate(turretAngle + Math.PI / 2, resizedTurret.getWidth() / 2, resizedTurret.getHeight() / 2);
		
		AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
		BufferedImage rotatedTurret = rotateOp.filter(resizedTurret, null);
		
		g.drawImage(rotatedTurret, x, y, null);
	}
}
