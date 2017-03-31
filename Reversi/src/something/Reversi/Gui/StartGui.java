package something.Reversi.Gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class StartGui extends Application{
    private Stage primaryStage;
    private Stage gameStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;

        startGameStage();

//        showInitPopUp();
    }

    private void addShutdownOnClose(Stage stage){
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    private void showInitPopUp() {
        primaryStage.setTitle("menu");
        primaryStage.setScene(new InitPopUp().scene);
        primaryStage.show();
        addShutdownOnClose(primaryStage);
    }

    private void hideInitPopUp(){
        primaryStage.hide();
    }

    private void startGameStage(){
        gameStage = new Stage();
        gameStage.setTitle("REVERSI!~~~");
        gameStage.setScene(new GuiSettings().scene);
        gameStage.setResizable(false);
        gameStage.show();
        addShutdownOnClose(gameStage);
    }

    public static void main(String[] args){ Application.launch(args);}
}
