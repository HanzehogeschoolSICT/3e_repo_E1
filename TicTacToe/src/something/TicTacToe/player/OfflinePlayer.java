package something.TicTacToe.player;

import something.TicTacToe.Controller;

public class OfflinePlayer extends Player {

	public OfflinePlayer(PlayerType playerType) {
		super(playerType);
	}

	@Override
	public void makeMove(int index, Controller board) {
		if(hasTurn()) {
			setTurn(false, board);
		}
	}

}
