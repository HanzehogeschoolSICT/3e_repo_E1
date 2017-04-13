package something.Reversi.Gui;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ModeSelectController {
    public Button onlineButton;
    public Button offlineButton;
    public Button testButton;
    public Pane rootPane;
    public TextField usernameField;
    public ChoiceBox<PlayerType> playerSelect;


    @FXML
    protected void initialize() {
        ObservableList<PlayerType> items = playerSelect.getItems();
        items.addAll(PlayerType.values());
    }

    public void startTest(ActionEvent actionEvent) {
        System.out.println(actionEvent);

    }

    public void startOffline(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        InitPopUp initPopUp = new InitPopUp(stage);
    }

    public void startOnline(ActionEvent actionEvent) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        PlayerType type = playerSelect.getValue();
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
    
}
