/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dart.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import dart.Board;
import dart.Consts.Direction;
import dart.Consts.TowerType;
import dart.ImageManager;
import dart.Runner;
import dart.RunnerManager;
import dart.network.Handler;
import dart.tower.Square;
import dart.tower.Tower;

/**
 *
 * @author Jens
 * 
 * 
 * Main component of the game screen
 * 
 */
@SuppressWarnings("serial")
class GameMap extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener {
    
    private double _refX;
    private double _refY;
    // _zoomX >= 1, _zoomY >= 1
    private double _zoom;
    private double _zoomX;
    private double _zoomY;
    private boolean _resized = false;
    private Board _board;
    
    private TowerType _towerHeld = TowerType.NONE;
    private int _cursorBlockX = -1;
    private int _cursorBlockY = -1;
    
    private Timer _windowMoveLeftTimer;
    private Timer _windowMoveRightTimer;
    private Timer _windowMoveUpTimer;
    private Timer _windowMoveDownTimer;
    private final int _windowMoveDelta = 20; // pixels
    private final int _windowMoveDelay = 4;
    
    private StatusMenu _statusMenu;
    
    private RunnerManager _runners;
    
    private Handler _handler;
    
    // should also have a reference to mini-map
    public GameMap(StatusMenu menu, Board b, RunnerManager rm, Handler handler) {
        super();
        
        

        _zoom = 10;
        _zoomX = _zoom;
        _zoomY = 1.58 * _zoom;
        _refX = 0.5 - 0.5 / _zoomX;
        _refY = 0.5 - 0.5 / _zoomY;
        _windowMoveLeftTimer = new Timer(_windowMoveDelay, new windowMoveListener(Direction.LEFT));
        _windowMoveRightTimer = new Timer(_windowMoveDelay, new windowMoveListener(Direction.RIGHT));
        _windowMoveUpTimer = new Timer(_windowMoveDelay, new windowMoveListener(Direction.UP));
        _windowMoveDownTimer = new Timer(_windowMoveDelay, new windowMoveListener(Direction.DOWN));
        
        
        _statusMenu = menu;
        _board = b;
        _handler = handler;
        _runners = rm;
        
        this.setBackground(Color.BLACK);
        this.setVisible(true);
        this.setLayout(null);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addKeyListener(this);
        
        setFocusable(true);
        requestFocus(true);
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        
    	// right-clicking while holding a tower cancels the tower-holding effect
    	if(_towerHeld != TowerType.NONE && me.getButton() == MouseEvent.BUTTON3) {
            _towerHeld = TowerType.NONE;
            return ;
    	}
    	
        // if a tower is selected and the left mouse button is clicked
        if(_towerHeld != TowerType.NONE && me.getButton() == MouseEvent.BUTTON1) {
        	
        	// modify the network packet to request a tower
        	_handler.setBuyTower(_towerHeld,findBlockX(me.getX()), findBlockY(me.getY()));
        }
        
        // otherwise (if no tower is being held)
        else {
        	
        	// find the block coordinates of the mouse
        	int x = findBlockX(me.getX());
        	int y = findBlockY(me.getY());
        	
        	// if mouse is clicked while hovering over a tower, set  _statusmenu to hold the tower, 
        	// otherwise disassociate _statusmenu with any tower
        	if(_board.isTower(x,y)) {
        		_statusMenu.setTower((Tower) _board.getSquare(x, y));
//        		_statusMenu.repaint();
        	} else {
        		_statusMenu.setTower(null);
//        		_statusMenu.repaint();
        	}
        }
        
        requestFocus(true);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        return ;
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        return ;
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        return ;
    }

    @Override
    public void mouseExited(MouseEvent me) {
    	_cursorBlockX = _cursorBlockY = -1;
    }

