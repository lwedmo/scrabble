public class ScrabbleSlot {
    public int               freespot[];
    public char              formed[];
    ScrabbleDictionary       dict;
    private static final int N = ScrabbleBoard.DIMENSION;

    ScrabbleSlot(ScrabbleBoard board, int x, int y, int len, boolean across,
            ScrabbleDictionary dict) {
        this.dict = dict;
        freespot = new int[len];
        int f = 0; // index into the formed array
        int i = 0;
        int formlen = 0;
        int tilesLeft = len;
        if (across) {
            while (x > 0 && board.getLetter(x - 1, y) != 0) {
                x--;
            }
            int x0 = x;
            for (; x0 < N; x0++) {
                char c = board.crossWord[x0][y];
                if (c == 0) {
                    if (tilesLeft <= 0) {
                        break;
                    }
                    tilesLeft--;
                }
            }
            formlen = x0 - x;
            formed = new char[formlen];
            for (x0 = x; i < formlen; i++, x0++) {
                char c = board.crossWord[x0][y];
                if (c == 0) {
                    freespot[f++] = i;
                }
                formed[i] = c;
            }
        }
        else {//down
            while (y > 0 && board.getLetter(x, y - 1) != 0) {
                y--;
            }
            int y0 = y;
            for (; y0 < N; y0++) {
                char c = board.crossWord[x][y0];
                if (c == 0) {
                    if (tilesLeft <= 0) {
                        break;
                    }
                    tilesLeft--;
                }
            }
            formlen = y0 - y;
            formed = new char[formlen];
            for (y0 = y; i < formlen; i++, y0++) {
                char c = board.crossWord[x][y0];
                if (c == 0) {
                    freespot[f++] = i;
                }
                formed[i] = c;
            }
        }
        //System.out.println(this);
    }

    public boolean doesItFit() {
        return dict.isLegal(formed);
    }

    /**
     * Word test using a single character
     */
    public boolean doesItFit(char c, int i) {
        formed[freespot[i]] = c;
        return dict.isLegal(formed);
    }

    public void set(char c, int i) {
        formed[freespot[i]] = c;
    }

    public String toString() {
        for (int i = 0; i < freespot.length; i++) {
            formed[freespot[i]] = '_';
        }
        return new String(formed);
    }

    public void init(String s) {
        for (int i = 0; i < s.length(); i++) {
            formed[freespot[i]] = s.charAt(i);
        }
    }

    public void init(char[] a) {
        for (int i = 0; i < a.length; i++) {
            formed[freespot[i]] = a[i];
        }
    }

    public void init(char c, int i) {
        formed[freespot[i]] = c;
    }
}