/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dart;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Jens
 */
public final class Consts {
    
    
    public static final int SCREEN_WIDTH = 1024;
    public static final int SCREEN_HEIGHT = 768;
    public static final int BUTTON_SIZE = (int) SCREEN_WIDTH / 18;
    public static final int PIXEL_PER_BLOCK = 20;
    public static final ArrayList<Color> PLAYER_COLORS = new ArrayList<Color>();
    
    static {
    	PLAYER_COLORS.add(Color.BLUE);
    	PLAYER_COLORS.add(Color.RED);
    	PLAYER_COLORS.add(Color.CYAN);
    	PLAYER_COLORS.add(Color.MAGENTA);
    	PLAYER_COLORS.add(Color.ORANGE);
    }
    
    public enum UnitType {
    	TOWER, RUNNER, BONUS
    }
    
    public enum Direction {
    	UP, DOWN, LEFT, RIGHT
    }
    
    public enum TowerType {
    	NONE, SLOWTOWER, TOWER2, TOWER3
    }
    
    public enum RunnerType {
    	NONE, RUNNER1, RUNNER2, RUNNER3
    }
    
    public enum BonusType {
    	NONE, BONUS1, BONUS2, BONUS3
    }
    
    public enum AttrType {
    	NONE, DAMAGE,SPEED,RANGE,ARMOR,HEALTH,SPECIAL
    	
    }
    
    public enum TextureType {
    	GRASS, GRAVEL, DIRT, LASERTEX
    }
    
}


