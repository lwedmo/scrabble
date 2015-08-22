
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * The basis of the visual presentation of the Scrabble game.
 */
public class ScrabbleFrame extends JFrame {
    private ScrabbleCanvas  canvas;
    private Rack            rack;
    private ScrabbleControl control;
    private MessagePanel    messagePanel;
    private ScrabbleGame    game;

    public ScrabbleFrame(ScrabbleBoard board, Rack rack, ScrabbleGame game) {
        super("Scrabble Board");

        this.rack = rack;
        this.game = game;

        canvas = new ScrabbleCanvas(this, new DisplayBoard(board));
        control = new ScrabbleControl(game);
        messagePanel = new MessagePanel();

        this.canvas.setSize(new Dimension(400, 400));
        this.rack.setSize(new Dimension(400, 40));
        this.control.setSize(new Dimension(150, 400));
        this.messagePanel.setSize(new Dimension(400, 200));

        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.add(canvas, BorderLayout.CENTER);
        //bannerPanel.add (rack, BorderLayout.SOUTH);
        bannerPanel.add(control, BorderLayout.EAST);
        bannerPanel.add(messagePanel, BorderLayout.NORTH);

        bannerPanel.setBorder(BorderFactory.createTitledBorder("Scrabble"));

        //Add the components to the scrabble frame
        Container contentPane = getContentPane();
        contentPane.add(bannerPanel, BorderLayout.CENTER);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        this.pack();
        this.setVisible(true);
    }

    public synchronized void repaint() {
        canvas.repaint();
        rack.repaint();
        control.repaint();
    }

    public static void main(String[] args) {
        ScrabbleBoard board = new ScrabbleBoard();
        Rack rack = new Rack();
        JFrame frame = new ScrabbleFrame(board, rack, null);
    }

    public void canvasClick(ScrabbleCell cell) {
        canvasClick(cell.x, cell.y);
    }

    public void canvasClick(int x, int y) {
        getGame().userMove(getControl().getWord(), x, y,
                getControl().getAcross());
    }

    public ScrabbleGame getGame() {
        return game;
    }

    public ScrabbleControl getControl() {
        return control;
    }

    public ScrabbleCanvas getCanvas() {
        return canvas;
    }

    public void showMessageDialog(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }
}