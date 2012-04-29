package dart;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class ImageManager {

	private static ArrayList<BufferedImage> textureImages;
	private static ArrayList<BufferedImage> turretImages;
	
	private static ArrayList<BufferedImage> scaledTextures;
	private static ArrayList<BufferedImage> scaledTurrets;
	
	private static ArrayList<BufferedImage> towerImages; // for buttons
	
	private static double _scaleX, _scaleY;
	
	private static int _scaleWidth, _scaleHeight;
	
	public static void initialize() {
		textureImages = new ArrayList<BufferedImage>();
		turretImages = new ArrayList<BufferedImage>();
		scaledTextures = new ArrayList<BufferedImage>();
		scaledTurrets = new ArrayList<BufferedImage>();
		towerImages = new ArrayList<BufferedImage>();
		
		BufferedImage dirt = null, gravel = null, grass = null, lasertex = null;
		BufferedImage laserturret = null;
		
		try {
			dirt = ImageIO.read(new File("resources/Dirt.png"));
			gravel = ImageIO.read(new File("resources/Gravel.jpg"));
			grass = ImageIO.read(new File("resources/Grass.png"));
			lasertex = ImageIO.read(new File("resources/Laser turret tex.png"));
			laserturret = ImageIO.read(new File("resources/Laser turret.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		_scaleX = _scaleY = 1.0;
		_scaleWidth = _scaleHeight = 0;
		
		textureImages.add(dirt);
		textureImages.add(gravel);
		textureImages.add(grass);
		textureImages.add(lasertex);
		scaledTextures.add(dirt);
		scaledTextures.add(gravel);
		scaledTextures.add(grass);
		scaledTextures.add(lasertex);
		
		turretImages.add(laserturret);
		scaledTurrets.add(laserturret);
		

		for(int i = 0; i != turretImages.size(); i++) {

			BufferedImage tex = textureImages.get(3 + i);
			BufferedImage turret = turretImages.get(i);
			BufferedImage tower = new BufferedImage(tex.getWidth(), tex.getHeight(), BufferedImage.TYPE_INT_ARGB);
			
			int turretX = (tex.getWidth() - turret.getWidth()) / 2;
			int turretY = (tex.getHeight() - turret.getHeight()) / 2;
			
			Graphics g = tower.getGraphics();
			g.drawImage(tex,0,0,null);
			g.drawImage(turret,turretX,turretY,null);
			
			towerImages.add(tower);
		}
		
		
	}
	
	
	public static void resizeImages(double scaleX, double scaleY)
	{
		if(_scaleX == scaleX && _scaleY == scaleY) {
			return ;
		}
		else {
			
			_scaleWidth = _scaleHeight = 0;
			_scaleX = scaleX;
			_scaleY = scaleY;
			
			scaledTextures.clear();
			scaledTurrets.clear();

			AffineTransform at = new AffineTransform();
			at.scale(scaleX, scaleY);
			AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			
			for(BufferedImage im : textureImages) {
				BufferedImage scaledInstance = scaleOp.filter(im, null);
				scaledTextures.add(scaledInstance);
			}
			
			for(BufferedImage im : turretImages) {
				BufferedImage scaledInstance = scaleOp.filter(im, null);
				scaledTurrets.add(scaledInstance);
			}
			
		}
	}
	
	public static void resizeImages(int width, int height) {
		if(_scaleWidth == width && _scaleHeight == height) {
			return ;
		}
		else {
			
			scaledTextures.clear();
			scaledTurrets.clear();
			
			_scaleX = _scaleY = 0.0;
			_scaleWidth = width;
			_scaleHeight = height;
			
			for(BufferedImage im : textureImages) {
				AffineTransform at = new AffineTransform();
				at.scale((double) (width + 1) / (double) im.getWidth(), (double) (height + 1) / (double) im.getHeight());
				AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				BufferedImage scaledInstance = scaleOp.filter(im, null);
				scaledTextures.add(scaledInstance);
			}
			
			for(BufferedImage im : turretImages) {
				AffineTransform at = new AffineTransform();
				at.scale((double) (width + 1) / (double) im.getWidth(), (double) (height + 1) / (double) im.getHeight());
				AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				BufferedImage scaledInstance = scaleOp.filter(im, null);
				scaledTurrets.add(scaledInstance);
			}
		}
		
	}
	
	public static BufferedImage getResizedTexture(Consts.TextureType tt) {
		switch(tt) {
		case DIRT:
			return scaledTextures.get(0);
		case GRAVEL:
			return scaledTextures.get(1);
		case GRASS:
			return scaledTextures.get(2);
		case LASERTEX:
			return scaledTextures.get(3);
		}
		return null;
	}
	
	public static BufferedImage getResizedTurret(Consts.TowerType tt) {
		switch(tt) {
		case SLOWTOWER:
			return scaledTurrets.get(0);
		}
		return null;
	}
	
	public static BufferedImage getResizedTowerTexture(Consts.TowerType tt) {
		switch(tt) {
		case SLOWTOWER:
			return scaledTextures.get(3);
		}
		return null;
	}
	
	public static BufferedImage getTexture(Consts.TextureType tt) {
		switch(tt) {	
		case DIRT:
			return textureImages.get(0);
		case GRAVEL:
			return textureImages.get(1);
		case GRASS:
			return textureImages.get(2);
		case LASERTEX:
			return textureImages.get(3);
		}
		return null;
	}
	
	public static BufferedImage getTurret(Consts.TowerType tt) {
		switch(tt) {
		case SLOWTOWER:
			return turretImages.get(0);
		}
		return null;
	}
	
	public static BufferedImage getTowerTexture(Consts.TowerType tt) {
		switch(tt) {
		case SLOWTOWER:
			return textureImages.get(3);
		}
		return null;
	}
	
	
	public static BufferedImage getTowerImage(Consts.TowerType tt) {
		switch(tt) {
		case SLOWTOWER:
			return towerImages.get(0);
		}
		return null;
	}
}
