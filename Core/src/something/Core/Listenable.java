package something.Core;

import something.Core.event.GameEvent;
import something.Core.event.GameEventListener;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;

public class Listenable {
    private LinkedList<WeakReference<GameEventListener>> eventListeners = new LinkedList<>();

    public void registerEventListener(GameEventListener eventListener) {
        eventListeners.add(new WeakReference<>(eventListener));
    }

    protected void fireEvent(GameEvent gameEvent) {
        for (Iterator<WeakReference<GameEventListener>> iterator = eventListeners.iterator(); iterator.hasNext(); ) {
            WeakReference<GameEventListener> reference = iterator.next();
            GameEventListener gameEventListener = reference.get();
            if (gameEventListener != null) {
                gameEventListener.handleEvent(gameEvent);
            } else {
                iterator.remove();
            }
        }
    }
}
