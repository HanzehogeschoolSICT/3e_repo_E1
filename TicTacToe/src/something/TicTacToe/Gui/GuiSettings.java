package something.TicTacToe.Gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import something.TicTacToe.Controller;
import something.TicTacToe.Mark;
import something.TicTacToe.TicTacToeBoard;

public class GuiSettings {
    Scene scene;
    Integer turn = 0;
    TicTacToeBoard ticTacToeBoard = new TicTacToeBoard();
    private BorderPane borderPane;
    private Controller controller;
    
    private GraphicsContext graphicsContext;
    
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
        borderPane = new BorderPane();
        Canvas canvas = makeCanvas();
        borderPane.setCenter(canvas);
        rootGroup.getChildren().add(borderPane);

        ToolBar toolBar = new ToolBar();
        Button forfeit = new Button("Forfeit");
        forfeit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean check = controller.forfeit();
                if (!check) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Disconnecting failed");
                    alert.setHeaderText("Forfeiting the match failed, please try again");
                    alert.setContentText(null);
                    alert.show();
                }
            }
        });

        toolBar.getItems().add(forfeit);
        borderPane.setTop(toolBar);

        Integer canvasH = (int)canvas.getHeight();
        Integer canvasW = (int)canvas.getWidth();

        graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	if(controller.getPlayer().hasTurn()) {
	            	int index = getMoveIndex(event.getSceneX(), event.getSceneY());
	    			boolean success = makeMove(index);
	    			
	            	if(success) {
	            		controller.getPlayer().makeMove(index);
	            	}
            	}
            }
        });

        drawGrid(canvasH, canvasW);

        return rootGroup;
    }

    private int getMoveIndex(double x, double y){
        Integer posOnBoard = null;
        
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
        
        return posOnBoard.intValue();
    }

    private int setTurn(){
        Integer whoseTurn = turn%2;
        return whoseTurn;
    }

    public boolean makeMove(int posOnBoard){
        Integer getTurn = setTurn();
        Mark mark = ticTacToeBoard.makeTurn(posOnBoard, getTurn);
        if (mark == Mark.CROSS){
            redrawBoard();
            turn = turn+1;
            return true;
        }
        if (mark == Mark.NOUGHT){
            redrawBoard();
            turn = turn+1;
            return true;
        }
        return false;
    }

    private void redrawBoard(){
        Mark[] bord = ticTacToeBoard.getBoard();
        for(int i = 0; i<bord.length; i++){
            if (bord[i] == Mark.CROSS){ whereToDraw(i, Mark.CROSS); }
            if (bord[i] == Mark.NOUGHT){ whereToDraw(i, Mark.NOUGHT); }
        }

    }

    private void whereToDraw(int posOnBoard, Mark mark){
        if(posOnBoard == 0){ drawPlay(10, 10, mark);}
        if(posOnBoard == 1){ drawPlay(160, 10, mark);}
        if(posOnBoard == 2){ drawPlay(310, 10, mark);}
        if(posOnBoard == 3){ drawPlay(10, 160, mark);}
        if(posOnBoard == 4){ drawPlay(160, 160, mark);}
        if(posOnBoard == 5){ drawPlay(310, 160, mark);}
        if(posOnBoard == 6){ drawPlay(10, 310, mark);}
        if(posOnBoard == 7){ drawPlay(160, 310, mark);}
        if(posOnBoard == 8){ drawPlay(310, 310, mark);}
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
        Canvas canvas = new Canvas(500,500);
        return canvas;
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
        Scene scene = new Scene(makeRootGroup(),450,450, Color.LIGHTGRAY);
        return scene;
    }
}
