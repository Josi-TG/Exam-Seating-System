package examsystem.ui.theme;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 * A rounded, bordered container with an optional heading — the basic
 * building block every panel in this app is composed from, standing
 * in for the "cards and panels" Apple's HIG describes.
 */
public class Card extends JPanel {

    public Card(String title) {
        super(new BorderLayout(0, Theme.SPACING_SM));
        setOpaque(false);
        setBorder(new EmptyBorder(Theme.SPACING_MD, Theme.SPACING_MD, Theme.SPACING_MD, Theme.SPACING_MD));

        if (title != null) {
            JLabel heading = new JLabel(title);
            heading.setFont(Theme.FONT_HEADING);
            heading.setForeground(Theme.TEXT_PRIMARY);
            add(heading, BorderLayout.NORTH);
        }
    }

    public Card() {
        this(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Theme.SURFACE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Theme.RADIUS_MEDIUM, Theme.RADIUS_MEDIUM);
        g2.setColor(Theme.BORDER);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Theme.RADIUS_MEDIUM, Theme.RADIUS_MEDIUM);
        g2.dispose();
        super.paintComponent(g);
    }
}
