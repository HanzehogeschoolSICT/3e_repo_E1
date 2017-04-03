package something.Client.player;

import java.util.Random;

import something.Client.event.events.MoveEvent;

public class OfflinePlayer extends Player {

	public OfflinePlayer(PlayerType playerType) {
		super(new Random().nextInt() + "", playerType);
	}

	@Override
	public void makeMove(int index) {
		getOpponent().callEvent(new MoveEvent(getOpponent(), getUsername(), "", index + ""));
	}

}
