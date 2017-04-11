package something.TicTacToe.Gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import something.Core.AbstractGameController;
import something.Core.Client;
import something.Core.event.events.game.GameFinishedEvent;
import something.Core.player.ManualPlayer;
import something.Core.player.OnlinePlayer;
import something.Core.player.Player;
import something.TicTacToe.TicTacToeBoard;
import something.TicTacToe.player.TicTacToeAIPlayer;

import java.util.Optional;

import static something.TicTacToe.Gui.BoardGUI.getMoveIndex;


public class InitPopUp {
    Scene scene;
    private TextField username;
    final ToggleGroup playerOneGroup = new ToggleGroup();
    final ToggleGroup playerTwoGroup = new ToggleGroup();
    private StartGui startGui;

    public InitPopUp(StartGui startGui) {
        this.startGui = startGui;
        try {
            this.scene = makeScene();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private BorderPane makePane() {
        BorderPane borderPane = new BorderPane();

        HBox usernameBar = new HBox(20);
        Text usernameText = new Text();
        usernameText.setFont(new Font(15));
        usernameText.setText("Username:");
        username = new TextField();
        usernameBar.getChildren().addAll(usernameText, username);
        borderPane.setTop(usernameBar);

        VBox playerOne = new VBox(10);
        VBox playerTwo = new VBox(10);

        Insets insets = new Insets(30, 15, 10, 15);
        playerOne.setPadding(insets);
        playerTwo.setPadding(insets);

        Text playerOneText = new Text();
        playerOneText.setFont(new Font(15));
        playerOneText.setText("Player 1");

        Text playerTwoText = new Text();
        playerTwoText.setFont(new Font(15));
        playerTwoText.setText("Player 2");

        RadioButton playerOneSelf = new RadioButton("Me");
        playerOneSelf.setUserData("Me");
        playerOneSelf.setSelected(true);
        RadioButton playerOnePC = new RadioButton("PC");
        playerOnePC.setUserData("PC");

        RadioButton playerTwoOnline = new RadioButton("Online");
        playerTwoOnline.setUserData("Online");
        playerTwoOnline.setSelected(true);
        RadioButton playerTwoPC = new RadioButton("PC");
        playerTwoPC.setUserData("PC");

        playerOneSelf.setToggleGroup(playerOneGroup);
        playerOnePC.setToggleGroup(playerOneGroup);

        playerTwoOnline.setToggleGroup(playerTwoGroup);
        playerTwoPC.setToggleGroup(playerTwoGroup);


        playerOne.getChildren().addAll(playerOneText, playerOneSelf, playerOnePC);
        playerTwo.getChildren().addAll(playerTwoText, playerTwoOnline, playerTwoPC);

        borderPane.setLeft(playerOne);
        borderPane.setRight(playerTwo);

        Button submit = new Button("Login");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (playerOneGroup.getSelectedToggle() != null && playerTwoGroup.getSelectedToggle() != null && username.getText().length() > 0) {
                    String playerOne = playerOneGroup.getSelectedToggle().getUserData().toString();
                    String playerTwo = playerTwoGroup.getSelectedToggle().getUserData().toString();
                    //controller.processLogin(playerOne, playerTwo, username.getText());

                    Player<TicTacToeBoard> player1 = null;
                    Player<TicTacToeBoard> player2 = null;

                    EventHandler<MouseEvent> mouseEventHandler = null;

                    switch (playerOne) {
                        case "Me":
                            ManualPlayer<TicTacToeBoard> manualPlayer = (ManualPlayer<TicTacToeBoard>) (player1 = new ManualPlayer<>());
                            mouseEventHandler = event12 -> {
                                    int index = getMoveIndex(event12.getSceneX(), event12.getSceneY());
                                    manualPlayer.makeMove(index);
                                };
                            break;
                        case "PC":
                            player1 = new TicTacToeAIPlayer();
                            break;
                    }

                    switch (playerTwo) {
                        case "Online":
                            player2 = new OnlinePlayer<>(getClient());
                            break;
                        case "PC":
                            player2 = new TicTacToeAIPlayer();
                            break;
                    }

                    if (mouseEventHandler == null) mouseEventHandler = event1 -> {};
                    TicTacToeBoard ticTacToeBoard = new TicTacToeBoard();
                    AbstractGameController<TicTacToeBoard> controller = new AbstractGameController<>(ticTacToeBoard, player1, player2);
                    startGui.startGameStage(ticTacToeBoard, mouseEventHandler);
                    controller.registerEventListener(controllerEvent -> {
                        if (controllerEvent instanceof GameFinishedEvent) {
                            Platform.runLater(() -> {
                                String victoryText = "Tie";
                                Optional<Boolean> victor = ((GameFinishedEvent) controllerEvent).getVictor();
                                if (victor.isPresent()) {
                                    if (victor.get()) {
                                        victoryText = "Player 1 wins!";
                                    } else {
                                        victoryText = "Player 2 wins!";
                                    }
                                }
                                startGui.showResult(victoryText);
                            });
                        }
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("An error occurred!");
                    alert.setHeaderText("Fill in every field please");
                    alert.showAndWait();
                }

            }
        });
        borderPane.setBottom(submit);

        return borderPane;
    }

    private Scene makeScene() {
        Scene scene = new Scene(makePane(), 250, 250, Color.WHEAT);
        return scene;
    }

    public Client getClient() {
        Client client = new Client();
        // TODO Fixme!
        return client;
    }
}