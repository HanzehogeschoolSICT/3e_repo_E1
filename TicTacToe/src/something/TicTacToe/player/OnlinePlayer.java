package something.TicTacToe.player;

import something.Client.Client;
import something.TicTacToe.Controller;

public class OnlinePlayer extends Player {

	private Client client;
	
	public OnlinePlayer(PlayerType playerType, Client client) {
		super(playerType);
		
		this.client = client;
	}
	
	@Override
	public void makeMove(int index, Controller controller) {
		if(hasTurn()) {
			client.move(index + "");
			setTurn(false, controller);
		}
	}
}
