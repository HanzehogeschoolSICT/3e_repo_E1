package something.Client.player;

import something.Client.Board;
import something.Client.event.events.MoveEvent;
import something.Client.event.events.YourTurnEvent;

import java.util.Random;

public class OfflinePlayer<GameType extends Board> extends Player<GameType> {

	private OfflinePlayer<GameType> opponent;
	
	public OfflinePlayer(PlayerType<GameType> playerType) {
		super(new Random().nextInt() + "", playerType);
	}

	public void setOpponent(OfflinePlayer<GameType> opponent) {
		this.opponent = opponent;
		if(opponent.getOpponent() == null) {
			opponent.setOpponent(this);
		}
	}
	
	public OfflinePlayer<GameType> getOpponent() {
		return opponent;
	}
	
	@Override
	public void makeMove(int index) {
		callEvent(new MoveEvent(this, getUsername(), "", index + ""));
		getOpponent().callEvent(new MoveEvent(getOpponent(), getUsername(), "", index + ""));
		
		setHasTurn(false);
		getOpponent().callEvent(new YourTurnEvent<>(getOpponent(), ""));
	}
}
