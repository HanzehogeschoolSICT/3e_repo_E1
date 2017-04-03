package something.Reversi.Gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;
import javafx.scene.transform.Rotate;
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

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("test");
                getMoveIndex(event.getSceneX(), event.getSceneY());
                }
        });

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


    private int getMoveIndex(double x, double y){
        Integer posOnBoard = null;

        if (x<75.0 && y<75.0){
            posOnBoard = 0;
            System.out.println(posOnBoard);
        } if (75.0<x && x<150.0 && y<75.0){
            posOnBoard = 1;
            System.out.println(posOnBoard);
        } if (150.0<x && x<225.0 && y<75.0){
            posOnBoard = 2;
            System.out.println(posOnBoard);
        } if (225.0<x && x<300.0 && y<75.0){
            posOnBoard = 3;
            System.out.println(posOnBoard);
        } if (300.0<x && x<375.0 && y<75.0){
            posOnBoard = 4;
            System.out.println(posOnBoard);
        } if (x<375.0 && 150.0<y && y<75.0){
            posOnBoard = 5;
            System.out.println(posOnBoard);
        } if (75.0<x && x<150.0 && y<75.0){
            posOnBoard = 1;
            System.out.println(posOnBoard);
        } if (150.0<x && x<225.0 && y<75.0){
            posOnBoard = 2;
            System.out.println(posOnBoard);
        } if (225.0<x && x<300.0 && y<75.0){
            posOnBoard = 3;
            System.out.println(posOnBoard);
        } if (300.0<x && x<375.0 && y<75.0){
            posOnBoard = 4;
        } if (x<375.0 && 150.0<y && y<75.0){
            posOnBoard = 5;
        } if (150.0<x && x<300.0 && 150.0<y && y<300.0){
            posOnBoard = 4;
        } if (300.0<x && x<450.0 && 150.0<y && y<300.0){
            posOnBoard = 5;
        } if (x<150.0 && 300.0<y && y<450.0){
            posOnBoard = 6;
        } if(150.0<x && x<300.0 &&300.0<y && y<450.0){
            posOnBoard = 7;
        } if(300.0<x && x<450.0 && 300.0<y && y<450.0){
            posOnBoard = 8;
        }

        return posOnBoard.intValue();
    }

    private Canvas makeCanvas() {
        Canvas canvas = new Canvas(600,600);
        return canvas;
    }

    private void drawBlack(){

    }

    private void drawWhite(){

    }

    private Scene makeScene() {
        Scene scene = new Scene(makeRootGroup(), 590, 590, true);
        return scene;
    }
}
