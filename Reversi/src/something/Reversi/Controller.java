package something.Reversi;

import javafx.application.Platform;
import something.Client.event.GameEvent;
import something.Client.event.GameEventListener;
import something.Client.event.events.*;
import something.Client.player.OfflinePlayer;
import something.Client.player.OnlinePlayer;
import something.Client.player.Player;
import something.Client.player.PlayerType;
import something.Reversi.Gui.StartGui;
import something.Reversi.player.AIPlayer;
import something.Reversi.player.HumanPlayer;

import java.util.Objects;
import java.util.Random;

public class Controller implements GameEventListener<ReversiBoard> {

	private StartGui gui;
	
	private Player<ReversiBoard> player1;
	private Player<ReversiBoard> player2;
	
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
    	System.out.println(playerMode);
	    PlayerType<ReversiBoard> playerType = Objects.equals(playerMode, "Me") ? new HumanPlayer() : new AIPlayer(Tile.WHITE);	// TODO: AI player type, TODO: Let AI know it's color
    	
        if (Objects.equals(opponentMode, "Online")) {
            player1 = new OnlinePlayer<>(username, playerType);
            player1.registerEventListener(this);
            
            gui.hideInitPopUp();
            gui.waitPopUp();
            
        } else {
        	player1 = new OfflinePlayer<>(playerType);
            player1.registerEventListener(this);
        	player2 = new OfflinePlayer<>(new AIPlayer(Tile.BLACK));    // TODO: Fixme, was new HumanPlayer
            player2.registerEventListener(this);

        	startOfflineMatch((OfflinePlayer<ReversiBoard>) player1, (OfflinePlayer<ReversiBoard>) player2, "Reversi");
        }
    }
	
	private void startOfflineMatch(OfflinePlayer<ReversiBoard> player, OfflinePlayer<ReversiBoard> player2, String game) {
		OfflinePlayer<ReversiBoard> hasMove = new Random().nextInt(2) == 0 ? player : player2;
		
		player.setOpponent(player2);
		player.callEvent(new MatchStartEvent(player, game, hasMove.getUsername(), player2.getUsername()));
		player2.callEvent(new MatchStartEvent(player2, game, hasMove.getUsername(), player.getUsername()));
		hasMove.callEvent(new YourTurnEvent<>(hasMove, ""));
	}
	
	@Override
	public void handleEvent(GameEvent<ReversiBoard> e) {
		Platform.runLater(() -> {
			System.out.println(e);
			
			if(e instanceof MatchStartEvent) {		
				gui.hideInitPopUp();
				gui.closeWaitPopUp();
				gui.startGameStage();
				
			} else if(e instanceof MatchFinishEvent) {
				MatchFinishEvent event = (MatchFinishEvent) e;
				
				gui.endGameStage();
				if(player1 instanceof OfflinePlayer) {
					gui.showInitPopUp();
				} else {
					gui.waitPopUp();
				}
				gui.showResult(event.getResult());
				
			} else if(e instanceof YourTurnEvent) {
				YourTurnEvent<ReversiBoard> event = (YourTurnEvent<ReversiBoard>) e;
				Player<ReversiBoard> player = (Player<ReversiBoard>) event.getClient();
				
				player.setHasTurn(true);
				
				if(player.getPlayerType() instanceof AIPlayer) {
					int move = player.getPlayerType().getMove(gui.getGUI().getReversiBoard());
					player.makeMove(move);
				}
				
			} else if(e instanceof MoveEvent) {
				MoveEvent event = (MoveEvent) e;

				gui.getGUI().makeMove(Integer.parseInt(event.getMove()));
			
				//TODO check if end of game for offline
				
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
