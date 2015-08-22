
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

/**
 * A canvas on which to display the game board. Draw the board
 */
class ScrabbleCanvas extends Canvas implements DropTargetListener,
        DragSourceListener, DragGestureListener {
    private DisplayBoard     board;
    private static final int N = ScrabbleBoard.DIMENSION;
    private ScrabbleFrame    frame;
    private DragSource       dragSource;
    private DropTarget       dropTarget;

    public void paint(Graphics g) {
        int sq = Math.min(getHeight(), getWidth()) / N;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                char a = board.getLetter(j, i);
                TileDrawer.draw(g, sq, j, i, a);
            }
        }
    }

    public ScrabbleCanvas(ScrabbleFrame frame, DisplayBoard board) {
        this.board = board;
        this.frame = frame;

        // Field mouse clicks.
        this.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (getFrame() != null) {
                    getFrame().canvasClick(getCell(event.getPoint()));
                }
            }
        });

        dropTarget = new DropTarget(this, this);
        dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this,
                DnDConstants.ACTION_MOVE, this);
    }

    public ScrabbleFrame getFrame() {
        return frame;
    }

    public synchronized void setDisplayBoard(DisplayBoard board) {
        this.board = board;
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
    public void dragEnter(DropTargetDragEvent dtde) {
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
            if (this.getFrame().getGame().getThinking()) {
                return;
            }
            ScrabbleCell cell = this.getCell(event.getLocation());
            if (getBoard().getLetter(cell.x, cell.y) != 0) {
                return; // dropping on tile on another
            }
            Transferable transferable = event.getTransferable();
            String transferData = (String) transferable
                    .getTransferData(ScrabbleTransferable.tileFlavor);
            StringTokenizer tokenizer = new StringTokenizer(transferData, ",");
            char letter = tokenizer.nextToken().charAt(0);
            if (letter == '?') {
                String s = JOptionPane.showInputDialog(this,
                        "What letter would you like this blank to represent?");
                if (s == null || s.length() == 0
                        || !Character.isLetter(s.charAt(0))) {
                    return;
                }
                letter = Character.toLowerCase(s.charAt(0));
            }
            char source = tokenizer.nextToken().charAt(0);
            if (source == 'R') {
                // rack to board move
                int x = Integer.parseInt(tokenizer.nextToken());
                Rack rack = this.getFrame().getGame().getRack(0);
                rack.removeAt(x);
                getBoard().setLetter(cell.x, cell.y, letter, false);
                board = new DisplayBoard(getBoard());
                this.getFrame().repaint();
            }
            else if (source == 'B') {
                // board to board move
                int x = Integer.parseInt(tokenizer.nextToken());
                int y = Integer.parseInt(tokenizer.nextToken());
                getBoard().setLetter(x, y, (char) 0, false);
                getBoard().setLetter(cell.x, cell.y, letter, false);
                board = new DisplayBoard(getBoard());
                this.getFrame().getCanvas().repaint();
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
        if (this.getFrame().getGame().getThinking()) {
            return;
        }
        ScrabbleCell cell = this.getCell(event.getDragOrigin());
        ScrabbleBoard b = this.getFrame().getGame().getBoard();
        char letter = b.getLetter(cell.x, cell.y);
        if (letter == 0) {
            return;
        }
        if (b.isPermanent(cell.x, cell.y)) {
            return;
        }
        String transferData = letter + ",B," + cell.x + ',' + cell.y;
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

    /**
     * find the cell containg the point
     */
    private ScrabbleCell getCell(Point point) {
        int edge = Math.min(getHeight(), getWidth()) / N;
        int x = point.x / edge;
        int y = point.y / edge;
        if (x < 0)
            x = 0;
        if (x >= N)
            x = N - 1;
        if (y < 0)
            y = 0;
        if (y >= N)
            y = N - 1;
        return new ScrabbleCell(x, y);
    }

    public ScrabbleBoard getBoard() {
        return this.getFrame().getGame().getBoard();
    }
}