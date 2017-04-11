package something.Core.event.events.client;

import something.Core.Client;
import something.Core.event.GameEvent;

public class ChallengeCancelledEvent implements GameEvent {

	private Client client;
	private final String challengeNumber;
	
	public ChallengeCancelledEvent(Client client, String challengeNumber) {
		this.client = client;
		this.challengeNumber = challengeNumber;
	}
	
	public Client getClient() {
		return client;
	}
	
	public String getChallengeNumber() {
		return challengeNumber;
	}
}
