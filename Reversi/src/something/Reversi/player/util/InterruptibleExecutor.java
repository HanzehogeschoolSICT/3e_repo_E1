package something.Reversi.player.util;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class InterruptibleExecutor {
    private ConcurrentLinkedDeque<Runnable> runnableQueue;
    private Thread[] threads;
    private boolean spinLock;

    public InterruptibleExecutor(int poolSize) {
        runnableQueue = new ConcurrentLinkedDeque<>();
        threads = new Thread[poolSize];
        spinLock = false;
        for (int i = 0; i < poolSize; i++) {
            (threads[i] = new Thread(() -> {
                try {
                    while (true) {
                        if (!spinLock) {
                            Runnable runnable = runnableQueue.pollFirst();
                            if (runnable != null) {
                                runnable.run();
                            } else {
                                Thread.sleep(100);
                            }
                        } else {
                            Thread.sleep(100);
                        }
                    }
                } catch (InterruptedException e) {
                }
            })).start();
        }
    }

    public synchronized void submit(Runnable runnable) {
        runnableQueue.offerLast(runnable);
    }

    public synchronized ArrayList<Runnable> stop() throws IllegalStateException {
        if (threads != null) {
            if (!spinLock) {
                ArrayList<Runnable> spares = new ArrayList<>();
                spinLock = true;
                while (true) {
                    Runnable runnable = runnableQueue.pollFirst();
                    if (runnable == null) break;
                    spares.add(runnable);
                }
                return spares;
            } else {
                throw new IllegalStateException("Already stopped!");
            }
        } else {
            throw new IllegalStateException("Already shut down!");
        }
    }

    public synchronized void start() throws IllegalStateException {
        if (threads != null) {
            if (spinLock) {
                spinLock = false;
            } else {
                throw new IllegalStateException("Already running!");
            }
        } else {
            throw new IllegalStateException("Already shut down!");
        }
    }

    public synchronized void shutdown() {
        if (!spinLock) stop();
        for (Thread thread : threads) thread.interrupt();
        threads = null;
    }

    public boolean isRunning() {
        return !spinLock;
    }
}
