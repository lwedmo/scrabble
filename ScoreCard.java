
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * The score frame contains a text item which accumulates scoring information.
 */
public class ScoreCard extends JScrollPane {
    ScoreCard() {
        super(new JTextArea());
        getTextArea().setEditable(false);
        getTextArea().setFont(new Font("Courier", Font.PLAIN, 12));
        setPreferredSize(new Dimension(300, 100));
        setAlignmentX((float) 0.5);
    }

    public static void main(String argv[]) {
        ScoreCard card = new ScoreCard();
        JFrame frame = new JFrame("Scrabble ScoreCard");
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(card);
        frame.pack();
        frame.setVisible(true);

        for (int i = 0; i < 100; i++) {
            card.log("This is a test message line #" + (i + 1) + ".\n");
        }
    }

    /**
     * make a log entry
     */
    public void log(String s) {
        getTextArea().append(s);
    }

    public JTextArea getTextArea() {
        return (JTextArea) getViewport().getView();
    }
}