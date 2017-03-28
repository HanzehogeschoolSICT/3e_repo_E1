package GUI;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GuiSettings {
    Scene scene;

    public GuiSettings(){
        try{
            this.scene = makeScene();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private Group makeRootGroup() {
        Group rootGroup = new Group();

        Canvas canvas = makeCanvas();
        rootGroup.getChildren().add(canvas);


        Integer canvasH = (int)canvas.getHeight();
        Integer canvasW = (int)canvas.getWidth();

        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                clicked(event.getSceneX(),event.getSceneY(), graphicsContext);
            }
        });

        drawGrid(graphicsContext, canvasH, canvasW);

        return rootGroup;
    }

    private void clicked(double x, double y, GraphicsContext context){
        System.out.println(x+" "+y);
        if (x<150.0 && y<150.0){
            System.out.println("X=1, Y=1");
//           context.strokeOval(20.0,30.0,50.0,60.0);
        } if (150.0<x && x<300.0 && y<150.0){
            System.out.println("X=2, Y=1");
//            drawThing(2);
        } if (300.0<x && x<450.0 && y<150.0){
            System.out.println("X=4, Y=1");
//            drawThing(3);
        } if (x<150.0 && 150.0<y && y<300.0){
            System.out.println("X=1, Y=2");
//            drawThing(4);
        } if (150.0<x && x<300.0 && 150.0<y && y<300.0){
            System.out.println("X=2, Y=2");
//            drawThing(5);
        } if (300.0<x && x<450.0 && 150.0<y && y<300.0){
            System.out.println("X=3, Y=2");
        } if (x<150.0 && 300.0<y && y<450.0){
            System.out.println("X=1, Y=3");
        } if(150.0<x && x<300.0 &&300.0<y && y<450.0){
            System.out.println("X=2, Y=3");
        } if(300.0<x && x<450.0 && 300.0<y && y<450.0){
            System.out.println("X=3, Y=3");
        } if(0.0<x && 450.0<y || 450.0<x && 0.0<y){
            System.out.println("klik op het bord aub!");
        }
    }

    private void drawThing(int x){ // kan gebruikt worden voor dingen, is nu nog niet het geval
        switch (x) {
            case 1:
                System.out.println("1");
                break;
            case 2:
                System.out.println("2");
                break;
            case 3:
                System.out.println("3");
                break;
            case 4:
                System.out.println("4");
                break;
            case 5:
                System.out.println("5");
                break;
        }
    }

    private Canvas makeCanvas(){
        Canvas canvas = new Canvas(500,500);
        return canvas;
    }

    private void drawGrid(GraphicsContext graphicsContext,int height, int width) {
        graphicsContext.setLineWidth(1.0);
        graphicsContext.setStroke(Color.BLACK);
        for (int i = 0; i < height; i += 150) {
            double x = i + 0.5;
            graphicsContext.moveTo(x, 0);
            graphicsContext.lineTo(x, width);
            graphicsContext.stroke();
        }

        for(int i = 0; i<width; i+=150){
            double y = i+0.5;
            graphicsContext.moveTo(0, y);
            graphicsContext.lineTo(height, y);
            graphicsContext.stroke();
        }
    }

    private Scene makeScene(){
        Scene scene = new Scene(makeRootGroup(),450,450, Color.WHEAT);
        return scene;
    }
}
