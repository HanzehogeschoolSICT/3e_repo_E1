package something.Client.player;

import something.Client.Board;
import something.Client.Client;

public abstract class Player<GameType extends Board> extends Client<GameType> {
	
	private String username;
	private PlayerType<GameType> playerType;
	private boolean playersTurn = false;
		
	public Player(String username, PlayerType<GameType> playerType) {
		this.username = username;
		this.playerType = playerType;
	}
	
	public String getUsername() {
		return username;
	}
	
	public PlayerType<GameType> getPlayerType() {
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
