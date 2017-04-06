package something.TicTacToe;

import javafx.application.Platform;
import something.Client.event.GameEvent;
import something.Client.event.GameEventListener;
import something.Client.event.events.*;
import something.Client.player.OfflinePlayer;
import something.Client.player.OnlinePlayer;
import something.Client.player.Player;
import something.Client.player.PlayerType;
import something.TicTacToe.Gui.GameBoard;
import something.TicTacToe.Gui.StartGui;
import something.TicTacToe.player.AIPlayer;
import something.TicTacToe.player.HumanPlayer;

import java.util.Objects;
import java.util.Random;

public class Controller implements GameEventListener<GameBoard> {

	private StartGui gui;
	
	private Player<GameBoard> player1;
	private Player<GameBoard> player2;
	
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
    	PlayerType<GameBoard> playerType = Objects.equals(playerMode, "Me") ? new HumanPlayer() : new AIPlayer();
    	
        if (Objects.equals(opponentMode, "Online")) {
            player1 = new OnlinePlayer<>(username, playerType);
            player1.registerEventListener(this);
            
            gui.hideInitPopUp();
            gui.waitPopUp();
            
        } else {
        	player1 = new OfflinePlayer<>(playerType);
            player1.registerEventListener(this);
        	player2 = new OfflinePlayer<>(new AIPlayer());
            player2.registerEventListener(this);

        	startOfflineMatch((OfflinePlayer<GameBoard>) player1, (OfflinePlayer<GameBoard>) player2, "Tic-tac-toe");
        }
    }
	
	private void startOfflineMatch(OfflinePlayer<GameBoard> player1, OfflinePlayer<GameBoard> player2, String game) {
		OfflinePlayer<GameBoard> hasMove = new Random().nextInt(2) == 0 ? player1 : player2;
		
		player1.setOpponent(player2);
		player1.callEvent(new MatchStartEvent(player1, game, hasMove.getUsername(), player2.getUsername()));
		player2.callEvent(new MatchStartEvent(player2, game, hasMove.getUsername(), player1.getUsername()));
		hasMove.callEvent(new YourTurnEvent<>(hasMove, ""));
		
		OfflinePlayer hasNotMove = hasMove == player1 ? player2 : player1;
		if(hasNotMove.getPlayerType() instanceof AIPlayer) {
			AIPlayer aiPlayer = (AIPlayer) hasNotMove.getPlayerType();
			
			aiPlayer.setIsCross(true);
		}
	}
	
	@Override
	public void handleEvent(GameEvent<GameBoard> e) {
		Platform.runLater(() -> {			
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
				YourTurnEvent<GameBoard> event = (YourTurnEvent<GameBoard>) e;
				Player<GameBoard> player = (Player<GameBoard>) event.getClient();
				
				player.setHasTurn(true);
				
				if(player.getPlayerType() instanceof AIPlayer) {
					int move = player.getPlayerType().getMove(gui.getBoard());
					player.makeMove(move);
				}
				
			} else if(e instanceof MoveEvent) {
				MoveEvent event = (MoveEvent) e;
				
				gui.getBoard().makeMove(Integer.parseInt(event.getMove()));
				
				if(player1 instanceof OfflinePlayer) {
					Mark[] board = gui.getBoard().getTicTacToeBoard().getBoard();
					if(AIPlayer.checkVictory(board) != 0 || AIPlayer.isFull(board)) {
						player1.callEvent(new MatchFinishEvent(player1, "", "", "", ""));
					}
				}
				
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
