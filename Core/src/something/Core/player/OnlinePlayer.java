package something.Core.player;

import something.Core.Board;
import something.Core.Client;
import something.Core.event.GameEvent;
import something.Core.event.events.common.MoveEvent;
import something.Core.event.events.player.EnemyMoveEvent;

public class OnlinePlayer<GameType extends Board> extends Player<GameType> {
    public final Client client;

    public OnlinePlayer(Client client) {
        this.client = client;

        //Gameserver IP: 145.33.225.170
        //client.connect(InetAddress.getByName("localhost"), 7789);

        this.client.registerEventListener(event -> {
            if (event instanceof MoveEvent) {
                fireEvent(event);
            }
        });
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
