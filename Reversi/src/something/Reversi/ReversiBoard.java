package something.Reversi;

public class ReversiBoard {
    private Tile[] board;

    public ReversiBoard(){
        this.board = new Tile[64];
        for (int i = 0; i<board.length; i++){
            board[i] = Tile.BLACK;
        }
    }

    public String toString(){
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

    public Tile makeTurn(int posOnBoard, int turn) {
        if (this.board[posOnBoard] == Tile.EMPTY) {
            Tile tile;
            if (turn % 2 == 0) {
                tile = Tile.BLACK;
                board[posOnBoard] = tile;
            } else {
                tile = Tile.WHITE;
                board[posOnBoard] = tile;
            }
            return tile;
        } else {
            return Tile.EMPTY;
        }
    }

    public Tile[] getBoard(){
        return this.board;
    }

    public void emptyBoard(){
        for (int i = 0; i < board.length; i++) {
            board[i] = Tile.EMPTY;
        }
    }
}



