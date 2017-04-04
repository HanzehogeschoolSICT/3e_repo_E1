package something.Reversi.player;

import java.util.Random;

import javafx.application.Platform;
import something.Client.event.GameEvent;
import something.Client.event.GameEventListener;
import something.Client.event.events.ChallengeReceiveEvent;
import something.Client.event.events.MatchFinishEvent;
import something.Client.event.events.MatchStartEvent;
import something.Client.event.events.MoveEvent;
import something.Client.event.events.YourTurnEvent;
import something.Client.player.OfflinePlayer;
import something.Client.player.OnlinePlayer;
import something.Client.player.Player;
import something.Client.player.PlayerType;
import something.Reversi.Gui.StartGui;
import something.TicTacToe.player.AIPlayer;
import something.TicTacToe.player.HumanPlayer;

public class Controller implements GameEventListener {

	private StartGui gui;
	
	private Player player;
	private Player player2;
	
	public Controller(StartGui startGui) {
		this.gui = startGui;

		/*boolean online = false;
		if(online) {
			player = new OnlinePlayer("player", new HumanPlayer());
			player.registerEventListener(this);
			player2 = new OnlinePlayer("player2", new HumanPlayer());
			player2.registerEventListener(this);
			
			player.subscribe("Tic-tac-toe");
			player2.subscribe("Tic-tac-toe");
			
		} else {
			player = new OfflinePlayer(new HumanPlayer());
			player.registerEventListener(this);
			player2 = new OfflinePlayer(new HumanPlayer());
			player2.registerEventListener(this);
			
			startOfflineMatch((OfflinePlayer) player, (OfflinePlayer) player2, "Tic-tac-toe");
		}*/		
	}

	public Player getPlayer() {
		return player;
	}
	
	public void processLogin(String playerMode, String opponentMode, String username) {
    	PlayerType playerType = playerMode == "Me" ? new HumanPlayer() : new AIPlayer();
    	
        if (opponentMode == "Online") {
            player = new OnlinePlayer(username, playerType);
            player.registerEventListener(this);
            
        } else {
        	player = new OfflinePlayer(playerType);
            player.registerEventListener(this);
        	player2 = new OfflinePlayer(new AIPlayer());
            player2.registerEventListener(this);

        	startOfflineMatch((OfflinePlayer) player, (OfflinePlayer) player2, "Reversi");
        }
    }
	
	private void startOfflineMatch(OfflinePlayer player, OfflinePlayer player2, String game) {
		OfflinePlayer hasMove =  new Random().nextInt(2) == 0 ? player : player2;
				
		player.setOpponent(player2);
		player.callEvent(new MatchStartEvent(player, game, hasMove.getUsername(), player2.getUsername()));
		player2.callEvent(new MatchStartEvent(player2, game, hasMove.getUsername(), player.getUsername()));
		hasMove.callEvent(new YourTurnEvent(hasMove, ""));
	}
	
	@Override
	public void handleEvent(GameEvent e) { //TODO implements all events and responses
		Platform.runLater(() -> {
			System.out.println(e);
			
			if(e instanceof MatchStartEvent) {				
				gui.hideInitPopUp();
				gui.startGameStage();
				
			} else if(e instanceof MatchFinishEvent) {
				//TODO back to init screen
				gui.showInitPopUp();
				
			} else if(e instanceof YourTurnEvent) {
				YourTurnEvent event = (YourTurnEvent) e;
				Player player = (Player) event.getClient();
				
				player.setHasTurn(true);
				
				int move = new Random().nextInt(9); //TODO get player/AI move
				player.makeMove(move);
				System.out.println(player.getUsername() + " move " + move);
				
				player.setHasTurn(false);
				
			} else if(e instanceof MoveEvent) {
				MoveEvent event = (MoveEvent) e;
				Player player = (Player) event.getClient();

				//TODO render move
				System.out.println(player.getUsername() + " render " + event.getMove());
			
				//TODO check if end of game
				
			} else if(e instanceof ChallengeReceiveEvent) {
				ChallengeReceiveEvent event = (ChallengeReceiveEvent) e;
			
				//TODO show challenge popup
			}
		});
	}
}
