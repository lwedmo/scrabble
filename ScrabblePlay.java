
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;

public class ScrabblePlay {
    private ScrabbleBoard       board;
    private static final int    N      = ScrabbleBoard.DIMENSION;
    private Rack                rack;
    private TileBag             bag;
    private int                 x;
    private int                 y;
    private boolean             across;

    private int                 bestScore;
    private int                 len;                             // The number
                                                                 // of letters
                                                                 // played
    public static final boolean ACROSS = true;
    public static final boolean DOWN   = false;
    public static final int     TOOFAR = 100;
    private String              word;
    private char[]              word0;
    private ScrabbleDictionary  dict;
    private ScrabbleWord        words[];
    private int                 nwords;
    public TreeSet              candidates;
    private ScrabbleSlot        slot;
    private DictionaryWord      dw;
    private int                 pcount;
    private Date                startDate;
    private int                 nblank;
    private int                 bix0;
    private int                 bix1;

    ScrabblePlay(ScrabbleBoard board, Rack rack, ScrabbleDictionary dict,
            TileBag bag) {
        this.board = board;
        this.rack = rack;
        this.dict = dict;
        this.words = new ScrabbleWord[8];
        for (int i = 0; i < words.length; i++) {
            words[i] = new ScrabbleWord(board);
        }
        this.bag = bag;
        candidates = new TreeSet(new CandidateComparator());
        dw = new DictionaryWord();
    }

    public Candidate move(Rack r) {
        rack = r;
        return move();
    }

    /**
     * find the move
     */
    public Candidate move() {
        if (board.getLetter(7, 7) == 0) {
            FirstMove first = new FirstMove(board, rack, dict, bag);
            return first.move();
        }
        Candidate result = null;

        // initialize the search
        bestScore = 0;
        candidates.clear();
        pcount = 0;
        startDate = new Date();

        playDirection(ACROSS);
        playDirection(DOWN);

        // show candidates

        /*
         * Iterator it = candidates.iterator(); while(it.hasNext()){ Object o =
         * it.next(); Candidate c = (Candidate)o; c.print(); }
         */

        synchronized (rack) {
            if (candidates.size() == 0) {
                // pass?
                System.out.println("*** pass ***");
                result = new Candidate();
                result.setDiscard(true);
                result.setPlayed(rack.toString());
                rack.exchange(rack.toString(), bag); // discard all
            }
            else {
                Candidate c = chooseMove();
                result = c;
                c.print();
                word = c.getPlayed();
                x = c.getX();
                y = c.getY();
                across = c.getAcross();
                bestScore = c.getScore();
                placeWord(true); // permanent
                rack.remove(c.getPlayed()); // to remove the blanks.
            }
            rack.drawAll(bag);
        }

        if (pcount > 0) {
            long elapsed = new Date().getTime() - startDate.getTime();
            long rate = elapsed == 0 ? 0 : (pcount / elapsed);
            System.out.println(pcount + " permutations, " + elapsed + " ms, "
                    + rate + " p/ms");
        }

        return result;
    }

    private void playDirection(boolean a) {
        across = a;
        // Initialize the word holders
        for (int i = 0; i < words.length; i++) {
            words[i].across = (i == 0 ? across : !across);
        }
        // Iterate across the board
        for (y = 0; y < N; y++) {
            for (x = 0; x < N; x++) {
                if (board.getLetter(x, y) == 0) {
                    playCell();
                }
            }
        }
    }

    /**
     * Look through all the plays possible on this cell.
     */
    private void playCell() {
        //System.out.println("playCell " + x + " " + y);
        int limit = rack.getTileCount();
        for (len = 1; len <= limit; len++) {
            playLength();
        }
    }

    private void playLength() {
        // Initialize for length.
        word0 = dw.word = new char[len];
        if (minDist() <= len && len <= maxDist()) {
            slot = new ScrabbleSlot(board, x, y, len, across, dict);
            PermuteUnique i = new PermuteUnique(rack.toString(), len);
            while (i.hasNext()) {
                word = (String) i.next();
                playWord();
            }
        }
    }

