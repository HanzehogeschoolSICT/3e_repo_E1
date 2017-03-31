package something.TicTacToe.player;

public class OfflinePlayer extends Player {

	public OfflinePlayer(PlayerType playerType) {
		super(playerType);
	}

	@Override
	public void makeMove(int index) {		
		//TODO send move to board
	}

}
