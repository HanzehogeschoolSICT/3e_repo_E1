package something.TicTacToe.Gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import something.TicTacToe.Controller;
import something.TicTacToe.Mark;
import something.TicTacToe.TicTacToeBoard;

public class GuiSettings {
    Scene scene;
    Integer turn = 0;
    TicTacToeBoard ticTacToeBoard = new TicTacToeBoard();
    private Controller controller;

    public GuiSettings(Controller controller){
        this.controller = controller;
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
        Integer posOnBoard;
        if (x<150.0 && y<150.0){
            posOnBoard = 0;
            testDrawPlay(posOnBoard, context);
        } if (150.0<x && x<300.0 && y<150.0){
            posOnBoard = 1;
            testDrawPlay(posOnBoard, context);
        } if (300.0<x && x<450.0 && y<150.0){
            posOnBoard = 2;
            testDrawPlay(posOnBoard, context);
        } if (x<150.0 && 150.0<y && y<300.0){
            posOnBoard = 3;
            testDrawPlay(posOnBoard, context);
        } if (150.0<x && x<300.0 && 150.0<y && y<300.0){
            posOnBoard = 4;
            testDrawPlay(posOnBoard, context);
        } if (300.0<x && x<450.0 && 150.0<y && y<300.0){
            posOnBoard = 5;
            testDrawPlay(posOnBoard, context);
        } if (x<150.0 && 300.0<y && y<450.0){
            posOnBoard = 6;
            testDrawPlay(posOnBoard, context);
        } if(150.0<x && x<300.0 &&300.0<y && y<450.0){
            posOnBoard = 7;
            testDrawPlay(posOnBoard, context);
        } if(300.0<x && x<450.0 && 300.0<y && y<450.0){
            posOnBoard = 8;
            testDrawPlay(posOnBoard, context);
        } if(0.0<x && 450.0<y || 450.0<x && 0.0<y){
            System.out.println("klik op het bord aub!");
            ticTacToeBoard.emptyBoard();
            emptyBoard(context);        //TODO: deze functie moet via controller aangeroepen worden door de client
            drawGrid(context,450,450);
        }
    }

    private int setTurn(){              //TODO: deze functie moet uit de server komen, hier wordt bepaald wie aan zet is
        Integer whoseTurn = turn%2;
        System.out.println(whoseTurn);
        return whoseTurn;
    }

    private void testDrawPlay(int posOnBoard, GraphicsContext context){
        Integer getTurn = setTurn();
        Mark mark = ticTacToeBoard.MakeTurn(posOnBoard, getTurn);
        if (mark == Mark.CROSS){
            redrawBoard(context);
            turn = turn+1;
        }
        if (mark == Mark.NOUGHT){
            redrawBoard(context);
            turn = turn+1;
        }
        System.out.println(ticTacToeBoard.toString());
    }

    private void redrawBoard(GraphicsContext context){
        Mark[] bord = ticTacToeBoard.getBoard();
        for(int i = 0; i<bord.length; i++){
            if (bord[i] == Mark.CROSS){ whereToDraw(i, context, Mark.CROSS); }
            if (bord[i] == Mark.NOUGHT){ whereToDraw(i, context, Mark.NOUGHT); }
        }

    }

    private void emptyBoard(GraphicsContext context){
        context.clearRect(0,0,450,450);
    }

    private void whereToDraw(int posOnBoard, GraphicsContext context, Mark mark){
        if(posOnBoard == 0){ drawPlay(10, 10, context, mark);}
        if(posOnBoard == 1){ drawPlay(160, 10, context, mark);}
        if(posOnBoard == 2){ drawPlay(310, 10, context, mark);}
        if(posOnBoard == 3){ drawPlay(10, 160, context, mark);}
        if(posOnBoard == 4){ drawPlay(160, 160, context, mark);}
        if(posOnBoard == 5){ drawPlay(310, 160, context, mark);}
        if(posOnBoard == 6){ drawPlay(10, 310, context, mark);}
        if(posOnBoard == 7){ drawPlay(160, 310, context, mark);}
        if(posOnBoard == 8){ drawPlay(310, 310, context, mark);}
    }

    private void drawPlay(double x, double y, GraphicsContext context, Mark mark){
        if (mark==Mark.CROSS){
            drawCross(x, y, context);
        }
        if (mark==Mark.NOUGHT){
            drawCircle(x, y, context);
        }
    }

    private void drawCircle(double x, double y, GraphicsContext context){
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
