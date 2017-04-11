//package something.TicTacToe;
//
//import javafx.application.Platform;
//import something.Core.event.GameEvent;
//import something.Core.event.GameEventListener;
//import something.Core.event.events.client.ChallengeReceiveEvent;
//import something.Core.event.events.client.MatchFinishEvent;
//import something.Core.event.events.client.MatchStartEvent;
//import something.Core.event.events.common.MoveEvent;
//import something.Core.event.events.player.YourTurnEvent;
//import something.Core.player.ManualPlayer;
//import something.Core.player.OnlinePlayer;
//import something.Core.player.Player;
//import something.TicTacToe.Gui.BoardGUI;
//import something.TicTacToe.Gui.StartGui;
//import something.TicTacToe.player.TicTacToeAIPlayer;
//
//import java.util.Objects;
//import java.util.Random;
//
//public class Controller implements GameEventListener {
//
//	private StartGui gui;
//
//	private Player player1;
//	private Player player2;
//
//	public Controller(StartGui startGui) {
//		this.gui = startGui;
//	}
//
//	public Player getPlayer() {
//		return player1;
//	}
//
//	public Player getPlayerOnTurn() {
//		if(player1 != null && player1.hasTurn()) {
//			return player1;
//		}
//		if(player2 != null && player2.hasTurn()) {
//			return player2;
//		}
//		return null;
//	}
//
//	public void processLogin(String playerMode, String opponentMode, String username) {
//    	PlayerType<BoardGUI> playerType = Objects.equals(playerMode, "Me") ? new something.TicTacToe.player.HumanPlayer() : new TicTacToeAIPlayer();
//
//        if (Objects.equals(opponentMode, "Online")) {
//            player1 = new OnlinePlayer(username, playerType);
//            player1.registerEventListener(this);
//
//            gui.hideInitPopUp();
//            gui.waitPopUp();
//
//        } else {
//        	player1 = new ManualPlayer<>(playerType);
//            player1.registerEventListener(this);
//        	player2 = new ManualPlayer<>(new TicTacToeAIPlayer());
//            player2.registerEventListener(this);
//
//        	startOfflineMatch((ManualPlayer<BoardGUI>) player1, (ManualPlayer<BoardGUI>) player2, "Tic-tac-toe");
//        }
//    }
//
//	private void startOfflineMatch(ManualPlayer<BoardGUI> player1, ManualPlayer<BoardGUI> player2, String game) {
//		ManualPlayer<BoardGUI> hasMove = new Random().nextInt(2) == 0 ? player1 : player2;
//
//		player1.setOpponent(player2);
//		player1.callEvent(new MatchStartEvent(player1, game, hasMove.getUsername(), player2.getUsername()));
//		player2.callEvent(new MatchStartEvent(player2, game, hasMove.getUsername(), player1.getUsername()));
//		hasMove.callEvent(new YourTurnEvent());
//
//		ManualPlayer hasNotMove = hasMove == player1 ? player2 : player1;
//		if(hasNotMove.getPlayerType() instanceof TicTacToeAIPlayer) {
//			TicTacToeAIPlayer aiPlayer = (TicTacToeAIPlayer) hasNotMove.getPlayerType();
//
//			aiPlayer.setIsCross(true);
//		}
//	}
//
//	@Override
//	public void pushEvent(GameEvent e) {
//		Platform.runLater(() -> {
//			if(e instanceof MatchStartEvent) {
//				gui.hideInitPopUp();
//				gui.closeWaitPopUp();
//				gui.startGameStage();
//
//			} else if(e instanceof MatchFinishEvent) {
//				MatchFinishEvent event = (MatchFinishEvent) e;
//
//				gui.endGameStage();
//				if(player1 instanceof ManualPlayer) {
//					gui.showInitPopUp();
//				} else {
//					gui.waitPopUp();
//				}
//				gui.showResult(event.getResult());
//
//			} else if(e instanceof YourTurnEvent) {
//				YourTurnEvent event = (YourTurnEvent) e;
//				Player player = (Player) event.getClient();
//
//				player.setHasTurn(true);
//
//				if(player.getPlayerType() instanceof TicTacToeAIPlayer) {
//					int move = player.getPlayerType().getMove(gui.getBoard());
//					player.makeMove(move);
//				}
//
//			} else if(e instanceof MoveEvent) {
//				MoveEvent event = (MoveEvent) e;
//
//				gui.getBoard().makeMove(Integer.parseInt(event.getMove()));
//
//				if(player1 instanceof ManualPlayer) {
//					Mark[] board = gui.getBoard().getTicTacToeBoard().getMarks();
//					if(TicTacToeAIPlayer.scoreBoard(board) != 0 || TicTacToeAIPlayer.isFull(board)) {
//						player1.callEvent(new MatchFinishEvent(player1, "", "", "", ""));
//					}
//				}
//
//			} else if(e instanceof ChallengeReceiveEvent) {
//				ChallengeReceiveEvent event = (ChallengeReceiveEvent) e;
//				Player player = (Player) event.getClient();
//
//				boolean confirm = gui.confirmGameDialog(event.getChallenger());
//
//				if(confirm) {
//					player.acceptChallenge(Integer.parseInt(event.getChallengeNumber()));
//				}
//			}
//		});
//	}
//}
