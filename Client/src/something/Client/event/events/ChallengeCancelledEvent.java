package something.Client.event.events;

import something.Client.Client;
import something.Client.event.GameEvent;

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
