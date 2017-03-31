package something.TicTacToe.player;

import something.TicTacToe.Gui.GameBoard;

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
	
	public void setTurn(boolean turn, GameBoard board) {
		playersTurn = turn;
		
		if(turn == true) {
			if(playerType instanceof AIPlayer) {
				int index = playerType.getMove(board);
				boolean success = board.makeMove(index);
				
	        	if(success) {
	        		makeMove(index, board);
	        	}			
			}
		}
	}
	
	public abstract void makeMove(int index, GameBoard board);
	
}