    private void playWord() {
        //System.out.println("playWord " + word);
        findBlanks();

        switch (nblank) {
            case 0: {
                pcount++;
                if (slot.doesItFit()) {
                    tryWord();
                }
                break;
            }

            case 1: { // one blank in our rack
                for (char c = 'a'; c <= 'z'; c++) {
                    pcount++;
                    if (slot.doesItFit(c, bix0)) {
                        word0[bix0] = c;
                        word = new String(word0);
                        tryWord();
                    }
                }
                word0[bix0] = Letter.BLANK; // restore
                break;
            }

            case 2: { // two blanks can take a long time.
                String xx = "sex";
                for (int i = 0; i < xx.length(); i++) {
                    char a = xx.charAt(i);
                    word0[bix0] = a;
                    slot.set(a, bix0);
                    for (char b = 'a'; b <= 'z'; b++) {
                        pcount++;
                        if (slot.doesItFit(b, bix1)) {
                            word0[bix1] = b;
                            word = new String(word0);
                            tryWord();
                        }
                    }
                }
                word0[bix0] = word0[bix1] = Letter.BLANK; // restore
                break;
            }

        }
    }

    private void tryWord() {
        //System.out.println("tryWord " + word);
        //if(!slot.doesItFit(word)){
        //return;
        //}
        placeWord(false);
        scoreIt();
        removeWord();
    }

    /**
     * How far to the nearest letter?
     */
    private int minDist() {
        int d = 0;
        if (across) {
            if (x > 0 && board.getLetter(x - 1, y) != 0) {
                return 1;
            }
            for (;; d++) {
                if (x + d >= N) {
                    return TOOFAR;
                }
                if (board.getLetter(x + d, y) != 0) {
                    return d;
                }
            }
        }
        else {
            if (y > 0 && board.getLetter(x, y - 1) != 0) {
                return 1;
            }
            for (;; d++) {
                if (y + d >= N) {
                    return TOOFAR;
                }
                if (board.getLetter(x, y + d) != 0) {
                    return d;
                }
            }

        }
    }

    /**
     * how far to the wall? count blank cells, hop over placed tiles
     */
    private int maxDist() {
        int d = 0;
        if (across) {
            for (int i = 0; x + i < N; i++) {
                if (board.getLetter(x + i, y) == 0) {
                    d++;
                }
            }
        }
        else {
            for (int i = 0; y + i < N; i++) {
                if (board.getLetter(x, y + i) == 0) {
                    d++;
                }
            }
        }
        return d;
    }

    private boolean emptyRow() {
        return board.emptyRow(y);
    }

    private boolean emptyColumn() {
        return board.emptyColumn(x);
    }

    /**
     * Calculate bi the blank value and blank index arrays.
     */
    private void findBlanks() {
        nblank = 0;
        for (int i = 0; i < len; i++) {
            if ((word0[i] = word.charAt(i)) == Letter.BLANK) {
                nblank++; // count a blank
            }
        }
        if (nblank > 0) {
            int i = 0;
            for (; i < len; i++) {
                if (word0[i] == Letter.BLANK) {
                    bix0 = i++;
                    break;
                }
            }
            for (; i < len; i++) {
                if (word0[i] == Letter.BLANK) {
                    bix1 = i;
                    break;
                }
            }
        }
        slot.init(word0);
    }

    private void placeWord(boolean perm) {
        board.setWord(word, x, y, across, perm);
    }

    private void removeWord() {
        board.removeWord(x, y, across);
    }

    /**
     * is the whole placement legal?
     */
    private boolean isLegal(String w) {
        boolean b = dict.isLegal(w);
        //System.out.println ("legal " + w + " " + b);
        return b;
    }

    /**
     * @return true if there is a crossword at this point. fill out an entry in
     *         the words array. later, incrementing nwords consitutes acceptance
     */
    private boolean findCrossWord(int x0, int y0) {

        //System.out.println("findCrossWord(" + x0 + ", " + y0 + ")" );

        int nfound = 0; // number of cross letters found
        if (across) {
            words[nwords].head.x = x0;
            for (int i = 0; y0 + i >= 0 && getLetter(x0, y0 + i) != 0; i--) {
                words[nwords].head.y = y0 + i;
            }
            x0 = words[nwords].head.x;
            y0 = words[nwords].head.y + 1;
            if (y0 < N && getLetter(x0, y0) != 0) {
                nfound++; // at least 2 letters long
            }
        }
        else { // down
            words[nwords].head.y = y0;
            for (int i = 0; x0 + i >= 0 && getLetter(x0 + i, y0) != 0; i--) {
                words[nwords].head.x = x0 + i;
            }

            x0 = words[nwords].head.x + 1;
            y0 = words[nwords].head.y;

            if (x0 < N && getLetter(x0, y0) != 0) {
                nfound++; // at least 2 letters long
            }
        }
        return nfound > 0;
    }

