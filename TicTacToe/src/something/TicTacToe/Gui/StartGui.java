package something.TicTacToe.Gui;


import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import something.TicTacToe.Controller;

import java.util.Optional;


public class StartGui extends Application{
    private Stage primaryStage;
    private Stage gameStage;
    private Controller controller;
    private Alert waitAlert, confirmGame;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        controller = new Controller(this);


        this.primaryStage.setTitle("menu");
        this.primaryStage.setScene(new InitPopUp(controller).scene);
        this.primaryStage.show();

    }


    public void hideInitPopUp() {
        primaryStage.hide();
    }




    public void startGameStage() {
        gameStage = new Stage();
        gameStage.setTitle("Tic Tac Toe");
        gameStage.setScene(new GuiSettings(controller).scene);
        gameStage.show();
    }

    public void startPopUp () {
        gameStage.close();
        primaryStage.show();
    }

    public void waitPopUp () {
        waitAlert = new Alert(Alert.AlertType.INFORMATION);
        waitAlert.setTitle("Tic Tac Toe");
        waitAlert.setHeaderText(null);
        waitAlert.setContentText("Waiting for a game");
        waitAlert.initStyle(StageStyle.UNDECORATED);
        waitAlert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
        waitAlert.show();
    }

    public void closeWaitPopUp() {
        waitAlert.close();
    }

    public boolean confirmGameDialog(String opponentName) {
        confirmGame = new Alert(Alert.AlertType.CONFIRMATION);
        confirmGame.setTitle("You have been challenged");
        confirmGame.setHeaderText("Challenger: " + opponentName);
        confirmGame.setContentText("Would you like to accept this game?");

        Optional<ButtonType> result = confirmGame.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }



    public static void main(String[] args){ Application.launch(args); }

}
