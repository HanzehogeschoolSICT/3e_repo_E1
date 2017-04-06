package something.TicTacToe.player;

import something.Client.player.PlayerType;
import something.TicTacToe.Gui.GameBoard;

public class HumanPlayer implements PlayerType<GameBoard> {

	@Override
	public int getMove(GameBoard board) {
		return 0;
	}
}
