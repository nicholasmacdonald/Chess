package ca.nicholasmacdonald.chess.board;

import ca.nicholasmacdonald.chess.piece.Piece;

import java.util.Objects;

/**
 * A square on the chess board
 *
 * @author Nicholas MacDonald
 */
public class Square {
    private final int x;
    private final int y;
    private Piece piece;

    public Square(int x, int y) {
        this(x, y, null);
    }

    public Square(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, piece);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Square square = (Square) obj;
        return x == square.x && y == square.y && piece == square.piece;
    }
}
