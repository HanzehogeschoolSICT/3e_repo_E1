package something.TicTacToe.Gui;


import javafx.application.Application;
import javafx.stage.Stage;
import something.TicTacToe.Controller;


public class StartGui extends Application{
    private Stage primaryStage;
    private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        controller = new Controller(this);


        this.primaryStage.setTitle("menu");
        this.primaryStage.setScene(new InitPopUp(controller).scene);
        this.primaryStage.show();


    }


    public void startGameStage() {
        primaryStage.hide();
        Stage gameStage = new Stage();
        gameStage.setTitle("Tic Tac Toe");
        gameStage.setScene(new GuiSettings(controller).scene);
        gameStage.show();
    }

    public static void main(String[] args){ Application.launch(args); }

}
