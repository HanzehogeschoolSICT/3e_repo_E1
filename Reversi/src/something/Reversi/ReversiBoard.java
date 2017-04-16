package something.Reversi;

import something.Core.Board;
import something.Core.IllegalMoveException;
import something.Core.event.events.common.BoardUpdateEvent;

import java.util.Collection;
import java.util.HashMap;

public class ReversiBoard extends Board {
    private Tile[] board;

    public ReversiBoard() {
        this.board = new Tile[64];
        for (int i = 0; i < board.length; i++) {
            board[i] = Tile.EMPTY;
        }
        board[27] = Tile.WHITE;
        board[28] = Tile.BLACK;
        board[35] = Tile.BLACK;
        board[36] = Tile.WHITE;
    }

    public String toString() {
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

    public boolean makeMove(int move, boolean isPlayer1) throws IllegalMoveException {
        Tile tile = isPlayer1 ? Tile.BLACK : Tile.WHITE;
        if (this.board[move] == Tile.EMPTY) {
            if (isMoveValid(move, isPlayer1)) {
                board[move] = tile;
                turnTiles(move, tile);
            }
        }
        fireEvent(new BoardUpdateEvent());
        return !(getValidMoves(true).size() == 0 && getValidMoves(false).size() == 0);
    }


    @Override
    public Victor getVictor() throws IllegalStateException {
        int player1Count = 0;
        int player2Count = 0;
        for (Tile tile : board) {
            if (tile == Tile.BLACK) player1Count++;
            if (tile == Tile.WHITE) player2Count++;
        }
        if (player1Count > player2Count) return Victor.PLAYER1;
        if (player2Count > player1Count) return Victor.PLAYER2;
        if (player1Count == player2Count) return Victor.TIE;
        return Victor.NONE;
    }

    private void turnTiles(int index, Tile tile) {
        //check horizontal
        int startHor = (index - (index % 8));
        turnTilesInDirection(index, startHor + 7, 1, tile); //to the right
        turnTilesInDirection(index, startHor, -1, tile); //to the left
        //check vertical
        int startVer = (index % 8);
        turnTilesInDirection(index, 56 + startVer, 8, tile); //downwards
        turnTilesInDirection(index, startVer, -8, tile); //upwards
        //check diagonal 1
        int[] diag1Coordinates = determineDownwardsDiagonal(index);
        turnTilesInDirection(index, diag1Coordinates[0], -9, tile); //upwards
        turnTilesInDirection(index, diag1Coordinates[1], 9, tile); //downwards
        //check diagonal 2
        int[] diag2Coordinates = determineUpwardsDiagonal(index);
        turnTilesInDirection(index, diag2Coordinates[0], -7, tile); //upwards
        turnTilesInDirection(index, diag2Coordinates[1], 7, tile); //downwards
    }

    private void turnTilesInDirection(int index, int endIndex, int stepSize, Tile tile) {
        boolean otherStone = false;
        int iterator = index;
        while (iterator != endIndex) {
            iterator += stepSize;
            if (board[iterator] != tile && board[iterator] != Tile.EMPTY) {
                otherStone = true;
            } else if (board[iterator] == tile && otherStone) {
                flipStones(index, iterator, stepSize, tile);
                break;
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

    public int[] getScore() {
        int[] score = new int[2];
        for (Tile tile : board) {
            if (tile == Tile.BLACK) {
                score[0]++;
            }
            if (tile == Tile.WHITE) {
                score[1]++;
            }
        }
        return score;
    }


    public Tile[] getBoard() {
        return this.board;
    }

    public void emptyBoard() {
        for (int i = 0; i < board.length; i++) {
            board[i] = Tile.EMPTY;
        }
    }

    public HashMap<Integer, Integer> getValidMoves(boolean isPlayer1) {
        HashMap<Integer, Integer> validMoves = new HashMap<>();
        for (int i = 0; i < 64; i++) {
            int moveScore = getMoveFlips(i, isPlayer1);
            if (moveScore > 0) {
                validMoves.put(i, moveScore);
            }
        }
        System.out.println(validMoves);
        return validMoves;

    }

    @Override
    public boolean isMoveValid(int move, boolean firstPlayerAtTurn) {
        int test = getMoveFlips(move, firstPlayerAtTurn);
        System.out.println(test);
        return test > 0;
    }

    private int getMoveFlips(int index, boolean isPlayer1) {
        Tile tile = isPlayer1 ? Tile.BLACK : Tile.WHITE;
        if (board[index] != Tile.EMPTY) return -1;
        //check horizontal
        int startHor = (index - (index % 8));
        int horizontal = checkLineBoard(startHor, startHor + 8, 1, index, tile);
        if (horizontal > 0) {
            return horizontal;
        }
        //check vertical
        int startVer = (index % 8);
        int temp = startVer + 56;
        int vert = checkLineBoard(startVer, 56 + startVer, 8, index, tile);
        if (vert > 0) {
            return vert;
        }
        //check diagonal 1
        int[] diag1Coordinates = determineDownwardsDiagonal(index);
        int diag1 = checkLineBoard(diag1Coordinates[0], diag1Coordinates[1], 9, index, tile);
        if (diag1 > 0) {
            return diag1;
        }
        //check diagonal 2
        int[] diag2Coordinates = determineUpwardsDiagonal(index);
        int diag2 = checkLineBoard(diag2Coordinates[0], diag2Coordinates[1], 7, index, tile);
        if (diag2 > 0) {
            return diag2;
        }
        return -1;
    }

    private int[] determineDownwardsDiagonal(int index) {
        int[] temp = {Integer.MAX_VALUE, Integer.MAX_VALUE};
        int startIndex = index;
        while (temp[0] == Integer.MAX_VALUE) {
            if (startIndex < 0) {
                temp[0] = index;
            } else if (startIndex < 8) {
                temp[0] = startIndex;
            } else if (startIndex % 8 == 0) {
                temp[0] = startIndex;
            }
            startIndex = startIndex - 9;
        }
        startIndex = index;
        while (temp[1] == Integer.MAX_VALUE) {
            if (startIndex > 63) {
                temp[1] = index;
            } else if (startIndex > 56) {
                temp[1] = startIndex;
            } else if ((startIndex - 7) % 8 == 0) {
                temp[1] = startIndex;
            }
            startIndex = startIndex + 9;
        }
        return temp;
    }

    private int[] determineUpwardsDiagonal(int index) {
        int[] temp = {Integer.MAX_VALUE, Integer.MAX_VALUE};
        int startIndex = index;
        while (temp[0] == Integer.MAX_VALUE) {
            if (startIndex < 0) {
                temp[0] = index;
            } else if (startIndex < 8) {
                temp[0] = startIndex;
            } else if ((startIndex - 7) % 8 == 0) {
                temp[0] = startIndex;
            }
            startIndex = startIndex - 7;
        }
        startIndex = index;
        while (temp[1] == Integer.MAX_VALUE) {
            if (startIndex > 63) {
                temp[1] = index;
            } else if (startIndex > 56) {
                temp[1] = startIndex;
            } else if (startIndex % 8 == 0) {
                temp[1] = startIndex;
            }
            startIndex = startIndex + 7;
        }
        return temp;
    }


    private int checkLineBoard(int startIndex, int endIndex, int stepSize, int index, Tile tile) {
        boolean ownStone = false;
        boolean otherStone = false;
        int step = 0;
        for (int i = startIndex; i < endIndex; i += stepSize) {
            if (index == i) {
                ownStone = false;
                otherStone = false;
                step = 0;
            } else if (board[i] == tile) {
                if (otherStone && i > index) {
                    return step;
                }
                ownStone = true;
                if (otherStone && i < index) {
                    otherStone = false;
                    step = 0;
                }
            } else if (board[i] != tile && board[i] != Tile.EMPTY) {
                step++;
                if (ownStone && i == index - stepSize && i < index) {
                    return step;
                }
                otherStone = true;
                if (i > index && ownStone) {
                    return -1;
                }
            } else if (board[i] == Tile.EMPTY && i > index) {
                return -1;
            } else if (board[i] == Tile.EMPTY) {
                step = 0; ownStone = false; otherStone = false;
            }

        }
        return -1;
    }
}



