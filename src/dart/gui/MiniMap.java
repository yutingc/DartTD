/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dart.gui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import dart.Board;
import dart.Runner;
import dart.RunnerManager;

/**
 *
 * @author Jens
 */
@SuppressWarnings("serial")
public class MiniMap extends JPanel implements MouseListener, MouseMotionListener {
    
	private Board _board;
	private RunnerManager _runners;
	private GameMap _gameMap;
	
//	private final double _ratioX = 2.0;
//	private final double _ratioY = 1.35;
	
    public MiniMap(Board b, RunnerManager runners, GameMap gameMap) {
        super();
        
        _board = b;
        _runners = runners;
        _gameMap = gameMap;
        
        this.setBackground(java.awt.Color.BLACK);
        this.setVisible(true);
        
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public void paint(Graphics g) {
    	
    	int width = getWidth();
    	int height = getHeight();
    	
    	// paint a mini-version of the entire map
    	int boardWidth = _board.getWidth();
    	int boardHeight = _board.getHeight();

    	double blockWidth = (double) width / (double) boardWidth;
    	double blockHeight = (double) height / (double) boardHeight;
    	
    	Graphics2D brush = (Graphics2D) g;
    	
//    	System.out.println("blockWidth: " + blockWidth + ", blockHeight: " + blockHeight);
    	
    	brush.scale(blockWidth, blockHeight);
    	
    	
    	for(int i = 0; i < boardWidth; i++)
    		for(int j = 0; j < boardHeight; j++) {
    			
    			// if this square is a tower, draw the tower with the player's assigned color
    			if(_board.isTower(i, j)) {
    				brush.setColor(Color.RED);
    			} 
    			
    			// otherwise, draw a rectangle with an approximate texture color representing this square
    			else {
    				switch(_board.getTile(i, j)) {
    					case GRAVEL:
    						brush.setColor(new Color(243,218,115));
    						break;
    					case GRASS:
    						brush.setColor(Color.GREEN.darker().darker());
    						break;
    					case DIRT:
    						brush.setColor(new Color(51,25,0));
    						break;
    				}
    			}
				brush.drawRect(i, j, 1, 1);
    		}
    	

    	// draw the runners
    	for(ArrayList<Runner> player : _runners.getAllRunners())
    		for(Runner r : player) {
    			Ellipse2D circle = new Ellipse2D.Double(r.getPosX(), r.getPosY(), 1, 1);
    			brush.setColor(Color.RED);
    			brush.draw(circle);
    		}

    	// draw a yellow rectangle showing where in the minimap GameMap is focused on
    	double topleftX = _gameMap.getRefX() * boardWidth;
    	double topleftY = _gameMap.getRefY() * boardHeight;
    	
    	Rectangle2D rect = new Rectangle2D.Double(topleftX, topleftY, 
    			(1.0 /  _gameMap.getZoomX()) * boardWidth, 
    			(1.0 / _gameMap.getZoomY()) * boardHeight);
    	brush.setColor(Color.YELLOW);
    	brush.draw(rect);
    	
    	
//    	brush.drawLine(topleftX, topleftY, toprightX, toprightY);
    }

    
    private void moveGameFrame(int mouseX, int mouseY) {
		int x = mouseX, y = mouseY;
		int width = getWidth(), height = getHeight();
		double refx = ((double) x / (double) width) - 0.5 / _gameMap.getZoomX();
		double refy = ((double) y / (double) height) - 0.5 / _gameMap.getZoomY();
		_gameMap.setRef(refx, refy);
		_gameMap.repaint();
		repaint();
    }
    
	@Override
	public void mouseClicked(MouseEvent e) {
		moveGameFrame(e.getX(), e.getY());
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		moveGameFrame(e.getX(), e.getY());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		moveGameFrame(e.getX(), e.getY());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
