package something.Reversi.Gui;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import something.Reversi.ReversiBoard;

public class GuiSettings {
    Scene scene;
    ReversiBoard reversiBoard = new ReversiBoard();

    public GuiSettings(){
        try{
            this.scene = makeScene();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Group makeRootGroup(){
        Group rootGroup = new Group();

        Canvas canvas = makeCanvas();
        rootGroup.getChildren().add(canvas);

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        Integer canvasW = (int) canvas.getWidth();
        Integer canvasH = (int) canvas.getHeight();

        drawGrid(graphicsContext, canvasW, canvasH);

        return rootGroup;
    }

    private void drawGrid(GraphicsContext graphicsContext, int canvasW, int canvasH) {
        graphicsContext.setLineWidth(1.0);
        graphicsContext.setStroke(Color.BLACK);
        for(int i = 0; i<canvasW; i=i+75){
            double y = i+0.5;
            graphicsContext.moveTo(0, y);
            graphicsContext.lineTo(canvasH, y);
            graphicsContext.stroke();
        }
        for(int i = 0; i<canvasH; i=i+75){
            double x = i+0.5;
            graphicsContext.moveTo(x, 0);
            graphicsContext.lineTo(x, canvasW);
            graphicsContext.stroke();
        }
    }

    private Canvas makeCanvas() {
        Canvas canvas = new Canvas(600,600);
        return canvas;
    }

    private Scene makeScene() {
        Scene scene = new Scene(makeRootGroup(), 590, 590, Color.AQUAMARINE);
        return scene;
    }
}
