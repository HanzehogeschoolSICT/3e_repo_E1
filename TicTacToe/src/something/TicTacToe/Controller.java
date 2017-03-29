package something.TicTacToe;

import something.Client.Client;
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
public class Controller {
	
    private Client client;
    private StartGui startGui;

    private Player player;
    
    public Controller(StartGui startGui) {
        this.startGui = startGui;
    }

    public void processLogin(String playerMode, String opponentMode, String username) {
    	PlayerType playerType = playerMode == "Me" ? new HumanPlayer() : new AIPlayer();
    	
        if (opponentMode == "Online") {
            client = new Client();
            player = new OnlinePlayer(playerType, client);
            
            client.login(username);
            client.subscribe("Tic-tac-toe");
            
            //TODO await game
            
        } else {
        	player = new OfflinePlayer(playerType);
        	
        	//TODO start game
            startGui.startGameStage();
        }
    }
}
