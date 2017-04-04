package something.Reversi.player;

/**
 * Created by samikroon on 4/4/17.
 */
public class IllegalMoveException extends Exception {

    public IllegalMoveException(){}

    public IllegalMoveException(String message) {
        super(message);
    }

    public IllegalMoveException(Throwable cause) {
        super(cause);
    }

    public IllegalMoveException(String message, Throwable cause) {
        super(message, cause);
    }
}
