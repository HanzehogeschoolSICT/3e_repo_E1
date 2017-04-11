package something.Reversi.Gui;

import java.util.Optional;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import something.Client.event.GameEvent;
import something.Client.event.events.MatchStartEvent;
import something.Reversi.Controller;

public class StartGui extends Application{
    private Stage primaryStage;
    private Stage gameStage;
    private Stage waitPopUp;
    private Controller controller;
    private Alert confirmGame;
    private GuiSettings guiSettings;
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.controller = new Controller(this);
        
		showInitPopUp();

/*
        final PhongMaterial redmaterial = new PhongMaterial();
        redmaterial.setSpecularColor(Color.ORANGE);
        redmaterial.setDiffuseColor(Color.RED);

        Cylinder cylinder = new Cylinder(160, 130);
        cylinder.setTranslateX(200);
        cylinder.setTranslateY(-25);
        cylinder.setTranslateZ(600);
        Rotate rxBox = new Rotate(0, 0, 0, 0, Rotate.X_AXIS);
        Rotate ryBox = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        Rotate rzBox = new Rotate(0, 0, 0, 0, Rotate.Z_AXIS);
        rxBox.setAngle(00);
        ryBox.setAngle(90);
        rzBox.setAngle(60);
        cylinder.getTransforms().addAll(rxBox, ryBox, rzBox);
        cylinder.setMaterial(redmaterial);

        PointLight light = new PointLight();
        light.setTranslateX(350);
        light.setTranslateY(100);
        light.setTranslateZ(300);

        // Create a Camera to view the 3D Shapes
        PerspectiveCamera camera = new PerspectiveCamera(false);
        camera.setTranslateX(50);
        camera.setTranslateY(-50);
        camera.setTranslateZ(200);

        Group root = new Group(cylinder, light);

        // Create a Scene with depth buffer enabled
        Scene scene = new Scene(root, 400, 200, true);
        // Add the Camera to the Scene
        scene.setCamera(camera);

        // Add the Scene to the Stage
        primaryStage.setScene(scene);
        // Set the Title of the Stage
        primaryStage.setTitle("An Example with Predefined 3D Shapes");
        // Display the Stage
        primaryStage.show();
*/
    }

    public GuiSettings getGUI() {
    	return guiSettings;
    }
    
    public void addShutdownOnClose(Stage stage){
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    public void waitPopUp () {
        waitPopUp = new Stage();
        waitPopUp.setTitle("Wait or Challenge");
        waitPopUp.setScene(new WaitPopUp(controller).scene);
        waitPopUp.show();
        addShutdownOnClose(waitPopUp);
    }

    public void closeWaitPopUp() {
    	if(waitPopUp != null) {
	        waitPopUp.close();
	        waitPopUp = null;
    	}
    }

    public void showInitPopUp() {
        primaryStage.setTitle("menu");
        primaryStage.setScene(new InitPopUp(controller).scene);
        primaryStage.show();
        addShutdownOnClose(primaryStage);
    }

    public void hideInitPopUp(){
        primaryStage.hide();
    }

    public void startGameStage(){
    	if(gameStage == null) {
	        gameStage = new Stage();
	        gameStage.setTitle("REVERSI!~~~");
	        gameStage.setScene((guiSettings = new GuiSettings(controller)).scene);
	        gameStage.setResizable(false);
	        gameStage.show();
	        addShutdownOnClose(gameStage);
    	}
    }
    
    public void endGameStage() {
        gameStage.close();
        gameStage = null;
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
    
    public void showResult(String result) {
        Alert resultInfo = new Alert(Alert.AlertType.INFORMATION);
        resultInfo.setTitle("Game Result");
        resultInfo.setHeaderText(result);
        resultInfo.setContentText(null);
        resultInfo.show();
    }
}
