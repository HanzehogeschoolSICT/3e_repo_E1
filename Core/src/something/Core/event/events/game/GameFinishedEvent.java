package something.Core.event.events.game;

import something.Core.Board;
import something.Core.event.GameEvent;

public class GameFinishedEvent implements GameEvent {
    private Board.Victor victor;

    public GameFinishedEvent(Board.Victor victor) {
        this.victor = victor;
    }

    public Board.Victor getVictor() {
        return victor;
    }
}
