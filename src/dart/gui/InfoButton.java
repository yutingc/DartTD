/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dart.gui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

import dart.network.Handler;

/**
 *
 * @author Jens
 */
@SuppressWarnings("serial")
public abstract class InfoButton extends JButton {

    private JPopupMenu _toolTipPanel;
    private JLabel _toolTip;
    private GameMap _gameMap;
    private Handler _handler;
    
    // The constructor also needs a parameter for host
    public InfoButton(ImageIcon image, String toolTip, GameMap map, Handler handler) {
        super(image);
        
        _toolTipPanel = new JPopupMenu();
        
        //_toolTipPanel.setPopupSize(300, 100);
        _toolTipPanel.setBackground(Color.CYAN);
        _toolTip = new JLabel(toolTip);
        _toolTipPanel.add(_toolTip);
        
        this.addMouseListener(new ToolTipListener());
        
        _gameMap = map;
        _handler = handler;
    }
    
    public abstract void mouseClickAction(GameMap map, Handler handler);

    private class ToolTipListener implements MouseListener {
        
        @Override
        public void mouseClicked(MouseEvent me) {
        	
        	mouseClickAction(_gameMap, _handler);
        	
        }

        @Override
        public void mousePressed(MouseEvent me) {
    //        throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseReleased(MouseEvent me) {
    //        throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void mouseEntered(MouseEvent me) {
            
        	// Show the tool tip
            _toolTipPanel.show(me.getComponent(),me.getX(),me.getY());
        }

        @Override
        public void mouseExited(MouseEvent me) {
            _toolTipPanel.setVisible(false);
        }
    }
}
