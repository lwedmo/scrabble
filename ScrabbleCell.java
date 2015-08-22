
import java.awt.Color;

public class ScrabbleCell {

    public int                    x;
    public int                    y;

    // Types
    public static final short     L2            = 1;
    public static final short     L3            = 2;
    public static final short     W2            = 3;
    public static final short     W3            = 4;
    public static final short     PLAIN         = 0;

    private static final short    cellTypes[][] = {
            { W3, 0, 0, L2, 0, 0, 0, W3, 0, 0, 0, L2, 0, 0, W3 },
            { 0, W2, 0, 0, 0, L3, 0, 0, 0, L3, 0, 0, 0, W2, 0 },
            { 0, 0, W2, 0, 0, 0, L2, 0, L2, 0, 0, 0, W2, 0, 0 },
            { L2, 0, 0, W2, 0, 0, 0, L2, 0, 0, 0, W2, 0, 0, L2 },
            { 0, 0, 0, 0, W2, 0, 0, 0, 0, 0, W2, 0, 0, 0, 0 },
            { 0, L3, 0, 0, 0, L3, 0, 0, 0, L3, 0, 0, 0, L3, 0 },
            { 0, 0, L2, 0, 0, 0, L2, 0, L2, 0, 0, 0, L2, 0, 0 },
            { W3, 0, 0, L2, 0, 0, 0, W2, 0, 0, 0, L2, 0, 0, W3 },
            { 0, 0, L2, 0, 0, 0, L2, 0, L2, 0, 0, 0, L2, 0, 0 },
            { 0, L3, 0, 0, 0, L3, 0, 0, 0, L3, 0, 0, 0, L3, 0 },
            { 0, 0, 0, 0, W2, 0, 0, 0, 0, 0, W2, 0, 0, 0, 0 },
            { L2, 0, 0, W2, 0, 0, 0, L2, 0, 0, 0, W2, 0, 0, L2 },
            { 0, 0, W2, 0, 0, 0, L2, 0, L2, 0, 0, 0, W2, 0, 0 },
            { 0, W2, 0, 0, 0, L3, 0, 0, 0, L3, 0, 0, 0, W2, 0 },
            { W3, 0, 0, L2, 0, 0, 0, W3, 0, 0, 0, L2, 0, 0, W3 },

                                                };
    private static final String[] dls           = new String[] { "DOUBLE",
            "LETTER", "SCORE"                  };
    private static final String[] tls           = new String[] { "TRIPLE",
            "LETTER", "SCORE"                  };
    private static final String[] dws           = new String[] { "DOUBLE",
            "WORD", "SCORE"                    };
    private static final String[] tws           = new String[] { "TRIPLE",
            "WORD", "SCORE"                    };

    ScrabbleCell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    ScrabbleCell(ScrabbleCell cell) {
        x = cell.x;
        y = cell.y;
    }

    ScrabbleCell() {
    }

    /**
     * Return our X coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * return our y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @return our letter multiplier
     */
    public int letterPremium() {
        return letterPremium(x, y);
    }

    /**
     * @return the letter multiplier of the cell at x, y
     */
    public static int letterPremium(int x, int y) {
        switch (getCellType(x, y)) {
            case L2:
                return 2;
            case L3:
                return 3;
            default:
                return 1;
        }
    }

    /**
     * @return our word multiplier
     */
    public int wordPremium() {
        return wordPremium(x, y);
    }

    /**
     * @return the word multiplier of the cell at x, y
     */
    public static int wordPremium(int x, int y) {
        switch (getCellType(x, y)) {
            case W2:
                return 2;
            case W3:
                return 3;
            default:
                return 1;
        }
    }

    /**
     * @return our background color
     */
    public Color getColor() {
        return getColor(x, y);
    }

    /**
     * @return the background color of the cell at x, y
     */
    public static Color getColor(int x, int y) {
        // ? Create colors
        switch (getCellType(x, y)) {
            case L2:
                return ScrabbleColor.doubleLetterScore;
            case L3:
                return ScrabbleColor.tripleLetterScore;
            case W2:
                return ScrabbleColor.doubleWordScore;
            case W3:
                return ScrabbleColor.tripleWordScore;
            default:
                return ScrabbleColor.plain;
        }
    }

    public static short getCellType(int x, int y) {
        return cellTypes[x][y];
    }

    public static String[] getWords(int x, int y) {
        switch (getCellType(x, y)) {
            case L2:
                return dls;
            case L3:
                return tls;
            case W2:
                return dws;
            case W3:
                return tws;
        }
        return null;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}