package something.Core.event.events.game;

import something.Core.event.GameEvent;

import java.util.Optional;

public class GameFinishedEvent implements GameEvent {
    private Optional<Boolean> victor;

    public GameFinishedEvent(Optional<Boolean> victor) {
        this.victor = victor;
    }

    public Optional<Boolean> getVictor() {
        return victor;
    }
}
