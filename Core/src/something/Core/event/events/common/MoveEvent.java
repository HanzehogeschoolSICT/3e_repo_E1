package something.Core.event.events.common;

import something.Core.event.GameEvent;

public class MoveEvent implements GameEvent {
	public final int move;
	
	public MoveEvent(int move) {
		this.move = move;
	}
}
