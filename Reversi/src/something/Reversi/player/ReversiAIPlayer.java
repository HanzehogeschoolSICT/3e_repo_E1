package something.Reversi.player;

import something.Core.player.AIPlayer;
import something.Reversi.ReversiBoard;

import java.util.HashMap;
import java.util.Map;

public class ReversiAIPlayer extends AIPlayer<ReversiBoard> {
    @Override
    public int decideMove() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<Integer, Integer> validMoves = board.getValidMoves(isPlayer1());
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

    @Override
    protected void reset() {}
}
