//
// // Display a rack
//

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class RackDisplay {
    private JFrame             frame;
    private JPanel             panel;
    private GridBagLayout      layout;
    private GridBagConstraints constraint;
    private Rack               rack;      // subclass of canvas

    RackDisplay(Rack rack) {
        this.rack = rack;

        frame = new JFrame("Scrabble Tile Rack");
        panel = new JPanel();
        layout = new GridBagLayout();
        constraint = new GridBagConstraints();

        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 1;
        constraint.weighty = 1;

        layout.setConstraints(rack, constraint);
        panel.setLayout(layout);

        rack.setSize(new Dimension(449, 64));
        rack.repaint(100);
        //frame.setSize(new Dimension(400,400));

        panel.add(rack);
        frame.getContentPane().add(panel);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String argv[]) {
        RackDisplay card = new RackDisplay(new Rack("ABCDEFG"));
    }

    public void setLocation(JFrame parent) {
        Rectangle r = parent.getBounds();
        Point p = new Point(r.x, r.y + r.height + 10);
        frame.setLocation(p);
    }

    public synchronized void repaint() {
        rack.repaint();
    }
}