package something.Reversi;

public class ReversiBoard {
    private Tile[] board;

    public ReversiBoard(){
        this.board = new Tile[64];
        for (int i = 0; i<board.length; i++){
            board[i] = Tile.EMPTY;
        }
        board[27]=Tile.WHITE;
        board[28]=Tile.BLACK;
        board[35]=Tile.BLACK;
        board[36]=Tile.WHITE;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            builder.append(board[i].toString());
            if (i % 8 == 7) {
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
            System.out.println("MAG NIET");
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



