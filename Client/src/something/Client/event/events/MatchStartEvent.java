package something.Client.event.events;

import something.Client.Client;
import something.Client.event.GameEvent;

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
