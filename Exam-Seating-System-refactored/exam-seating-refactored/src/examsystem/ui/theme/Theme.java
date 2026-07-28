package examsystem.ui.theme;

import java.awt.Color;
import java.awt.Font;

/**
 * Design tokens for the application's Apple-inspired visual language.
 * Every screen pulls colors, fonts and spacing from here so the whole
 * app stays visually consistent. Nothing in this class affects behavior.
 */
public final class Theme {

    private Theme() { }

    // ---- Palette (restrained, neutral, one accent) ----
    public static final Color BACKGROUND      = new Color(0xF5, 0xF5, 0xF7); // page background
    public static final Color SURFACE         = Color.WHITE;                 // cards / panels
    public static final Color BORDER          = new Color(0xE3, 0xE3, 0xE6);
    public static final Color TEXT_PRIMARY    = new Color(0x1D, 0x1D, 0x1F);
    public static final Color TEXT_SECONDARY  = new Color(0x6E, 0x6E, 0x73);
    public static final Color ACCENT          = new Color(0x00, 0x7A, 0xFF); // Apple-style blue
    public static final Color ACCENT_HOVER    = new Color(0x0A, 0x66, 0xD6);
    public static final Color ACCENT_PRESSED  = new Color(0x08, 0x53, 0xAF);
    public static final Color DANGER          = new Color(0xFF, 0x3B, 0x30);
    public static final Color DANGER_HOVER    = new Color(0xE0, 0x2E, 0x24);
    public static final Color SUCCESS         = new Color(0x30, 0xB0, 0x50);
    public static final Color SELECTION       = new Color(0xD8, 0xE9, 0xFF);
    public static final Color TABLE_STRIPE    = new Color(0xFA, 0xFA, 0xFB);

    // ---- Typography (falls back gracefully across OSes) ----
    private static final String FONT_FAMILY = pickAvailableFont();

    public static final Font FONT_TITLE     = new Font(FONT_FAMILY, Font.BOLD, 20);
    public static final Font FONT_HEADING   = new Font(FONT_FAMILY, Font.BOLD, 14);
    public static final Font FONT_BODY      = new Font(FONT_FAMILY, Font.PLAIN, 13);
    public static final Font FONT_LABEL     = new Font(FONT_FAMILY, Font.PLAIN, 12);
    public static final Font FONT_BUTTON    = new Font(FONT_FAMILY, Font.BOLD, 13);
    public static final Font FONT_MONO      = new Font("Monospaced", Font.PLAIN, 12);

    // ---- Spacing scale (4pt base grid) ----
    public static final int SPACING_XS = 4;
    public static final int SPACING_SM = 8;
    public static final int SPACING_MD = 16;
    public static final int SPACING_LG = 24;
    public static final int SPACING_XL = 32;

    // ---- Shape ----
    public static final int RADIUS_SMALL = 8;
    public static final int RADIUS_MEDIUM = 12;

    private static String pickAvailableFont() {
        String[] preferred = {"SF Pro Text", "Helvetica Neue", "Segoe UI", "Ubuntu", "SansSerif"};
        java.util.Set<String> available = java.util.Set.of(
            java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String candidate : preferred) {
            if (available.contains(candidate)) {
                return candidate;
            }
        }
        return Font.SANS_SERIF;
    }
}
