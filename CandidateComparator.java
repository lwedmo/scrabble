
import java.util.*;

public class CandidateComparator implements Comparator {
    public CandidateComparator() {
    }

    /**
     * @return comparison value based on score, for a stable sort.
     */
    public int compare(Object a, Object b) {
        Candidate x = (Candidate) a;
        Candidate y = (Candidate) b;

        int c = y.getScore() - x.getScore();
        if (c != 0) {
            return c;
        }

        // alphabetical order
        c = x.getWordFormed().compareTo(y.getWordFormed());
        if (c != 0) {
            return c;
        }
        return 0;
    }
}