package something.Core;

import something.Core.event.GameEvent;
import something.Core.event.GameEventListener;

import java.util.LinkedList;

public class Listenable {
    private LinkedList<GameEventListener> eventListeners = new LinkedList<>();

    public void registerEventListener(GameEventListener eventListener) {
        eventListeners.add(eventListener);
    }

    protected void fireEvent(GameEvent gameEvent) {
        for (GameEventListener eventListener : eventListeners) eventListener.handleEvent(gameEvent);
    }
}
