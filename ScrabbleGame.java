
import java.util.Iterator;

public class ScrabbleGame implements Runnable {
    private ScrabbleBoard      board;
    private TileBag            bag;
    private Rack               racks[];
    private ScrabbleDictionary dict;
    private int                passcount;
    private int                score[];
    private int                nplayer;
    private FirstMove          first;
    private ScrabblePlay       play;
    private int                moveNumber;
    private Scrabble           scrabble;                               // parent
                                                                       // pointer
    private boolean            thinking      = false;
    private boolean            userWentFirst = true;
    private static final int   N             = ScrabbleBoard.DIMENSION;

    ScrabbleGame(Scrabble scrabble) {
        this.scrabble = scrabble;
        init();
    }

    public void playself() {
        for (;;) {
            playone();
        }
    }

    public void drawTiles() {
        for (int j = 0; j < racks.length; j++) {
            racks[j].drawAll(bag);
            racks[j].print();
        }
        System.out.println(bag);
    }

    public void playone() {
        drawTiles();
        System.out.println(bag);
        play.move(racks[moveNumber % 2]);

        board.print();

        if (moveNumber == 0) {
            System.out.println(first.score() + " points");
            String bl = first.blanks();
            if (bl.length() > 0) {
                System.out.println("blanks " + bl);
            }
            score[moveNumber % 2] += first.score();
        }
        else {
            if (play.getScore() == 0) {
                passcount++;
            }
            else {
                passcount = 0;
            }
            System.out.println(play.getScore() + " points this turn");
            score[moveNumber % 2] += play.getScore();
        }

        System.out.println("move #" + (moveNumber + 1) + " score " + score[0]
                + " to " + score[1]);

        if (racks[0].isEmpty() || racks[1].isEmpty() || passcount == 2) {
            System.out.println("quitting");
            racks[0].print();
            racks[1].print();
            bag.print();
            System.out.println("passcount is " + passcount);
            System.exit(0);
        }
        moveNumber++;
    }

    public void init() {
        nplayer = 2;
        dict = new ScrabbleDictionary();
        board = new ScrabbleBoard();
        racks = new Rack[nplayer];
        score = new int[nplayer];
        for (int i = 0; i < nplayer; i++) {
            racks[i] = new Rack(this);
            score[i] = 0;
        }
        bag = new TileBag();
        first = new FirstMove(board, racks[0], dict, bag);
        play = new ScrabblePlay(board, racks[1], dict, bag);
        passcount = 0;
        moveNumber = 0;
        drawTiles();
    }

    public ScrabbleBoard getBoard() {
        return board;
    }

    public Rack getRack(int n) {
        return racks[n];
    }

    // The user clicked this cell
    public boolean userMove(String word, int x, int y, boolean across) {
        System.out.println("userMove(" + word + ',' + x + ',' + y + ','
                + across + ')');
        if (getThinking() || gameOver()) {
            return false;
        }
        Rack rack = getUserRack();
        String played = null;
        Candidate candidate = null;
        try {
            getDictionary().assertLegal(word);
            disambiguate(word, rack);
            played = board.setUserWord(word, x, y, across, false, rack);
            //board.print();
            ScrabblePlay play = new ScrabblePlay(board, rack, dict, bag);
            candidate = play.scoreIt(x, y, across, played);
            board.removeWord(x, y, across);
            board.setUserWord(word, x, y, across, true, rack);
            candidate.print();
        } catch (ScrabbleException ex) {
            showMessageDialog(ex.getMessage());
            board.removeWord(x, y, across);
            System.out.println(ex.getMessage());
            return false;
        }
        // Successful move.
        log(candidate);
        rack.remove(played);
        drawTiles();
        initializeDisplay();
        moveNumber++;
        getScrabble().getFrame().getCanvas().setDisplayBoard(
                new DisplayBoard(board));
        getScrabble().repaint();

        if (gameOver() == false) {
            new Thread(this).start(); // kick off the computer's solver.
        }
        return true;
    }

    public ScrabbleDictionary getDictionary() {
        return dict;
    }

    public Scrabble getScrabble() {
        return scrabble;
    }

    public void initializeDisplay() {
        getScrabble().getFrame().getControl().getTextField().setText("");
    }

    public synchronized boolean getThinking() {
        return thinking;
    }

    public synchronized void setThinking(boolean t) {
        thinking = t;
    }

    public void run() {
        setThinking(true);
        log(play.move(getMyRack()));
        moveNumber++;
        getScrabble().getFrame().getCanvas().setDisplayBoard(
                new DisplayBoard(board));
        getScrabble().getFrame().repaint();
        //board.print();
        racks[0].print();
        racks[1].print();
        setThinking(false);
    }

    public synchronized void log(Candidate c) {
        if (c == null) {
            log("pass\n");
        }
        int player = moveNumber % 2;
        score[player] += c.getScore();
        int total = score[player];
        ScoreCard card = getScoreCard();
        log(c.displayForm(total));
        log((player == 0) ? " - " : "\n");
    }

    public void log(String s) {
        getScoreCard().log(s);
    }

    public ScoreCard getScoreCard() {
        return getScrabble().getScoreCard();
    }

    public Rack getUserRack() {
        return racks[0];
    }

    public Rack getMyRack() {
        return racks[1];
    }

