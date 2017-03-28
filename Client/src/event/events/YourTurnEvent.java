package event.events;

import event.GameEvent;

public class YourTurnEvent implements GameEvent {

	private String turnMessage;
	
	public YourTurnEvent(String turnMessage) {
		this.turnMessage = turnMessage;
	}
	
	public String getTurnMessage() {
		return turnMessage;
	}
}
