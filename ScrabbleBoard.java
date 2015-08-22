
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the board with played pieces.
 */
public class ScrabbleBoard {
    public static final int DIMENSION = 15;

    public char             crossWord[][];
    private boolean         permanent[][]; // true while the letter is
                                           // provisional
    private boolean         columnEmpty[];
    private boolean         rowEmpty[];

    ScrabbleBoard() {
        clear();
    }

    public void clear() {
        crossWord = new char[DIMENSION][];
        for (int i = 0; i < DIMENSION; i++) {
            crossWord[i] = new char[DIMENSION];
            for (int j = 0; j < DIMENSION; j++) {
                crossWord[i][j] = 0;
            }
        }
        permanent = new boolean[DIMENSION][];
        for (int i = 0; i < DIMENSION; i++) {
            permanent[i] = new boolean[DIMENSION];
            for (int j = 0; j < DIMENSION; j++) {
                permanent[i][j] = false;
            }
        }
        rowEmpty = new boolean[DIMENSION];
        columnEmpty = new boolean[DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            rowEmpty[i] = columnEmpty[i] = true;
        }
    }

    /**
     * @return the letter on the tile at these coordinates. return zero if there
     *         is no tile placed here if there is a blank tile here, return it's
     *         assigned letter Blanks come out in lower case.
     */
    public char getLetter(int x, int y) {
        return crossWord[x][y];
    }

    public void setLetter(int x, int y, char letter, boolean perm) {
        crossWord[x][y] = letter;
        permanent[x][y] = perm;
    }

    public void removeLetter(int x, int y) {
        crossWord[x][y] = 0;
    }

    /**
     * @return the board as a string suitable for display
     */
    public String toString() {
        char s[] = new char[15 * 31];
        int z = 0;
        for (int y = 0; y < DIMENSION; y++) {
            for (int x = 0; x < DIMENSION; x++) {
                char c = crossWord[x][y];
                if (c == 0) {
                    c = '.';
                }
                s[z++] = c;
                s[z++] = ' ';
            }
            s[z++] = '\n';
        }
        return new String(s);
    }

    public void print() {
        System.out.println(this);
    }

    /**
     * return the score for the letter on the board.
     */
    private int tileScore(int x, int y) {
        int s = Letter.getScore(crossWord[x][y]);
        if (!permanent[x][y]) {
            s *= ScrabbleCell.letterPremium(x, y);
        }
        return s;
    }

    /**
     * @return the word premium for this cell This will be 1, 2, or 3.
     */
    public int wordPremiumForCell(int x, int y) {
        return permanent[x][y] ? 1 : ScrabbleCell.wordPremium(x, y);
    }

    /**
     * @return the score for the word with given start and direction.
     */
    public int wordScore(int x, int y, boolean across) {
        int sum = 0;
        int product = 1;
        if (across) {
            for (; x < DIMENSION && crossWord[x][y] != 0; x++) {
                sum += tileScore(x, y);
                if (!permanent[x][y]) {
                    product *= wordPremiumForCell(x, y);
                }
            }
        }
        else {
            for (; y < DIMENSION && crossWord[x][y] != 0; y++) {
                sum += tileScore(x, y);
                if (!permanent[x][y]) {
                    product *= wordPremiumForCell(x, y);
                }
            }
        }
        return sum * product;
    }

    public char getBlankValue(int x, int y) {
        return Character.toUpperCase(crossWord[x][y]);
    }

    /**
     * set the word in the scrabble board
     * 
     * @param word
     *            is the word with blanks
     * @param x
     *            is the horiz. coord of word beginning
     * @param y
     *            is the vert coord of word beginning
     * @param across
     *            is true for across placement, false for down
     * @perm, the permancy flag is true for permnent, false for temporary
     *        placement
     */
    public void setWord(String word, int x, int y, boolean across, boolean perm) {
        if (across) {
            for (int i = 0; i < word.length(); x++) {
                if (crossWord[x][y] == 0) { // cell is empty?
                    char c = word.charAt(i);
                    if (c == Letter.BLANK) {
                        throw new RuntimeException("setting BLANK");
                    }
                    crossWord[x][y] = c;
                    permanent[x][y] = perm;
                    if (perm) {
                        rowEmpty[y] = columnEmpty[x] = false;
                    }
                    i++;
                }
            }
        }
        else {
            for (int i = 0; i < word.length(); y++) {
                if (crossWord[x][y] == 0) {
                    char c = word.charAt(i);
                    if (c == Letter.BLANK) {
                        throw new RuntimeException("setting BLANK");
                    }
                    crossWord[x][y] = c;
                    permanent[x][y] = perm;
                    if (perm) {
                        rowEmpty[y] = columnEmpty[x] = false;
                    }
                    i++;
                }
            }
        }
    }

