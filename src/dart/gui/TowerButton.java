package dart.gui;

import javax.swing.ImageIcon;

import dart.Consts;
import dart.ImageManager;
import dart.network.Handler;


@SuppressWarnings({ "serial"})
public class TowerButton extends InfoButton {

	Consts.TowerType _type;
	
	public TowerButton(Consts.TowerType tt, String toolTip, GameMap map, Handler handler) {
		super(new ImageIcon(ImageManager.getTowerImage(tt)), toolTip, map, handler);
		_type = tt;
	}
	
	public void mouseClickAction(GameMap map, Handler handler) {
        
        map.setTowerHeld(Consts.TowerType.SLOWTOWER);
        map.requestFocus(true);
	}
	
}
