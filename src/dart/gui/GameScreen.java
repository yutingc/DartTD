package dart.gui;

import javax.swing.JFrame;

import dart.Board;
import dart.Consts;
import dart.ImageManager;
import dart.RunnerManager;
import dart.network.Handler;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jens
 */
@SuppressWarnings("serial")
public class GameScreen extends JFrame {
    
    private int _width = Consts.SCREEN_WIDTH;
    private int _height = Consts.SCREEN_HEIGHT;
   
    private Board _board;
    private RunnerManager _runners;
    private StatusBar _statusBar;
    private MiniMap _miniMap;
    private StatusMenu _statusMenu;
    private GameMap _gameMap;
    private TowerMenu _towerMenu;
    private RunnerMenu _runnerMenu;
    private BonusMenu _bonusMenu;
    
    private Handler _handler;
    
    
    public GameScreen(Board b, RunnerManager rm, Handler handler) {
        super("Dart Tower Defense");

        ImageManager.initialize();
        
        setSize(_width, _height);
        setResizable(false);
        setLayout(null);
        
        _board = b;
        _runners = rm;
        _handler = handler;
        _statusBar = new StatusBar();

        _statusMenu = new StatusMenu(_handler);
        _gameMap = new GameMap(_statusMenu, _board, _runners, _handler);
        _miniMap = new MiniMap(_board, _runners, _gameMap);
        _towerMenu = new TowerMenu(_gameMap, _handler);
        _runnerMenu = new RunnerMenu(_gameMap, _handler);
        _bonusMenu = new BonusMenu(_gameMap, _handler);
        
        _statusBar.setSize(_width, _height / 20);
        _statusBar.setLocation(0,0);
        
        _gameMap.setSize(_width * 5 /6, _height * 7/10);
        _gameMap.setLocation(0, _height /20);
        
        _statusMenu.setSize(_width * 7/12, _height * 1/4);
        _statusMenu.setLocation(_width * 1/4, _height * 3/4 - 1);
        
        _towerMenu.setSize(_width / 6, _height * 7/20);
        _towerMenu.setLocation(_width * 5/6, _height / 20);
        
        _runnerMenu.setSize(_width / 6, _height * 7/20);
        _runnerMenu.setLocation(_width * 5/6, _height * 2/5);
        
        _bonusMenu.setSize(_width / 6, _height / 4);
        _bonusMenu.setLocation(_width * 5/6, _height * 3/4);
        
        
        _miniMap.setSize(_width / 4, _height / 4);
        _miniMap.setLocation(0, _height * 3 / 4 - 1);
        
        this.add(_miniMap);
        this.add(_gameMap);
        this.add(_towerMenu);
        this.add(_statusMenu);
        this.add(_runnerMenu);
        this.add(_bonusMenu);
        this.add(_statusBar);
        
        this.setVisible(true);
    }
    
    public void setTowerSelected(Consts.TowerType tt) {
    	switch(tt) {
    	case NONE: 
    		_gameMap.setTowerHeld(Consts.TowerType.NONE);
    		break;
    	}
    }
    
//    public void buildTower(int x, int y, Consts.TowerType tt, Player p) {
//    	_gameMap.buildTower(x, y, p, tt);
//    }
//    
//    public void sendRunner(Consts.RunnerType rt, int playerIndex) {
//    	_gameMap.sendRunner(null);
//    	
//    }
//    
//    public void applyBonus(Consts.BonusType bt, int playerIndex) {
//    	
//    }
}
