package something.Reversi.player;

import something.Core.player.AIPlayer;
import something.Reversi.ReversiBoard;

import java.util.HashMap;
import java.util.Map;

public class TileGreedyReversiAIPlayer extends AIPlayer<ReversiBoard> {
    @Override
    public int decideMove() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<Integer, Integer> validMoves = board.getValidMoves(isPlayer1());
        int bestMove = -1;
        int bestScore = -1;
        for (Map.Entry<Integer, Integer> moveScoreEntry : validMoves.entrySet()) {
            if (bestScore < moveScoreEntry.getValue()) {
                bestMove = moveScoreEntry.getKey();
                bestScore = moveScoreEntry.getValue();
            }
        }
        return bestMove;
    }

    @Override
    protected void reset() {}
}
