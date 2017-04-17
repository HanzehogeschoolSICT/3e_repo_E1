package something.Reversi.player;

import something.Core.player.AIPlayer;
import something.Reversi.ReversiBoard;

import java.util.HashMap;
import java.util.Random;

public class RandomReversiAIPlayer extends AIPlayer<ReversiBoard> {
    private static Random random = new Random();

    @Override
    public int decideMove() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HashMap<Integer, Integer> validMoves = board.getValidMoves(this.isPlayer1);
        Integer[] moveArray = validMoves.keySet().toArray(new Integer[validMoves.size()]);
        if (moveArray.length > 0) {
            return moveArray[random.nextInt(moveArray.length)];
        } else {
            return -1;
        }
    }

    @Override
    protected void reset() {

    }
}
