package something.Core.event.events.client;

import something.Core.Client;
import something.Core.event.GameEvent;

public class MatchStartEvent implements GameEvent {

	private final Client client;
	private final String gameType;
	private final String playerToMove;
	private final String opponent;
	
	public MatchStartEvent(Client client, String gameType, String playerToMove, String opponent) {
		this.client = client;
		this.gameType = gameType;
		this.playerToMove = playerToMove;
		this.opponent = opponent;
	}
	
	public Client getClient() {
		return client;
	}
	
	public String getGameType() {
		return gameType;
	}
	
	public String getPlayerToMove() {
		return playerToMove;
	}
	
	public String getOpponent() {
		return opponent;
	}
}
