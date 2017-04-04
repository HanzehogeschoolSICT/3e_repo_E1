package something.Reversi.Gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import something.Client.player.Player;
import something.Reversi.Controller;
import something.Reversi.IllegalMoveException;
import something.Reversi.ReversiBoard;
import something.Reversi.Tile;

public class GuiSettings {
    Scene scene;
    Integer turn = 0;
    ReversiBoard reversiBoard = new ReversiBoard();
    private GraphicsContext graphicsContext;
    private Controller controller;
    
    public GuiSettings(Controller controller){
    	this.controller = controller;
        try{
            this.scene = makeScene();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public ReversiBoard getReversiBoard() {
    	return reversiBoard;
    }
    
    private Group makeRootGroup(){
        Group rootGroup = new Group();

        Canvas canvas = makeCanvas();
        rootGroup.getChildren().add(canvas);

        graphicsContext = canvas.getGraphicsContext2D();

        Integer canvasW = (int) canvas.getWidth();
        Integer canvasH = (int) canvas.getHeight();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = getMoveIndex(event.getSceneX(), event.getSceneY());

                Player player = controller.getPlayerOnTurn();
                if(player != null) {
                	boolean success = makeMove(index);
                	
                	if(success) {
                		player.makeMove(index);
                	}
                }
                
                System.out.println(reversiBoard.toString());
            }
        });

        drawGrid(canvasW, canvasH);
        redrawBoard();

        return rootGroup;
    }

    private void drawGrid(int canvasW, int canvasH) {
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

    private int setTurn(){
        return turn%2;
    }

    private int getMoveIndex(double x, double y) {
        int xCoord = (int) Math.floor(y / 75) * 8;
        int yCoord = (int) Math.floor(x / 75);
        int posOnBoard = xCoord + yCoord;

        System.out.println("tile on board: " + posOnBoard);

        return posOnBoard;
    }

    public boolean makeMove(int posOnBoard){
        int getTurn = setTurn();
        boolean makeTurn = false;
        try {
            makeTurn = reversiBoard.makeTurn(posOnBoard, getTurn);
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
        if(makeTurn == true){
            redrawBoard();
            turn = turn+1;
            return true;
        }
        else {
            return false;
        }
    }

    private Canvas makeCanvas() {
        Canvas canvas = new Canvas(600,600);
        return canvas;
    }

    private void redrawBoard(){
        Tile[] board = reversiBoard.getBoard();
//        x = modulo, y = delen, vermenigvuldig met grootte tegel
        for(int i = 0; i<board.length; i++){
            int xCoord = i%8*75;
            int yCoord = i/8*75;
            if (board[i] == Tile.BLACK) { drawPlay(xCoord, yCoord, Tile.BLACK); }
            if (board[i] == Tile.WHITE) { drawPlay(xCoord, yCoord, Tile.WHITE); }
        }
    }

    private void drawPlay(int xCoord, int yCoord, Tile tile){
//        int x = (xCoord*75)+5;
//        int y = (yCoord*75)+5;
        if(tile == Tile.BLACK){ drawBlack(xCoord, yCoord); }
        if(tile == Tile.WHITE){ drawWhite(xCoord, yCoord); }
    }

    private void drawBlack(int x, int y){
        int xNew = x+5;
        int yNew = y+5;
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillOval(xNew, yNew, 60, 60);
    }

    private void drawWhite(int x, int y){
        int xNew = x+7;
        int yNew = y+7;
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(xNew, yNew, 60, 60);
    }


    private Scene makeScene() {
        Scene scene = new Scene(makeRootGroup(), 590, 590, Color.DARKGREEN);
        return scene;
    }
}
