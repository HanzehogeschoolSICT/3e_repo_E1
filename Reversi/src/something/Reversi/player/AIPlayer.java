package something.Reversi.player;

import something.Client.player.PlayerType;
import something.Reversi.ReversiBoard;
import something.Reversi.Tile;

import java.util.HashMap;
import java.util.Map;

public class AIPlayer implements PlayerType<ReversiBoard> {
    private Tile tile;

    public AIPlayer(Tile tile) {
        this.tile = tile;
    }

	@Override
	public int getMove(ReversiBoard board) {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<Integer, Integer> validMoves = board.getValidMoves(tile);
        int optimalMove = -1;
        int optimalScore = -1;
        for (Map.Entry<Integer, Integer> moveScoreEntry : validMoves.entrySet()) {
            if (optimalScore < moveScoreEntry.getValue()) {
                optimalMove = moveScoreEntry.getKey();
                optimalScore = moveScoreEntry.getValue();
            }
        }
        return optimalMove;
	}

}
