package something.Core.player;

import something.Core.Board;
import something.Core.Listenable;
import something.Core.event.GameEvent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public abstract class Player<GameType extends Board> extends Listenable {
    private BlockingQueue<GameEvent> eventQueue = new ArrayBlockingQueue<>(20);
    protected GameType board;
    protected boolean isPlayer1;
    private Thread eventThread;

    public void reset(GameType board, boolean isFirst) {
        this.isPlayer1 = isFirst;
        this.board = board;
        eventQueue.clear();
        if (eventThread != null) {
            eventThread.interrupt();
        }
        (eventThread = new Thread(() -> {
            while (true) {
                try {
                    handleEvent(eventQueue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break; // Exit immediately
                }
            }
        })).start();
        reset();
    }

    protected abstract void handleEvent(GameEvent event);

    protected abstract void reset();

    public void pushEvent(GameEvent event) {
        if (eventThread != null) {
            try {
                eventQueue.put(event);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalStateException("Events are interrupted!");
        }
    }

    public void interruptEvents() {
        if (eventThread != null) {
            eventThread.interrupt();
            eventThread = null;
        }
    }

    public boolean isPlayer1() {
        return isPlayer1;
    }

    public void setPlayer1(boolean player1) {
        isPlayer1 = player1;
    }
}
