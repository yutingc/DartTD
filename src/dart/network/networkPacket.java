package dart.network;

import dart.Consts.*;

public class networkPacket {
	private int _playerIndex;
	private long _currentTime; // current time of each player
	private int _x,_y;
	private AttrType _towerUpgrade; 
	private TowerType _towerPurchase;
	private RunnerType _sendRunner;
	private boolean _attribute1Change,_attribute2Change,_attribute3Change;
	
	public networkPacket()
	{
		//for initialization, set some unreasonable number
		_playerIndex = -1;
		_currentTime = -1;
		_x = -500;
		_y = -500;
		_towerUpgrade = AttrType.NONE;
		_towerPurchase = TowerType.NONE;
		_sendRunner = RunnerType.NONE; 
//		_attribute1Change = false;
//		_attribute2Change = false;
//		_attribute3Change = false;
		
	}
	public void setPlayerIndex(int playerIndex)
	{
		_playerIndex = playerIndex;
	
	}

	public int getPlayerIndex()
	{
		return _playerIndex;
	}
	
	public void setCurrentTime(long currentTime)
	{
		_currentTime = currentTime; 
	}
	public long getCurrentTime()
	{
		return _currentTime;
	}

	public void setX(int x)
	{
		_x = x;
	}

	public int getX()
	{
		return _x;
	}

	public void setY(int y)
	{
		_y = y;
	}

	public int getY()
	{
		return _y;
	}
	public void setTowerUpgrade(AttrType towerUpgrade)
	{
		_towerUpgrade = towerUpgrade;
	}
	public AttrType getTowerUpgrade()
	{
		return _towerUpgrade;
	}
	public void setTowerPurchase(TowerType towerPurchase)
	{
		_towerPurchase = towerPurchase;
	}
	public TowerType getTowerPurchase()
	{
		return _towerPurchase;
	}
	
	
	public void setAttribute1Change(boolean attribute1Change)
	{
		_attribute1Change = attribute1Change;
	}
	public boolean getAttribute1Change()
	{
		return _attribute1Change;
	}
	public void setAttribute2Change(boolean attribute2Change)
	{
		_attribute2Change = attribute2Change;
	}
	public boolean getAttribute2Change()
	{
		return _attribute2Change;
	}
	public void setAttribute3Change(boolean attribute3Change)
	{
		_attribute3Change = attribute3Change;
	}
	public boolean getAttribute3Change()
	{
		return _attribute3Change;
	}
	public void setSendRunner(RunnerType sendRunner)
	{
		 _sendRunner = sendRunner;
	}
	public RunnerType getSendRunner()
	{
		return _sendRunner;
	}
	public void setConstants()
	{
		
	}
}

