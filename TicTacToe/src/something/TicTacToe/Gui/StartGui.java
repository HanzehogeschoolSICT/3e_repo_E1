package something.TicTacToe.Gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import something.TicTacToe.Controller;

import java.util.Optional;


public class StartGui extends Application{
    private Stage primaryStage, gameStage, waitPopUp;
    private Controller controller;
    private Alert confirmGame;
    private GameBoard board;
    
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
        gameStage.setScene((board = new GameBoard(controller)).scene);
        gameStage.show();
        addShutdownOnClose(gameStage);
    }

    public void endGameStage() {
        gameStage.close();
        gameStage = null;
    }
    
    public GameBoard getBoard() {
    	return board;
    }

    public void waitPopUp () {
    	waitPopUp = new Stage();
        waitPopUp.setTitle("Wait or Challenge");
        waitPopUp.setScene(new WaitPopUp(controller).scene);
        waitPopUp.show();
        addShutdownOnClose(waitPopUp);
    }

    public void closeWaitPopUp() {
    	waitPopUp.close();
    	waitPopUp = null;
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
}
