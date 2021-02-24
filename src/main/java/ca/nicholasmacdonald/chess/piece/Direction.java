package ca.nicholasmacdonald.chess.piece;

/**
 * The different directions a piece can move
 *
 * @author Nicholas MacDonald
 */
public enum Direction {
    NORTH(0, -1),
    NORTH_EAST(1, -1),
    EAST(1, 0),
    SOUTH_EAST(1, 1),
    SOUTH(0, 1),
    SOUTH_WEST(-1, 1),
    WEST(-1, 0),
    NORTH_WEST(-1, -1);

    private final int diffX;
    private final int diffY;

    Direction(int diffX, int diffY) {
        this.diffX = diffX;
        this.diffY = diffY;
    }

    public int getDiffX() {
        return diffX;
    }

    public int getDiffY() {
        return diffY;
    }
}