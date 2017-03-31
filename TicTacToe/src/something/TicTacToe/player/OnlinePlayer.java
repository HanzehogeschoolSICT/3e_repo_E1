package something.TicTacToe.player;

import something.Client.Client;

public class OnlinePlayer extends Player {

	private Client client;
	
	public OnlinePlayer(PlayerType playerType, Client client) {
		super(playerType);
		
		this.client = client;
	}
	
	@Override
	public void makeMove(int index) {
		if(hasTurn()) {
			client.move(index + "");
			setTurn(false);
		}
	}
}
