package something.TicTacToe.Gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import something.TicTacToe.Controller;


public class InitPopUp {
    Scene scene;
    private TextField username;
    final ToggleGroup playerOneGroup = new ToggleGroup();
    final ToggleGroup playerTwoGroup = new ToggleGroup();
    private Controller controller;

    public InitPopUp(Controller controller){
        this.controller = controller;
        try{
            this.scene = makeScene();
        } catch (Exception e){
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
        RadioButton playerOnePC = new RadioButton("PC");
        playerOnePC.setUserData("PC");

        RadioButton playerTwoOnline = new RadioButton("Online");
        playerTwoOnline.setUserData("Online");
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
                if (playerOneGroup.getSelectedToggle() != null && playerTwoGroup.getSelectedToggle() != null &&
                        username.getText() != null) {
                    String playerOne = playerOneGroup.getSelectedToggle().getUserData().toString();
                    String playerTwo = playerTwoGroup.getSelectedToggle().getUserData().toString();
                    controller.setPlayers(playerOne, playerTwo, username.getText());
                    controller.login(username.getText());
                    /*System.out.println(playerOneGroup.getSelectedToggle().getUserData().toString());
                    System.out.println(playerTwoGroup.getSelectedToggle().getUserData().toString());
                    System.out.println(username.getText());*/
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("You ****** up");
                    alert.setHeaderText("Fill in every field please");
                    alert.showAndWait();
                }

            }
        });
        borderPane.setBottom(submit);

        return borderPane;
    }





    private Scene makeScene(){
        Scene scene = new Scene(makePane(),200 ,200 , Color.WHEAT);
        return scene;
    }
}