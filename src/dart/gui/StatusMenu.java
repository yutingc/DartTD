/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dart.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import dart.Consts.AttrType;
import dart.network.Handler;
import dart.tower.Tower;

/**
 *
 * @author Jens
 */
@SuppressWarnings("serial")
class StatusMenu extends JPanel{
	
	private Handler _handler;
	private Tower _tower;
	
//	private JLabel _damage;
//	private JLabel _special;
//	private JLabel _armor;
//	private JLabel _speed;
//	private JLabel _range;
	
	private UpgradeButton _damageUpg;
	private UpgradeButton _armorUpg;
	private UpgradeButton _speedUpg;
	private UpgradeButton _rangeUpg;
    
    public StatusMenu(Handler handler) {
        super();
        
        _tower = null;
        _handler = handler;
        
        _damageUpg = new UpgradeButton(AttrType.DAMAGE);
        _damageUpg.setVisible(false);
        _armorUpg = new UpgradeButton(AttrType.ARMOR);
        _armorUpg.setVisible(false);
        _speedUpg = new UpgradeButton(AttrType.SPEED);
        _speedUpg.setVisible(false);
        _rangeUpg = new UpgradeButton(AttrType.RANGE);
        _rangeUpg.setVisible(false);
        
//        _damage = new JLabel("Damage");
//        _special = new JLabel("Special");
//        _armor = new JLabel("Armor");
//        _speed = new JLabel("Speed");
//        _range = new JLabel("Range");
//        
//        _damage.setLocation(20,20);
//        _armor.setLocation(20,40);
//        _speed.setLocation(20,60);
//        _range.setLocation(20,80);
//        _special.setLocation(20,100);
//        
//        add(_damage);
//        add(_special);
//        add(_armor);
//        add(_speed);
//        add(_range);
        
        add(_damageUpg);
        add(_armorUpg);
        add(_speedUpg);
        add(_rangeUpg);
        
        this.setLayout(null);
        this.setBackground(Color.GRAY);
        this.setVisible(true);
    }
    
    public void setTower(Tower t) {
    	_tower = t;
    }
    
    public boolean isTowerSelected() {
    	if(_tower == null)
    		return false;
    	else
    		return true;
    }

	public Tower getTower() {
		return _tower;
	}
	
	@Override
	public void paint(Graphics graphics) {
		super.paint(graphics);
		Graphics2D g = (Graphics2D) graphics;
		if(_tower != null) {
			g.drawString("Damage: " + _tower.getDamage(), 30, 30);
			g.drawString("Upgrade Cost: " + _tower.getDamageCost(), 150, 30);
			g.drawString("Armor: " + _tower.getArmor(), 30, 60);
			g.drawString("Upgrade Cost: " + _tower.getArmorCost(), 150, 60);
			g.drawString("Speed: " + _tower.getSpeed(), 30, 90);
			g.drawString("Upgrade Cost: " + _tower.getSpeedCost(), 150, 90);
			g.drawString("Range: " + _tower.getRange(), 30, 120);
			g.drawString("Upgrade Cost: " + _tower.getRangeCost(), 150, 120);
			_damageUpg.setLocation(300, 10);
	        _damageUpg.setVisible(true);
	        _armorUpg.setLocation(300, 40);
	        _armorUpg.setVisible(true);
	        _speedUpg.setLocation(300, 70);
	        _speedUpg.setVisible(true);
	        _rangeUpg.setLocation(300, 100);
	        _rangeUpg.setVisible(true);
//			g.drawString("Special: " + _tower.getSpecial(), 30, 110);
		} else {
	        _damageUpg.setVisible(false);
	        _armorUpg.setVisible(false);
	        _speedUpg.setVisible(false);
	        _rangeUpg.setVisible(false);
		}
	}
	
	private class UpgradeButton extends JButton {
		
		private AttrType _type;
		
		public UpgradeButton(AttrType at) {
			super(new ImageIcon("resources/arrow-up.gif"));
			
			this.setSize(30, 30);
			this.setFocusable(false);
			
			_type = at;
			addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					_handler.setUpgradeTower(_type, _tower.getX(), _tower.getY());
				}
			});
		}
		
		
	}
	
}
