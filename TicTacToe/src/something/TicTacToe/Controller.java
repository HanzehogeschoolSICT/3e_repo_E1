package something.TicTacToe;

import javafx.application.Platform;
import something.Client.Client;
import something.Client.event.GameEvent;
import something.Client.event.GameEventListener;
import something.Client.event.events.MatchFinishEvent;
import something.Client.event.events.MatchStartEvent;
import something.TicTacToe.Gui.StartGui;
import something.TicTacToe.player.AIPlayer;
import something.TicTacToe.player.HumanPlayer;
import something.TicTacToe.player.OfflinePlayer;
import something.TicTacToe.player.OnlinePlayer;
import something.TicTacToe.player.Player;
import something.TicTacToe.player.PlayerType;

/**
 * Created by samikroon on 3/28/17.
 */
public class Controller implements GameEventListener {
	
    private Client client;
    private StartGui startGui;

    private Player player;
    private boolean subscribe;
    
    public Controller(StartGui startGui) {
        this.startGui = startGui;
    }

    public void processLogin(String playerMode, String opponentMode, String username, boolean subscribe) {
    	this.subscribe = subscribe;
    	PlayerType playerType = playerMode == "Me" ? new HumanPlayer() : new AIPlayer();
    	
    	startGui.hideInitPopUp();
    	
        if (opponentMode == "Online") {
            client = new Client();
            client.registerEventListener(this);
            
            player = new OnlinePlayer(playerType, client);
            client.login(username);
            
            if(subscribe) {
            	client.subscribe("Tic-tac-toe");
            	startGui.waitPopUp("Waiting for a game");
            } else {
            	startGui.waitPopUp("Waiting for a challenge");
            }
            
        } else {
        	player = new OfflinePlayer(playerType);
        	
            startGui.startGameStage();
        }
    }

	@Override
	public void handleEvent(GameEvent e) {
		System.out.println(e);
		
		if(e instanceof MatchStartEvent) {
			MatchStartEvent event = (MatchStartEvent) e;
			
			Platform.runLater(() -> {
				startGui.closeWaitPopUp();
				startGui.startGameStage();
			});
			
		} else if(e instanceof MatchFinishEvent) {
			MatchFinishEvent event = (MatchFinishEvent) e;
			
			Platform.runLater(() -> {
				startGui.endGameStage();
				startGui.waitPopUp("");
			});
			if(subscribe) {
            	client.subscribe("Tic-tac-toe");
			}
		}
	}
}
