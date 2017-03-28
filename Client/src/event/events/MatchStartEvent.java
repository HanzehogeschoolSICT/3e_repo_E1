package event.events;

import event.GameEvent;

public class MatchStartEvent implements GameEvent {

	private String gameType;
	private String playerToMove;
	private String opponent;
	
	public MatchStartEvent(String gameType, String playerToMove, String opponent) {
		this.gameType = gameType;
		this.playerToMove = playerToMove;
		this.opponent = opponent;
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
