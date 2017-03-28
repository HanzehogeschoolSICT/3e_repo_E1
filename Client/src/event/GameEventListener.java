package event;

@FunctionalInterface
public interface GameEventListener {
    public void handleEvent(GameEvent event);
}
