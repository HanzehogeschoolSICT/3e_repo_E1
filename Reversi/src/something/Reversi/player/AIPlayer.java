package something.Reversi.player;

import something.Client.Board;
import something.Client.player.PlayerType;
import something.Reversi.ReversiBoard;

public class AIPlayer implements PlayerType {

	@Override
	public int getMove(Board b) {
		ReversiBoard board = (ReversiBoard) b;
		
		return 0;
	}

}