    public Candidate scoreIt(int x, int y, boolean across, String played)
            throws ScrabbleException {
        // Initialize
        this.x = x;
        this.y = y;
        this.across = across;
        this.word = played;
        this.len = played.length();
        for (int i = 0; i < words.length; i++) {
            words[i].across = (i == 0 ? across : !across);
        }

        //
        // Analyze
        //
        scoreIt();

        //
        // Return the found candiate representing the user's move.
        //
        Iterator i = candidates.iterator();
        if (i.hasNext() == false) {
            throw new ScrabbleException(dict.getLastFailedString()
                    + ": Illegal crossword");
        }
        Object o = i.next();
        Candidate candidate = (Candidate) o;
        return candidate;
    }

    private void scoreIt() {
        nwords = 0;

        int x0 = x;
        int y0 = y;

        // back up to the beginning of any word we may have formed
        if (across) {
            while (x0 > 0 && getLetter(x0 - 1, y0) != 0) {
                x0--;
            }
        }
        else {
            while (y0 > 0 && getLetter(x0, y0 - 1) != 0) {
                y0--;
            }
        }

        words[nwords].head.x = x0;
        words[nwords].head.y = y0;

        if (!isLegal(words[nwords].toString())) {
            //System.out.println(words[nwords].toString() + "main word illegal
            // - return");
            return;
        }
        nwords++; // main word is legal

        // step down the word, looking for crosses.
        if (across) {
            for (int i = 0; x + i < N && getLetter(x + i, y) != 0; i++) {
                if (!isPermanent(x + i, y)) {
                    if (findCrossWord(x + i, y)) {
                        if (!isLegal(words[nwords].toString())) {
                            //System.out.println(words[nwords].toString() + "
                            // illegal - return - across");
                            return;
                        }
                        nwords++; // word accepted
                    }
                }
            }
        }
        else {
            for (int i = 0; y + i < N && getLetter(x, y + i) != 0; i++) {
                if (!isPermanent(x, y + i)) {
                    if (findCrossWord(x, y + i)) {
                        if (!isLegal(words[nwords].toString())) {
                            //System.out.println(words[nwords].toString() + "
                            // illegal - return - down");
                            return;
                        }
                        nwords++;
                    }
                }
            }
        }

        // find score
        int s = getPlayScore();

        //board.print();

        // CANDIDATE
        String formed = new String(words[0].toString());
        String played = new String(word);
        String leave = rack.leave(played);

        candidates.add(new Candidate(formed, played, leave, x, y, across, s));

        //System.out.println("**** new Candidate " + formed + " " + s + "
        // pts.");
    }

    private int getPlayScore() {
        int s = 0;
        for (int i = 0; i < nwords; i++) {
            int incr = board.wordScore(words[i].head.x, words[i].head.y,
                    words[i].across);
            s += incr;
        }
        if (len == 7) {
            s += 50;
        }
        return s;
    }

    private boolean isPermanent(int a, int b) {
        return board.isPermanent(a, b);
    }

    private char getLetter(int a, int b) {
        return board.getLetter(a, b);
    }

    public int getScore() {
        return bestScore;
    }

    public static void main(String av[]) {
        ScrabbleDictionary dict = new ScrabbleDictionary();
        ScrabbleBoard board = new ScrabbleBoard();
        Rack racks[] = new Rack[2];
        for (int i = 0; i < 2; i++) {
            racks[i] = new Rack();
        }
        TileBag bag = new TileBag();
        FirstMove first = new FirstMove(board, racks[0], dict, bag);
        ScrabblePlay play = new ScrabblePlay(board, racks[1], dict, bag);

        int passcount = 0;
        int score[] = { 0, 0 };

        for (int i = 0;; i++) { // i - the move counter

            for (int j = 0; j < racks.length; j++) {
                racks[j].drawAll(bag);
                racks[j].print();
            }

            System.out.println(bag);
            if (i == 0) {
                first.move();
            }
            else {
                play.move(racks[i % 2]);
            }
            board.print();

            if (i == 0) {
                System.out.println(first.score() + " points");
                String bl = first.blanks();
                if (bl.length() > 0) {
                    System.out.println("blanks " + bl);
                }
                score[i % 2] += first.score();
            }
            else {
                if (play.getScore() == 0) {
                    passcount++;
                }
                else {
                    passcount = 0;
                }
                System.out.println(play.getScore() + " points this turn");
                score[i % 2] += play.getScore();
            }

            System.out.println("move #" + (i + 1) + " score " + score[0]
                    + " to " + score[1]);

            if (racks[0].isEmpty() || racks[1].isEmpty() || passcount == 2) {
                System.out.println("quitting");
                racks[0].print();
                racks[1].print();
                bag.print();
                System.out.println("passcount is " + passcount);
                break;
            }
        }
    }

    private Candidate chooseMove() {
        return (Candidate) candidates.iterator().next();
    }
}