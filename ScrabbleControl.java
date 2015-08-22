//
// // // The Scrabble control is on the right and is used to drive the application.
// The user moves with it.
// I envision a text box and across / down radio button
//

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class ScrabbleControl extends JPanel implements ActionListener {
    private JRadioButton acrossButton;
    private JRadioButton downButton;
    private JTextField   textField;
    private JButton      discardButton;
    private ScrabbleGame game;
    private JButton      scrambleButton;
    private ScoreCard    scoreCard;
    private JButton      moveButton;

    ScrabbleControl(ScrabbleGame game) {
        this.game = game;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Create radio buttons
        acrossButton = new JRadioButton("across", true);
        downButton = new JRadioButton("down", false);

        ButtonGroup group = new ButtonGroup();
        group.add(acrossButton);
        group.add(downButton);

        textField = new JTextField();
        textField.setMaximumSize(new Dimension(200, 22));

        scrambleButton = new JButton("SCRAMBLE");
        scrambleButton.setActionCommand("scramble");
        scrambleButton.addActionListener(this);

        discardButton = new JButton("DISCARD");
        discardButton.setActionCommand("discard");
        discardButton.addActionListener(this);

        moveButton = new JButton("MAKE IT SO");
        moveButton.setActionCommand("make it so");
        moveButton.addActionListener(this);

        scoreCard = new ScoreCard();

        this.add(textField);
        this.add(acrossButton);
        this.add(downButton);
        this.add(discardButton);
        this.add(scrambleButton);
        this.add(moveButton);
        this.add(scoreCard);
    }

    public static void main(String args[]) {
        ScrabbleControl control = new ScrabbleControl(null);
        JFrame frame = new JFrame();
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.getContentPane().add(control);
        frame.pack();
        frame.setVisible(true);
    }

    public boolean getAcross() {
        return getAcrossButton().getModel().isSelected();
    }

    public String getWord() {
        return getTextField().getText();
    }

    public JRadioButton getAcrossButton() {
        return acrossButton;
    }

    public JTextField getTextField() {
        return textField;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("discard")) {
            discard();
        }
        else if (cmd.equals("scramble")) {
            scramble();
        }
        else if (cmd.equals("make it so")) {
            this.getGame().DndUserMove();
        }

    }

    public void discard() {
        getGame().discard();
    }

    public ScrabbleGame getGame() {
        return game;
    }

    public void scramble() {
        getGame().scramble();
    }

    public ScoreCard getScoreCard() {
        return scoreCard;
    }
}