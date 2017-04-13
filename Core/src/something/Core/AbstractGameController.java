package something.Core;

import something.Core.event.GameEvent;
import something.Core.event.GameEventListener;
import something.Core.event.events.common.MoveEvent;
import something.Core.event.events.game.*;
import something.Core.event.events.player.EnemyMoveEvent;
import something.Core.event.events.player.YourTurnEvent;
import something.Core.player.Player;

public class AbstractGameController<GameType extends Board> extends Listenable {
    private final GameType board;
    private final Player<GameType> player1;
    private final Player<GameType> player2;
    private boolean firstPlayerAtTurn;

    public AbstractGameController(GameType board, Player<GameType> player1, Player<GameType> player2) {
        this.board = board;
        this.player1 = player1;
        this.player2 = player2;
        this.firstPlayerAtTurn = true;
        player1.reset(board, true);
        player2.reset(board, false);

        player1.registerEventListener(new GameControllerEventListener(true));
        player2.registerEventListener(new GameControllerEventListener(false));

        fireEvent(new GameStartEvent());
    }

    public void start() {
        player1.pushEvent(new YourTurnEvent());
    }

    public void interrupt() {
        System.out.println("Interrupting!");
        player1.interruptEvents();
        player2.interruptEvents();
    }

    public class GameControllerEventListener implements GameEventListener {
        private boolean isPlayer1;

        public GameControllerEventListener(boolean isPlayer1) {
            this.isPlayer1 = isPlayer1;
        }

        public void handleEvent(GameEvent event) {
            if (event instanceof MoveEvent) {
                if (firstPlayerAtTurn == isPlayer1) {
                    if (board.isMoveValid(((MoveEvent) event).move, firstPlayerAtTurn)) {
                        try {
                            if (board.makeMove(((MoveEvent) event).move, firstPlayerAtTurn)) {
                                firstPlayerAtTurn = !firstPlayerAtTurn;
                                if (isPlayer1) {
                                    player2.pushEvent(new EnemyMoveEvent(((MoveEvent) event).move));
                                    player2.pushEvent(new YourTurnEvent());
                                } else {
                                    player1.pushEvent(new EnemyMoveEvent(((MoveEvent) event).move));
                                    player1.pushEvent(new YourTurnEvent());
                                }
                            } else {
                                Board.Victor victor = board.getVictor();
                                if (victor != Board.Victor.TIE) {
                                    boolean firstWon = victor == Board.Victor.PLAYER1;
                                    player1.pushEvent(firstWon ? new VictoryEvent() : new LossEvent());
                                    player2.pushEvent(firstWon ? new LossEvent() : new VictoryEvent());
                                } else {
                                    player1.pushEvent(new TieEvent());
                                    player2.pushEvent(new TieEvent());
                                }
                                fireEvent(new GameFinishedEvent(victor));
                                System.out.println("VICTORY FOR: " + victor);
                            }
                        } catch (IllegalMoveException e) {
                            e.printStackTrace();
                            System.exit(-1);    // This should _never_ happen.
                        }
                    }
                }
            }
        }
    }
}
