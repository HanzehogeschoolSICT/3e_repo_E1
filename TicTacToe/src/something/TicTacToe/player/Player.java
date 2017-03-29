package something.TicTacToe.player;

public abstract class Player {
	
	private PlayerType playerType;
	
	public Player(PlayerType playerType) {
		this.playerType = playerType;
	}
	
	public PlayerType getPlayerType() {
		return playerType;
	}
	
	public abstract void makeMove();
	
}
