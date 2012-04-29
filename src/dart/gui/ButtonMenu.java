/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dart.gui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import dart.Consts;
import dart.Consts.UnitType;
import dart.network.Handler;

/**
 *
 * @author Jens
 */
@SuppressWarnings("serial")
public class ButtonMenu extends JPanel {
    
    private int _nextX = 0;
    private int _nextY = 0;
    private int _buttonDimSize = Consts.BUTTON_SIZE;
    
    private GameMap _gameMap;
    private Handler _handler;
    
    public ButtonMenu(GameMap map, Handler handler) {
        super();
        this.setLayout(null);
        
        _gameMap = map;
        _handler = handler;
        
        this.setBackground(Color.yellow);
        this.setVisible(true);
        this.setFocusable(false);
    }
    
    public void addButton(String image_filepath, String toolTip, UnitType t) {
        
        
        // create a new infoButton instance with its corresponding tooltip
        ImageIcon buttonIcon = new ImageIcon(image_filepath);
        
        buttonIcon = new ImageIcon(buttonIcon.getImage()
                .getScaledInstance(Consts.BUTTON_SIZE,Consts.BUTTON_SIZE,Image.SCALE_SMOOTH));

        InfoButton button = null;
        
        switch(t) {
        	case TOWER:
	        	button = new TowerButton(Consts.TowerType.SLOWTOWER, toolTip, _gameMap, _handler);
	        	break;
        	case RUNNER:
	        	button = new RunnerButton(buttonIcon, toolTip, _gameMap, _handler);
	        	break;
        	case BONUS:
	        	button = new BonusButton(buttonIcon, toolTip, _gameMap, _handler);
	        	break;
        }
        
        
        // set the location and the size of the button
        button.setSize(_buttonDimSize, _buttonDimSize);
        button.setLocation(_nextY * Consts.BUTTON_SIZE, _nextX * Consts.BUTTON_SIZE);
        this.add(button);
        
        _nextX++;
        if( (_nextX+1) * Consts.BUTTON_SIZE > this.getWidth() ) {
            
            _nextY++;
            _nextX = 0;
            if( (_nextY + 1) * Consts.BUTTON_SIZE > this.getHeight() ) {
                return ;
            }
        }
        
    }
    
}
