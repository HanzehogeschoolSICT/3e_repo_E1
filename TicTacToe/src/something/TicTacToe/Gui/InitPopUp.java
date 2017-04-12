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
import something.Core.event.events.game.GameFinishedEvent;
import something.Core.player.ManualPlayer;
import something.Core.player.Player;
import something.TicTacToe.TicTacToeBoard;
import something.TicTacToe.player.TicTacToeAIPlayer;

import java.util.Optional;

import static something.TicTacToe.Gui.BoardGUI.getMoveIndex;


public class InitPopUp {
    private Scene scene;
    private final ToggleGroup playerOneGroup = new ToggleGroup();
    private final ToggleGroup playerTwoGroup = new ToggleGroup();

    public InitPopUp(Stage parent) {
        this.scene = makeScene();
        parent.setScene(scene);
    }

    private BorderPane makePane() {
        BorderPane borderPane = new BorderPane();

        HBox usernameBar = new HBox(20);
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

        RadioButton playerTwoOnline = new RadioButton("Human");
        playerTwoOnline.setUserData("Human");
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
                if (playerOneGroup.getSelectedToggle() != null && playerTwoGroup.getSelectedToggle() != null) {
                    String playerOne = playerOneGroup.getSelectedToggle().getUserData().toString();
                    String playerTwo = playerTwoGroup.getSelectedToggle().getUserData().toString();

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
                        case "Human":   // TODO: bind controls
                            player2 = new ManualPlayer<>();
                            break;
                        case "PC":
                            player2 = new TicTacToeAIPlayer();
                            break;
                        default:
                            player2 = null;
                            break;
                    }
                    EventHandler<MouseEvent> finalMouseEventHandler = mouseEventHandler;
                    TicTacToeBoard ticTacToeBoard = new TicTacToeBoard();

                    Platform.runLater(() -> {
                        Stage gameStage = new Stage();
                        gameStage.setTitle("Tic Tac Toe");
                        gameStage.setScene(new BoardGUI(ticTacToeBoard, finalMouseEventHandler).scene);
                        gameStage.show();
                    });

                    AbstractGameController<TicTacToeBoard> controller = new AbstractGameController<>(ticTacToeBoard, player1, player2);
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
                                Alert resultInfo = new Alert(Alert.AlertType.INFORMATION);
                                resultInfo.setTitle("Game Result");
                                resultInfo.setHeaderText(victoryText);
                                resultInfo.setContentText(null);
                                resultInfo.show();
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
        return new Scene(makePane(), 250, 250, Color.WHEAT);
    }

    public Scene getScene() {
        return scene;
    }
}