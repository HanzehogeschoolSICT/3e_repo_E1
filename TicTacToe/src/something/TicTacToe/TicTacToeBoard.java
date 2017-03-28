package something.TicTacToe;

public class TicTacToeBoard {
    private Mark[] board;

    public TicTacToeBoard() {
        this.board = new Mark[9];
        for (int i = 0; i < board.length; i++) board[i] = Mark.EMPTY;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            builder.append(board[i].toString());
            if (i%3 == 2) {
                builder.append('\n');
            } else {
                builder.append('|');
            }
        }
        return builder.toString();
    }

    enum Mark {
        EMPTY(" "),
        NOUGHT("O"),
        CROSS("X");

        private String str;

        Mark(String str) {
            this.str = str;
        }

        @Override
        public String toString() {
            return str;
        }
    }
}
