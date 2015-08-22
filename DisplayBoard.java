public class DisplayBoard {
    public final static int DIMENSION = 15;
    private char            crossWord[][];

    public char getLetter(int x, int y) {
        return crossWord[x][y];
    }

    /**
     * construct from a ScrabbleBoard object
     */
    DisplayBoard(ScrabbleBoard board) {
        crossWord = new char[DIMENSION][];
        for (int i = 0; i < DIMENSION; i++) {
            crossWord[i] = new char[DIMENSION];
            for (int j = 0; j < DIMENSION; j++) {
                crossWord[i][j] = board.getLetter(i, j);
            }
        }
    }
}