package ca.nicholasmacdonald.chess.board;

import ca.nicholasmacdonald.chess.piece.Bishop;
import ca.nicholasmacdonald.chess.piece.King;
import ca.nicholasmacdonald.chess.piece.Knight;
import ca.nicholasmacdonald.chess.piece.Pawn;
import ca.nicholasmacdonald.chess.piece.Player;
import ca.nicholasmacdonald.chess.piece.Queen;
import ca.nicholasmacdonald.chess.piece.Rook;

import java.util.Arrays;
import java.util.Iterator;

/**
 * The chess board that holds all the squares
 *
 * @author Nicholas MacDonald
 */
public class Board implements Iterable<Square> {
    public static final int NUMBER_OF_SQUARES_IN_LINE = 8;

    private final Square[][] squares;

    public Board() {
        this.squares = new Square[NUMBER_OF_SQUARES_IN_LINE][NUMBER_OF_SQUARES_IN_LINE];
        prepareBoard();
    }

    private void prepareBoard() {
        // Black pieces
        this.squares[0][0] = new Square(0, 0, new Rook(Player.BLACK));
        this.squares[0][1] = new Square(1, 0, new Knight(Player.BLACK));
        this.squares[0][2] = new Square(2, 0, new Bishop(Player.BLACK));
        this.squares[0][3] = new Square(3, 0, new Queen(Player.BLACK));
        this.squares[0][4] = new Square(4, 0, new King(Player.BLACK));
        this.squares[0][5] = new Square(5, 0, new Bishop(Player.BLACK));
        this.squares[0][6] = new Square(6, 0, new Knight(Player.BLACK));
        this.squares[0][7] = new Square(7, 0, new Rook(Player.BLACK));

        // Black pawns
        for (int i = 0; i < NUMBER_OF_SQUARES_IN_LINE; i++) {
            this.squares[1][i] = new Square(i, 1, new Pawn(Player.BLACK));
        }

        // White pieces
        this.squares[7][0] = new Square(0, 7, new Rook(Player.WHITE));
        this.squares[7][1] = new Square(1, 7, new Knight(Player.WHITE));
        this.squares[7][2] = new Square(2, 7, new Bishop(Player.WHITE));
        this.squares[7][3] = new Square(3, 7, new Queen(Player.WHITE));
        this.squares[7][4] = new Square(4, 7, new King(Player.WHITE));
        this.squares[7][5] = new Square(5, 7, new Bishop(Player.WHITE));
        this.squares[7][6] = new Square(6, 7, new Knight(Player.WHITE));
        this.squares[7][7] = new Square(7, 7, new Rook(Player.WHITE));

        // White pawns
        for (int i = 0; i < NUMBER_OF_SQUARES_IN_LINE; i++) {
            this.squares[6][i] = new Square(i, 6, new Pawn(Player.WHITE));
        }

        // Fill in rest of slots with empty squares
        for (int x = 0; x < NUMBER_OF_SQUARES_IN_LINE; x++) {
            for (int y = 2; y < NUMBER_OF_SQUARES_IN_LINE - 2; y++) {
                this.squares[y][x] = new Square(x, y);
            }
        }
    }

    /**
     * Gets the square at the given set of coordinates
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the square
     * @throws IllegalArgumentException if the coordinates are invalid
     */
    public Square getSquare(int x, int y) {
        if (!isCoordinatesValid(x, y)) {
            throw new IllegalArgumentException("Can not get square at invalid coordinates: (" + x + ", " + y + ")");
        }

        return squares[y][x];
    }

    /**
     * Returns true if the given set of coordinates is valid
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return true if the given set of coordinates is valid
     */
    public boolean isCoordinatesValid(int x, int y) {
        return x >= 0 && x <= NUMBER_OF_SQUARES_IN_LINE - 1 && y >= 0 && y <= NUMBER_OF_SQUARES_IN_LINE - 1;
    }

    @Override
    public Iterator<Square> iterator() {
        return Arrays.stream(squares).flatMap(Arrays::stream).iterator();
    }
}
