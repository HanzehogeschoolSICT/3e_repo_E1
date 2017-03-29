package something.TicTacToe.Gui;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
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

        showInitPopUp();
    }

    public void addShutdownOnClose(Stage stage) {
    	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        }); 
    }
    
    public void showInitPopUp() {
    	primaryStage.setTitle("menu");
        primaryStage.setScene(new InitPopUp(controller).scene);
        primaryStage.show();
        addShutdownOnClose(primaryStage);
    }
    
    public void hideInitPopUp() {
        primaryStage.hide();
    }

    public void startGameStage() {
        gameStage = new Stage();
        gameStage.setTitle("Tic Tac Toe");
        gameStage.setScene(new GuiSettings(controller).scene);
        gameStage.show();
        addShutdownOnClose(gameStage);
    }

    public void endGameStage() {
        gameStage.close();
        gameStage = null;
    }
    
    public void startPopUp () {
        gameStage.close();
        primaryStage.show();
    }

    public void waitPopUp (String message) {
    	if(waitAlert == null) {
	        waitAlert = new Alert(Alert.AlertType.INFORMATION);
	        waitAlert.setTitle("Tic Tac Toe");
	        waitAlert.setHeaderText(null);
	        waitAlert.setContentText(message);
	        waitAlert.initStyle(StageStyle.UNDECORATED);
	        waitAlert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
    	}
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
