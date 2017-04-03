package something.Reversi;

import java.util.Random;

import javafx.application.Platform;
import something.Client.event.GameEvent;
import something.Client.event.GameEventListener;
import something.Client.event.events.MoveEvent;
import something.Client.event.events.YourTurnEvent;
import something.Client.player.OnlinePlayer;
import something.Client.player.Player;
import something.Reversi.Gui.StartGui;
import something.TicTacToe.player.HumanPlayer;

public class Controller implements GameEventListener {

	private StartGui gui;
	
	public Controller(StartGui startGui) {
		this.gui = startGui;
		
		//TODO UI for online or offline
		Player player = new OnlinePlayer("1", new HumanPlayer());
		player.registerEventListener(this);
		Player player2 = new OnlinePlayer("2", new HumanPlayer());
		player2.registerEventListener(this);
		
		player.subscribe("Tic-tac-toe");
		player2.subscribe("Tic-tac-toe");
	}

	@Override
	public void handleEvent(GameEvent e) { //TODO implements all events and responses
		Platform.runLater(() -> {
			System.out.println(e);
			
			if(e instanceof YourTurnEvent) {
				YourTurnEvent event = (YourTurnEvent) e;
				Player player = (Player) event.getClient();
				
				int move = new Random().nextInt(9); //TODO get player/AI move
				player.makeMove(move);
				System.out.println(player.getUsername() + " move " + move);
				
			} else if(e instanceof MoveEvent) {
				MoveEvent event = (MoveEvent) e;
				Player player = (Player) event.getClient();

				//TODO render move
				System.out.println(player.getUsername() + " render " + event.getMove());
			}
		});
	}
}
