package ca.nicholasmacdonald.chess.piece;

import ca.nicholasmacdonald.chess.board.Board;
import ca.nicholasmacdonald.chess.board.Square;

import java.util.HashSet;
import java.util.Set;

/**
 * A piece that can be on a square
 *
 * @author Nicholas MacDonald
 */
public abstract class Piece {
    private final Player player;

    protected Piece(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the possible moves the piece can make in a direction
     *
     * @param board the board
     * @param currentSquare the square that the piece is currently on
     * @param range how many squares the piece can move
     * @param direction the direction
     * @return a set of the possible moves
     */
    protected Set<Square> getPossibleMoves(Board board, Square currentSquare, int range, Direction direction) {
        Set<Square> moves = new HashSet<>();

        for (int x = currentSquare.getX() + direction.getDiffX(), y = currentSquare.getY() + direction.getDiffY(), i = 0;
             board.isCoordinatesValid(x, y) && i < range; x += direction.getDiffX(), y += direction.getDiffY(), i++) {
            Square square = board.getSquare(x, y);
            if (square.getPiece() != null) {
                if (square.getPiece().getPlayer() != getPlayer()) {
                    moves.add(square);
                }
                break;
            }

            moves.add(square);
        }

        return moves;
    }

    /**
     * Gets the possible moves the piece can make in a list of directions
     *
     * @param board the board
     * @param currentSquare the square that the piece is currently on
     * @param range how many squares the piece can move
     * @param directions the directions
     * @return a set of the possible moves
     */
    protected Set<Square> getPossibleMoves(Board board, Square currentSquare, int range, Direction... directions) {
        Set<Square> moves = new HashSet<>();
        for (Direction direction : directions) {
            moves.addAll(getPossibleMoves(board, currentSquare, range, direction));
        }
        return moves;
    }

    /**
     * Gets the current legal moves the piece can make
     *
     * @param board the board
     * @param currentSquare the square that the piece is currently on
     * @return the legal moves
     */
    public abstract Set<Square> getLegalMoves(Board board, Square currentSquare);

    // Don't override equals - Only ever check reference
}
