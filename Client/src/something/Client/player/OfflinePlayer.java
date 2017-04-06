package something.Client.player;

import java.util.Random;

import something.Client.event.events.MoveEvent;
import something.Client.event.events.YourTurnEvent;

public class OfflinePlayer extends Player {

	private OfflinePlayer opponent;
	
	public OfflinePlayer(PlayerType playerType) {
		super(new Random().nextInt() + "", playerType);
	}

	public void setOpponent(OfflinePlayer opponent) {
		this.opponent = opponent;
		if(opponent.getOpponent() == null) {
			opponent.setOpponent(this);
		}
	}
	
	public OfflinePlayer getOpponent() {
		return opponent;
	}
	
	@Override
	public void makeMove(int index) {
		callEvent(new MoveEvent(this, getUsername(), "", index + ""));
		getOpponent().callEvent(new MoveEvent(getOpponent(), getUsername(), "", index + ""));
		
		setHasTurn(false);
		getOpponent().callEvent(new YourTurnEvent(getOpponent(), ""));
	}
}
