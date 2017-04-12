package something.Core;

import java.util.Optional;

public abstract class Board extends Listenable {
    /**
     *
     * @param move
     * @param firstPlayerAtTurn
     * @return
     */
    public abstract boolean isMoveValid(int move, boolean firstPlayerAtTurn);

    /**
     * Executes a move on this board
     * @param move Move to execute
     * @param firstPlayerAtTurn True if the player making the move is player 1 (Moves first), false otherwise
     * @return True if the game has not yet ended
     * @throws IllegalMoveException If the made move is invalid
     */
    public abstract boolean makeMove(int move, boolean firstPlayerAtTurn) throws IllegalMoveException;

    /**
     * Returns the victor of this match, or throws an IllegalStateException if no-one has won yet.
     * @return Optional of the victor of this match: True if player 1 won. False if player 2 won. Null if the match was a tie
     * @throws IllegalStateException No-one has won yet.
     */
    public abstract Optional<Boolean> getVictor() throws IllegalStateException;
}
