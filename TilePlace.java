/**
 * The placing of a tile
 */
public class TilePlace implements Comparable {
    public char    letter;
    public int     x;
    public int     y;
    public int     blank; // value assigned for a blank tile.
    public boolean newly; // tile is newly placed ??

    TilePlace(Tile tile, ScrabbleCell cell, char blankValue) {
    }

    TilePlace(char letter, int x, int y) {
        this.letter = letter;
        this.x = x;
        this.y = y;
        this.newly = true;
    }

    TilePlace(char letter, int x, int y, char blank) {
        this.letter = letter;
        this.x = x;
        this.y = y;
        this.blank = blank;
        this.newly = true;
    }

    TilePlace() {
        letter = 0;
        x = y = -1;
        blank = 0;
    }

    /**
     * Compare positionally.
     * 
     * Compares this object with the specified object for order. Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * <p>
     * 
     * The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt>
     * for all <tt>x</tt> and <tt>y</tt>. (This implies that
     * <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * <p>
     * 
     * The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * <p>
     * 
     * Finally, the implementer must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     * <p>
     * 
     * It is strongly recommended, but <i>not </i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>. Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact. The recommended
     * language is "Note: this class has a natural ordering that is inconsistent
     * with equals."
     * 
     * @param o
     *            the Object to be compared.
     * @return a negative integer, zero, or a positive integer as this object is
     *         less than, equal to, or greater than the specified object.
     * 
     * @throws ClassCastException
     *             if the specified object's type prevents it from being
     *             compared to this Object.
     */
    public int compareTo(Object o) {
        TilePlace a = (TilePlace) o;
        int c = y - a.y;
        if (c != 0) {
            return c;
        }
        return x - a.x;
    }
}