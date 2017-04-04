package something.Client.player;

import something.Client.Client;
import something.Client.player.PlayerType;

public abstract class Player extends Client {
	
	private String username;
	private PlayerType playerType;
	private boolean playersTurn = false;
		
	public Player(String username, PlayerType playerType) {
		this.username = username;
		this.playerType = playerType;
	}
	
	public String getUsername() {
		return username;
	}
	
	public PlayerType getPlayerType() {
		return playerType;
	}
	
	public boolean hasTurn() {
		return playersTurn;
	}
	
	public void setHasTurn(boolean turn) {
		playersTurn = turn;
	}
	
	public abstract void makeMove(int index);
	
}
