package something.Client.event;

@FunctionalInterface
public interface GameEventListener {
    void handleEvent(GameEvent event);
}
