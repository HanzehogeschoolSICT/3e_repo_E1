package something.TicTacToe;


import javafx.stage.Stage;
import something.Client.Client;
import something.TicTacToe.Gui.GuiSettings;
import something.TicTacToe.Gui.StartGui;

/**
 * Created by samikroon on 3/28/17.
 */
public class Controller {
    private Client client;
    private StartGui startGui;

    public Controller(StartGui startGui) {
        this.startGui = startGui;
    }

    public void login(String username) {
    }

    public void processLogin(String player1, String player2, String username) {
        if (player2 == "Online") {
            System.out.println(username);
            client = new Client();
            System.out.println("login = " + client.login(username));
        }


        startGui.startGameStage();


    }
}
