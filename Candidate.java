/**
 * We represent one of the possible plays
 */
public class Candidate {
    private String               played;         // Tiles played
    private String               leave;          // Tiles left
    private String               formed;         // Principle word formed
    private int                  score;
    private int                  x;
    private int                  y;
    private boolean              across;
    private int                  bingoScore;
    private final static boolean RIGHT   = false;
    private final static boolean LEFT    = true;
    private boolean              discard = false;

    /**
     * print ourselves on standard output
     */
    public void print() {
        System.out.println(this);
    }

    public String toString() {
        return "played " + ScrabbleString.sort(played) + ", leave="
                + ScrabbleString.sort(leave) + new Leave(leave) + ", forming="
                + formed + ", score=" + score + ", x=" + x + ", y=" + y
                + (across ? ", across " : " down ")
                + (bingoScore == 0 ? "" : (", bingoscore " + bingoScore));
    }

    /**
     * Returns 19 character representation of the move
     */
    public String displayForm(int total) {
        if (getDiscard() == false) {
            char letter = "ABCDEFGHIJKLMNO".charAt(x);
            String number = Integer.toString(y + 1);
            String coord = across ? number + letter : letter + number;
            return pad(formed, 9, RIGHT) + pad(coord, 3, RIGHT)
                    + pad(Integer.toString(score), 3, LEFT)
                    + pad(Integer.toString(total), 4, LEFT);
        }
        else {
            String action = (getPlayed().length() == 0 ? "PASS" : "DISCARD");
            return pad(getPlayed(), 8, false) + pad(action, 7, false)
                    + pad(Integer.toString(total), 4, LEFT);
        }
    }

    public static String pad(String string, int width, boolean left) {
        int d = width - string.length();
        StringBuffer buf = new StringBuffer();
        while (d-- > 0) {
            buf.append(" ");
        }
        String p = buf.toString();
        return left ? p + string : string + p;
    }

    public Candidate(String formed, String played, String leave, int x, int y,
            boolean across, int score) {
        this.formed = formed;
        this.played = played;
        this.leave = leave;
        this.x = x;
        this.y = y;
        this.across = across;
        this.score = score;
    }

    public Candidate() {
    }

    /**
     * @return the score for this candidate
     */
    public int getScore() {
        return score;
    }

    /**
     * @return the principle word formed by this candidate.
     */
    public String getWordFormed() {
        return formed;
    }

    public int getBlankCount() {
        int n = 0;
        for (int i = 0; i < played.length(); i++) {
            if (played.charAt(i) == Letter.BLANK) {
                n++;
            }
        }
        return n;
    }

    /**
     * @return the string of played letters
     */
    public String getPlayed() {
        return played;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean getAcross() {
        return across;
    }

    /**
     * @return the standard notation for this play
     */
    public String standardNotation() {
        return "";
    }

    public void setDiscard(boolean discard) {
        this.discard = discard;
    }

    public boolean getDiscard() {
        return discard;
    }

    public void setPlayed(String played) {
        this.played = played;
    }

    public String getLeave() {
        return leave;
    }

    public int leaveScore() {
        return new Leave(getLeave()).getValue();
    }

    public boolean isBingo() {
        return getPlayed().length() == 7;
    }

    public boolean playedBlank() {
        String s = getPlayed();
        for (int i = s.length() - 1; i >= 0; i--) {
            if (Character.isLowerCase(s.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}