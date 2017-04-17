package something.Reversi.player;

import something.Core.IllegalMoveException;
import something.Core.player.AIPlayer;
import something.Reversi.ReversiBoard;

import java.util.HashMap;

public class ScoreGreedyReversiAIPlayer extends AIPlayer<ReversiBoard> {
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
        try {
        for (Integer move : validMoves.keySet()) {
            ReversiBoard clone = board.clone();
            clone.makeMove(move, isPlayer1);
            int score = clone.getScore(isPlayer1);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
        return bestMove;
    }

    @Override
    protected void reset() {}
}
