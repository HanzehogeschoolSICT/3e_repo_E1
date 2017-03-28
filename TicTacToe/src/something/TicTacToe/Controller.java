package something.TicTacToe;


import something.Client.Client;

/**
 * Created by samikroon on 3/28/17.
 */
public class Controller {
    private Client client;

    public Controller() {

    }

    public void login(String username) {
    }

    public void setPlayers(String player1, String player2, String username) {
        if (player2 == "Online") {
            System.out.println(username);
            client = new Client();
            System.out.println("login = " + client.login(username));
        }
    }
}
