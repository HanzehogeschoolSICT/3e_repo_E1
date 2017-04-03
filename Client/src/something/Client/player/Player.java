package something.Client.player;

import something.Client.Client;
import something.Client.player.PlayerType;

public abstract class Player extends Client {
	
	private String username;
	private PlayerType playerType;
	private boolean playersTurn = false;
	
	private Player opponent;
	
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
	
	public Player getOpponent() {
		return opponent;
	}
	
	public void setOpponent(Player opponent) {
		this.opponent = opponent;
		if(opponent.getOpponent() == null) {
			opponent.setOpponent(this);
		}
	}
	
	public void setHasTurn(boolean turn) {
		playersTurn = turn;
	}
	
	public abstract void makeMove(int index);
	
}
