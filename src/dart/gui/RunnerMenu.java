/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dart.gui;

import dart.Consts.*;
import dart.network.Handler;

/**
 *
 * @author Jens
 */
@SuppressWarnings("serial")
public class RunnerMenu extends ButtonMenu {
	
    public RunnerMenu(GameMap map, Handler handler) {
        super(map, handler);
        
        this.addButton("resources/runner1.png", "stub runner", UnitType.RUNNER);
    }
    
}
