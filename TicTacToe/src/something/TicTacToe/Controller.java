package something.TicTacToe;

import javafx.application.Platform;
import something.Client.Client;
import something.Client.event.GameEvent;
import something.Client.event.GameEventListener;
import something.Client.event.events.ChallengeReceiveEvent;
import something.Client.event.events.MatchFinishEvent;
import something.Client.event.events.MatchStartEvent;
import something.Client.event.events.MoveEvent;
import something.Client.event.events.YourTurnEvent;
import something.Client.player.OfflinePlayer;
import something.Client.player.OnlinePlayer;
import something.Client.player.PlayerType;
import something.TicTacToe.Gui.StartGui;
import something.TicTacToe.player.AIPlayer;
import something.TicTacToe.player.HumanPlayer;
import something.TicTacToe.player.Player;

/**
 * Created by samikroon on 3/28/17.
 */
public class Controller implements GameEventListener {
	
    private Client client;
    private StartGui startGui;

    private String username;
    private Player player;
    private Player offlineOpponent;
    
    public Controller(StartGui startGui) {
        this.startGui = startGui;
    }
    
    public Client getClient() {
        return client;
    }
    
    public StartGui getGUI() {
        return startGui;
    }
    
    public Player getPlayer() {
    	return player;
    }
    
    public Player getOpponentPlayer() {
    	return offlineOpponent;
    }
    
    public String getUsername() {
    	return username;
    }
    
    public void processLogin(String playerMode, String opponentMode, String username) {
    	this.username = username;
    	PlayerType playerType = playerMode == "Me" ? new HumanPlayer() : new AIPlayer();

    	startGui.hideInitPopUp();
    	
        if (opponentMode == "Online") {
            client = new Client();
            client.registerEventListener(this);
            
            player = new OnlinePlayer(playerType, client);
            client.login(username);
            
            startGui.waitPopUp();
            
        } else {
        	player = new OfflinePlayer(playerType);
        	offlineOpponent = new OfflinePlayer(new AIPlayer());
        	
        	loadAIPlayerCross(player, false);
            loadAIPlayerCross(offlineOpponent, true);
            
            startGui.startGameStage();
            
            player.setTurn(true, this);
        }
    }

	@Override
	public void handleEvent(GameEvent e) {		
		if(e instanceof MatchStartEvent) {
			MatchStartEvent event = (MatchStartEvent) e;
			
			Platform.runLater(() -> {
				startGui.closeWaitPopUp();
				startGui.startGameStage();
			});
			loadAIPlayerCross(player, event.getOpponent().equals(event.getPlayerToMove()));
			
		} else if(e instanceof MatchFinishEvent) {
			MatchFinishEvent event = (MatchFinishEvent) e;
			
			Platform.runLater(() -> {
				startGui.endGameStage();
				startGui.waitPopUp();
				startGui.showResult(event.getResult());
			});
			
		} else if(e instanceof YourTurnEvent) {
			YourTurnEvent event = (YourTurnEvent) e;
			
			Platform.runLater(() -> {
				player.setTurn(true, this);
			});
			
		} else if(e instanceof MoveEvent) {
			MoveEvent event = (MoveEvent) e;
		
			Platform.runLater(() -> {
				startGui.getBoard().makeMove(Integer.parseInt(event.getMove()));
			});
			
		} else if(e instanceof ChallengeReceiveEvent) {
			ChallengeReceiveEvent event = (ChallengeReceiveEvent) e;
		
			Platform.runLater(() -> {
				boolean confirm = startGui.confirmGameDialog(event.getChallenger());
				
				if(confirm) {
					client.acceptChallenge(Integer.parseInt(event.getChallengeNumber()));
				}
			});
		}
	}
	
	public void loadAIPlayerCross(Player player, boolean cross) {
		if(player.getPlayerType() instanceof AIPlayer) {
			AIPlayer aiPlayer = (AIPlayer) player.getPlayerType();
			aiPlayer.setIsCross(cross);
		}
	}
}
