package something.TicTacToe.Gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import something.Core.Client;
import something.TicTacToe.TicTacToeBoard;

import java.util.Optional;


public class StartGui extends Application{
    private Stage primaryStage, gameStage, waitPopUp;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        //controller = new Controller(this);

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
        primaryStage.setScene(new InitPopUp(this).scene);
        primaryStage.show();
        addShutdownOnClose(primaryStage);
    }
    
    public void hideInitPopUp() {
        primaryStage.hide();
    }

    public void startGameStage(TicTacToeBoard ticTacToeBoard, EventHandler<MouseEvent> mouseEventHandler) {
    	if(gameStage == null) {
	        gameStage = new Stage();
	        gameStage.setTitle("Tic Tac Toe");
            gameStage.setScene(new BoardGUI(ticTacToeBoard, mouseEventHandler).scene);
	        gameStage.show();
	        addShutdownOnClose(gameStage);
    	}
    }

    public void endGameStage() {
    	if(gameStage != null) {
	        gameStage.close();
	        gameStage = null;
    	}
    }

    public Stage waitPopUp (Client client, String username) {
    	waitPopUp = new Stage();
        waitPopUp.setTitle("Wait or Challenge");
        waitPopUp.setScene(new WaitPopUp(client, username).scene);
        waitPopUp.show();
        addShutdownOnClose(waitPopUp);
        return waitPopUp;
    }

    public void closeWaitPopUp() {
    	if(waitPopUp != null) {
	    	waitPopUp.close();
	    	waitPopUp = null;
    	}
    }

    public void showResult(String result) {
        Alert resultInfo = new Alert(Alert.AlertType.INFORMATION);
        resultInfo.setTitle("Game Result");
        resultInfo.setHeaderText(result);
        resultInfo.setContentText(null);
        resultInfo.show();
    }

    public boolean confirmGameDialog(String opponentName) {
        Alert confirmGame = new Alert(Alert.AlertType.CONFIRMATION);
        confirmGame.setTitle("You have been challenged");
        confirmGame.setHeaderText("Challenger: " + opponentName);
        confirmGame.setContentText("Would you like to accept this game?");
        
        Optional<ButtonType> result = confirmGame.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
}
