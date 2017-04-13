package something.Core;

import something.Core.event.GameEvent;
import something.Core.event.GameEventListener;
import something.Core.debug.ConcurrentListDebug;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Listenable {
    private final List<WeakReference<GameEventListener>> eventListeners = ConcurrentListDebug.getProxy(new ArrayList<>());

    public void registerEventListener(GameEventListener eventListener) {
        synchronized (eventListeners) {
            eventListeners.add(new WeakReference<>(eventListener));
        }
    }

    protected void fireEvent(GameEvent gameEvent) {
        synchronized (eventListeners) {
            for (WeakReference<GameEventListener> reference : eventListeners) {
                GameEventListener gameEventListener = reference.get();
                if (gameEventListener != null) {
                    gameEventListener.handleEvent(gameEvent);
                }
            }
        }
    }

    protected void clearListeners() {
        eventListeners.clear();
    }
}
