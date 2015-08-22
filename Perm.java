
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * A permuting iterator
 */
public class Perm implements Iterator {
    static String   cacheString[];
    static TreeSet  set[];
    static Iterator it;
    private int     r;            // how many at a time?
    private char    seed[];       // set set
    private int     ix[];
    private char    perm[];
    private int     len;

    Perm(String s, int r) {
        this.r = r;
        this.seed = s.toCharArray();

        if (set == null) {
            set = (TreeSet[]) Array.newInstance(new TreeSet().getClass(), 8);
        }
        if (cacheString == null) {
            cacheString = (String[]) Array.newInstance(new String().getClass(),
                    8);
        }
        if (cacheString[r] == null || set[r] == null
                || !s.equals(cacheString[r])) {
            buildSet();
        }
        it = set[r].iterator();
    }

    private void buildSet() {
        // initialize
        set[r] = new TreeSet(new StringComparator());
        ix = new int[r];
        perm = new char[r];
        recurse(0);
        cacheString[r] = new String(seed);
    }

    private void recurse(int pos) {
        int i, j;
        for (i = 0; i < len; i++) {
            for (j = 0; j < pos; j++) { // dup check
                if (i == ix[j]) {
                    break;
                }
            }
            if (i == 0 || i == j) {
                perm[pos] = seed[ix[pos] = i];
                if (pos + 1 >= len) {
                    set[r].add(new String(perm));
                }
                else {
                    recurse(pos + 1);
                }
            }
        }
    }

    private class StringComparator implements Comparator {
        public StringComparator() {
        }

        public int compare(Object a, Object b) {
            return ((String) b).compareTo((String) a);
        }
    }

    public boolean hasNext() {
        return it.hasNext();
    }

    public Object next() {
        return it.next();
    }

    public void remove() {
    }

    private static void test(String s, int n) {
        for (PermuteUnique pi = new PermuteUnique(s, n); pi.hasNext();) {
            System.out.println(pi.next());
        }
    }

    public static void main(String av[]) {
    	test(av[0], av[0].length());
    }
}