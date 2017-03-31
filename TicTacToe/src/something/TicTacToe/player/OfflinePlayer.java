package something.TicTacToe.player;

import something.TicTacToe.Gui.GameBoard;

public class OfflinePlayer extends Player {

	public OfflinePlayer(PlayerType playerType) {
		super(playerType);
	}

	@Override
	public void makeMove(int index, GameBoard board) {
		if(hasTurn()) {
			setTurn(false, board);
		}
	}

}
