package something.Core.player;

import something.Core.Board;
import something.Core.event.GameEvent;
import something.Core.event.events.common.MoveEvent;
import something.Core.event.events.player.YourTurnEvent;

public abstract class AIPlayer<GameType extends Board> extends Player<GameType> {
    @Override
    public void handleEvent(GameEvent event) {
        if (event instanceof YourTurnEvent) {
            fireEvent(new MoveEvent(null, decideMove()));
        }
    }

    public abstract int decideMove();
}
