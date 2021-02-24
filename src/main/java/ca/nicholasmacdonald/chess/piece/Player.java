package ca.nicholasmacdonald.chess.piece;

/**
 * The different players that can play chess
 *
 * @author Nicholas MacDonald
 */
public enum Player {
    BLACK("Black"),
    WHITE("White");

    private final String name;

    Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
