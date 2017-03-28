package something.TicTacToe.Gui;


import javafx.application.Application;
import javafx.stage.Stage;
import something.TicTacToe.Controller;


public class StartGui extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception{
        Controller controller = new Controller();
        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(new GuiSettings().scene);
        primaryStage.show();

        Stage secondaryStage = new Stage();
        secondaryStage.setTitle("Login");
        secondaryStage.setScene(new InitPopUp(controller).scene);
        secondaryStage.show();

    }

    public static void main(String[] args){ Application.launch(args); }

}
