package something.Core.player;

import something.Core.Board;
import something.Core.event.GameEvent;
import something.Core.event.events.common.MoveEvent;
import something.Core.event.events.player.YourTurnEvent;


public class ManualPlayer<GameType extends Board> extends Player<GameType> {
    private boolean canMove = false;

    @Override
    protected void reset() {}

    @Override
    public void handleEvent(GameEvent event) {
        if (event instanceof YourTurnEvent) {
            canMove = true;
        }
    }

    public void makeMove(int move) {
        if (canMove) {
            if (board.isMoveValid(move, isPlayer1)) {
                fireEvent(new MoveEvent(null, move));
                canMove = false;
            } else {
                System.err.println("Invalid move!");
            }
        } else {
            System.err.println("Not your turn: " + isPlayer1);
        }
    }
}
