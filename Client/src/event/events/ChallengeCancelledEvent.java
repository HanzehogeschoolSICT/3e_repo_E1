package event.events;

import event.GameEvent;

public class ChallengeCancelledEvent implements GameEvent {

	private String challengeNumber;
	
	public ChallengeCancelledEvent(String challengeNumber) {
		this.challengeNumber = challengeNumber;
	}
	
	public String getChallengeNumber() {
		return challengeNumber;
	}
}
