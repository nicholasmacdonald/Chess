package ca.nicholasmacdonald.chess.game;

import ca.nicholasmacdonald.chess.board.Board;
import ca.nicholasmacdonald.chess.board.Square;
import ca.nicholasmacdonald.chess.piece.King;
import ca.nicholasmacdonald.chess.piece.Pawn;
import ca.nicholasmacdonald.chess.piece.Piece;
import ca.nicholasmacdonald.chess.piece.Player;
import ca.nicholasmacdonald.chess.piece.Queen;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the state of the chess game
 *
 * @author Nicholas MacDonald
 */
public class GameManager {
    private final Board board;
    private Player currentPlayer;
    private GameState state;

    private Square selectedSquare;
    private Set<Square> selectedPossibleMoves;

    public GameManager() {
        this.board = new Board();
        this.selectedSquare = null;
        this.selectedPossibleMoves = Collections.emptySet();
        this.currentPlayer = Player.WHITE;
        this.state = GameState.PLAYING;
    }

    /**
     * Handles the selection of a square
     *
     * @param square the square the user clicked
     * @return true if the selection was successful
     */
    public boolean handleSelect(Square square) {
        if (square.getPiece().getPlayer() == currentPlayer) {
            setSelectedSquare(square);
            return true;
        }
        return false;
    }

    /**
     * Handles the move (dropping the selected square)
     *
     * @param moveSquare the square the piece was dropped on
     * @return true if the move was successful
     */
    public boolean handleMove(Square moveSquare) {
        final boolean canMove = selectedPossibleMoves.contains(moveSquare);

        if (canMove) {
            // Move the piece
            Piece piece = selectedSquare.getPiece();
            moveSquare.setPiece(piece);
            selectedSquare.setPiece(null);

            // If pawn hits the end, turn it into a queen
            if (piece instanceof Pawn) {
                final int endY;
                final boolean pawnAtEnd;

                if (currentPlayer == Player.BLACK) {
                    endY = Board.NUMBER_OF_SQUARES_IN_LINE - 1;
                    pawnAtEnd = selectedSquare.getY() == endY - 1;
                } else {
                    endY = 0;
                    pawnAtEnd = selectedSquare.getY() == endY + 1;
                }

                if (pawnAtEnd) {
                    Square endSquare = board.getSquare(selectedSquare.getX(), endY);
                    endSquare.setPiece(new Queen(currentPlayer));
                }
            }

            Player enemyPlayer = currentPlayer == Player.BLACK ? Player.WHITE : Player.BLACK;

            // Check if enemy has moves
            boolean enemyPlayerHasMoves = false;
            for (Square square : getPlayerSquares(enemyPlayer)) {
                if (!getPossibleMoves(square).isEmpty()) {
                    enemyPlayerHasMoves = true;
                    break;
                }
            }

            // Check the state of the game
            if (isInCheck(enemyPlayer)) {
                if (enemyPlayerHasMoves) {
                    state = enemyPlayer == Player.BLACK ? GameState.BLACK_CHECK : GameState.WHITE_CHECK;
                } else {
                    state = enemyPlayer == Player.BLACK ? GameState.BLACK_CHECKMATE : GameState.WHITE_CHECKMATE;
                }
            } else if (!enemyPlayerHasMoves) {
                state = GameState.STALEMATE;
            } else {
                state = GameState.PLAYING;
            }

            currentPlayer = enemyPlayer;
        }

        setSelectedSquare(null);
        return canMove;
    }

    public Square getSelectedSquare() {
        return selectedSquare;
    }

    private void setSelectedSquare(Square selectedSquare) {
        this.selectedSquare = selectedSquare;
        this.selectedPossibleMoves = getPossibleMoves(selectedSquare);
    }

    private Set<Square> getPossibleMoves(Square square) {
        if (square == null || square.getPiece() == null) {
            return Collections.emptySet();
        }

        Set<Square> moves = square.getPiece().getLegalMoves(board, square);

        // Filter out any move that would result in the player being in check
        moves.removeIf(move -> isPlayerInCheckAfterMove(square, move));
        return moves;
    }

    private Set<Square> getPlayerSquares(Player player) {
        Set<Square> squares = new HashSet<>();
        for (Square square : board) {
            if (square.getPiece() != null && square.getPiece().getPlayer() == player) {
                squares.add(square);
            }
        }
        return squares;
    }

    /**
     * Gets the possible moves the selected piece can make
     *
     * @return the possible moves the selected piece can make
     */
    public Set<Square> getSelectedPossibleMoves() {
        return Collections.unmodifiableSet(selectedPossibleMoves);
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getState() {
        return state;
    }

    private boolean isPlayerInCheckAfterMove(Square currentSquare, Square moveSquare) {
        final Player player = currentSquare.getPiece().getPlayer();

        Piece currentPiece = currentSquare.getPiece();
        Piece movePiece = moveSquare.getPiece();

        // Make the temporary move
        currentSquare.setPiece(null);
        moveSquare.setPiece(currentPiece);

        // Check if in check
        boolean check = isInCheck(player);

        // Undo the temporary move after checking
        currentSquare.setPiece(currentPiece);
        moveSquare.setPiece(movePiece);
        return check;
    }

    private boolean isInCheck(Player player) {
        Square kingSquare = getKingSquare(player);

        for (Square square : board) {
            Piece piece = square.getPiece();
            if (piece != null && piece.getPlayer() != player) {
                Set<Square> possibleEnemyMoves = piece.getLegalMoves(board, square);
                if (possibleEnemyMoves.contains(kingSquare)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Square getKingSquare(Player player) {
        for (Square square : board) {
            if (square.getPiece() instanceof King && square.getPiece().getPlayer() == player) {
                return square;
            }
        }

        throw new IllegalStateException("Could not find king on the board");
    }
}
