/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dart.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import dart.Board;
import dart.Player;

/**
 *
 * @author Jens
 */
@SuppressWarnings("serial")
public class StatusBar extends JPanel {
	
    
	// the constructor should take as an argument a class that has the resource stats of the player
    public StatusBar() {
        super();
        
        this.setBackground(Color.BLUE);
        this.setVisible(true);
    }
    
    // display method
    public void paint(Graphics graphics, Player p) {
    	Graphics2D g = (Graphics2D) graphics;
    	g.drawString("Money: " + p.getMoney(), 20, 10);
    	
    }
}
