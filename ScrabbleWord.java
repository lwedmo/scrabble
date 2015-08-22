public class ScrabbleWord {
    public ScrabbleBoard board;
    public ScrabbleCell  head;
    public boolean       across; // Does the word read accross or down?

    ScrabbleWord(ScrabbleBoard b, ScrabbleCell head, boolean h) {
        this.board = b;
        this.head = new ScrabbleCell(head);
        this.across = h;
    }

    ScrabbleWord(ScrabbleBoard b) {
        this.head = new ScrabbleCell();
        this.board = b;
    }

    public String toString() {
        int N = ScrabbleBoard.DIMENSION;
        int x = head.x;
        int y = head.y;
        int len = 0;
        char s[];
        if (across) {
            for (int i = 0; x + i < N && board.getLetter(x + i, y) != 0; i++) {
                len++; // measure
            }
            s = new char[len]; // allocate
            len = 0;
            for (int i = 0; x + i < N; i++) {
                char c = board.getLetter(x + i, y);
                if (c == 0) {
                    break;
                }
                s[i] = c;
            }
        }
        else {
            for (int i = 0; y + i < N && board.getLetter(x, y + i) != 0; i++) {
                len++; // measure
            }
            s = new char[len];
            len = 0;
            for (int i = 0; y + i < N; i++) {
                char c = board.getLetter(x, y + i);
                if (c == 0) {
                    break;
                }
                s[i] = c;
            }

        }
        return new String(s);
    }

    public static void main(String av[]) {
        ScrabbleBoard board = new ScrabbleBoard();
        ScrabbleWord word = new ScrabbleWord(board);

        board.setLetter(6, 7, 'A', true);
        board.setLetter(7, 7, 'B', true);
        board.setLetter(8, 7, 'C', true);

        word.head.x = 6;
        word.head.y = 7;
        word.across = true;
        System.out.println(word.toString());

        board.setLetter(7, 8, 'D', true);
        board.setLetter(7, 9, 'E', true);
        board.setLetter(7, 10, 'F', true);

        word.head.x = 7;
        word.head.y = 7;
        word.across = false;
        System.out.println(word.toString());

    }
}