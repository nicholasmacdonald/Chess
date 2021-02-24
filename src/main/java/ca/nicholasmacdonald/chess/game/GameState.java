package ca.nicholasmacdonald.chess.game;

/**
 * The different game states that chess can be in
 *
 * @author Nicholas MacDonald
 */
public enum GameState {
    PLAYING("Playing..."),
    BLACK_CHECK("Black is in check!"),
    WHITE_CHECK("White is in check!"),
    BLACK_CHECKMATE("White wins, Black is in checkmate!"),
    WHITE_CHECKMATE("Black wins, White is in checkmate!"),
    STALEMATE("No one wins, stalemate!");

    private final String description;

    GameState(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
