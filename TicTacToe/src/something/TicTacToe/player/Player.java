package something.TicTacToe.player;

public abstract class Player {
	
	private PlayerType playerType;
	private boolean playersTurn = false;
	
	public Player(PlayerType playerType) {
		this.playerType = playerType;
	}
	
	public PlayerType getPlayerType() {
		return playerType;
	}
	
	public boolean hasTurn() {
		return playersTurn;
	}
	
	public void setTurn(boolean turn) {
		playersTurn = turn;
	}
	
	public abstract void makeMove(int index);
	
}
