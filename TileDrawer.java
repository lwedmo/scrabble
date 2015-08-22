/*
 * TileDrawer.java
 *
 * Created on February 22, 2002, 6:55 PM
 */

/**
 * 
 * @author lwe
 * @version
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class TileDrawer {

    /** Creates new TileDrawer */
    public TileDrawer() {
    }

    /**
     * Draw a tile on on canvas.
     * 
     * @param sq
     *            is the size of the square.
     * @param x
     *            is a cell coordinate
     */
    public static void draw(Graphics g, int sq, int x, int y, char letter) {
        String words[] = ScrabbleCell.getWords(x, y);

        g.setColor(letter == 0 ? ScrabbleCell.getColor(x, y)
                : ScrabbleColor.wood);
        x *= sq;
        y *= sq;
        g.fill3DRect(x, y, sq - 1, sq - 1, false); // background

        // Draw those three little words
        if (letter == 0 && words != null) {
            Font wordFont = new Font("SansSerif", Font.BOLD, sq / 5);
            FontMetrics wordMetrics = g.getFontMetrics(wordFont);
            int wh = wordMetrics.getHeight();
            int wy = y + (sq - words.length * wh) / 2 + wh
                    - wordMetrics.getMaxDescent() - sq / 40;
            for (int i = 0; i < words.length; i++) {
                String w = words[i];
                char wordAr[] = new char[w.length()];
                w.getChars(0, w.length(), wordAr, 0);
                int ww = wordMetrics.charsWidth(wordAr, 0, wordAr.length);
                int wx = x + (sq - ww) / 2;
                g.setFont(wordFont);
                g.setColor(Color.black);
                g.setPaintMode();
                g.drawString(w, wx, wy);
                wy += wh;
            }
        }

        if (letter == 0 || letter == '?' || letter == ' '
                || Letter.isBlank(letter)) {
            return;
        }

        if (letter == '?') {
            letter = ' ';
        }
        char face[] = { letter };
        char subAr[] = new char[2];
        String subStr = String.valueOf(Letter.getScore(letter));
        int subLen = subStr.length();
        subStr.getChars(0, subLen, subAr, 0);

        Font font = new Font("SansSerif", Font.BOLD, (3 * sq) / 4);
        Font subFont = new Font("SansSerif", Font.BOLD, sq / 4);
        FontMetrics metrics = g.getFontMetrics(font);
        FontMetrics subMetrics = g.getFontMetrics(subFont);
        int cw = metrics.charWidth(letter);
        int ch = metrics.getHeight();
        int scw = subMetrics.charsWidth(subAr, 0, subLen);
        int sch = subMetrics.getHeight();

        x += (sq - (cw + scw)) / 2;
        y += sq - (sq - ch) / 2 - metrics.getMaxDescent() - sq / 20;
        g.setFont(font);
        g.setColor(Color.black);
        g.setPaintMode();
        g.drawString(new String(face), x, y);

        // The subscript
        x += cw;
        y += sch / 4;
        g.setFont(subFont);
        g.drawString(String.valueOf(Letter.getScore(letter)), x, y);
    }
}