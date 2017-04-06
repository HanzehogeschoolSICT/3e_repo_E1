package something.TicTacToe.player;

import something.Client.player.PlayerType;
import something.TicTacToe.Gui.GameBoard;
import something.TicTacToe.Mark;

public class AIPlayer implements PlayerType<GameBoard> {
    
	boolean isCross = false;
	
	public void setIsCross(boolean isCross) {
		this.isCross = isCross;
	}
	
    @Override
    public int getMove(GameBoard b) {

        Mark[] board = b.getTicTacToeBoard().getBoard();
    	
        int index = -1;
        int value = isCross ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == Mark.EMPTY) {
                board[i] = isCross ? Mark.CROSS : Mark.NOUGHT;
                int newValue = miniMax(board, 0, !isCross);
                if ((isCross && newValue > value) || (!isCross && newValue < value)) {
                    value = newValue;
                    index = i;
                }
                board[i] = Mark.EMPTY;
            }
        }
        return index;
    }

    private static int miniMax(Mark[] board, int depth, boolean isCross) {
        int victory = checkVictory(board);
        if (victory == 0) {
            if (isFull(board)) {
                return 0;
            } else {
                if (isCross) {
                    int value = Integer.MIN_VALUE;
                    for (int i = 0; i < board.length; i++) {
                        if (board[i] == Mark.EMPTY) {
                            board[i] = Mark.CROSS;
                            value = Math.max(value, miniMax(board, depth + 1, false));
                            board[i] = Mark.EMPTY;
                        }
                    }
                    return value - depth;
                } else {
                    int value = Integer.MAX_VALUE;
                    for (int i = 0; i < board.length; i++) {
                        if (board[i] == Mark.EMPTY) {
                            board[i] = Mark.NOUGHT;
                            value = Math.min(value, miniMax(board, depth + 1, true));
                            board[i] = Mark.EMPTY;
                        }
                    }
                    return value + depth;
                }
            }
        }
        return victory + depth;
    }

    public static boolean isFull(Mark[] board) {
        for (Mark mark : board) if (mark == Mark.EMPTY) return false;
        return true;
    }

    public static int checkVictory(Mark[] board) {
        for (int i = 0; i < 3; i++) {
            if (board[i*3] == Mark.CROSS && board[i*3 + 1] == Mark.CROSS && board[i*3 + 2] == Mark.CROSS) return 10;
            if (board[i] == Mark.CROSS && board[i + 3] == Mark.CROSS && board[i + 6] == Mark.CROSS) return 10;

            if (board[i*3] == Mark.NOUGHT && board[i*3 + 1] == Mark.NOUGHT && board[i*3 + 2] == Mark.NOUGHT) return -10;
            if (board[i] == Mark.NOUGHT && board[i + 3] == Mark.NOUGHT && board[i + 6] == Mark.NOUGHT) return -10;
        }
        if (board[0] == Mark.CROSS && board[4] == Mark.CROSS && board[8] == Mark.CROSS) return 10;
        if (board[2] == Mark.CROSS && board[4] == Mark.CROSS && board[6] == Mark.CROSS) return 10;

        if (board[0] == Mark.NOUGHT && board[4] == Mark.NOUGHT && board[8] == Mark.NOUGHT) return -10;
        if (board[2] == Mark.NOUGHT && board[4] == Mark.NOUGHT && board[6] == Mark.NOUGHT) return -10;
        return 0;
    }
}
