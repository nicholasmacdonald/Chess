package ca.nicholasmacdonald.chess.piece;

import ca.nicholasmacdonald.chess.board.Board;
import ca.nicholasmacdonald.chess.board.Square;

import java.util.Set;

/**
 * The rook
 *
 * @author Nicholas MacDonald
 */
public class Rook extends Piece {
    public Rook(Player player) {
        super(player);
    }

    @Override
    public Set<Square> getLegalMoves(Board board, Square currentSquare) {
        return getPossibleMoves(board, currentSquare, Board.NUMBER_OF_SQUARES_IN_LINE, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    }
}
