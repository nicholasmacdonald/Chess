package ca.nicholasmacdonald.chess;

import ca.nicholasmacdonald.chess.board.Board;
import ca.nicholasmacdonald.chess.board.Square;
import ca.nicholasmacdonald.chess.game.GameManager;
import ca.nicholasmacdonald.chess.piece.Bishop;
import ca.nicholasmacdonald.chess.piece.King;
import ca.nicholasmacdonald.chess.piece.Knight;
import ca.nicholasmacdonald.chess.piece.Pawn;
import ca.nicholasmacdonald.chess.piece.Piece;
import ca.nicholasmacdonald.chess.piece.Player;
import ca.nicholasmacdonald.chess.piece.Queen;
import ca.nicholasmacdonald.chess.piece.Rook;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles user input and rendering using the processing library
 *
 * @author Nicholas MacDonald
 */
public class Chess extends PApplet {
    private static final int BOARD_SIZE = 800;
    private static final int SQUARE_SIZE = BOARD_SIZE / Board.NUMBER_OF_SQUARES_IN_LINE;
    private static final int PIECE_SIZE = 100;

    private static final int HIGHLIGHT_STROKE_WEIGHT = 5;
    private static final int POSSIBLE_MOVE_ELLIPSE_SIZE = 25;

    private static final int TEXT_AREA_WIDTH = BOARD_SIZE;
    private static final int TEXT_AREA_HEIGHT = 100;
    private static final int TEXT_AREA_X = BOARD_SIZE / 2;
    private static final int TEXT_AREA_Y = 50;
    private static final int TEXT_AREA_TEXT_SIZE = 24;
    private static final int TEXT_AREA_TEXT_X = 10;

    private static final int RESTART_BUTTON_SIZE = 70;
    private static final int RESTART_BUTTON_X = 750;
    private static final int RESTART_BUTTON_Y = TEXT_AREA_HEIGHT / 2;

    // Colours
    private static final int BLACK = 0;
    private static final int WHITE = 255;
    private static final int GREY = 80;

    // Colours obtained from color(r, g, b)
    private static final int CREAM = -1184302;
    private static final int OLIVE = -8807079;
    private static final int BROWN = -7248860;
    private static final int RED = -65536;
    private static final int LIGHT_RED = -49839;

    // The amount of milliseconds that the failed selection highlight should stay
    private static final int FAILED_SELECTION_MILLIS = 120;

    private final Map<Class<? extends Piece>, PImage> blackImageMap;
    private final Map<Class<? extends Piece>, PImage> whiteImageMap;

    private GameManager gameManager;

    // Failed selection highlight
    private Square failedSelectionSquare;
    private long failedSelectionTimestamp;

    private PImage restartButtonImage;

    public Chess() {
        this.blackImageMap = new HashMap<>();
        this.whiteImageMap = new HashMap<>();
        this.gameManager = new GameManager();
    }

    @Override
    public void settings() {
        size(BOARD_SIZE, BOARD_SIZE + TEXT_AREA_HEIGHT);
    }

    @Override
    public void setup() {
        loadChessImages();
        rectMode(CENTER);
        ellipseMode(CENTER);
        imageMode(CENTER);
    }

    @Override
    public void draw() {
        background(WHITE);
        checkForPieceDrop();
        drawTiles();
        drawTextArea();
        drawButtons();
        drawHighlights();
        drawPieces();
        drawPossibleMoves();
    }

    @Override
    public void mousePressed() {
        // If the restart button was clicked
        if (isMouseInRect(RESTART_BUTTON_X, RESTART_BUTTON_Y, restartButtonImage.width, restartButtonImage.height)) {
            restartGame();
            return;
        }

        Square clickedSquare = getSquareAtMouse();
        if (clickedSquare != null && clickedSquare.getPiece() != null) {
            boolean selected = gameManager.handleSelect(clickedSquare);

            if (!selected) {
                failedSelectionSquare = clickedSquare;
                failedSelectionTimestamp = millis();
            }
        }
    }

    private void loadChessImages() {
        for (Class<? extends Piece> pieceClass : Arrays.asList(Bishop.class, King.class, Knight.class, Pawn.class, Queen.class, Rook.class)) {
            final String typeName = pieceClass.getSimpleName().toLowerCase();
            final String blackPath = "images/pieces/black-" + typeName + ".png";
            final String whitePath = "images/pieces/white-" + typeName + ".png";
            final PImage blackImage = loadImage(blackPath);
            final PImage whiteImage = loadImage(whitePath);
            blackImage.resize(PIECE_SIZE, PIECE_SIZE);
            whiteImage.resize(PIECE_SIZE, PIECE_SIZE);
            blackImageMap.put(pieceClass, blackImage);
            whiteImageMap.put(pieceClass, whiteImage);
        }

        restartButtonImage = loadImage("images/restart-button.png");
        restartButtonImage.resize(RESTART_BUTTON_SIZE, RESTART_BUTTON_SIZE);

        // Change from black to white
        restartButtonImage.filter(INVERT);
    }

    private void checkForPieceDrop() {
        if (!mousePressed && gameManager.getSelectedSquare() != null) {
            Square square = getSquareAtMouse();
            if (square != null) {
                gameManager.handleMove(square);
            }
        }
    }

