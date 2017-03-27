package GUI;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;

/**
 * Created by MSI on 27-3-2017.
 */
public class GuiSettings {
    Scene scene;

    public GuiSettings(){
        try{
            this.scene = makeScene();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private VBox rootBox(){
        VBox vBox = new VBox();
        return vBox;
    }

    private Scene makeScene(){
        Scene scene = new Scene(rootBox(), 500, 500);
        return scene;
    }
}
