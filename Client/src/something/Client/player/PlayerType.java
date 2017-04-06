package something.Client.player;

import something.Client.Board;

public interface PlayerType<GameState extends Board> {
	int getMove(GameState board);
}
