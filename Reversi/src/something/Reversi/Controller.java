package something.Reversi;

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
import something.Reversi.player.AIPlayer;
import something.Reversi.player.HumanPlayer;

public class Controller implements GameEventListener {

	private StartGui gui;
	
	private Player player1;
	private Player player2;
	
	public Controller(StartGui startGui) {
		this.gui = startGui;
	}

	public Player getPlayer() {
		return player1;
	}
	
	public Player getPlayerOnTurn() {
		if(player1 != null && player1.hasTurn()) {
			return player1;
		}
		if(player2 != null && player2.hasTurn()) {
			return player2;
		}
		return null;
	}
	
	public void processLogin(String playerMode, String opponentMode, String username) {
    	PlayerType playerType = playerMode == "Me" ? new HumanPlayer() : new AIPlayer();
    	
        if (opponentMode == "Online") {
            player1 = new OnlinePlayer(username, playerType);
            player1.registerEventListener(this);
            
            gui.hideInitPopUp();
            gui.waitPopUp();
            
        } else {
        	player1 = new OfflinePlayer(playerType);
            player1.registerEventListener(this);
        	player2 = new OfflinePlayer(new AIPlayer());
            player2.registerEventListener(this);

        	startOfflineMatch((OfflinePlayer) player1, (OfflinePlayer) player2, "Reversi");
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
	public void handleEvent(GameEvent e) {
		Platform.runLater(() -> {
			System.out.println(e);
			
			if(e instanceof MatchStartEvent) {		
				gui.hideInitPopUp();
				gui.closeWaitPopUp();
				gui.startGameStage();
				
			} else if(e instanceof MatchFinishEvent) {
				gui.endGameStage();
				if(player1 instanceof OfflinePlayer) {
					gui.showInitPopUp();
				} else {
					gui.waitPopUp();
				}
				
			} else if(e instanceof YourTurnEvent) {
				YourTurnEvent event = (YourTurnEvent) e;
				Player player = (Player) event.getClient();
				
				player.setHasTurn(true);
				
				if(player.getPlayerType() instanceof AIPlayer) {
					int move = player.getPlayerType().getMove(gui.getGUI().getReversiBoard());
					player.makeMove(move);
				}
				
			} else if(e instanceof MoveEvent) {
				MoveEvent event = (MoveEvent) e;

				gui.getGUI().makeMove(Integer.parseInt(event.getMove()));
			
				//TODO check if end of game
				
			} else if(e instanceof ChallengeReceiveEvent) {
				ChallengeReceiveEvent event = (ChallengeReceiveEvent) e;
				Player player = (Player) event.getClient();
				
				boolean confirm = gui.confirmGameDialog(event.getChallenger());
				
				if(confirm) {
					player.acceptChallenge(Integer.parseInt(event.getChallengeNumber()));
				}
			}
		});
	}
}