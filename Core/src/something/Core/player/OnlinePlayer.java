package something.Core.player;

import something.Core.Board;
import something.Core.Client;
import something.Core.GameTask;
import something.Core.event.GameEvent;
import something.Core.event.events.common.MoveEvent;
import something.Core.event.events.game.ForfeitEvent;
import something.Core.event.events.player.EnemyMoveEvent;

import java.io.IOException;


public class OnlinePlayer<GameType extends Board> extends Player<GameType> {
    private final Client client;

    public OnlinePlayer(Client client, String username) {
        this.client = client;
        //Gameserver IP: 145.33.225.170
        //client.connect(InetAddress.getByName("localhost"), 7789);
        GameTask.submit(() ->
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
        } else if (event instanceof ForfeitEvent) {
            try {
                client.forfeit();
            } catch (IOException e) {e.printStackTrace();}
        }
    }
}
