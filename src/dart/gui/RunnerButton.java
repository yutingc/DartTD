package dart.gui;

import javax.swing.ImageIcon;
import dart.network.Handler;

import dart.Consts;

@SuppressWarnings("serial")
public class RunnerButton extends InfoButton {

	public RunnerButton(ImageIcon image, String toolTip, GameMap map, Handler handler) {
		super(image, toolTip, map, handler);
		this.setFocusable(false);
	}
	
	public void mouseClickAction(GameMap map, Handler handler) {
		
		
//		map.sendRunner(new Runner());
		
		// otherwise, modify the packet to request a runner
		handler.sendRunner(Consts.RunnerType.RUNNER1);
		map.requestFocus(true);
	}
	
}
