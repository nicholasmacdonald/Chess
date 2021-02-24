package ca.nicholasmacdonald.chess.piece;

import ca.nicholasmacdonald.chess.board.Board;
import ca.nicholasmacdonald.chess.board.Square;

import java.util.Set;

/**
 * The king
 *
 * @author Nicholas MacDonald
 */
public class King extends Piece {
    public King(Player player) {
        super(player);
    }

    @Override
    public Set<Square> getLegalMoves(Board board, Square currentSquare) {
        return getPossibleMoves(board, currentSquare, 1, Direction.values());
    }
}
