package something.Client.event.events;

import something.Client.Client;
import something.Client.event.GameEvent;

public class ChallengeReceiveEvent implements GameEvent {
	
	private final Client client;
	private final String challenger;
	private final String gameType;
	private final String challengeNumber;
	
	public ChallengeReceiveEvent(Client client, String challenger, String gameType, String challengeNumber) {
		this.client = client;
		this.challenger = challenger;
		this.gameType = gameType;
		this.challengeNumber = challengeNumber;
	}
	
	public Client getClient() {
		return client;
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