    private void drawTiles() {
        noStroke();

        for (Square square : gameManager.getBoard()) {
            final int squareX = square.getX();
            final int squareY = square.getY();
            final int pixelX = getPixelXFromSquareX(square.getX());
            final int pixelY = getPixelYFromSquareY(square.getY());

            fill((squareX + squareY) % 2 == 0 ? CREAM : OLIVE);
            rect(pixelX, pixelY, SQUARE_SIZE, SQUARE_SIZE);
        }
    }

    private void drawTextArea() {
        noStroke();
        fill(BROWN);
        rect(TEXT_AREA_X, TEXT_AREA_Y, TEXT_AREA_WIDTH, TEXT_AREA_HEIGHT);

        fill(WHITE);
        textSize(TEXT_AREA_TEXT_SIZE);
        text("Current Move: " + gameManager.getCurrentPlayer().getName(), TEXT_AREA_TEXT_X, 30);
        text(gameManager.getState().getDescription(), TEXT_AREA_TEXT_X, 80);
    }

    private void drawButtons() {
        image(restartButtonImage, RESTART_BUTTON_X, RESTART_BUTTON_Y);
    }

    private void drawHighlights() {
        Square selectedSquare = gameManager.getSelectedSquare();

        // Highlight current square
        Square square = getSquareAtMouse();
        if (square != null) {
            if (selectedSquare == null) {
                highlight(square, BLACK);
            } else if (!square.equals(selectedSquare) && !gameManager.getSelectedPossibleMoves().contains(square)) {
                highlight(square, RED);
            }
        }

        // Highlight selected square when dragging piece
        if (selectedSquare != null) {
            highlight(selectedSquare, BLACK);
        }

        // Highlight failed selection
        if (failedSelectionSquare != null) {
            highlight(failedSelectionSquare, RED);

            if (millis() - failedSelectionTimestamp >= FAILED_SELECTION_MILLIS) {
                failedSelectionSquare = null;
            }
        }
    }

    private void highlight(Square square, int colour) {
        noFill();
        strokeWeight(HIGHLIGHT_STROKE_WEIGHT);

        final int pixelX = getPixelXFromSquareX(square.getX());
        final int pixelY = getPixelYFromSquareY(square.getY());
        final int size = SQUARE_SIZE - HIGHLIGHT_STROKE_WEIGHT;
        stroke(colour);
        rect(pixelX, pixelY, size, size);
    }

    private void drawPossibleMoves() {
        Square selectedSquare = gameManager.getSelectedSquare();

        if (selectedSquare != null) {
            for (Square square : gameManager.getSelectedPossibleMoves()) {
                final int pixelX = getPixelXFromSquareX(square.getX());
                final int pixelY = getPixelYFromSquareY(square.getY());
                final int colour = square.getPiece() == null ? GREY : LIGHT_RED;

                noStroke();
                fill(colour, 200);
                ellipse(pixelX, pixelY, POSSIBLE_MOVE_ELLIPSE_SIZE, POSSIBLE_MOVE_ELLIPSE_SIZE);
            }
        }

    }

    private void drawPieces() {
        fill(BLACK);

        // Show all the pieces except for the selected piece
        for (Square square : gameManager.getBoard()) {
            Piece piece = square.getPiece();
            if (piece != null && gameManager.getSelectedSquare() != square) {
                drawPiece(piece, getPixelXFromSquareX(square.getX()), getPixelYFromSquareY(square.getY()));
            }
        }

        // Show the selected piece
        Square selectedSquare = gameManager.getSelectedSquare();
        if (selectedSquare != null) {
            drawPiece(selectedSquare.getPiece(), mouseX, mouseY);
        }
    }

    private void drawPiece(Piece piece, int pixelX, int pixelY) {
        Class<? extends Piece> clazz = piece.getClass();
        PImage image = piece.getPlayer() == Player.BLACK ? blackImageMap.get(clazz) : whiteImageMap.get(clazz);
        image(image, pixelX, pixelY);
    }

    private boolean isMouseInRect(int x, int y, int width, int height) {
        final int halfWidth = width / 2;
        final int halfHeight = height / 2;

        return mouseX >= x - halfWidth &&
                mouseX <= x + halfWidth &&
                mouseY >= y - halfHeight &&
                mouseY <= y + halfHeight;
    }

    private void restartGame() {
        this.gameManager = new GameManager();
    }

    private Square getSquareAtMouse() {
        final int squareX = getSquareXFromPixelX(mouseX);
        final int squareY = getSquareYFromPixelY(mouseY);
        return gameManager.getBoard().isCoordinatesValid(squareX, squareY) ? gameManager.getBoard().getSquare(squareX, squareY) : null;
    }

    private static int getPixelXFromSquareX(int squareX) {
        return (squareX + 1) * SQUARE_SIZE - SQUARE_SIZE / 2;
    }

    private static int getPixelYFromSquareY(int squareY) {
        return (squareY + 1) * SQUARE_SIZE - SQUARE_SIZE / 2 + TEXT_AREA_HEIGHT;
    }

    private static int getSquareXFromPixelX(int pixelX) {
        return pixelX / SQUARE_SIZE;
    }

    private static int getSquareYFromPixelY(int pixelY) {
        // Quick fix for hovering over the text area causing the squares under it to be highlighted
        if (pixelY >= 0 && pixelY <= TEXT_AREA_Y + TEXT_AREA_HEIGHT / 2) {
            return -1;
        }

        return (pixelY - TEXT_AREA_HEIGHT) / SQUARE_SIZE;
    }

    // Entry point of the application
    public static void main(String[] args) {
        PApplet.main(Chess.class);
    }
}
