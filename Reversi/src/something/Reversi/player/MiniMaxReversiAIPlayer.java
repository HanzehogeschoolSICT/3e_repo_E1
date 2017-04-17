package something.Reversi.player;

import something.Core.IllegalMoveException;
import something.Core.player.AIPlayer;
import something.Reversi.ReversiBoard;
import something.Reversi.player.util.InterruptibleExecutor;

import java.util.*;
import java.util.concurrent.*;

public class MiniMaxReversiAIPlayer extends AIPlayer<ReversiBoard> {
    private static final int TOTAL_TIME = 9500;
    private final int THINKING_TIME;
    private InterruptibleExecutor buildTreeExecutor = new InterruptibleExecutor(2);
    private ExecutorService parseExecutor = Executors.newSingleThreadExecutor();

    public MiniMaxReversiAIPlayer(int thinking_time) {
        THINKING_TIME = thinking_time;
    }

    @Override
    public int decideMove() {
        if (!buildTreeExecutor.isRunning()) buildTreeExecutor.start();
        long startBuild = System.currentTimeMillis();
        RecursiveMap<Integer> moveMap = buildTree(buildTreeExecutor, board.clone(), isPlayer1);
        try {
            Thread.sleep(THINKING_TIME);
        } catch (InterruptedException ignored) {
        }
        buildTreeExecutor.stop();

        long spentTime = (System.currentTimeMillis() - startBuild);
        System.out.println("Tree building complete: " + spentTime);
        Future<Integer> future = parseExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                try {
                    long startParse = System.currentTimeMillis();
                    int bestMove = -1;
                    int bestScore = Integer.MIN_VALUE;
                    for (Map.Entry<Integer, RecursiveMap<Integer>> move : moveMap.entrySet()) {
                        int score = miniMax(0, move.getValue(), board.clone(), !isPlayer1);
                        if (score > bestScore) {
                            bestScore = score;
                            bestMove = move.getKey();
                        }
                    }
                    System.out.println("Tree parse complete: " + (System.currentTimeMillis() - startParse));
                    return bestMove;
                } catch (IllegalMoveException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        });

        try {
            System.out.println("Planning for: " + (TOTAL_TIME - spentTime));
            return future.get(TOTAL_TIME - spentTime, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | TimeoutException | ExecutionException e1) {
            future.cancel(true);
            HashMap<Integer, Integer> validMoves = board.getValidMoves(isPlayer1());
            int bestMove = -1;
            int bestScore = -1;
            for (Map.Entry<Integer, Integer> moveScoreEntry : validMoves.entrySet()) {
                if (bestScore < moveScoreEntry.getValue()) {
                    bestMove = moveScoreEntry.getKey();
                    bestScore = moveScoreEntry.getValue();
                }
            }
            return bestMove;
        }
    }

    private int miniMax(int depth, RecursiveMap<Integer> moveMap, ReversiBoard board, boolean isPlayer1) throws IllegalMoveException {
        //System.out.println("TEST: " + depth);
        int value = Integer.MIN_VALUE;
        for (Map.Entry<Integer, RecursiveMap<Integer>> move : moveMap.entrySet()) {
            ReversiBoard clone = board.clone();
            clone.makeMove(move.getKey(), isPlayer1);
            RecursiveMap<Integer> subMap = move.getValue();
            if (subMap == null || subMap.size() == 0) {
                value = Math.max(value, board.getScore(isPlayer1));
            } else {
                value = Math.max(value, miniMax(depth + 1, subMap, board, !isPlayer1));
            }
        }
        return value;
    }

    @Override
    protected void reset() {
        buildTreeExecutor.stop();
    }

    @Override
    protected void finalize() {
        buildTreeExecutor.shutdown();
        parseExecutor.shutdownNow();
    }

    private static RecursiveMap<Integer> buildTree(InterruptibleExecutor interruptibleExecutor, ReversiBoard board, boolean isPlayer1) {
        RecursiveMap<Integer> newMap = new RecursiveMap<>();
        Set<Integer> moves = board.getValidMoves(isPlayer1).keySet();
        for (Integer integer : moves) {
            newMap.put(integer, null);
            ReversiBoard clone = board.clone();
            try {
                clone.makeMove(integer, isPlayer1);
            } catch (IllegalMoveException e) {
                throw new RuntimeException("Invalid move while building tree, getValidMoves is broken!", e);
            }
            interruptibleExecutor.submit(() -> newMap.put(integer, buildTree(interruptibleExecutor, clone, !isPlayer1)));
        }
        return newMap;
    }

    private static class RecursiveMap<T> extends HashMap<T, RecursiveMap<T>> {
    }

    public static void main(String[] args) throws IllegalMoveException, InterruptedException, ExecutionException {
        InterruptibleExecutor interruptibleExecutor = new InterruptibleExecutor(1);
        buildTree(interruptibleExecutor, new ReversiBoard(), true);
        Thread.sleep(200);
        ArrayList<Runnable> stop = interruptibleExecutor.stop();
        System.out.println(stop.size());
        interruptibleExecutor.shutdown();
    }
}
