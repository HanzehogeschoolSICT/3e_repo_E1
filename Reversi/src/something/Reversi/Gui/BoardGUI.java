package something.Reversi.Gui;


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
import something.Core.IllegalMoveException;
import something.Core.event.events.game.ForfeitEvent;
import something.Reversi.ReversiBoard;
import something.Reversi.Tile;

public class BoardGUI extends Listenable {
    Scene scene;
    ReversiBoard reversiBoard;
    private EventHandler<MouseEvent> mouseEventEventHandler;
    private GraphicsContext graphicsContext;
    private Integer canvasH, canvasW;
    
    public BoardGUI(ReversiBoard reversiBoard, EventHandler<MouseEvent> mouseEventEventHandler){
        this.reversiBoard = reversiBoard;
        this.mouseEventEventHandler = mouseEventEventHandler;
        this.scene = makeScene();
        reversiBoard.registerEventListener(event -> {
            if (event instanceof BoardUpdateEvent) {
                redrawBoard();
            }
        });
    }

    public ReversiBoard getReversiBoard() {
    	return reversiBoard;
    }
    
    private Group makeRootGroup(){
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

        canvasH = (int)canvas.getHeight();
        canvasW = (int)canvas.getWidth();

        graphicsContext = canvas.getGraphicsContext2D();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);

        drawGrid(canvasW, canvasH);

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



    public static int getMoveIndex(double x, double y) {
        int xCoord = (int) Math.floor(y / 75) * 8;
        int yCoord = (int) Math.floor(x / 75);
        int posOnBoard = xCoord + yCoord;

        System.out.println("tile on board: " + posOnBoard);

        return posOnBoard;
    }

    public boolean makeMove(int posOnBoard){

        boolean makeTurn = false;
        try {
            makeTurn = reversiBoard.makeMove(posOnBoard, false); //TODO: which player is on turn?
        } catch (IllegalMoveException e) {
            e.printStackTrace();
        }
        if(makeTurn){
            redrawBoard();
            return true;
        }
        else {
            return false;
        }
    }

    private Canvas makeCanvas() {
        return new Canvas(600,600);
    }

    private void redrawBoard(){
        Tile[] board = reversiBoard.getBoard();
        graphicsContext.clearRect(0,0,600,600);
        drawGrid(canvasW, canvasH);
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
