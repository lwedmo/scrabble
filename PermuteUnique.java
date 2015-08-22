
import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;

public class PermuteUnique implements Iterator {
    static String  cacheString[];
    static TreeSet set[];
    Iterator       it;
    private int    r;            // how many at a time?
    private char   seed[];       // set set
    private int    ix[];
    private char   perm[];
    private int    len;

    PermuteUnique(String s, int r) {
        this.r = r;
        this.seed = s.toCharArray();
        this.len = s.length();

        if (set == null) {
            set = (TreeSet[]) Array.newInstance(new TreeSet().getClass(), r+1);
        }
        if (cacheString == null) {
            cacheString = (String[]) Array.newInstance(new String().getClass(), r+1);
        }
        if (cacheString[r] == null || set[r] == null
                || !s.equals(cacheString[r])) {
            buildSet();
        }
        this.it = set[r].iterator();
    }

    private void buildSet() {
        set[r] = new TreeSet(new StringComparator());
        cacheString[r] = new String(seed);

        ix = new int[r];
        perm = new char[r];
        recurse(0);
    }

    private void newPerm(String p) {
        set[r].add(p);
    }

    private void recurse(int pos) {
        int i, j;
        for (i = 0; i < len; i++) {
            boolean dup = false;
            for (j = 0; j < pos; j++) {
                if (i == ix[j]) {
                    dup = true;
                    break;
                }
            }
            if (dup == false) {
                perm[pos] = seed[ix[pos] = i];
                if (pos + 1 >= r) {
                    newPerm(new String(perm));
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
        System.out.println();
        PermuteUnique pi = new PermuteUnique(s, n);
        System.out.println("permute " + s + ", " + n + ", count = "
                + pi.count());
        while (pi.hasNext()) {
            System.out.println(pi.next());
        }
    }

    public int count() {
        return set[r].size();
    }

    public static void main(String av[]) {
        test("BEKORSU", 7);
        test("123", 3);
        test("123", 2);
        test("123", 1);
        test("1234", 4);
        test("12345", 4);
        test("12345", 5);

        test("AABB", 4);
        test("AAAAA", 5);
        test("AAAABC", 6);
        test("AAAABC", 6);
    }
}