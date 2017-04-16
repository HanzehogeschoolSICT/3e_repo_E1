package something.TicTacToe.ui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import something.Core.Listenable;
import something.Core.event.events.common.BoardUpdateEvent;
import something.Core.event.events.game.ForfeitEvent;
import something.TicTacToe.Mark;
import something.TicTacToe.TicTacToeBoard;

public class BoardGUI extends Listenable {
    final Scene scene;
    private TicTacToeBoard ticTacToeBoard;
    private EventHandler<MouseEvent> mouseEventEventHandler;
    private GraphicsContext graphicsContext;

    public BoardGUI(TicTacToeBoard ticTacToeBoard, EventHandler<MouseEvent> mouseEventEventHandler){
        ticTacToeBoard.registerEventListener(event -> {
            if (event instanceof BoardUpdateEvent) {
                redrawBoard();
            }
        });
        this.ticTacToeBoard = ticTacToeBoard;
        this.mouseEventEventHandler = mouseEventEventHandler;
        this.scene = makeScene();

    }

    public TicTacToeBoard getTicTacToeBoard() {
    	return ticTacToeBoard;
    }

    private Group makeRootGroup() {
        Group rootGroup = new Group();
        BorderPane borderPane = new BorderPane();
        Canvas canvas = makeCanvas();
        borderPane.setCenter(canvas);
        rootGroup.getChildren().add(borderPane);
        ToolBar toolBar = new ToolBar();
        Button forfeit = new Button("Forfeit");
        forfeit.setOnAction(event -> fireEvent(new ForfeitEvent()));
        toolBar.getItems().add(forfeit);
        borderPane.setBottom(toolBar);

        Integer canvasH = (int)canvas.getHeight();
        Integer canvasW = (int)canvas.getWidth();

        graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);

        drawGrid(canvasH, canvasW);

        return rootGroup;
    }

    public static int getMoveIndex(double x, double y){
        //return  ((int) x/150) + (int) ((y/150)*3);

        int posOnBoard = 0;

        if (x<150.0 && y<150.0){
            posOnBoard = 0;
        } if (150.0<x && x<300.0 && y<150.0){
            posOnBoard = 1;
        } if (300.0<x && x<450.0 && y<150.0){
            posOnBoard = 2;
        } if (x<150.0 && 150.0<y && y<300.0){
            posOnBoard = 3;
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

        return posOnBoard;
    }


    private void redrawBoard(){
        Mark[] bord = ticTacToeBoard.getMarks();
        for(int i = 0; i<bord.length; i++){
            if (bord[i] == Mark.CROSS){ whereToDraw(i, Mark.CROSS); }
            if (bord[i] == Mark.NOUGHT){ whereToDraw(i, Mark.NOUGHT); }
        }

    }

    private void whereToDraw(int posOnBoard, Mark mark){
        int x = posOnBoard % 3;
        int y = posOnBoard / 3;
        drawPlay(x*150+10, y*150+10, mark);
//        if(posOnBoard == 0){ drawPlay(10, 10, mark);   }
//        if(posOnBoard == 1){ drawPlay(160, 10, mark);  }
//        if(posOnBoard == 2){ drawPlay(310, 10, mark);  }
//        if(posOnBoard == 3){ drawPlay(10, 160, mark);  }
//        if(posOnBoard == 4){ drawPlay(160, 160, mark); }
//        if(posOnBoard == 5){ drawPlay(310, 160, mark); }
//        if(posOnBoard == 6){ drawPlay(10, 310, mark);  }
//        if(posOnBoard == 7){ drawPlay(160, 310, mark); }
//        if(posOnBoard == 8){ drawPlay(310, 310, mark); }
    }

    private void drawPlay(double x, double y, Mark mark){
        if (mark==Mark.CROSS){
            drawCross(x, y);
        }
        if (mark==Mark.NOUGHT){
            drawCircle(x, y);
        }
    }

    private void drawCircle(double x, double y){
        graphicsContext.setLineWidth(4);
        graphicsContext.setStroke(Color.CADETBLUE);
        graphicsContext.strokeOval(x,y,120,120);
    }

    private void drawCross(double x, double y){
    	graphicsContext.setLineWidth(4);
    	graphicsContext.setStroke(Color.CORAL);
    	graphicsContext.strokeLine(x,y,x+120,y+120);
    	graphicsContext.strokeLine(x+120,y, x, y+120);
    }

    private Canvas makeCanvas(){
        return new Canvas(500,450);
    }

    private void drawGrid(int height, int width) {
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
        Scene scene = new Scene(makeRootGroup(), 450, 500, Color.LIGHTGRAY);
        return scene;
    }
}