    @Override
    public void mouseDragged(MouseEvent me) {
        return ;
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        if(_towerHeld != TowerType.NONE) {
        	
        	// the horizontal location of the tower should the mouse be clicked
            _cursorBlockX = (int) ( ((double) findBlockX(me.getX()) * ( (double) _zoomX * getWidth() / (double) _board.getWidth())
            		- _refX * ( (double) _zoomX * getWidth())));
            // vertical
            _cursorBlockY = (int) ( ((double) findBlockY(me.getY()) * ( (double) _zoomY * getHeight() / (double) _board.getHeight())
            		- _refY * (double) _zoomY * getHeight()) );
//            repaint();
        }
        
    }

	@Override
	public void mouseWheelMoved(MouseWheelEvent me) {
		// TODO Auto-generated method stub
		_zoom -= me.getWheelRotation() * 0.5;
		_zoom = Math.min(_zoom, 30);
		_zoom = Math.max(_zoom, 1);
		_zoomX = _zoom;
		_zoomY = _zoom * 1.58;
		if(_refX + 1.0 / _zoomX > 1)
			_refX = 1 - 1.0 / _zoomX;
		if(_refY + 1.0 / _zoomY > 1)
			_refY = 1 - 1.0 / _zoomY;
		_resized = false;
	}

    
	@Override
	public void keyPressed(KeyEvent ke) {
		
		
		switch(ke.getKeyCode()) {
			case KeyEvent.VK_RIGHT:
				_windowMoveRightTimer.start();
				break;
			case KeyEvent.VK_LEFT:
				_windowMoveLeftTimer.start();
				break;
			case KeyEvent.VK_DOWN:
				_windowMoveDownTimer.start();
				break;
			case KeyEvent.VK_UP:
				_windowMoveUpTimer.start();
				break;
			
		}
	}

	@Override
	public void keyReleased(KeyEvent ke) {
		
		switch(ke.getKeyCode()) {
		case KeyEvent.VK_RIGHT:
			_windowMoveRightTimer.stop();
			break;
		case KeyEvent.VK_LEFT:
			_windowMoveLeftTimer.stop();
			break;
		case KeyEvent.VK_DOWN:
			_windowMoveDownTimer.stop();
			break;
		case KeyEvent.VK_UP:
			_windowMoveUpTimer.stop();
			break;
	}
	}

	@Override
	public void keyTyped(KeyEvent ke) {
		// if key is escape
		// display menu
		switch(ke.getKeyCode()) {
		case KeyEvent.VK_T:
			break;
		}
	}

    
//	public void buildTower(int x, int y, Player p, Consts.TowerType type) {
//
//    	Consts.TextureType texture = _board.getTile(x, y);
//    	BufferedImage turret = null;
//    	try {
//    		turret = ImageIO.read(new File("resources/Laser turret.png"));
//    	} catch (IOException e) {
//    		System.err.println("failed to read turret image");
//    	}
//    	
//    	_board.changeSquare(x,y, new Tower(x, y, texture, turret));
//    	
//        this.setCursor(Cursor.getDefaultCursor());
//        _towerSelected = false;
//        repaint();
//        
//    }
    
//-------------Setter Methods-----------------
    public void setTowerHeld(TowerType tt) {
        _towerHeld = tt;
    }
    
    public void setRef(double x, double y) {
    	_refX = Math.max(0, x);
    	_refY = Math.max(0, y);
    	_refX = Math.min(_refX, 1 - 1.0 / _zoomX);
    	_refY = Math.min(_refY, 1 - 1.0 / _zoomY);
    }
    
    
//-------------Getter Methods-----------------
    
    public double getZoomX() {
    	return _zoomX;
    }
    
    public double getZoomY() {
    	return _zoomY;
    }
    
    public double getRefX() {
    	return _refX;
    }
    
    public double getRefY() {
    	return _refY;
    }
    
//-------------Utility Methods------------------
    private int findBlockX(int pixel) {
    	
    	double percent = (double) pixel / (double) (getWidth() * _zoomX);
    	return ((int) (((double) (percent + _refX) * _zoomX * getWidth()) / ((double) getWidth() * _zoomX / (double) _board.getWidth())));
    	
    }
    
