package something.TicTacToe;

import something.Core.Board;
import something.Core.event.events.common.BoardUpdateEvent;

public class TicTacToeBoard extends Board {
	private Mark[] board;

	public TicTacToeBoard() {
		this.board = new Mark[9];
		for (int i = 0; i < board.length; i++)
			board[i] = Mark.EMPTY;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < board.length; i++) {
			builder.append(board[i].toString());
			if (i % 3 == 2) {
				builder.append('\n');
			} else {
				builder.append('|');
			}
		}
		return builder.toString();
	}

    @Override
    public boolean isMoveValid(int move, boolean firstPlayerAtTurn) {
        return board[move] == Mark.EMPTY;
    }

    public boolean makeMove(int posOnBoard, boolean turn) {
		if (this.board[posOnBoard] == Mark.EMPTY) {
			Mark mark;
            if (turn) {
                mark = Mark.CROSS;
                board[posOnBoard] = mark;
            } else {
                mark = Mark.NOUGHT;
                board[posOnBoard] = mark;
            }
        }
		fireEvent(new BoardUpdateEvent());
		return getVictor() != null;
	}

    @Override
    public Victor getVictor() {
        for (int i = 0; i < 3; i++) {
            if (board[i*3] == Mark.CROSS && board[i*3 + 1] == Mark.CROSS && board[i*3 + 2] == Mark.CROSS) return Victor.PLAYER1;
            if (board[i] == Mark.CROSS && board[i + 3] == Mark.CROSS && board[i + 6] == Mark.CROSS) return Victor.PLAYER1;

            if (board[i*3] == Mark.NOUGHT && board[i*3 + 1] == Mark.NOUGHT && board[i*3 + 2] == Mark.NOUGHT) return Victor.PLAYER2;
            if (board[i] == Mark.NOUGHT && board[i + 3] == Mark.NOUGHT && board[i + 6] == Mark.NOUGHT) return Victor.PLAYER2;
        }
        if (board[0] == Mark.CROSS && board[4] == Mark.CROSS && board[8] == Mark.CROSS) return Victor.PLAYER1;
        if (board[2] == Mark.CROSS && board[4] == Mark.CROSS && board[6] == Mark.CROSS) return Victor.PLAYER1;

        if (board[0] == Mark.NOUGHT && board[4] == Mark.NOUGHT && board[8] == Mark.NOUGHT) return Victor.PLAYER2;
        if (board[2] == Mark.NOUGHT && board[4] == Mark.NOUGHT && board[6] == Mark.NOUGHT) return Victor.PLAYER2;
        return isFull() ? Victor.TIE :  Victor.NONE;
    }

    public boolean isFull() {
        for (Mark mark : board) {
            if (mark == Mark.EMPTY) {
                return false;
            }
        }
        return true;
    }

	public Mark[] getMarks() {
		return this.board;
	}
}
