package something.Core.event.events.common;

import something.Core.event.GameEvent;

public class MoveEvent implements GameEvent {
	public final String player;
	public final int move;
	
	public MoveEvent(String player, int move) {
		this.player = player;
		this.move = move;
	}

	@Override
	public String toString() {
		return "MoveEvent{" +
				"player='" + player + '\'' +
				", move=" + move +
				'}';
	}
}
