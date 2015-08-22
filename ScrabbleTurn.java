/**
 * A player's turn.
 */
public class ScrabbleTurn {
    private TilePlace[]    trace;           // The tile placements comprising
                                            // this move.
    private boolean        horizontal;
    private ScrabbleBoard  board;
    private ScrabblePlayer player;          // The player whose turn we are.
    private int            passDiscardCount; // When we choose to pass, how many
                                             // tiles

    // to discard.

    public ScrabbleTurn(ScrabbleBoard board, TilePlace[] trace) {
        this.board = board;
        this.trace = trace;
        horizontal = true;
        if (trace.length > 1 && trace[0].y != trace[1].y) {
            horizontal = false;
        }
        passDiscardCount = 0;
    }

    /**
     * Return the score for this turn.
     */
    public static int getScore() {
        int score = 0;
        /*
         * int wordMultiplier = 1; int N = xtrace.length; for (int i = 0; i < N;
         * i++){ // find all newly amended words int x = xtrace[i]; int y =
         * ytrace[i]; score += board.getLetter(x,y) *
         * ScrabbleCell.getLetterMultiplier(x,y); wordMultiplier *=
         * ScrabbleCell.getWordMultiplier(x,y); } score *= wordMultiplier; if (N ==
         * 7){ score += 50; }
         */
        return score;
    }

    /**
     * Is the move legal on the board?
     */
    public boolean isLegal() {
        return true;
    }

    public void setX() {
    }

    public void setY() {
    }

    public void setHorizontal() {
    }

    public void drawNewTiles() {
    }

    /**
     * Place the tiles on the board comprising this move.
     */
    public void placeTiles(boolean newly) {
        for (int i = 0; i < trace.length; i++) {
            board.setLetter(trace[i].x, trace[i].y, trace[i].letter, !newly);
        }
    }

    /**
     * Remove this turn's tiles from the board.
     */
    public void removeTiles() {
        for (int i = 0; i < trace.length; i++) {
            board.removeLetter(trace[i].x, trace[i].y);
        }
    }

    public boolean isPass() {
        return passDiscardCount > 0 || trace.length == 0;
    }
}