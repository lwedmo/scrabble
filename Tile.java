
import java.awt.Canvas;
import java.awt.Graphics;

/**
 * representing a tile.
 */
public class Tile extends Canvas {
    public char    letter; // our letter
    public char    blank; // chosen letter if blank
    public boolean newly; // true if we are newly played

    Tile() {
        letter = 0;
        blank = 0;
        newly = false;
    }

    Tile(char letter) {
        this.letter = letter;
        blank = 0;
        newly = false;
    }

    public void paint(Graphics g) {
    }

    public int hashCode() {
        return letter;
    }

    public String toString() {
        char a[] = { letter != 0 ? letter : '0' };
        return new String(a);
    }

    public static void main(String[] av) {
        Tile t = new Tile('A');
        System.out.println(t);
    }

    public boolean equals(Object obj) {
        return letter == ((Tile) obj).letter;
    }

    public char getLetter() {
        return this.letter;
    }

    public void setLetter(char c) {
        this.letter = c;
    }
}