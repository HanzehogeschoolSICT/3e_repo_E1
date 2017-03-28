package something.TicTacToe.Gui;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;

public class GuiAttributes {
    GraphicsContext graphicsContext;
    Label label;

    public GuiAttributes(GraphicsContext graphicsContext){
        try{
            this.graphicsContext = graphicsContext;
        } catch (Exception e){
            e.printStackTrace();
        }
    }


}
