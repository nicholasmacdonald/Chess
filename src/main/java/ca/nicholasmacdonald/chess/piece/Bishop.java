package ca.nicholasmacdonald.chess.piece;

import ca.nicholasmacdonald.chess.board.Board;
import ca.nicholasmacdonald.chess.board.Square;

import java.util.Set;

/**
 * The bishop
 *
 * @author Nicholas MacDonald
 */
public class Bishop extends Piece {
    public Bishop(Player player) {
        super(player);
    }

    @Override
    public Set<Square> getLegalMoves(Board board, Square currentSquare) {
        return getPossibleMoves(board, currentSquare, Board.NUMBER_OF_SQUARES_IN_LINE, Direction.NORTH_EAST, Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.NORTH_WEST);
    }
}
