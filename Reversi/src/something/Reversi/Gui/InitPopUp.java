package something.Reversi.Gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import something.Reversi.player.Controller;

public class InitPopUp {
    Scene scene;
    private TextField username;
    final ToggleGroup playerOneGroup = new ToggleGroup();
    final ToggleGroup playerTwoGroup = new ToggleGroup();
    private Controller controller;
    
    public InitPopUp(Controller controller){
    	this.controller = controller;
        try {
            this.scene = makeScene();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Scene makeScene() {
        Scene scene = new Scene(makePane(), 250, 250);
        return scene;
    }

    private BorderPane makePane() {
        BorderPane borderPane = new BorderPane();

        HBox usernameBarRev = new HBox(20);
        Text usernameTextRev = new Text();
        usernameTextRev.setFont(new Font(15));
        usernameTextRev.setText("Username:");
        username = new TextField();
        usernameBarRev.getChildren().addAll(usernameTextRev, username);
        borderPane.setTop(usernameBarRev);

        VBox playerOneRev = new VBox(10);
        VBox playerTwoRev = new VBox(10);

        Insets insets = new Insets(30, 15, 10, 15);
        playerOneRev.setPadding(insets);
        playerTwoRev.setPadding(insets);

        Text playerOneTextRev = new Text();
        playerOneTextRev.setFont(new Font(15));
        playerOneTextRev.setText("Player 1");

        Text playerTwoTextRev = new Text();
        playerTwoTextRev.setFont(new Font(15));
        playerTwoTextRev.setText("Player 2");

        RadioButton playerOneSelfRev = new RadioButton("Me");
        playerOneSelfRev.setUserData("Me");
        RadioButton playerOnePCRev = new RadioButton("PC");
        playerOnePCRev.setUserData("PC");

        RadioButton playerTwoOnlineRev = new RadioButton("Online");
        playerTwoOnlineRev.setUserData("Online");
        RadioButton playerTwoPCRev = new RadioButton("PC");
        playerTwoPCRev.setUserData("PC");

        playerOneSelfRev.setToggleGroup(playerOneGroup);
        playerOnePCRev.setToggleGroup(playerOneGroup);

        playerTwoOnlineRev.setToggleGroup(playerTwoGroup);
        playerTwoPCRev.setToggleGroup(playerTwoGroup);

        playerOneRev.getChildren().addAll(playerOneTextRev, playerOneSelfRev, playerOnePCRev);
        playerTwoRev.getChildren().addAll(playerTwoTextRev, playerTwoOnlineRev, playerTwoPCRev);

        borderPane.setLeft(playerOneRev);
        borderPane.setRight(playerTwoRev);

        Button submit = new Button("Login");
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (playerOneGroup.getSelectedToggle() != null && playerTwoGroup.getSelectedToggle() != null && username.getText() != "") {
                    String playerOne = playerOneGroup.getSelectedToggle().getUserData().toString();
                    String playerTwo = playerTwoGroup.getSelectedToggle().getUserData().toString();
                                        
                    controller.processLogin(playerOne, playerTwo, username.getText());
                    
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
}
