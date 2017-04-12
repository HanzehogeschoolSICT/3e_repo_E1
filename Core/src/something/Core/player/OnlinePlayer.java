package something.Core.player;

import something.Core.Board;
import something.Core.Client;
import something.Core.event.GameEvent;
import something.Core.event.events.common.MoveEvent;
import something.Core.event.events.game.GameStartEvent;
import something.Core.event.events.player.EnemyMoveEvent;
import something.Core.event.events.player.YourTurnEvent;

public class OnlinePlayer<GameType extends Board> extends Player<GameType> {
    private final Client client;
    private boolean isReady;
    public OnlinePlayer(Client client) {
        this.client = client;
        this.isReady = false;
        //Gameserver IP: 145.33.225.170
        //client.connect(InetAddress.getByName("localhost"), 7789);

        this.client.registerEventListener(event -> {
            if (event instanceof YourTurnEvent) {
                if (!isReady) {
                    isReady = true;
                    this.isPlayer1 = false;
                    this.fireEvent(new GameStartEvent());
                }
            }
            if (event instanceof MoveEvent) {
                if (!isReady) {
                    isReady = true;
                    this.isPlayer1 = true;
                    this.fireEvent(new GameStartEvent());
                }
                this.fireEvent(event);
            }
        });
	}

    @Override
    protected void reset() {
        isReady = false;
        // TODO: check match state
    }

    @Override
    public void handleEvent(GameEvent event) {
        if (event instanceof EnemyMoveEvent) {
            client.makeMove(((EnemyMoveEvent) event).move);
        }
    }
}
