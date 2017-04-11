package something.Core.event.events.client;

import something.Core.Client;
import something.Core.event.GameEvent;

public class MatchFinishEvent implements GameEvent {

	private final Client client;
	private final String result;
	private final String playerOneScore;
	private final String playerTwoScore;
	private final String comment;
	
	public MatchFinishEvent(Client client, String result, String playerOneScore, String playerTwoScore, String comment) {
		this.client = client;
		this.result = result;
		this.playerOneScore = playerOneScore;
		this.playerTwoScore = playerTwoScore;
		this.comment = comment;
	}
	
	public Client getClient() {
		return client;
	}
	
	public String getResult() {
		return result;
	}
	
	public String getPlayerOneScore() {
		return playerOneScore;
	}
	
	public String getPlayerTwoScore() {
		return playerTwoScore;
	}
	
	public String getComment() {
		return comment;
	}
}
