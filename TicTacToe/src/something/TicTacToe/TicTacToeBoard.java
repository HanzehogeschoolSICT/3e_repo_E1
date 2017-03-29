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

    public Mark testMakeTurn(int posOnBoard, int turn){
     if(this.board[posOnBoard] == Mark.EMPTY){
         Mark mark;
         if(turn%2==0){
             mark = Mark.NOUGHT;
             board[posOnBoard] = mark;
         } else {
             mark = Mark.CROSS;
             board[posOnBoard] = mark;
         }
         return mark;
     }  else {
         System.out.println("ja dit kan niet hoor");
         return Mark.EMPTY;
     }

    }

    public Mark[] getBoard(){
        return this.board;
    }


    public boolean makeTurn(double x, double y, int turn, int posOnBoard){
        if(this.board[posOnBoard] == Mark.EMPTY) {
            if (x < 150.0 && y < 150.0) {
                changeBoard(0, turn);
            }
            if (150.0 < x && x < 300.0 && y < 150.0) {
                changeBoard(1, turn);
            }
            if (300.0 < x && x < 450.0 && y < 150.0) {
                changeBoard(2, turn);
            }
            if (x < 150.0 && 150.0 < y && y < 300.0) {
                changeBoard(3, turn);
            }
            if (150.0 < x && x < 300.0 && 150.0 < y && y < 300.0) {
                changeBoard(4, turn);
            }
            if (300.0 < x && x < 450.0 && 150.0 < y && y < 300.0) {
                changeBoard(5, turn);
            }
            if (x < 150.0 && 300.0 < y && y < 450.0) {
                changeBoard(6, turn);
            }
            if (150.0 < x && x < 300.0 && 300.0 < y && y < 450.0) {
                changeBoard(7, turn);
            }
            if (300.0 < x && x < 450.0 && 300.0 < y && y < 450.0) {
                changeBoard(8, turn);
            }
            return true;
        } else {
            System.out.println("NEEEEEEE");
            return false;
        }
    }

    public void emptyBoard(){
        for(int i=0; i<board.length; i++){
            board[i] = Mark.EMPTY;
        }
    }

    private void changeBoard(int position, int turn){
        if (turn==0) {
            board[position] = Mark.NOUGHT;
        } else {
            board[position] = Mark.CROSS;
        }
    }

}
