package something.Reversi.Gui;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import something.Reversi.Controller;

public class StartGui extends Application{
    private Stage primaryStage;
    private Stage gameStage;
    private Controller controller;
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        this.controller = new Controller(this);
        
        startGameStage();
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
