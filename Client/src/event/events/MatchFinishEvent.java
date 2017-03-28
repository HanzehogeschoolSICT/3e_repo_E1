package event.events;

import event.GameEvent;

public class MatchFinishEvent implements GameEvent {

	private String result;
	private String playerOneScore;
	private String playerTwoScore;
	private String comment;
	
	public MatchFinishEvent(String result, String playerOneScore, String playerTwoScore, String comment) {
		this.result = result;
		this.playerOneScore = playerOneScore;
		this.playerTwoScore = playerTwoScore;
		this.comment = comment;
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