    private int findBlockY(int pixel) {
    	
    	double percent = (double) pixel / (double) (getHeight() * _zoomY);
    	return (int) ((percent + _refY) * _board.getHeight());
    	
    }
    
    
    
//---------------Paint Method-----------------
    @Override
    public void paint(Graphics graphics) {
    	
        Graphics2D g = (Graphics2D) graphics;
        int w = _board.getWidth();
        int h = _board.getHeight();
        
        double squarePixelX = (double) _zoomX * getWidth() / (double) w;
        double squarePixelY = (double) _zoomY * getHeight() / (double) h;
        
        
        // resize images in ImageManager if they haven't been resized
        if(!_resized) {
        	ImageManager.resizeImages((int) squarePixelX, (int) squarePixelY);
        	_resized = true;
        }
        
        // draw the board
        for(int i = (int) (w * _refX); i < (int) (w * (_refX + 1.0 / _zoomX)) + 1 && i < w; i++)
        	for(int j = (int) (h * _refY); j < (int) (h * (_refY + 1.0 / _zoomY)) + 1 && j < h; j++) {
        		
        		Square t = _board.getSquare(i,j);
            	int screenX = (int) ( ((double) i * squarePixelX - _refX * (double) _zoomX * getWidth()) );
            	int screenY = (int) ( ((double) j * squarePixelY - _refY * (double) _zoomY * getHeight()) );
            	
            	t.paint(g, screenX, screenY);
        	}
        
        // draw runners
        for(ArrayList<Runner> player : _runners.getAllRunners())
        	for(Runner r : player) {
            	
            	int screenX = (int) ( ((double) r.getPosX() * squarePixelX - _refX * (double) _zoomX * getWidth()) );
            	int screenY = (int) ( ((double) r.getPosY() * squarePixelY - _refY * (double) _zoomY * getHeight()) );
            	r.paint(g, screenX, screenY, (int) squarePixelX, (int) squarePixelY);
        	}
        
        // draw the tower held by the cursor
        if(_towerHeld != TowerType.NONE && _cursorBlockX > 0) {
        	g.drawImage(ImageManager.getResizedTowerTexture(_towerHeld), _cursorBlockX, _cursorBlockY, this);
        	g.drawImage(ImageManager.getResizedTurret(_towerHeld), _cursorBlockX, _cursorBlockY, this);
        }
        
        // update information on statusMenu if a tower is selected
        if(_statusMenu.isTowerSelected()) {
        	Tower t = _statusMenu.getTower();
        	int tx = t.getX(), ty = t.getY(), tw = t.getWidth(), th = t.getHeight();
        	int cx = (int) ( ((double) tx * squarePixelX - _refX * (double) _zoomX * getWidth()) );
        	int cy = (int) ( ((double) ty * squarePixelY - _refY * (double) _zoomY * getHeight()) );
        	Ellipse2D circle = new Ellipse2D.Double(cx - tw * 0.207, cy - th * 0.207, tw * 1.414, th * 1.414);
        	g.setPaint(Color.GREEN);
        	g.draw(circle);
        }
    }
    
    private class windowMoveListener implements ActionListener {
    	
    	Direction _dir;
    	
    	public windowMoveListener(Direction d) {
    		_dir = d;
    	}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			double percentDelta = (double) _windowMoveDelta / (double) (getWidth() * _zoomX);
			switch(_dir) {
				case UP:
					if(_refY - percentDelta >= 0) {
						_refY -= percentDelta;
//						repaint();
					}
					break;
				case DOWN:
					if(_refY + 1.0 / _zoomY + percentDelta <= 1) {
						_refY += percentDelta;
//						repaint();
					}
					break;
				case LEFT: 
					if(_refX - percentDelta >= 0) {
						_refX -= percentDelta;
//						repaint();
					}
					break;
				case RIGHT:
					if(_refX + 1.0 / _zoomX + percentDelta <= 1 ) {
						_refX += percentDelta;
//						repaint();
					}
					break;
			}
		}
    	
    }


}