    public void discard() {
        if (getThinking()) {
            return;
        }
        String requested = getScrabble().getFrame().getControl().getTextField()
                .getText().toUpperCase();
        Candidate candidate = new Candidate();
        candidate.setDiscard(true);
        candidate.setPlayed(requested);
        char discarded[] = new char[7];
        int nd = 0;
        Rack r = getUserRack();
        try {
            for (int i = 0; i < requested.length(); i++) {
                char c = requested.charAt(i);
                if (r.contains(c) == false) {
                    throw new ScrabbleException("The " + c
                            + " tile is not on your rack");
                }
                r.remove(c);
                discarded[nd++] = c;
                bag.addLetter(c);
                bag.shuffle();
            }
        } catch (ScrabbleException ex) {
            System.out.println(ex.getMessage());
            for (int i = 0; i < nd; i++) {
                r.add(discarded[i]);
            }
            return;
        }
        r.drawAll(bag);
        System.out.println(bag);
        int player = moveNumber % 2;
        log(candidate);
        initializeDisplay();
        moveNumber++;
        getScrabble().repaint();
        if (gameOver() == false) {
            new Thread(this).start();
        }
    }

    public static String pad(String string, int width, boolean left) {
        return Candidate.pad(string, width, left);
    }

    public void scramble() {
        Rack r = getUserRack();
        synchronized (r) {
            StringBuffer b = new StringBuffer(r.toString());
            r.empty();
            while (b.length() > 0) {
                int n = (int) (Math.random() * b.length());
                r.add(b.charAt(n));
                b.delete(n, n + 1);
            }
        }
        getScrabble().getRackDisplay().repaint();
    }

    public boolean gameOver() {
        return bag.empty()
                && (getMyRack().isEmpty() || getUserRack().isEmpty());
    }

    public void showMessageDialog(String msg) {
        getScrabble().showMessageDialog(msg);
    }

    public void disambiguate(String word, Rack rack) throws ScrabbleException {
        int n = rack.blankCount();
        if (n > 0 && lowerCount(word) > n) {
            throw new ScrabbleException("Play blanks using lower case letters.");
        }
    }

    private static int lowerCount(String word) {
        int n = 0;
        for (int i = 0; i < word.length(); i++) {
            if (Character.isLowerCase(word.charAt(i))) {
                n++;
            }
        }
        return n;
    }

    public void DndUserMove() {
        boolean across = true;
        int x0 = -1;
        int y0 = -1;
        String word = null;
        java.util.List tilePlace = board.getPlacedTileArray();
        try {
            int rank = -1;
            int size = tilePlace.size();
            if (size == 0) {
                throw new ScrabbleException("Drag some tiles to the board");
            }
            if (board.isCenterEmpty()) {
                throw new ScrabbleException("First word must touch the center");
            }
            // Figure out across / down
            for (int i = 0; i < size; i++) {
                TilePlace a = (TilePlace) tilePlace.get(i);
                for (int j = i + 1; j < size; j++) {
                    TilePlace b = (TilePlace) tilePlace.get(j);
                    if (a.x == b.x) {
                        across = false;
                        rank = a.x;
                    }
                    else if (a.y == b.y) {
                        across = true;
                        rank = a.y;
                    }
                    else {
                        throw new ScrabbleException(
                                "Tiles not in a single line");
                    }
                }
            }
            if (size == 1) {
                TilePlace a = (TilePlace) tilePlace.get(0);
                boolean h = board.hasPermanentNeighbors(true, a.x, a.y);
                boolean v = board.hasPermanentNeighbors(false, a.x, a.y);
                if (h && !v) {
                    across = true;
                    rank = a.y;
                }
                else {
                    across = false; // includes the ambiguous case
                    rank = a.x;
                }
            }
            TilePlace first = (TilePlace) tilePlace.get(0);
            int x = first.x;
            int y = first.y;
            // back up
            if (across) {
                while (x > 0 && board.getLetter(x - 1, y) != 0) {
                    x--;
                }
            }
            else {
                while (y > 0 && board.getLetter(x, y - 1) != 0) {
                    y--;
                }
            }
            x0 = x;
            y0 = y;
            // trace forward
            boolean connect = false;
            StringBuffer buf = new StringBuffer();
            if (across) {
                for (; x < N && board.getLetter(x, y) != 0; x++) {
                    buf.append(board.getLetter(x, y));
                    if (board.crossedAt(x, y)) {
                        connect = true;
                    }
                }
            }
            else {
                for (; y < N && board.getLetter(x, y) != 0; y++) {
                    buf.append(board.getLetter(x, y));
                    if (board.crossedAt(x, y)) {
                        connect = true;
                    }
                }
            }
            if (!connect) {
                throw new ScrabbleException("Word does not connect");
            }
            word = buf.toString();
        } catch (ScrabbleException ex) {
            showMessageDialog(ex.getMessage());
            System.out.println(ex.getMessage());
            sweep(tilePlace);
            this.getScrabble().getFrame().repaint();
            return;
        }
        this.sweep(tilePlace); // put tiles back on rack for moving
        this.userMove(word, x0, y0, across);
        this.getScrabble().getFrame().repaint();
    }

    /**
     * move all placed tiles back on the rack.
     */
    private void sweep(java.util.List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            TilePlace t = (TilePlace) it.next();
            board.removeLetter(t.x, t.y);
            this.racks[0].add(Character.isLowerCase(t.letter) ? '?' : t.letter);
        }
        this.getScrabble().getFrame().getCanvas().setDisplayBoard(
                new DisplayBoard(this.getScrabble().getGame().getBoard()));
    }
}