    /**
     * set a word in the scrabble board as entered by the user In this case, the
     * word is the formed word and thus includes letters already played. Pass
     * blanks as lower case letters.
     * 
     * @param word
     *            is the word with blanks
     * @param x
     *            is the horiz. coord of word beginning
     * @param y
     *            is the vert coord of word beginning
     * @param across
     *            is true for across placement, false for down
     * @perm, the permancy flag is true for permnent, false for temporary
     *        placement
     * @return played tile string
     */
    public String setUserWord(String word, int x, int y, boolean across,
            boolean perm, Rack rack) throws ScrabbleException {
        if (rack.hasBlank() == false) {
            word = word.toUpperCase();
        }
        boolean connected = false;
        Rack rackCopy = new Rack(rack);
        char playedArray[] = new char[7];
        int nplayed = 0;
        if (across) {
            for (int i = 0; i < word.length(); i++) {
                if (x + i >= DIMENSION) {
                    throw new ScrabbleException(
                            "Play would run off the edge of the board.");
                }
                char c = word.charAt(i);
                if (crossWord[x + i][y] == 0) { // cell is empty?
                    if (c == Letter.BLANK) {
                        throw new RuntimeException("setting BLANK");
                    }
                    rackCopy.playTile(c);
                    crossWord[x + i][y] = c;
                    playedArray[nplayed++] = c;
                    permanent[x + i][y] = perm;
                    if (perm) {
                        rowEmpty[y] = columnEmpty[x] = false;
                    }
                    else {
                        if (crossedAt(x + i, y)) {
                            connected = true;
                        }
                    }
                }
                else if (Character.toUpperCase(crossWord[x + i][y]) != c) {
                    throw new ScrabbleException("Word doesn't fit.");
                }
            }
        }
        else {
            for (int i = 0; i < word.length(); i++) {
                if (y + i >= DIMENSION) {
                    throw new ScrabbleException(
                            "Play would run off the edge of the board.");
                }
                char c = word.charAt(i);
                if (crossWord[x][y + i] == 0) {
                    rackCopy.playTile(c);
                    crossWord[x][y + i] = c;
                    playedArray[nplayed++] = c;
                    permanent[x][y + i] = perm;
                    if (perm) {
                        rowEmpty[y] = columnEmpty[x] = false;
                    }
                    else {
                        if (crossedAt(x, y + i)) {
                            connected = true;
                        }
                    }

                }
                else if (Character.toUpperCase(crossWord[x][y + i]) != c) {
                    throw new ScrabbleException("Word doesn't fit.");
                }
            }
        }
        if (perm == false && connected == false) {
            throw new ScrabbleException("Word does not connect.");
        }
        char pa[] = new char[nplayed];
        for (int i = 0; i < nplayed; i++) {
            pa[i] = playedArray[i];
        }
        String played = new String(pa);
        System.out.println("played " + played);
        return played;
    }

    /**
     * Remove the temporary word at the given coordinates.
     */
    public void removeWord(int x, int y, boolean across) {
        if (across) {
            for (; x < DIMENSION && crossWord[x][y] != 0; x++) {
                if (!permanent[x][y]) {
                    crossWord[x][y] = 0;
                }
            }
        }
        else {
            for (; y < DIMENSION && crossWord[x][y] != 0; y++) {
                if (!permanent[x][y]) {
                    crossWord[x][y] = 0;
                }
            }
        }
    }

    /**
     * is there a permanently placed tile at these coordinates
     * 
     * @return true if so.
     */
    public boolean isPermanent(int x, int y) {
        return permanent[x][y];
    }

    public boolean emptyRow(int y) {
        if (y == 7) {
            return false;
        }
        return rowEmpty[y];
    }

    public boolean emptyColumn(int x) {
        if (x == 7) {
            return false;
        }
        return columnEmpty[x];
    }

    public boolean isBlank(int x, int y) {
        return Character.isLowerCase(crossWord[x][y]);
    }

    //
    // unit test
    //
    public static void main(String av[]) {
        ScrabbleBoard b = new ScrabbleBoard();
        b.print();
        b.setLetter(7, 6, 'A', true);
        b.setLetter(7, 7, 'B', true);
        b.setLetter(7, 8, 'C', true);
        b.print();
    }

    public boolean crossedAt(int x, int y) {
        return isCenter(x, y) || anyPermanent(x + 1, y)
                || anyPermanent(x, y + 1) || anyPermanent(x - 1, y)
                || anyPermanent(x, y - 1);
    }

    public boolean anyPermanent(int x, int y) {
        return onBoard(x, y) && getLetter(x, y) != 0 && isPermanent(x, y);
    }

    public boolean onBoard(int x, int y) {
        return inRange(x) && inRange(y);
    }

    public boolean inRange(int coord) {
        return 0 <= coord && coord < DIMENSION;
    }

    public boolean isCenter(int x, int y) {
        return x == 7 && y == 7;
    }

    /**
     * Get the number of temporary tiles.
     */
    public int getTempCount() {
        int count = 0;
        for (int y = 0; y < DIMENSION; y++) {
            for (int x = 0; x < DIMENSION; x++) {
                if (this.permanent[x][y] == false) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Return a list of the placed tiles
     */
    public List getPlacedTileArray() {
        List list = new ArrayList();
        for (int y = 0; y < DIMENSION; y++) {
            for (int x = 0; x < DIMENSION; x++) {
                if (this.crossWord[x][y] != 0 && this.permanent[x][y] == false) {
                    list.add(new TilePlace(this.crossWord[x][y], x, y));
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    public boolean isCenterEmpty() {
        return this.getLetter(7, 7) == 0;
    }

    public boolean hasPermanentNeighbors(boolean across, int x, int y) {
        if (across) {
            return anyPermanent(x - 1, y) || anyPermanent(x + 1, y);
        }
        else {
            return anyPermanent(x, y - 1) || anyPermanent(x, y + 1);
        }
    }
}