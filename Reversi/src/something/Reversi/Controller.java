package something.Reversi;

import javafx.application.Platform;
import something.Core.event.GameEvent;
import something.Core.event.GameEventListener;
import something.Core.event.events.client.ChallengeReceiveEvent;
import something.Core.event.events.client.MatchFinishEvent;
import something.Core.event.events.client.MatchStartEvent;
import something.Core.event.events.common.MoveEvent;
import something.Core.event.events.player.YourTurnEvent;
import something.Core.player.ManualPlayer;
import something.Core.player.OnlinePlayer;
import something.Core.player.Player;
import something.Reversi.Gui.StartGui;
import something.Reversi.player.ReversiAIPlayer;

import java.util.Objects;
import java.util.Random;

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
    	System.out.println(playerMode);
	    PlayerType<ReversiBoard> playerType = Objects.equals(playerMode, "Me") ? new something.Reversi.player.HumanPlayer() : new ReversiAIPlayer(Tile.WHITE);	// TODO: AI player type, TODO: Let AI know it's color
    	
        if (Objects.equals(opponentMode, "Online")) {
            player1 = new OnlinePlayer(username, playerType);
            player1.registerEventListener(this);
            
            gui.hideInitPopUp();
            gui.waitPopUp();
            
        } else {
        	player1 = new ManualPlayer<>(playerType);
            player1.registerEventListener(this);
        	player2 = new ManualPlayer<>(new ReversiAIPlayer(Tile.BLACK));    // TODO: Fixme, was new ManualPlayer
            player2.registerEventListener(this);

        	startOfflineMatch((ManualPlayer<ReversiBoard>) player1, (ManualPlayer<ReversiBoard>) player2, "Reversi");
        }
    }
	
	private void startOfflineMatch(ManualPlayer<ReversiBoard> player, ManualPlayer<ReversiBoard> player2, String game) {
		ManualPlayer<ReversiBoard> hasMove = new Random().nextInt(2) == 0 ? player : player2;
		
		player.setOpponent(player2);
		player.callEvent(new MatchStartEvent(player, game, hasMove.getUsername(), player2.getUsername()));
		player2.callEvent(new MatchStartEvent(player2, game, hasMove.getUsername(), player.getUsername()));
		hasMove.callEvent(new YourTurnEvent());
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
				MatchFinishEvent event = (MatchFinishEvent) e;
				
				gui.endGameStage();
				if(player1 instanceof ManualPlayer) {
					gui.showInitPopUp();
				} else {
					gui.waitPopUp();
				}
				gui.showResult(event.getResult());
				
			} else if(e instanceof YourTurnEvent) {
				YourTurnEvent event = (YourTurnEvent) e;
				Player player = (Player) event.getClient();
				
				player.setHasTurn(true);
				
				if(player.getPlayerType() instanceof ReversiAIPlayer) {
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
