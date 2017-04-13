package something.Reversi.Gui;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import something.Core.Listenable;
import something.Core.event.events.common.BoardUpdateEvent;
import something.Reversi.ReversiBoard;
import something.Reversi.Tile;

public class BoardGUI extends Listenable {
    final Scene scene;
    private final ReversiBoard reversiBoard;
    private EventHandler<MouseEvent> mouseEventEventHandler;
    private GraphicsContext graphicsContext;
    private int canvasW, canvasH;

    public BoardGUI(ReversiBoard reversiBoard, EventHandler<MouseEvent> mouseEventEventHandler) {
        this.reversiBoard = reversiBoard;
        reversiBoard.registerEventListener(event -> {
            if (event instanceof BoardUpdateEvent) {
                redrawBoard();
            }
        });
        this.mouseEventEventHandler = mouseEventEventHandler;
        this.scene = makeScene();

    }


    public static int getMoveIndex(double x, double y) {
        int xCoord = (int) Math.floor(y / 75) * 8;
        int yCoord = (int) Math.floor(x / 75);

        return xCoord + yCoord;
    }

    private void drawPlay(int xCoord, int yCoord, Tile tile) {
        if (tile == Tile.BLACK) {
            drawBlack(xCoord, yCoord);
        }
        if (tile == Tile.WHITE) {
            drawWhite(xCoord, yCoord);
        }
    }

    private void drawBlack(int x, int y) {
        int xNew = x + 5;
        int yNew = y + 5;
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillOval(xNew, yNew, 60, 60);
    }

    private void drawWhite(int x, int y) {
        int xNew = x + 7;
        int yNew = y + 7;
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillOval(xNew, yNew, 60, 60);
    }

    private void redrawBoard() {
        System.out.println("before board init");
        Tile[] board = reversiBoard.getBoard();
        System.out.println("lel");
        graphicsContext.clearRect(0, 0, 600, 600);
        drawGrid(canvasW, canvasH);
//        x = modulo, y = delen, vermenigvuldig met grootte tegel
        for (int i = 0; i < board.length; i++) {
            int xCoord = i % 8 * 75;
            int yCoord = i / 8 * 75;
            if (board[i] == Tile.BLACK) {
                drawPlay(xCoord, yCoord, Tile.BLACK);
            }
            if (board[i] == Tile.WHITE) {
                drawPlay(xCoord, yCoord, Tile.WHITE);
            }
        }
    }

    private Canvas makeCanvas() {
        return new Canvas(600,600);
    }

    private Group makeRootGroup() {
        Group rootGroup = new Group();

        Canvas canvas = makeCanvas();
        rootGroup.getChildren().add(canvas);

        graphicsContext = canvas.getGraphicsContext2D();

        canvasW = (int) canvas.getWidth();
        canvasH = (int) canvas.getHeight();

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);

        drawGrid(canvasW, canvasH);
        redrawBoard();

        return rootGroup;
    }

    private void drawGrid(int canvasW, int canvasH) {
        graphicsContext.setLineWidth(1.0);
        graphicsContext.setStroke(Color.BLACK);
        for (int i = 0; i < canvasW; i = i + 75) {
            double y = i + 0.5;
            graphicsContext.moveTo(0, y);
            graphicsContext.lineTo(canvasH, y);
            graphicsContext.stroke();
        }

        for (int i = 0; i < canvasH; i = i + 75) {
            double x = i + 0.5;
            graphicsContext.moveTo(x, 0);
            graphicsContext.lineTo(x, canvasW);
            graphicsContext.stroke();
        }

    }

    private Scene makeScene() {
        return new Scene(makeRootGroup(), 600, 600, Color.LIGHTGRAY);
    }
}
