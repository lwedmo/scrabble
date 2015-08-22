import junit.framework.TestCase;


public class PermuteIteratorTest extends TestCase {


    // Unit testing
    public void testA() {
    	String s = "imustpore";
    	int n = 9;
    	ScrabbleDictionary d = new ScrabbleDictionary("/usr/share/dict/words");
        PermuteIterator pi = new PermuteIterator(s, n);
        while (pi.hasNext()) {
            Object o = pi.next();
            char[] a = (char[]) o;
            if (d.isLegal(a)) {
            	System.out.println(new String(a));
            }
        }
    }
	

}
