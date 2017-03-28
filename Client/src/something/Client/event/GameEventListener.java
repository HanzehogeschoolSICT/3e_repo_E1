package something.Client.event;

@FunctionalInterface
public interface GameEventListener {
    public void handleEvent(GameEvent event);
}
