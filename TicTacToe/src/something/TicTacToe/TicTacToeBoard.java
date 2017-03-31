package something.TicTacToe;

public class TicTacToeBoard {
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

	public Mark makeTurn(int posOnBoard, int turn) {
		if (this.board[posOnBoard] == Mark.EMPTY) {
			Mark mark;
			if (turn % 2 == 0) {
				mark = Mark.NOUGHT;
				board[posOnBoard] = mark;
			} else {
				mark = Mark.CROSS;
				board[posOnBoard] = mark;
			}
			return mark;
		} else {
			return Mark.EMPTY;
		}

	}

	public Mark[] getBoard() {
		return this.board;
	}

	public void emptyBoard() {
		for (int i = 0; i < board.length; i++) {
			board[i] = Mark.EMPTY;
		}
	}
}
