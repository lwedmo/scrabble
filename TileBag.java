
import java.util.Iterator;

/**
 * The bag of tiles Randomize Draw one at a time
 * 
 * @author Leonard Edmondson
 */
public class TileBag implements Iterator {
    private char            letters[];
    /**
     * the number of tiles in a full bag
     */
    public static final int MAX = 100;
    private int             count;

    /**
     * Draw a letter Throw exception when empty
     */
    public char draw() {
        return letters[--count];
    }

    public Object next() {
        return new Character(draw());
    }

    public void addLetter(char letter) {
        letters[count++] = letter;
    }

    /**
     * mix up the tiles in the bag
     */
    public void shuffle() {
        for (int i = 0; i < 1000; i++) {
            swap(random(), random());
        }
    }

    TileBag() {
        // Fill the bag.
        count = 0;
        letters = new char[MAX];

        for (char a = Letter.FIRST; a <= Letter.LAST; a++) {
            for (int i = Letter.getCount(a); i > 0; i--) {
                addLetter(a);
            }
        }
        addLetter(Letter.BLANK); // 2 blanks
        addLetter(Letter.BLANK);
        shuffle();
    }

    public void print() {
        System.out.println(this);
    }

    public static void main(String av[]) {
        TileBag p = new TileBag();
        while (p.hasNext()) {
            System.out.print(p.draw() + " ");
        }
        System.out.println();
    }

    // Return a random number between 0 and count - 1
    private int random() {
        return (int) (Math.random() * count);
    }

    // swap two letters
    private void swap(int a, int b) {
        char c = letters[a];
        letters[a] = letters[b];
        letters[b] = c;
    }

    public boolean hasNext() {
        return count > 0;
    }

    public boolean empty() {
        return count <= 0;
    }

    /**
     * @return a string which represents this bag
     */
    public String toString() {
        return "bag of " + count + " tiles";
    }

    public void remove() {
    }
}