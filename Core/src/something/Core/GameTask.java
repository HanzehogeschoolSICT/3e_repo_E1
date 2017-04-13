package something.Core;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameTask {
    private static ExecutorService taskExecutor;

    public static void start() {
        if (taskExecutor != null) {
            throw new IllegalStateException("Executor not stopped yet!");
        }
        taskExecutor = Executors.newSingleThreadExecutor();
    }

    public static List<Runnable> stop() {
        if (taskExecutor == null) {
            throw new IllegalStateException("Executor already stopped!");
        }
        List<Runnable> runnables = taskExecutor.shutdownNow();
        taskExecutor = null;
        return runnables;
    }

    public static void submit(Runnable o) {
        if (taskExecutor != null) {
            taskExecutor.submit(o);
        } else {
            throw new IllegalStateException("Executor not started yet!");
        }
    }
}
