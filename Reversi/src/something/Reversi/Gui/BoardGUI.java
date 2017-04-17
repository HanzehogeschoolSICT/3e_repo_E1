package something.Reversi.Gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import something.Core.Client;
import something.Core.Listenable;
import something.Core.event.events.common.BoardUpdateEvent;
import something.Core.player.Player;
import something.Reversi.ReversiBoard;
import something.Reversi.Tile;

import java.io.IOException;

public class BoardGUI extends Listenable {
    final Scene scene;
    private final ReversiBoard reversiBoard;
    private EventHandler<MouseEvent> mouseEventEventHandler;
    private GraphicsContext graphicsContext;
    private int canvasW, canvasH;
    private BorderPane borderPane;
    private Label scoreBlack, scoreWhite;
    private Circle turn;
    private int turnCount;

    public BoardGUI(ReversiBoard reversiBoard, EventHandler<MouseEvent> mouseEventEventHandler, Player<ReversiBoard> player1, Player<ReversiBoard> player2) {
        this.reversiBoard = reversiBoard;
        reversiBoard.registerEventListener(event -> {
            if (event instanceof BoardUpdateEvent) {
                redrawBoard();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        redrawScore();
                    }
                });
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
        Tile[] board = reversiBoard.getBoard();
        graphicsContext.clearRect(0, 0, 600, 600);
        drawGrid(canvasW, canvasH);
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

    private void redrawScore() {
        scoreBlack.setText(Integer.toString(reversiBoard.getScore(true)));
        scoreWhite.setText(Integer.toString(reversiBoard.getScore(false)));
        if (turnCount%2 == 0) {
            turn.setFill(Color.BLACK);
        } else {
            turn.setFill(Color.WHITE);
        }
        turnCount++;
    }

    private Canvas makeCanvas() {
        return new Canvas(600,600);
    }

    private Group makeRootGroup() {
        Group rootGroup = new Group();
        borderPane = new BorderPane();
        borderPane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        Canvas canvas = makeCanvas();
        borderPane.setCenter(canvas);
        rootGroup.getChildren().add(borderPane);
        graphicsContext = canvas.getGraphicsContext2D();
        canvasW = (int) canvas.getWidth();
        canvasH = (int) canvas.getHeight();
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEventEventHandler);
        drawGrid(canvasW, canvasH);
        setSideBar();
        redrawBoard();
        return rootGroup;
    }

    private void setSideBar() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().add(new Rectangle(200, 50, Color.GREEN));
        Label turnLabel = new Label("Turn:");
        turnLabel.setFont(Font.font("Helvetica", 18));
        turn = new Circle(30, Color.BLACK);
        turnCount = 1;
        Label scoreBlackLabel = new Label("Score black:");
        scoreBlackLabel.setFont(Font.font("Helvetica", 18));
        scoreBlack = new Label("2");
        scoreBlack.setFont(Font.font("Helvetica", 18));
        Label scoreWhiteLabel = new Label("Score white:");
        scoreWhiteLabel.setFont(Font.font("Helvetica", 18));
        scoreWhite = new Label("2");
        scoreWhite.setFont(Font.font("Helvetica", 18));
        vBox.getChildren().addAll(turnLabel, turn, new Label("\n\n\n") , scoreBlackLabel, scoreBlack, scoreWhiteLabel, scoreWhite);
        borderPane.setRight(vBox);
    }

    public void setToolbar(Client client) {
        ToolBar toolBar = new ToolBar();
        Button forfeit = new Button("Forfeit");
        forfeit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    client.forfeit();
                } catch (IOException e) {
                    displayConnectionError(e);
                }
            }
        });
        toolBar.getItems().add(forfeit);
        borderPane.setBottom(toolBar);
    }

    private void displayConnectionError(IOException e) {
        Alert resultInfo = new Alert(Alert.AlertType.INFORMATION);
        resultInfo.setTitle("ERROR");
        resultInfo.setHeaderText("Network error!\n" + e.getMessage());
        resultInfo.setContentText(null);
        resultInfo.show();
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
        return new Scene(makeRootGroup(), 800, 640, Color.LIGHTGRAY);
    }
}
