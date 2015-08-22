public class FirstMove {
    private ScrabbleBoard      board;
    private Rack               rack;
    private ScrabbleDictionary dict;
    String                     bestWord;
    char                       bestWord0[];
    int                        bestScore;
    int                        bestX;
    int                        bestY;
    char                       bestBlank[];
    int                        x;
    int                        y;
    String                     word;
    char[]                     word0;
    boolean                    across;
    int                        bi[];
    char                       bv[];
    TileBag                    bag;

    FirstMove(ScrabbleBoard board, Rack rack, ScrabbleDictionary dict,
            TileBag bag) {
        this.board = board;
        this.rack = rack;
        this.dict = dict;
        this.bag = bag;
    }

    public Candidate move() {
        bestScore = 0;
        across = false;
        for (int n = 1; n <= 7; n++) {
            PermuteIterator i = new PermuteIterator(rack.toString(), n);
            while (i.hasNext()) {
                word = new String((char[]) i.next());

                findBlanks();

                switch (bi.length) {

                    case 0:
                        if (dict.isLegal(word)) {
                            tryWord();
                        }
                        break;

                    case 1:
                        for (char c = 'A'; c <= 'Z'; c++) {
                            word0[bi[0]] = c;
                            word = new String(word0);
                            word0[bi[0]] = Letter.BLANK;

                            if (dict.isLegal(word)) {
                                bv[0] = c;
                                tryWord();
                            }
                        }
                        break;

                    case 2:
                        for (char a = 'A'; a <= 'Z'; a++) {
                            for (char b = 'A'; b <= 'Z'; b++) {
                                word0[bi[0]] = a;
                                word0[bi[1]] = b;
                                word = new String(word0);
                                word0[bi[0]] = Letter.BLANK;
                                word0[bi[1]] = Letter.BLANK;

                                if (dict.isLegal(word)) {
                                    bv[0] = a;
                                    bv[1] = b;
                                    tryWord();
                                }
                            }
                        }

                        break;
                }
            }
        }
        Candidate result = null;
        // play it
        if (bestScore > 0) {
            x = bestX;
            y = bestY;
            word = bestWord;
            word0 = bestWord0;
            if (Math.random() < .5) {
                y = x;
                x = 7;
                across = true;
            }
            placeWord(true); // permanent
            rack.remove(new String(word0));
            rack.drawAll(bag);
            result = new Candidate(word, new String(word0), null, x, y, across,
                    bestScore);
        }
        else {
            // Discard
            result = new Candidate();
            result.setDiscard(true);
            result.setPlayed(new String(word0));
        }
        return result;
    }

    public void tryWord() {
        y = 7;
        for (x = 1; x < 14; x++) {
            tryPlacement();
        }
    }

    public void tryPlacement() {
        if (x > 7 || x + word.length() - 1 < 7) {
            return; // out of range
        }
        placeWord(false); // temp.
        scoreIt();
        removeWord();
    }

    public void placeWord(boolean perm) {
        if (across) {
            for (int i = 0; i < word.length(); i++) {
                board.setLetter(x, y + i, word.charAt(i), perm);
            }
        }
        else {
            for (int i = 0; i < word.length(); i++) {
                board.setLetter(x + i, y, word.charAt(i), perm);
            }
        }
    }

    public void removeWord() {
        for (int i = 0; i < word.length(); i++) {
            board.removeLetter(x + i, y);
        }
    }

    public void scoreIt() {
        int sum = 0;
        for (int i = 0; i < word.length(); i++) {
            if (!isBlank(i)) {
                sum += Letter.getScore(word.charAt(i))
                        * ScrabbleCell.letterPremium(x + i, y);
            }
        }
        sum *= 2;
        if (word.length() == 7) {
            sum += 50;
        }
        if (sum > bestScore) {
            bestWord = new String(word);
            bestWord0 = word0;
            bestScore = sum;
            bestX = x;
            bestY = y;
            bestBlank = bv;
        }
    }

    // Is this the of one of the blanks being iterated?
    public boolean isBlank(int index) {
        return word0[index] == Letter.BLANK;
    }

    public int score() {
        return bestScore;
    }

    public String blanks() {
        return bestBlank == null ? "" : new String(bestBlank);
    }

    // mark the blanks.
    public void findBlanks() {
        word0 = new char[word.length()];
        int n = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            word0[i] = c;
            if (c == Letter.BLANK) {
                n++; // count a blank
            }
        }
        bi = new int[n];
        bv = new char[n];

        n = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == Letter.BLANK) {
                bi[n++] = i;
            }
        }
    }

    public static void main(String av[]) {
        ScrabbleDictionary dict = new ScrabbleDictionary();
        for (;;) {
            Rack rack = new Rack();
            ScrabbleBoard board = new ScrabbleBoard();
            TileBag bag = new TileBag();
            rack.drawAll(bag);
            FirstMove m = new FirstMove(board, rack, dict, bag);
            m.move();
            board.print();
            System.out.println(m.score() + " points");
            rack.print();
            String bl = m.blanks();
            if (bl.length() > 0) {
                System.out.println("blanks " + bl);
            }
            if (bl.length() == 2) {
                break;
            }
        }
    }
}