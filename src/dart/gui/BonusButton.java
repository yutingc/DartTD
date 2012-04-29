package dart.gui;

import javax.swing.ImageIcon;
import dart.network.*;

@SuppressWarnings("serial")
public class BonusButton extends InfoButton {

	public BonusButton(ImageIcon image, String toolTip, GameMap map, Handler handler) {
		
		super(image, toolTip, map, handler);

	}
	
	public void mouseClickAction(GameMap map, Handler handler) {
		
		// create and send package to host
		
	}
}
