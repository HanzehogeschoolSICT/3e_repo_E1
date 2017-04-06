package something.Client.event;

@FunctionalInterface
public interface GameEventListener<GameType> {
    void handleEvent(GameEvent<GameType> event);
}
