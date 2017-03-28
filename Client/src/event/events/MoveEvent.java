package event.events;

import event.GameEvent;

public class MoveEvent implements GameEvent {

	private String player;
	private String details;
	private String move;
	
	public MoveEvent(String player, String details, String move) {
		this.player = player;
		this.details = details;
		this.move = move;
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
