package dart.tower;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import dart.Consts;
import dart.ImageManager;

public class Square {

//--------------------------------
//Fields
//--------------------------------
	
	protected int posX, posY;

	protected boolean isWalkable;
	protected boolean isBuildable;
	
	protected Consts.TextureType tile;
	
	protected double tileScaleX;
	protected double tileScaleY;

	
//--------------------------------	
//Constructors
//--------------------------------	

	public Square(Square s)
	{
		posX = s.getX();
		posY = s.getY();
		tile = s.getTile();
		tileScaleX = s.getScaleX();
		tileScaleY = s.getScaleY();
		isWalkable = s.isWalkable();
		isBuildable = s.isBuildable();
	}
	
	//Create a square which only has is walkable field defined. (Debugging constructor) 
	public Square(boolean walkable)
	{
		this.isWalkable=walkable;
	}
	
	//Create a Square with an X and a Y and an image
	public Square(int x, int y,  Consts.TextureType tile)
	{
		this.posX=x;
		this.posY=y;
		this.tile=tile;
		this.tileScaleX = this.tileScaleY = 1.0;
	}
	
//--------------------------------
//Methods
//--------------------------------

//Getter Methods	
	public int getX()				{ return posX; }
	public int getY()				{ return posY;	}
	public boolean isWalkable() 	{ return isWalkable; }
	public boolean isBuildable()	{ return isBuildable;}
	
	public Consts.TextureType getTile()	{ return tile; }
	
	public double getScaleX() { return tileScaleX; }
	public double getScaleY() { return tileScaleY; }
	
	public int getWidth() {
		return ImageManager.getResizedTexture(tile).getWidth();
	}
	
	public int getHeight() {
		return ImageManager.getResizedTexture(tile).getHeight();
	}
	
//Setter Methods

	public void setX(int x) 						{ this.posX=x; }
	public void setY(int y) 						{ this.posY=y; }
	public void setWalkable(boolean isWalkable)		{ this.isWalkable=isWalkable; }
	public void setBuildable(boolean isBuildable)	{ this.isBuildable=isBuildable; }

//Paint Method (overridden by subclasses)
	
	// x,y are pixel coordinates
	public void paint(Graphics2D g, int x, int y) {
		BufferedImage resizedTile = ImageManager.getResizedTexture(tile);
		g.drawImage(resizedTile, x, y, null);
		
	}
}
