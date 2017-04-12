package something.TicTacToe.ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import something.TicTacToe.Gui.InitPopUp;
import something.TicTacToe.Gui.WaitPopUp;

public class ModeSelectController {
    public Button onlineButton;
    public Button offlineButton;
    public Button testButton;
    public Pane rootPane;

    public void startTest(ActionEvent actionEvent) {
        System.out.println(actionEvent);

    }

    public void startOffline(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        InitPopUp initPopUp = new InitPopUp(stage);
    }

    public void startOnline(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        WaitPopUp testUser = new WaitPopUp(stage, "TestUser");
    }
}
