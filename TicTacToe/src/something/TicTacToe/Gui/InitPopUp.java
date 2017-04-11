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
import javafx.stage.Stage;
import something.Core.AbstractGameController;
import something.Core.Client;
import something.Core.event.GameEvent;
import something.Core.event.GameEventListener;
import something.Core.event.events.client.ChallengeReceiveEvent;
import something.Core.event.events.client.MatchStartEvent;
import something.Core.event.events.common.MoveEvent;
import something.Core.event.events.game.GameFinishedEvent;
import something.Core.event.events.player.YourTurnEvent;
import something.Core.player.ManualPlayer;
import something.Core.player.OnlinePlayer;
import something.Core.player.Player;
import something.TicTacToe.TicTacToeBoard;
import something.TicTacToe.player.TicTacToeAIPlayer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.function.Consumer;

import static something.TicTacToe.Gui.BoardGUI.getMoveIndex;


public class InitPopUp {
    Scene scene;
    private TextField username;
    private final ToggleGroup playerOneGroup = new ToggleGroup();
    private final ToggleGroup playerTwoGroup = new ToggleGroup();
    private StartGui startGui;
    private Consumer<Boolean> plannedMatch;
    private Runnable showGui;

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

                    final Player<TicTacToeBoard> player1;
                    final Player<TicTacToeBoard> player2;

                    EventHandler<MouseEvent> mouseEventHandler = null;

                    switch (playerOne) {
                        case "Me":
                            ManualPlayer<TicTacToeBoard> manualPlayer = (ManualPlayer<TicTacToeBoard>) (player1 = new ManualPlayer<>());
                            mouseEventHandler = event12 -> manualPlayer.makeMove(getMoveIndex(event12.getSceneX(), event12.getSceneY()));
                            break;
                        case "PC":
                            player1 = new TicTacToeAIPlayer();
                            break;
                        default:
                            player1 = null;
                            break;
                    }

                    switch (playerTwo) {
                        case "Online":
                            player2 = new OnlinePlayer<>(getClient(username.getText()));
                            break;
                        case "PC":
                            player2 = new TicTacToeAIPlayer();
                            break;
                        default:
                            player2 = null;
                            break;
                    }

                    if (mouseEventHandler == null) mouseEventHandler = event1 -> {};
                    EventHandler<MouseEvent> finalMouseEventHandler = mouseEventHandler;
                    TicTacToeBoard ticTacToeBoard = new TicTacToeBoard();

                    showGui = () -> Platform.runLater(() -> startGui.startGameStage(ticTacToeBoard, finalMouseEventHandler));
                    plannedMatch = aBoolean -> {
                        Player<TicTacToeBoard> playerOne1 = player1;
                        Player<TicTacToeBoard> playerTwo1 = player2;
                        if (aBoolean) {
                            Player<TicTacToeBoard> temp = playerTwo1;
                            playerTwo1 = playerOne1;
                            playerOne1 = temp;
                        }
                        AbstractGameController<TicTacToeBoard> controller = new AbstractGameController<>(ticTacToeBoard, playerOne1, playerTwo1);
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
                    };

                    if (!(player2 instanceof OnlinePlayer)) {
                        plannedMatch.accept(false);
                        plannedMatch = null;
                    }

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

    public Client getClient(String username) {
        System.out.println(username);
        Client client = new Client();
        InetAddress address = null;
        try {
            address = InetAddress.getByName("localhost");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        client.connect(address, 7789);
        client.login(username);
        startGui.hideInitPopUp();
        Stage waitPopUp = startGui.waitPopUp(client, username);
        client.registerEventListener(new GameEventListener() {
            private boolean ready = false;
            @Override
            public void handleEvent(GameEvent event) {
                System.out.println(event);
                if (event instanceof ChallengeReceiveEvent) {
                    ChallengeReceiveEvent cre = (ChallengeReceiveEvent) event;
                    client.acceptChallenge(cre.getChallengeNumber());
                    ready = true;
                    Platform.runLater(waitPopUp::close);
                    showGui.run();
                }
                if (event instanceof MatchStartEvent) {
                    ready = true;
                    Platform.runLater(waitPopUp::close);
                    showGui.run();
                }
                if (event instanceof MoveEvent && ready) {
                    if (plannedMatch != null) {
                        plannedMatch.accept(true);
                        System.out.println("Running planned match!");
                    }
                }
                if (event instanceof YourTurnEvent && ready) {
                    if (plannedMatch != null) {
                        plannedMatch.accept(false);
                        System.out.println("Running planned match!");
                    }
                }
            }
        });
        return client;
    }

    private Scene makeScene() {
        return new Scene(makePane(), 250, 250, Color.WHEAT);
    }
}