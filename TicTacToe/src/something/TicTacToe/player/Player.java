package something.TicTacToe.player;

import something.TicTacToe.Controller;
import something.TicTacToe.Mark;

public abstract class Player {
	
	private PlayerType playerType;
	private boolean playersTurn = false;
	
	public Player(PlayerType playerType) {
		this.playerType = playerType;
	}
	
	public PlayerType getPlayerType() {
		return playerType;
	}
	
	public boolean hasTurn() {
		return playersTurn;
	}
	
	public void setTurn(boolean turn, Controller controller) {
		playersTurn = turn;
		
		if(turn == true) {
			if(playerType instanceof AIPlayer) {
				int index = playerType.getMove(controller.getGUI().getBoard());
				boolean success = controller.getGUI().getBoard().makeMove(index);
				
	        	if(success) {
	        		doMakeMove(index, controller);
	        	}			
			}
		}
	}
	
	public void doMakeMove(int index, Controller controller) {
		makeMove(index, controller);
		
		if(controller.getClient() == null) {
			Mark[] board = controller.getGUI().getBoard().getTicTacToeBoard().getBoard();
			
			int victory = AIPlayer.checkVictory(board);
			if(AIPlayer.isFull(board) || victory != 0) {
				controller.getGUI().endGameStage();
				controller.getGUI().showInitPopUp();
				controller.getGUI().showResult(victory == 0 ? "DRAW" : (AIPlayer.checkVictory(board) == 10 ? "LOSS" : "WIN"));
				
			} else {
				if(controller.getPlayer() == this) {
					controller.getOpponentPlayer().setTurn(true, controller);
				} else {
					controller.getPlayer().setTurn(true, controller);
				}
			}
		}
	}
	
	public abstract void makeMove(int index, Controller controller);
	
}
