package something.TicTacToe.player;

import something.Client.Client;
import something.Client.event.GameEvent;
import something.Client.event.GameEventListener;
import something.Client.event.events.MatchStartEvent;

public class OnlinePlayer extends Player implements GameEventListener {

	private Client client;
	
	public OnlinePlayer(PlayerType playerType, Client client) {
		super(playerType);
		
		this.client = client;
		client.registerEventListener(this);
	}
	
	@Override
	public void makeMove() {
		String move = getPlayerType().getMove();
		
		//TODO send move through client
	}

	@Override
	public void handleEvent(GameEvent e) {
		if(e instanceof MatchStartEvent) {
			MatchStartEvent event = (MatchStartEvent) e;
			
			//TODO start game
			
		} else if(true) {
			
		}
	}
}
