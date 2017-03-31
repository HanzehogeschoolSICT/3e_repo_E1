package something.TicTacToe.Gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import something.TicTacToe.Controller;

/**
 * Created by samikroon on 3/31/17.
 */
public class WaitPopUp {
    Scene scene;
    Controller controller;
    private ListView<String> playersListView = new ListView<>();

    public WaitPopUp(Controller controller) {
        this.controller = controller;
        try{
            this.scene = makeScene();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private BorderPane makePane() {
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
                controller.getClient().subscribe("Tic-tac-toe");
            }
        });
        return subscribe;
    }

    private Button challengeButton() {
        Button challenge = new Button("Challenge");
        challenge.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String opponent = playersListView.getSelectionModel().getSelectedItem();
                if (opponent != null) {
                    controller.getClient().challenge(opponent, "Tic-tac-toe");
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
                updateListView();
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

    private void updateListView () {
    	String[] playerArray = controller.getClient().getPlayers();
        
        ObservableList<String> observablePlayerList = FXCollections.observableArrayList();
        for (String player : playerArray) {
        	if(!controller.getUsername().equals(player)) {
        		observablePlayerList.add(player);
        	}
        }
        playersListView.setItems(observablePlayerList);
    }

    private Scene makeScene(){
        Scene scene = new Scene(makePane(), 300, 300, Color.WHEAT);
        return scene;
    }
}
