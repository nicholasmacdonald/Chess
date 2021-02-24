package ca.nicholasmacdonald.chess.piece;

import ca.nicholasmacdonald.chess.board.Board;
import ca.nicholasmacdonald.chess.board.Square;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The pawn
 *
 * @author Nicholas MacDonald
 */
public class Pawn extends Piece {
    public Pawn(Player colour) {
        super(colour);
    }

    private boolean hasMoved(Square currentSquare) {
        return getPlayer() == Player.BLACK ? currentSquare.getY() != 1 : currentSquare.getY() != Board.NUMBER_OF_SQUARES_IN_LINE - 2;
    }

    @Override
    public Set<Square> getLegalMoves(Board board, Square currentSquare) {
        Set<Square> moves = new HashSet<>();

        final Direction advancingDirection;
        final Direction[] attackingDirections;

        if (getPlayer() == Player.BLACK) {
            advancingDirection = Direction.SOUTH;
            attackingDirections = new Direction[]{Direction.SOUTH_WEST, Direction.SOUTH_EAST};
        } else {
            advancingDirection = Direction.NORTH;
            attackingDirections = new Direction[]{Direction.NORTH_WEST, Direction.NORTH_EAST};
        }

        final boolean moved = hasMoved(currentSquare);

        // Add all advancing moves - filter out any moves that would be attacking a piece
        moves.addAll(getPossibleMoves(board, currentSquare, moved ? 1 : 2, advancingDirection).stream()
                .filter(move -> move.getPiece() == null)
                .collect(Collectors.toSet()));

        // Add all attacking moves - filter out any moves that would not be attacking a piece
        moves.addAll(getPossibleMoves(board, currentSquare, 1, attackingDirections).stream()
                .filter(move -> move.getPiece() != null)
                .collect(Collectors.toSet()));

        return moves;
    }
}
