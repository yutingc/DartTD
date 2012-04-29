/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dart.gui;

import java.awt.Graphics;
import javax.swing.JPanel;

import dart.Board;

/**
 *
 * @author wayne
 */
@SuppressWarnings("serial")
public class BufferMap extends JPanel {
	
    Board _board;
    int _width;
    int _height;
    
    public BufferMap(Board board,int width, int height){
        _board = board;
        _width = width;
        _height = height;
    }
    
    @Override
    public void paint(Graphics graphics){
        
    }
    
}
