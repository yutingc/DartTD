/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dart.gui;

import dart.Consts;
import dart.network.Handler;

/**
 *
 * @author Jens
 */
@SuppressWarnings("serial")
public class TowerMenu extends ButtonMenu {
    
    public TowerMenu(GameMap map, Handler handler) {
        super(map, handler);
        
        this.addButton("resources/tower1.jpg", "This is your first tower", Consts.UnitType.TOWER);
        this.addButton("resources/tower2.jpg", "This is your second tower", Consts.UnitType.TOWER);
        // add other buttons
        // ...
    }
    
}
