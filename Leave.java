public class Leave {

    private static final int    DUP_WEIGHT     = 100;
    private static final int    LETTER_WEIGHT  = 130;
    private static final int    HIGH_WEIGHT    = 10;
    private static final int    BALANCE_WEIGHT = 100;
    private static final int    COUNT_WEIGHT   = 120;                          // value
                                                                               // of
                                                                               // leaving
                                                                               // few
                                                                               // letters

    private String              leave;
    private final static String singleValue    = "?SEXZRAHNCDMTIJKLPOYFBGWUVQ";
    private static int          ltab[];

    Leave(String leave) {
        this.leave = leave;
    }

    private int[] letterTable() {
        if (ltab == null) {
            ltab = new int[128];
            for (int i = 0; i < singleValue.length(); i++) {
                ltab[singleValue.charAt(i)] = i;
            }
        }
        return ltab;
    }

    private int letterValue(char c) {
        if (c == '?') {
            return 0;
        }
        if (c < 0 || letterTable().length <= c || !Character.isLetter(c)) {
            System.out.println("out of range letter " + c);
            return 27;
        }
        return letterTable()[c];
    }

    private int letterValue() {
        if (length() == 0) {
            return LETTER_WEIGHT;
        }
        int sum = 0;
        int count = 0;
        for (int i = 0; i < length(); i++) {
            char c = leave.charAt(i);
            if (Character.isLetter(c)) {
                sum += letterValue(c);
                count++;
            }
            else if (c == '?') {
                count += 3; // blank counts 3X !
            }
        }
        if (count > 0) {
            sum /= count;
        }
        else {
            return LETTER_WEIGHT;
        }
        sum = (26 - sum);
        sum *= LETTER_WEIGHT;
        sum /= 26;
        return sum;
    }

    private boolean isVowel(char c) {
        return " ?AEIOU".indexOf(c) >= 0;
    }

    private int vowelCount() {
        int count = 0;
        for (int i = 0; i < leave.length(); i++) {
            if (isVowel(leave.charAt(i))) {
                count++;
            }
        }
        return count;
    }

    private int consonantCount() {
        return length() - vowelCount();
    }

    private int length() {
        return leave.length();
    }

    private int vowelBalance() {
        int b = consonantCount() - vowelCount();
        //System.out.println("consonant/vowel balance " + leave + " " + b);
        return b;
    }

    private int balanceValue() {
        if (length() == 1) {
            return BALANCE_WEIGHT / 2;
        }
        if (evenCount()) {
            return vowelBalance() == 0 ? BALANCE_WEIGHT : 0;
        }
        else {
            return vowelBalance() == 1 ? BALANCE_WEIGHT : 0;
        }
    }

    public boolean evenCount() {
        return leave.length() % 2 == 0;
    }

    private int dupValue() {
        return dupCount() > 0 ? 0 : 100;
    }

    private int dupCount() {
        boolean eflag = false;
        int count = 0;
        String s = ScrabbleString.sort(leave);
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == s.charAt(i + 1)) {
                count++;
            }
            if (s.charAt(i) == 'E') {
                eflag = true;
            }
        }
        if (count > 0 && eflag == true) {
            count--;
        }
        return count;
    }

    public int getValue() {
        if (length() == 0) {
            return DUP_WEIGHT + LETTER_WEIGHT + HIGH_WEIGHT + BALANCE_WEIGHT
                    + COUNT_WEIGHT;
        }

        int x = dupValue() + balanceValue() + highValue() + letterValue()
                + countValue();
        //System.out.println("getValue() " + leave + " " + x);
        return x;
    }

    /**
     * return the combined score of the high scoring letters in the rack.
     */
    private int highScore() {
        int score = 0;
        for (int i = 0; i < leave.length(); i++) {
            char c = leave.charAt(i);
            if (Character.isLetter(c) && Letter.getScore(c) > 1) {
                score += Letter.getScore(c);
            }
        }
        return score;
    }

    private int highValue() {
        return -highScore() * HIGH_WEIGHT;
    }

    private int countValue() {
        return ((6 - length()) * COUNT_WEIGHT) / 5;
    }

    public String toString() {
        return "(" + getValue() + " = " + "dup " + dupValue() + ", " + "bal "
                + balanceValue() + ", " + "high " + highValue() + ", "
                + "lett " + letterValue() + ", " + "cnt " + countValue() + ")";
    }
}