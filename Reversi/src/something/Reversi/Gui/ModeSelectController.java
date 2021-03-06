package something.Reversi.Gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import something.Core.AbstractGameController;
import something.Core.Board;
import something.Core.event.events.game.GameFinishedEvent;
import something.Core.player.ManualPlayer;
import something.Core.player.Player;
import something.Reversi.ReversiBoard;

public class ModeSelectController {
    public Button onlineButton;
    public Button offlineButton;
    public Button testButton;
    public Pane rootPane;
    public TextField usernameField;
    public ChoiceBox<PlayerType> player1Select;
    public ChoiceBox<PlayerType> player2Select;


    @FXML
    protected void initialize() {
        player1Select.getItems().addAll(PlayerType.values());
        player2Select.getItems().addAll(PlayerType.values());
        player1Select.getSelectionModel().select(0);
        player2Select.getSelectionModel().select(0);
    }

    public void startTest(ActionEvent actionEvent) {
        // TODO if spare time
    }

    public void startOnline(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        PlayerType type = player1Select.getValue();
        if (usernameField.getText().length() > 0 && type != null) {
            WaitPopUp waitPopUp = new WaitPopUp(stage, usernameField.getText(), type);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Please fill in an username and a player type");
            alert.setContentText(null);
            alert.show();
        }
    }

    public void startOffline(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();

        Player<ReversiBoard> player1 = player1Select.getSelectionModel().getSelectedItem().getPlayer();
        Player<ReversiBoard> player2 = player2Select.getSelectionModel().getSelectedItem().getPlayer();

        EventHandler<MouseEvent> mouseEventHandler;

        if (player1 instanceof ManualPlayer) {
            if (player2 instanceof ManualPlayer) {
                mouseEventHandler = mouseEvent -> {
                    ((ManualPlayer) player1).makeMove(BoardGUI.getMoveIndex(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
                    ((ManualPlayer) player2).makeMove(BoardGUI.getMoveIndex(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
                };
            } else {
                mouseEventHandler = mouseEvent -> ((ManualPlayer) player1).makeMove(BoardGUI.getMoveIndex(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
            }
        } else if (player2 instanceof ManualPlayer) {
            mouseEventHandler = mouseEvent -> ((ManualPlayer) player2).makeMove(BoardGUI.getMoveIndex(mouseEvent.getSceneX(), mouseEvent.getSceneY()));
        } else {
            mouseEventHandler = mouseEvent -> {};
        }

        ReversiBoard reversiBoard = new ReversiBoard();
        AbstractGameController<ReversiBoard> controller = new AbstractGameController<>(reversiBoard, player1, player2, true);
        controller.start();
        Platform.runLater(() -> {
            stage.setTitle("Reversi");
            stage.setScene(new BoardGUI(reversiBoard, controller, mouseEventHandler, player1, player2).scene);
            stage.setOnCloseRequest(event12 -> {
                controller.interrupt();
                StartGui.shutdown();
            });
        });
        controller.registerEventListener(controllerEvent -> {
            if (controllerEvent instanceof GameFinishedEvent) {
                Platform.runLater(() -> {
                    String victoryText = "Tie";
                    Board.Victor victor = ((GameFinishedEvent) controllerEvent).getVictor();
                    if (victor == Board.Victor.PLAYER1) {
                        victoryText = "Black wins!";
                    } else if (victor == Board.Victor.PLAYER2) {
                        victoryText = "White wins!";
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
