package Gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GuiSettings {
    Scene scene;
    Integer turn = 0;

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
            drawPlay(10, 10, context);
        } if (150.0<x && x<300.0 && y<150.0){
            drawPlay(160, 10, context);
        } if (300.0<x && x<450.0 && y<150.0){
            drawPlay(310, 10, context);
        } if (x<150.0 && 150.0<y && y<300.0){
            drawPlay(10, 160, context);
        } if (150.0<x && x<300.0 && 150.0<y && y<300.0){
            drawPlay(160, 160, context);
        } if (300.0<x && x<450.0 && 150.0<y && y<300.0){
            drawPlay(310, 160, context);
        } if (x<150.0 && 300.0<y && y<450.0){
            drawPlay(10, 310, context);
        } if(150.0<x && x<300.0 &&300.0<y && y<450.0){
            drawPlay(160, 310, context);
        } if(300.0<x && x<450.0 && 300.0<y && y<450.0){
            drawPlay(310, 310, context);
        } if(0.0<x && 450.0<y || 450.0<x && 0.0<y){
            System.out.println("klik op het bord aub!");
        }
    }

    private int setTurn(){
        Integer whoseTurn = turn%2;
        turn=turn+1;
        System.out.println(whoseTurn);
        return whoseTurn;
    }

    private void drawPlay(double x, double y, GraphicsContext context){
        if(setTurn()==0){
            drawCircle(x, y, context);
        } else {
            drawCross(x, y, context);
        }
    }

    private void drawCircle(double x, double y, GraphicsContext context){
        System.out.println("x: "+x+"y: "+y);
        context.setLineWidth(4);
        context.setStroke(Color.CADETBLUE);
        context.strokeOval(x,y,120,120);
    }

    private void drawCross(double x, double y, GraphicsContext context){
        context.setLineWidth(4);
        context.setStroke(Color.CORAL);
        context.strokeLine(x,y,x+120,y+120);
        context.strokeLine(x+120,y, x, y+120);
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
        Scene scene = new Scene(makeRootGroup(),450,450, Color.LIGHTGRAY);
        return scene;
    }
}
