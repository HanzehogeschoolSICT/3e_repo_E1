package something.Reversi;

import java.util.ArrayList;

import something.Client.Board;

public class ReversiBoard extends Board {
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
            } else {
                tile = Tile.WHITE;
            }
            if (checkValidityMove(posOnBoard, tile)) {
                board[posOnBoard] = tile;
                turnTiles(posOnBoard, tile);
                return true;
            }
        } /*else {
            if (getValidMoves(Tile.BLACK).size() == 0 && getValidMoves(Tile.WHITE).size() == 0) {
                return false;
            }
            //throw new IllegalMoveException("Non-valid move");
            return false;
        }*/
        return false;
    }

    private void turnTiles(int index, Tile tile) {
        //check horizontal
        int startHor = (index - (index % 8));
        turnTilesInDirection(index, startHor+7, 1, tile ); //to the right
        turnTilesInDirection(index, startHor, -1, tile); //to the left
        //check vertical
        int startVer = (index%8);
        turnTilesInDirection(index, 56+startVer, 8, tile ); //downwards
        turnTilesInDirection(index, startVer, -8, tile ); //upwards
        //check diagonal 1
        int[] diag1Coordinates = determineDownwardsDiagonal(index);
        turnTilesInDirection(index, diag1Coordinates[0], -9, tile); //upwards
        turnTilesInDirection(index, diag1Coordinates[1], 9, tile); //downwards
        //check diagonal 2
        int[] diag2Coordinates = determineUpwardsDiagonal(index);
        turnTilesInDirection(index, diag2Coordinates[0], -7, tile); //upwards
        turnTilesInDirection(index, diag2Coordinates[1], 7, tile); //downwards
    }

    private void turnTilesInDirection (int index, int endIndex, int stepSize, Tile tile) {
        boolean otherStone = false;
        int iterator = index;
        while (iterator != endIndex) {
            iterator += stepSize;
            if (board[iterator] != tile && board[iterator] != Tile.EMPTY) {
                otherStone = true;
            } else if (board[iterator] == tile && otherStone) {
                flipStones(index, iterator, stepSize, tile);
            } else {
                break;
            }

        }
    }

    private void flipStones(int index, int endIndex, int stepSize, Tile tile) {
        int iterator = index;
        while (iterator != endIndex) {
            iterator += stepSize;
            board[iterator] = tile;
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
        if(checkLineBoard(startHor, startHor+7, 1, index, tile )) {return true;}
        //check vertical
        int startVer = (index%8);
        int temp = startVer+56;
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
        startIndex = index;
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
        startIndex = index;
        while (temp[1] == Integer.MAX_VALUE) {
            startIndex = startIndex+7;
            if (startIndex > 63) {temp[1] = index;}
            else if (startIndex > 56) {temp[1] = startIndex;}
            else if (startIndex%8 == 0) {temp[1] = startIndex;}
        }
        return temp;
    }


    private boolean checkLineBoard(int startIndex, int endIndex, int stepSize, int index, Tile tile) {
        boolean ownStone = false; boolean otherStone = false; boolean afterIndex = false;
        for (int i = startIndex; i != endIndex; i+=stepSize) {
            if (index == i) {
                ownStone = false; otherStone = false; afterIndex = true;
            } else if (board[i] == tile) {
                if (otherStone && i > index) {return true;}
                ownStone = true;
                if (otherStone && i < index) {otherStone = false;}
            } else if (board[i] != tile && board[i] != Tile.EMPTY) {
                if (ownStone && i == index-stepSize && i < index) {return true;}
                otherStone = true;
                if (i > index && ownStone) {return false;}
            } else if (board[i] == Tile.EMPTY && afterIndex) {
                return false;
            }
        }
        return false;
    }

    /*
    public static void main(String[] args) {
        ReversiBoard test = new ReversiBoard();
        try {
            test.makeTurn(9, 2);
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
        int counter = 0;
        for (Tile t : test.board) {
            if (counter % 8 == 0) {
                System.out.print("\n");
            }
            if (t == Tile.WHITE) {
                System.out.print("W ");
            }
            if (t == Tile.BLACK) {
                System.out.print("B ");
            }
            if (t == Tile.EMPTY) {
                System.out.print("E ");
            }

            counter++;

        }
    }
    */

}



