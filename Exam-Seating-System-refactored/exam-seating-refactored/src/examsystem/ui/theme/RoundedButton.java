package examsystem.ui.theme;

import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A flat, rounded-corner button in the Apple HIG style: no border,
 * soft rounded shape, subtle color shift on hover/press instead of
 * a 3D bevel. Used everywhere a JButton was previously used so every
 * screen shares the same control style.
 */
public class RoundedButton extends JButton {

    public enum Style { PRIMARY, SECONDARY, DESTRUCTIVE }

    private final Style style;
    private boolean hovered = false;
    private boolean pressed = false;

    public RoundedButton(String text, Style style) {
        super(text);
        this.style = style;
        setFont(Theme.FONT_BUTTON);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setForeground(style == Style.SECONDARY ? Theme.TEXT_PRIMARY : Color.WHITE);
        setBorder(javax.swing.BorderFactory.createEmptyBorder(
            Theme.SPACING_SM, Theme.SPACING_MD, Theme.SPACING_SM, Theme.SPACING_MD));

        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
            @Override public void mouseExited(MouseEvent e) { hovered = false; repaint(); }
            @Override public void mousePressed(MouseEvent e) { pressed = true; repaint(); }
            @Override public void mouseReleased(MouseEvent e) { pressed = false; repaint(); }
        });
    }

    private Color backgroundColor() {
        return switch (style) {
            case PRIMARY -> pressed ? Theme.ACCENT_PRESSED : hovered ? Theme.ACCENT_HOVER : Theme.ACCENT;
            case DESTRUCTIVE -> pressed || hovered ? Theme.DANGER_HOVER : Theme.DANGER;
            case SECONDARY -> pressed ? new Color(0xE1, 0xE1, 0xE4)
                             : hovered ? new Color(0xEC, 0xEC, 0xEF)
                             : new Color(0xF0, 0xF0, 0xF2);
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(backgroundColor());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), Theme.RADIUS_SMALL, Theme.RADIUS_SMALL);
        g2.dispose();
        super.paintComponent(g);
    }
}
