package something.Client.event.events;

import something.Client.Client;
import something.Client.event.GameEvent;

public class YourTurnEvent implements GameEvent {

	private final Client client;
	private final String turnMessage;
	
	public YourTurnEvent(Client client, String turnMessage) {
		this.client = client;
		this.turnMessage = turnMessage;
	}
	
	public Client getClient() {
		return client;
	}
	
	public String getTurnMessage() {
		return turnMessage;
	}
}
