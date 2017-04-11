package something.Reversi.player;

import something.Core.player.AIPlayer;
import something.Reversi.ReversiBoard;
import something.Reversi.Tile;

import java.util.HashMap;
import java.util.Map;

public class ReversiAIPlayer extends AIPlayer<ReversiBoard> {
    private final ReversiBoard board;
    private Tile tile;

    public ReversiAIPlayer(ReversiBoard board, Tile tile) {
        super(board);
        this.board = board;
        this.tile = tile;
    }

    @Override
    public int decideMove() {
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
