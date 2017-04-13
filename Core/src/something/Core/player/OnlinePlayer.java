package something.Core.player;

import something.Core.Board;
import something.Core.Client;
import something.Core.event.GameEvent;
import something.Core.event.events.common.MoveEvent;
import something.Core.event.events.player.EnemyMoveEvent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OnlinePlayer<GameType extends Board> extends Player<GameType> {
    private static ExecutorService taskExecutor = Executors.newSingleThreadExecutor();
    private final Client client;

    public OnlinePlayer(Client client, String username) {
        this.client = client;
        //Gameserver IP: 145.33.225.170
        //client.connect(InetAddress.getByName("localhost"), 7789);
        taskExecutor.submit(() ->
                this.client.registerEventListener(event -> {
                    if (event instanceof MoveEvent) {
                        if (!username.equals(((MoveEvent) event).player)) {
                            this.fireEvent(event);
                        }
                    }

                }));
    }

    @Override
    protected void reset() {
        // TODO: check match state
    }

    @Override
    public void handleEvent(GameEvent event) {
        if (event instanceof EnemyMoveEvent) {
            client.makeMove(((EnemyMoveEvent) event).move);
        }
    }
}
