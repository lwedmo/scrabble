
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

public class ScrabbleDictionary {
    Set        dict;
    DictionaryWord dw;
    private String lastFailedString;

    ScrabbleDictionary() {
    	try {
	        this.dw = new DictionaryWord();
	        // read in words.
	        dict = new TreeSet();
	        URL url = this.getClass().getClassLoader().getResource("dict");
	        BufferedReader in = null;
	        InputStreamReader inputStreamReader = new InputStreamReader(
	                new FileInputStream(new File(url.getFile())));
	        in = new BufferedReader(inputStreamReader);
	        String s = null;
            while ((s = in.readLine()) != null) {
                dict.add(new DictionaryWord(s));
            }
        } catch (Exception ex) {
            System.out.println("io exception");
        }
    }
    
    ScrabbleDictionary(String filename) {
    	try {
	        this.dw = new DictionaryWord();
	        // read in words.
	        dict = new TreeSet();
	        BufferedReader in = null;
	        InputStreamReader inputStreamReader = new InputStreamReader(
	                new FileInputStream(new File(filename)));
	        in = new BufferedReader(inputStreamReader);
	        String s = null;
            while ((s = in.readLine()) != null) {
                dict.add(new DictionaryWord(s));
            }
        } catch (Exception ex) {
            System.out.println("io exception");
        }
    }

    public boolean isLegal(String s) {
        boolean result = dict.contains(new DictionaryWord(s));
        if (result == false) {
            lastFailedString = s;
        }
        return result;
    }

    public boolean isLegal(char[] a) {
        dw.word = a;
        return dict.contains(dw);
    }

    public boolean isLegal(DictionaryWord w) {
        boolean b = dict.contains(w);
        System.out.println("legal ? " + w + " " + b);
        return b;
    }

    public Iterator iterator() {
        return dict.iterator();
    }

    public static void main(String av[]) {
        ScrabbleDictionary d = new ScrabbleDictionary();
        Iterator i = d.iterator();
        int counted = 0;
        while (i.hasNext()) {
            DictionaryWord w = (DictionaryWord) i.next();
            // System.out.println(w + " " + d.isLegal(w));
            if (d.isLegal(w)) {
                counted++;
            }
        }
        System.out.println(d.dict.size() + " words vs. " + counted);
        System.out.println("dish ? " + d.isLegal("Dish"));
    }

    public void assertLegal(String word) throws ScrabbleException {
        if (isLegal(word) == false) {
            throw new ScrabbleException(word
                    + " is not in the scrabble dictionary");
        }
    }

    public String getLastFailedString() {
        return lastFailedString;
    }
    
    public String toString () {
    	return "dictionary of " + this.dict.size();
    }
}