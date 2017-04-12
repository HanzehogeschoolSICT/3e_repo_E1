package something.TicTacToe.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StartGUI extends Application {
    public static void main(String[] args) throws IOException {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent modeSelect = FXMLLoader.load(StartGUI.class.getResource("/fxml/modeSelect.fxml"));

        Scene scene = new Scene(modeSelect, 200, 300);

        stage.setTitle("Game mode select");
        stage.setScene(scene);
        stage.show();
    }
}
