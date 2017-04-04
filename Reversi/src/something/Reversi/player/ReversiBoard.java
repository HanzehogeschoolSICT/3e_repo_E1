package something.Reversi.player;

import java.util.ArrayList;

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

    public boolean makeTurn(int posOnBoard, int turn) throws IllegalMoveException {
        if (this.board[posOnBoard] == Tile.EMPTY) {
            Tile tile;
            if (turn % 2 == 0) { //TODO: koppelen aan de client/server in plaats van een turn die steeds een opgehoogd wordt;
                tile = Tile.BLACK;
                board[posOnBoard] = tile;
            } else {
                tile = Tile.WHITE;
                board[posOnBoard] = tile;
            }
            return true;
        } else {
            if (getValidMoves(Tile.BLACK).size() == 0 && getValidMoves(Tile.WHITE).size() == 0) {
                return false;
            }
            throw new IllegalMoveException("Non-valid move");
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

    public ArrayList getValidMoves(Tile tile) {
        ArrayList<Integer> validMoves = new ArrayList<>();
        for (int i = 0; i < 64; i++) {
            if (checkValidityMove(i, tile)) {
                validMoves.add(i);
            }
        }
        return validMoves;
    }


    public boolean checkValidityMove (int index, Tile tile) {
        if (board[index] != Tile.EMPTY) {return false;}
        if (tile == Tile.EMPTY) {throw new IllegalArgumentException("Illegal tile");}
        //check horizontal
        int startHor = (index - (index % 8));
        if(checkLineBoard(startHor, startHor+8, 1, index, tile )) {return true;}
        //check vertical
        int startVer = (index%8);
        if(checkLineBoard(startVer, 56+startVer, 8, index, tile )) {return true;}
        //check diagonal 1
        int[] diag1Coordinates = determineDownwardsDiagonal(index);
        if (checkLineBoard(diag1Coordinates[0], diag1Coordinates[1], 9, index, tile)){return true;}
        //check diagonal 2
        int[] diag2Coordinates = determineUpwardsDiagonal(index);
        if (checkLineBoard(diag2Coordinates[0], diag2Coordinates[1], 7, index, tile)){return true;}

        return false;
    }

    private int[] determineDownwardsDiagonal(int index) {
        int[] temp = {Integer.MAX_VALUE, Integer.MAX_VALUE};
        int startIndex = index;
        while (temp[0] == Integer.MAX_VALUE) {
            startIndex = startIndex-9;
            if (startIndex < 0) {temp[0] = index;}
            else if (startIndex < 8) {temp[0] = startIndex;}
            else if (startIndex % 8 == 0) {temp[0] = startIndex;}
        }
        while (temp[1] == Integer.MAX_VALUE) {
            startIndex = startIndex+9;
            if (startIndex > 63) {temp[1] = index;}
            else if (startIndex > 56) {temp[1] = startIndex;}
            else if ((startIndex-7)%8 == 0) {temp[1] = startIndex;}
        }
        return temp;
    }

    private int[] determineUpwardsDiagonal(int index) {
        int[] temp = {Integer.MAX_VALUE, Integer.MAX_VALUE};
        int startIndex = index;
        while (temp[0] == Integer.MAX_VALUE) {
            startIndex = startIndex-7;
            if (startIndex < 0) {temp[0] = index;}
            else if (startIndex < 8) {temp[0] = startIndex;}
            else if ((startIndex-7)% 8 == 0) {temp[0] = startIndex;}
        }
        while (temp[1] == Integer.MAX_VALUE) {
            startIndex = startIndex+7;
            if (startIndex > 63) {temp[1] = index;}
            else if (startIndex > 56) {temp[1] = startIndex;}
            else if (startIndex%8 == 0) {temp[1] = startIndex;}
        }
        return temp;
    }


    private boolean checkLineBoard(int startIndex, int endIndex, int stepSize, int index, Tile tile) {
        boolean ownStone = false; boolean otherStone = false;
        for (int i = startIndex; i < endIndex; i+=stepSize) {
            if (index == i) {
                ownStone = false; otherStone = false;
            } else if (board[i] == tile) {
                if (otherStone && i > index) {return true;}
                ownStone = true;
                if (otherStone && i < index) {otherStone = false;}
            } else if (board[i] != tile && board[i] != Tile.EMPTY) {
                if (ownStone && i == index-stepSize && i < index) {return true;}
                otherStone = true;
                if (i > index && ownStone) {return false;}
            }
        }
        return false;
    }


}



