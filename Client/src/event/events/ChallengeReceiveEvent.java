package event.events;

import event.GameEvent;

public class ChallengeReceiveEvent implements GameEvent {
	
	private String challenger;
	private String gameType;
	private String challengeNumber;
	
	public ChallengeReceiveEvent(String challenger, String gameType, String challengeNumber) {
		this.challenger = challenger;
		this.gameType = gameType;
		this.challengeNumber = challengeNumber;
	}
	
	public String getChallenger() {
		return challenger;
	}
	
	public String getGameType() {
		return gameType;
	}
	
	public String getChallengeNumber() {
		return challengeNumber;
	}
}
