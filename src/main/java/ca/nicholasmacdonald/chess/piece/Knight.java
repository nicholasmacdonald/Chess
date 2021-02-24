package ca.nicholasmacdonald.chess.piece;

import ca.nicholasmacdonald.chess.board.Board;
import ca.nicholasmacdonald.chess.board.Square;

import java.util.HashSet;
import java.util.Set;

/**
 * The knight
 *
 * @author Nicholas MacDonald
 */
public class Knight extends Piece {
    // Knights can jump over pieces
    private static final int[][] POSSIBLE_DIRECTIONS = {
            {1, -2}, {2, -1}, {2, 1}, {1, 2}, {-1, 2}, {-2, 1}, {-2, -1}, {-1, -2}
    };

    public Knight(Player player) {
        super(player);
    }

    @Override
    public Set<Square> getLegalMoves(Board board, Square currentSquare) {
        Set<Square> moves = new HashSet<>();

        final int currentX = currentSquare.getX();
        final int currentY = currentSquare.getY();

        for (int[] dir : POSSIBLE_DIRECTIONS) {
            final int dirX = dir[0];
            final int dirY = dir[1];
            final int x = currentX + dirX;
            final int y = currentY + dirY;

            if (board.isCoordinatesValid(x, y)) {
                Square square = board.getSquare(x, y);
                if (square.getPiece() == null || square.getPiece().getPlayer() != getPlayer()) {
                    moves.add(square);
                }
            }
        }
        return moves;
    }
}
