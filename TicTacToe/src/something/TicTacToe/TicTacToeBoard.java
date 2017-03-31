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

	/*
	 * public boolean hasWon(String player)
  {
    int number = getPlayerType(player).ordinal();
    if ((this.grid[0][0] == number) && (this.grid[0][1] == number) && (this.grid[0][2] == number)) {
      return true;
    }
    if ((this.grid[1][0] == number) && (this.grid[1][1] == number) && (this.grid[1][2] == number)) {
      return true;
    }
    if ((this.grid[2][0] == number) && (this.grid[2][1] == number) && (this.grid[2][2] == number)) {
      return true;
    }
    if ((this.grid[0][0] == number) && (this.grid[1][0] == number) && (this.grid[2][0] == number)) {
      return true;
    }
    if ((this.grid[0][1] == number) && (this.grid[1][1] == number) && (this.grid[2][1] == number)) {
      return true;
    }
    if ((this.grid[0][2] == number) && (this.grid[1][2] == number) && (this.grid[2][2] == number)) {
      return true;
    }
    if ((this.grid[0][0] == number) && (this.grid[1][1] == number) && (this.grid[2][2] == number)) {
      return true;
    }
    if ((this.grid[2][0] == number) && (this.grid[1][1] == number) && (this.grid[0][2] == number)) {
      return true;
    }
    return false;
  }*/
}
