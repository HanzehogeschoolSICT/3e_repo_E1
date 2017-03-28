package something.Client.event.events;

import something.Client.Client;
import something.Client.event.GameEvent;

public class MoveEvent implements GameEvent {

	private final Client client;
	private final String player;
	private final String details;
	private final String move;
	
	public MoveEvent(Client client, String player, String details, String move) {
		this.client = client;
		this.player = player;
		this.details = details;
		this.move = move;
	}
	
	public Client getClient() {
		return client;
	}
	
	public String getPlayer() {
		return player;
	}
	
	public String getDetails() {
		return details;
	}
	
	public String getMove() {
		return move;
	}
}
