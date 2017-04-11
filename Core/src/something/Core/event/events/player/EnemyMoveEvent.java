package something.Core.event.events.player;

import something.Core.event.GameEvent;

public class EnemyMoveEvent implements GameEvent {
    public final int move;
    public EnemyMoveEvent(int move) {
        this.move = move;
    }
}
