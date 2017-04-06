package something.Client.event.events;

import something.Client.Client;
import something.Client.event.GameEvent;

public class YourTurnEvent<GameType> implements GameEvent<GameType> {

	private final Client<GameType> client;
	private final String turnMessage;
	
	public YourTurnEvent(Client<GameType> client, String turnMessage) {
		this.client = client;
		this.turnMessage = turnMessage;
	}
	
	public Client<GameType> getClient() {
		return client;
	}
	
	public String getTurnMessage() {
		return turnMessage;
	}
}
