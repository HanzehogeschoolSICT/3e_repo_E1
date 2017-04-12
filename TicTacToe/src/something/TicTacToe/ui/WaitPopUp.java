package something.TicTacToe.ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import something.Core.AbstractGameController;
import something.Core.Client;
import something.Core.event.GameEvent;
import something.Core.event.GameEventListener;
import something.Core.event.events.game.GameFinishedEvent;
import something.Core.event.events.game.GameStartEvent;
import something.Core.player.ManualPlayer;
import something.Core.player.OnlinePlayer;
import something.Core.player.Player;
import something.TicTacToe.TicTacToeBoard;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * Created by samikroon on 3/31/17.
 */
public class WaitPopUp {
    private Client client;
    private String username;
    private ListView<String> playersListView = new ListView<>();

    public WaitPopUp(Stage parent, String username, PlayerType type) {
        System.out.println(type);
        this.username = username;
        this.client = new Client();
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        try {
            client.connect(address, 7789);
            client.login(username);
            parent.setScene(makeScene());
            parent.setOnCloseRequest(event -> client.disconnect());
            Player<TicTacToeBoard> player = type.getPlayer();
            OnlinePlayer<TicTacToeBoard> onlinePlayer = new OnlinePlayer<>(client);
            onlinePlayer.registerEventListener(new GameEventListener() {
                @Override
                public void handleEvent(GameEvent event) {
                    if (event instanceof GameStartEvent) {
                        Player<TicTacToeBoard> player1;
                        Player<TicTacToeBoard> player2;
                        if (onlinePlayer.isPlayer1()) {
                            player1 = onlinePlayer;
                            player2 = player;
                        } else {
                            player1 = player;
                            player2 = onlinePlayer;
                        }
                        TicTacToeBoard ticTacToeBoard = new TicTacToeBoard();
                        AbstractGameController<TicTacToeBoard> controller = new AbstractGameController<>(ticTacToeBoard, player1, player2, false);
                        Platform.runLater(() -> {
                            parent.setTitle("Tic Tac Toe");
                            EventHandler<MouseEvent> mouseEventEventHandler = null;
                            if (player instanceof ManualPlayer) {
                                mouseEventEventHandler = mouseEvent -> ((ManualPlayer) player).makeMove(BoardGUI.getMoveIndex(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
                            }
                            parent.setScene(new BoardGUI(ticTacToeBoard, mouseEventEventHandler).scene);
                            parent.setOnCloseRequest(event12 -> controller.interrupt());
                        });
                        controller.registerEventListener(controllerEvent -> {
                            if (controllerEvent instanceof GameFinishedEvent) {
                                Platform.runLater(() -> {
                                    String victoryText = "Tie";
                                    Optional<Boolean> victor = ((GameFinishedEvent) controllerEvent).getVictor();
                                    if (victor.isPresent()) {
                                        if (victor.get()) {
                                            victoryText = "Cross wins!";
                                        } else {
                                            victoryText = "Nought wins!";
                                        }
                                    }
                                    Alert resultInfo = new Alert(Alert.AlertType.INFORMATION);
                                    resultInfo.setTitle("Game Result");
                                    resultInfo.setHeaderText(victoryText);
                                    resultInfo.setContentText(null);
                                    resultInfo.show();
                                });
                            }
                        });
                    }
                }
            });
        } catch (IOException e) {
            displayConnectionError(e);
        }
    }

    private BorderPane makePane() throws IOException {
        BorderPane borderPane = new BorderPane();
        playersListView.setPrefSize(200, 100);
        updateListView();
        Text topText = new Text("Wait for a game or challenge");
        borderPane.setTop(topText);
        borderPane.setCenter(playersListView);
        ToolBar toolBar = new ToolBar(challengeButton(), refreshButton(), subscribeButton());
        borderPane.setBottom(toolBar);

        return borderPane;
    }

    private Button subscribeButton() {
        Button subscribe = new Button("Subscribe");
        subscribe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    client.subscribe("Tic-tac-toe");
                } catch (IOException e) {
                    displayConnectionError(e);
                }
            }
        });
        return subscribe;
    }

    private void displayConnectionError(IOException e) {
        Alert resultInfo = new Alert(Alert.AlertType.INFORMATION);
        resultInfo.setTitle("ERROR");
        resultInfo.setHeaderText("Network error!\n" + e.getMessage());
        resultInfo.setContentText(null);
        resultInfo.show();
    }

    private Button challengeButton() {
        Button challenge = new Button("Challenge");
        challenge.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String opponent = playersListView.getSelectionModel().getSelectedItem();
                if (opponent != null) {
                    try {
                        client.challenge(opponent, "Tic-tac-toe");
                    } catch (IOException e) {
                        displayConnectionError(e);
                    }
                } else {
                    throwAlert();
                }
            }
        });
        return challenge;
    }

    private Button refreshButton() {
        Button refresh = new Button("Refresh");
        refresh.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    updateListView();
                } catch (IOException e) {
                    displayConnectionError(e);
                }
            }
        });
        return refresh;
    }

    private void throwAlert() {
        Alert noSelection = new Alert(Alert.AlertType.ERROR);
        noSelection.setTitle("Warning");
        noSelection.setHeaderText("You did not select a valid opponent");
        noSelection.setContentText(null);
        noSelection.show();
    }

    private void updateListView() throws IOException {
        String[] playerArray = client.getPlayers();

        ObservableList<String> observablePlayerList = FXCollections.observableArrayList();
        for (String player : playerArray) {
            if (!username.equals(player)) {
                observablePlayerList.add(player);
            }
        }
        playersListView.setItems(observablePlayerList);
    }

    private Scene makeScene() throws IOException {
        return new Scene(makePane(), 300, 300, Color.WHEAT);
    }

}
