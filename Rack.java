
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Rack extends Canvas implements DropTargetListener,
        DragSourceListener, DragGestureListener {

    private LinkedList      tiles = new LinkedList();
    private DragSource      dragSource;
    private DropTarget      dropTarget;
    private ScrabbleGame    game;
    public static final int MAX   = 7;               // max number of tiles per
                                                     // rack
    public static final int N     = 15;

    Rack() {
        init();
    }

    Rack(ScrabbleGame game) {
        init();
        this.game = game;
    }

    Rack(Rack r) {
        init();
        Iterator i = r.tiles.iterator();
        while (i.hasNext()) {
            tiles.add(i.next());
        }
    }

    Rack(String s) {
        init();
        for (int i = 0; i < s.length(); i++) {
            add(s.charAt(i));
        }
    }

    /**
     * Draw tiles until full from the bag
     */
    public void drawAll(TileBag bag) {
        int ndrawn = 0;
        //System.out.println("before draw " + bag);
        while (bag.empty() == false && full() == false) {
            drawOne(bag);
            ndrawn++;
        }
        //System.out.println("after draw " + bag + " having drawn " + ndrawn);

    }

    /**
     * @return true when our rack is full of tiles.
     */
    public boolean full() {
        return tiles != null && tiles.size() >= MAX;
    }

    /**
     * @return the number of tiles on our rack
     */
    public int getTileCount() {
        return tiles.size();
    }

    /**
     * Draw one tile.
     */
    public void drawOne(TileBag b) /* throws EmptyBag */{
        tiles.add(new Tile(b.draw()));
    }

    public void print() {
        System.out.println("rack holding tiles \"" + this + "\"");
    }

    public static void main(String[] args) {
        Rack h = new Rack();
        TileBag b = new TileBag();
        h.drawAll(b);
        h.print();
    }

    public void paint(Graphics g) {
        int sq = Math.min(getHeight(), getWidth() / 7);
        Iterator it = tiles.iterator();
        for (int j = 0; j < N && it.hasNext(); j++) {
            char a = ((Tile) (it.next())).letter;
            TileDrawer.draw(g, sq, j, 0, a);
        }
    }

    /**
     * @return the contents of the rack as a string
     */
    public String toString() {
        char s[] = new char[getTileCount()];
        Iterator i = tiles.iterator();
        for (int j = 0; i.hasNext(); j++) {
            s[j] = ((Tile) (i.next())).letter;
        }
        return new String(s);
    }

    public void playTile(char letter) throws ScrabbleException {
        if (Letter.isBlank(letter)) {
            letter = Letter.BLANK;
        }
        if (contains(letter) == false) {
            throw new ScrabbleException("You don't have a '" + letter
                    + "' tile on your rack (" + this.toString() + ")");
        }
        tiles.remove(new Tile(letter));
    }

    public void remove(char letter) {
        tiles.remove(new Tile(Letter.isBlank(letter) ? Letter.BLANK : Character
                .toUpperCase(letter)));
    }

    public void remove(String s) {
        for (int i = 0; i < s.length(); i++) {
            remove(s.charAt(i));
        }
    }

    /**
     * Replace on letter to the rack.
     */
    //public void replace (char letter){
    //tiles.add(new Tile(Letter.isBlank(letter) ? Letter.BLANK : letter));
    //}
    public boolean isEmpty() {
        return tiles.size() == 0;
    }

    public void exchange(String s, TileBag bag) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            remove(c);
            bag.addLetter(c);
        }
        bag.shuffle();
        drawAll(bag);
    }

    public String leave(String played) {
        boolean haveBlank = contains(Letter.BLANK);
        int nleft = tiles.size() - played.length();
        char[] leave = new char[nleft];

        // create array of tiles in the rack
        char rack[] = new char[getTileCount()];
        Iterator i = tiles.iterator();
        for (int j = 0; i.hasNext(); j++) {
            rack[j] = ((Tile) (i.next())).letter;
        }

        // mark out played tiles
        for (int j = 0; j < played.length(); j++) {
            char c = played.charAt(j);
            if (!haveBlank) {
                c = Character.toUpperCase(c);
            }
            for (int k = 0; k < rack.length; k++) {
                if (rack[k] == c
                        || (rack[k] == Letter.BLANK && Letter.isBlank(c))) {
                    rack[k] = 0;
                    break;
                }
                if (k == rack.length) {
                    throw new RuntimeException("letter " + c
                            + " not found in rack " + this);
                }
            }
        }

        int count = 0;
        for (int j = 0; j < rack.length; j++) {
            if (rack[j] != 0) {
                count++;
            }
        }
        if (nleft != count) {
            throw new RuntimeException("rack leave doesn't balance " + nleft
                    + " != " + count);
        }

        // form leave array
        int k = 0;
        for (int j = 0; j < rack.length; j++) {
            char c = rack[j];
            if (c != 0) {
                leave[k++] = c;
            }
        }

        return new String(leave);
    }

    public boolean contains(char letter) {
        letter = Character.toUpperCase(letter);
        Iterator i = tiles.iterator();
        for (int j = 0; i.hasNext(); j++) {
            if (letter == ((Tile) (i.next())).letter) {
                return true;
            }
        }
        return false;
    }

    public void scramble() {
    }

    public void add(char c) {
        tiles.add(new Tile(c));
    }

    private void init() {
        dropTarget = new DropTarget(this, this);
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this,
                DnDConstants.ACTION_MOVE, this);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });
    }

    public void empty() {
        tiles = new LinkedList();
    }

    public boolean hasBlank() {
        return contains(Letter.BLANK);
    }

    public int blankCount() {
        int n = 0;
        String s = toString();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == Letter.BLANK) {
                n++;
            }
        }
        return n;
    }

    /**
     * The drag operation has departed the <code>DropTarget</code> without
     * dropping.
     * <P>
     * 
     * @param dte
     *            the <code>DropTargetEvent</code>
     */
    public void dragExit(DropTargetEvent dte) {
    }

    /**
     * Called as the hotspot moves over a platform dependent drop site. This
     * method is invoked when the following conditions are true:
     * <UL>
     * <LI>The cursor's logical hotspot has moved but still intersects the
     * visible geometry of the <code>Component</code> associated with the
     * previous dragEnter() invocation.
     * <LI>That <code>Component</code> still has a <code>DropTarget</code>
     * associated with it.
     * <LI>That <code>DropTarget</code> is still active.
     * <LI>The <code>DropTarget</code>'s registered
     * <code>DropTargetListener</code> dragOver() method is invoked and
     * returns successfully.
     * <LI>The <code>DropTarget</code> does not reject the drag via
     * rejectDrag()
     * </UL>
     * <P>
     * 
     * @param dsde
     *            the <code>DragSourceDragEvent</code>
     */
    public void dragOver(DragSourceDragEvent dsde) {
    }

    /**
     * Called when a drag operation has encountered the <code>DropTarget</code>.
     * <P>
     * 
     * @param dtde
     *            the <code>DropTargetDragEvent</code>
     */
    public void dragEnter(DropTargetDragEvent event) {
        event.acceptDrag(DnDConstants.ACTION_MOVE);
    }

    /**
     * Called as the hotspot exits a platform dependent drop site. This method
     * is invoked when the following conditions are true:
     * <UL>
     * <LI>The cursor's logical hotspot no longer intersects the visible
     * geometry of the <code>Component</code> associated with the previous
     * dragEnter() invocation.
     * </UL>
     * OR
     * <UL>
     * <LI>The <code>Component</code> that the logical cursor's hotspot
     * intersected that resulted in the previous dragEnter() invocation no
     * longer has an active <code>DropTarget</code> or
     * <code>DropTargetListener</code> associated with it.
     * </UL>
     * OR
     * <UL>
     * <LI>The current <code>DropTarget</code>'s
     * <code>DropTargetListener</code> has invoked rejectDrag() since the last
     * dragEnter() or dragOver() invocation.
     * </UL>
     * <P>
     * 
     * @param dse
     *            the <code>DragSourceEvent</code>
     */
    public void dragExit(DragSourceEvent dse) {
    }

    /**
     * Called when the user has modified the drop gesture. This method is
     * invoked when the state of the input device(s) that the user is
     * interacting with changes. Such devices are typically the mouse buttons or
     * keyboard modifiers that the user is interacting with.
     * <P>
     * 
     * @param dsde
     *            the <code>DragSourceDragEvent</code>
     */
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    /**
     * The drag operation has terminated with a drop on this
     * <code>DropTarget</code>. This method is responsible for undertaking
     * the transfer of the data associated with the gesture. The
     * <code>DropTargetDropEvent</code> provides a means to obtain a
     * <code>Transferable</code> object that represents the data object(s) to
     * be transfered.
     * <P>
     * From this method, the <code>DropTargetListener</code> shall accept or
     * reject the drop via the acceptDrop(int dropAction) or rejectDrop()
     * methods of the <code>DropTargetDropEvent</code> parameter.
     * <P>
     * Subsequent to acceptDrop(), but not before,
     * <code>DropTargetDropEvent</code>'s getTransferable() method may be
     * invoked, and data transfer may be performed via the returned
     * <code>Transferable</code>'s getTransferData() method.
     * <P>
     * At the completion of a drop, an implementation of this method is required
     * to signal the success/failure of the drop by passing an appropriate
     * <code>boolean</code> to the <code>DropTargetDropEvent</code>'s
     * dropComplete(boolean success) method.
     * <P>
     * Note: The actual processing of the data transfer is not required to
     * finish before this method returns. It may be deferred until later.
     * <P>
     * 
     * @param dtde
     *            the <code>DropTargetDropEvent</code>
     */
    public void drop(DropTargetDropEvent event) {
        try {
            Transferable transferable = event.getTransferable();
            String transferData = (String) transferable
                    .getTransferData(ScrabbleTransferable.tileFlavor);
            StringTokenizer tokenizer = new StringTokenizer(transferData, ",");
            char letter = tokenizer.nextToken().charAt(0);
            char source = tokenizer.nextToken().charAt(0);
            int to = this.getTileIndex(event.getLocation(), source == 'B');
            if (source == 'R') {
                // Rack to rack
                to = this.getTileIndex(event.getLocation(), false);
                int from = Integer.parseInt(tokenizer.nextToken());
                if (to == from) {
                    return;
                }
                Tile a = (Tile) tiles.get(from);
                Tile b = (Tile) tiles.get(to);
                char c = a.getLetter();
                a.setLetter(b.getLetter());
                b.setLetter(c);
                repaint();
            }
            else if (source == 'B') {
                // board to rack move
                int x = Integer.parseInt(tokenizer.nextToken());
                int y = Integer.parseInt(tokenizer.nextToken());
                getBoard().setLetter(x, y, (char) 0, false);
                this.addAt(to, letter);
                this.getGame().getScrabble().getFrame().getCanvas()
                        .setDisplayBoard(new DisplayBoard(getBoard()));
                this.getGame().getScrabble().getFrame().repaint();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Called as the hotspot enters a platform dependent drop site. This method
     * is invoked when the following conditions are true:
     * <UL>
     * <LI>The logical cursor's hotspot initially intersects a GUI
     * <code>Component</code>'s visible geometry.
     * <LI>That <code>Component</code> has an active <code>DropTarget</code>
     * associated with it.
     * <LI>The <code>DropTarget</code>'s registered
     * <code>DropTargetListener</code> dragEnter() method is invoked and
     * returns successfully.
     * <LI>The registered <code>DropTargetListener</code> invokes the
     * <code>DropTargetDragEvent</code>'s acceptDrag() method to accept the
     * drag based upon interrogation of the source's potential drop action(s)
     * and available data types (<code>DataFlavor</code> s).
     * </UL>
     * <P>
     * 
     * @param dsde
     *            the <code>DragSourceDragEvent</code>
     */
    public void dragEnter(DragSourceDragEvent dsde) {
    }

    /**
     * Called when a drag operation is ongoing on the <code>DropTarget</code>.
     * <P>
     * 
     * @param dtde
     *            the <code>DropTargetDragEvent</code>
     */
    public void dragOver(DropTargetDragEvent dtde) {
    }

    /**
     * A <code>DragGestureRecognizer</code> has detected a platform-dependent
     * drag initiating gesture and is notifying this listener in order for it to
     * initiate the action for the user.
     * <P>
     * 
     * @param dge
     *            the <code>DragGestureEvent</code> describing the gesture
     *            that has just occurred
     */
    public void dragGestureRecognized(DragGestureEvent event) {
        Point origin = event.getDragOrigin();
        int sq = Math.min(getHeight(), getWidth() / 7);
        int x = origin.x / sq;
        if (x < 0) {
            return;
        }
        if (this.getTileCount() <= x) {
            return;
        }
        Tile tile = (Tile) tiles.get(x);
        String transferData = tile.getLetter() + ",R," + x;
        ScrabbleTransferable transferable = new ScrabbleTransferable(
                transferData);
        dragSource.startDrag(event, DragSource.DefaultMoveDrop, transferable,
                this);
    }

    /**
     * Called if the user has modified the current drop gesture.
     * <P>
     * 
     * @param dtde
     *            the <code>DropTargetDragEvent</code>
     */
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    /**
     * This method is invoked to signify that the Drag and Drop operation is
     * complete. The getDropSuccess() method of the
     * <code>DragSourceDropEvent</code> can be used to determine the
     * termination state. The getDropAction() method returns the operation that
     * the <code>DropTarget</code> selected (via the DropTargetDropEvent
     * acceptDrop() parameter) to apply to the Drop operation. Once this method
     * is complete, the current <code>DragSourceContext</code> and associated
     * resources become invalid.
     * <P>
     * 
     * @param dsde
     *            the <code>DragSourceDropEvent</code>
     */
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    private int getTileIndex(Point point, boolean allowSize) {
        int sq = Math.min(getHeight(), getWidth() / 7);
        int x = point.x / sq;
        if (x < 0) {
            x = 0;
        }
        else {
            if (allowSize) {
                if (x > this.getTileCount()) {
                    x = this.getTileCount();
                }
            }
            else {
                if (x >= this.getTileCount()) {
                    x = this.getTileCount() - 1;
                }
            }
        }
        return x;
    }

    public boolean indexInBounds(int x) {
        return 0 <= x && x < this.getTileCount();
    }

    public ScrabbleBoard getBoard() {
        return this.getGame().getBoard();
    }

    public ScrabbleGame getGame() {
        return game;
    }

    public void removeAt(int index) {
        tiles.remove(index);
    }

    public void addAt(int index, char letter) {
        tiles.add(index, new Tile(letter));
    }
}