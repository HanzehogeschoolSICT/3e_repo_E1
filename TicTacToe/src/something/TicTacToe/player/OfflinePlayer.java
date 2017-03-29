package something.TicTacToe.player;

public class OfflinePlayer extends Player {

	public OfflinePlayer(PlayerType playerType) {
		super(playerType);
	}

	@Override
	public void makeMove() {
		String move = getPlayerType().getMove();
		
		//TODO send move to board
	}

}
