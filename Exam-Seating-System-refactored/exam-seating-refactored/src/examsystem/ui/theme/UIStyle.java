package examsystem.ui.theme;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Static factory methods for building consistently styled Swing
 * components and layouts. Centralizing this here removes the
 * duplicated "build a labeled form" and "style this table" code that
 * used to be repeated in every panel of MainFrame.
 */
public final class UIStyle {

    private UIStyle() { }

    // ---------- Page / layout scaffolding ----------

    public static JPanel page() {
        JPanel panel = new JPanel(new BorderLayout(Theme.SPACING_MD, Theme.SPACING_MD));
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(new EmptyBorder(Theme.SPACING_LG, Theme.SPACING_LG, Theme.SPACING_LG, Theme.SPACING_LG));
        return panel;
    }

    /** A left-aligned row of action buttons with consistent spacing. */
    public static JPanel buttonRow(java.awt.Component... buttons) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, Theme.SPACING_SM, 0));
        row.setOpaque(false);
        for (java.awt.Component b : buttons) {
            row.add(b);
        }
        return row;
    }

    /**
     * Builds a labeled form inside a Card using a GridBagLayout, so field
     * labels and inputs line up cleanly regardless of how many rows there are.
     *
     * @param title  card heading
     * @param labels field labels, in order
     * @param fields matching text fields, in order
     * @param footer component placed under the fields (typically a button row)
     */
    public static Card labeledForm(String title, String[] labels, JTextField[] fields, Component footer) {
        Card card = new Card(title);
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(Theme.SPACING_XS, 0, Theme.SPACING_XS, Theme.SPACING_SM);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0;
            form.add(label(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.weightx = 1;
            form.add(fields[i], gbc);
        }

        JPanel body = new JPanel(new BorderLayout(0, Theme.SPACING_SM));
        body.setOpaque(false);
        body.add(form, BorderLayout.NORTH);
        if (footer != null) {
            body.add(footer, BorderLayout.SOUTH);
        }
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    // ---------- Components ----------

    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_LABEL);
        l.setForeground(Theme.TEXT_SECONDARY);
        return l;
    }

    public static JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_TITLE);
        l.setForeground(Theme.TEXT_PRIMARY);
        return l;
    }

    public static JTextField textField() {
        JTextField field = new JTextField();
        field.setFont(Theme.FONT_BODY);
        field.setForeground(Theme.TEXT_PRIMARY);
        Border line = BorderFactory.createLineBorder(Theme.BORDER, 1, true);
        Border padding = new EmptyBorder(Theme.SPACING_XS, Theme.SPACING_SM, Theme.SPACING_XS, Theme.SPACING_SM);
        field.setBorder(new CompoundBorder(line, padding));
        return field;
    }

    public static JTextArea monospaceArea() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(Theme.FONT_MONO);
        area.setForeground(Theme.TEXT_PRIMARY);
        area.setBackground(Theme.SURFACE);
        area.setBorder(new EmptyBorder(Theme.SPACING_SM, Theme.SPACING_SM, Theme.SPACING_SM, Theme.SPACING_SM));
        return area;
    }

    /** Wraps content in a titled Card + scroll pane, matching the app's card style. */
    public static Card cardWithScroll(String title, Component content) {
        Card card = new Card(title);
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1, true));
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(28);
        table.setFont(Theme.FONT_BODY);
        table.setForeground(Theme.TEXT_PRIMARY);
        table.setGridColor(Theme.BORDER);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(Theme.SELECTION);
        table.setSelectionForeground(Theme.TEXT_PRIMARY);
        table.setIntercellSpacing(new java.awt.Dimension(0, 1));
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(Theme.FONT_HEADING);
        header.setForeground(Theme.TEXT_SECONDARY);
        header.setBackground(Theme.SURFACE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER));
        header.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new StripedRowRenderer());
    }

    public static void styleTabbedPane(JTabbedPane tabs) {
        tabs.setFont(Theme.FONT_HEADING);
        tabs.setBackground(Theme.BACKGROUND);
        tabs.setForeground(Theme.TEXT_PRIMARY);
    }

    /** Alternating row backgrounds so long tables stay easy to scan. */
    private static class StripedRowRenderer extends javax.swing.table.DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                         boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setHorizontalAlignment(SwingConstants.LEFT);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Theme.SURFACE : Theme.TABLE_STRIPE);
            }
            return c;
        }
    }
}
