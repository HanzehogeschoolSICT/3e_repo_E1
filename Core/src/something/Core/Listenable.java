package something.Core;

import something.Core.event.GameEvent;
import something.Core.event.GameEventListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class Listenable {
    private List<WeakReference<GameEventListener>> eventListeners = new ArrayList<>();
    private List<WeakReference<GameEventListener>> removeList = new ArrayList<>();

    public synchronized void registerEventListener(GameEventListener eventListener) {
        eventListeners.add(new WeakReference<>(eventListener));
    }

    protected synchronized void fireEvent(GameEvent gameEvent) {
        for (WeakReference<GameEventListener> reference : eventListeners) {
            GameEventListener gameEventListener = reference.get();
            if (gameEventListener != null) {
                gameEventListener.handleEvent(gameEvent);
            } else {
                removeList.add(reference);
            }
        }
        eventListeners.removeAll(removeList);
        removeList.clear();
    }
}
