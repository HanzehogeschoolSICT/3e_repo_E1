package something.TicTacToe.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import something.Core.GameTask;

import java.io.IOException;

public class StartGUI extends Application {
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GameTask.start();
        Parent modeSelect = FXMLLoader.load(StartGUI.class.getResource("/fxml/modeSelect.fxml"));

        Scene scene = new Scene(modeSelect, 200, 350);

        stage.setTitle("Game mode select");
        stage.setScene(scene);
        stage.show();
    }

    public static void shutdown() {
        GameTask.stop();
        System.exit(0);
    }
}
