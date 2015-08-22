public class Scrabble {
    private ScrabbleGame  game;
    private ScrabbleFrame frame;
    private RackDisplay   rackdisplay;

    public Scrabble() {
        game = new ScrabbleGame(this);
        frame = new ScrabbleFrame(game.getBoard(), game.getRack(0), game);
        rackdisplay = new RackDisplay(game.getRack(0));
        rackdisplay.setLocation(frame);
    }

    public void playself() {
        for (;;) {
            game.playone();
            frame.repaint();
        }
    }

    public static void main(String av[]) {
        Scrabble scrabble = new Scrabble();
        scrabble.game.initializeDisplay();
    }

    public ScrabbleFrame getFrame() {
        return frame;
    }

    public ScoreCard getScoreCard() {
        return getFrame().getControl().getScoreCard();
    }

    public void repaint() {
        frame.repaint();
        rackdisplay.repaint();
    }

    public RackDisplay getRackDisplay() {
        return rackdisplay;
    }

    void showMessageDialog(String msg) {
        getFrame().showMessageDialog(msg);
    }

    public ScrabbleGame getGame() {
        return this.game;
    }
}