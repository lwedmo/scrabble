
import java.util.Iterator;

public class PermuteIterator implements Iterator<char[]> {
    private char result[];
    private int  counter[];
    private char letters[];
    private int  level;
    private int  r;
    private int  n;

    PermuteIterator(char[] letters, int r) {
        this.letters = letters;
        this.r = r;
        init();
    }

    PermuteIterator(String s, int r) {
        this.letters = s.toCharArray();
        this.r = r;
        init();
    }

    private void init() {
        this.n = letters.length;
        counter = new int[n];
        result = new char[r];
        level = 0;
        counter[0] = -1;
    }

    /**
     * Are there any more permutations?
     */
    public boolean hasNext() {
        for (int i = 0; i < r; i++) {
            if (i != (n - 1) - counter[i]) {
                return true;
            }
        }
        return false;
    }

    /**
     * return the next permutation.
     */
    public char[] next() {
        permute();
        return result;
    }

    public void remove() {
    }

    private boolean hasDup() {
        // Test for duplicates.
        for (int i = 0; i < level; i++) {
            if (counter[i] == counter[level]) {
                return true;
            }
        }
        return false;
    }

    private void permute() {
        for (counter[level]++;;) {
            for (; counter[level] < n; counter[level]++) {
                if (hasDup() == true) {
                    continue;
                }
                result[level] = letters[counter[level]];
                if (level == r - 1) {
                    return;
                }
                counter[++level] = 0; // recurse
                break;
            }
            if (counter[level] >= n) {
                counter[--level]++;
            }
        }
    }
    
    private static void test(String s, int n) {
        for (PermuteIterator pi = new PermuteIterator(s, n); pi.hasNext();) {
            System.out.println(pi.next());
        }
    }

    public static void main(String av[]) {
    	test(av[0], av[0].length());
    	
//        test("BEKORSU", 7);
//        test("123", 3);
//        test("123", 2);
//        test("123", 1);
//        test("1234", 4);
//        test("12345", 4);
//        test("12345", 5);
//
//        test("AABB", 4);
//        test("AAAAA", 5);
//        test("AAAABC", 6);
//        test("AAAABC", 6);

    }

}