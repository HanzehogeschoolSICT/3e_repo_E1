package something.Core;

public abstract class Board extends Listenable {

    public abstract boolean isMoveValid(int move, boolean firstPlayerAtTurn);

    /**
     * Executes a move on this board
     * @param move Move to execute
     * @param firstPlayerAtTurn True if the player making the move is player 1 (Moves first), false otherwise
     * @return True if the game has not yet ended
     * @throws IllegalMoveException If the made move is invalid
     */
    public abstract boolean makeMove(int move, boolean firstPlayerAtTurn) throws IllegalMoveException;

    public abstract Victor getVictor() throws IllegalStateException;

    public enum Victor {
        PLAYER1,
        PLAYER2,
        TIE,
        NONE
    }
}
