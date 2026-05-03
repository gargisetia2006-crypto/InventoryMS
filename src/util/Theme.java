package util;

import java.awt.*;

public class Theme {
    public static final Color BG_DARK      = new Color(28, 35, 49);
    public static final Color BG_SIDEBAR   = new Color(20, 26, 38);
    public static final Color BG_CARD      = new Color(35, 44, 61);
    public static final Color ACCENT       = new Color(99, 179, 237);
    public static final Color ACCENT2      = new Color(72, 149, 239);
    public static final Color SUCCESS      = new Color(72, 199, 142);
    public static final Color WARNING      = new Color(255, 190, 11);
    public static final Color DANGER       = new Color(255, 99, 99);
    public static final Color TEXT_PRIMARY = new Color(226, 232, 240);
    public static final Color TEXT_MUTED   = new Color(113, 128, 150);
    public static final Color BORDER       = new Color(45, 55, 72);

    public static final Font FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_HEADING  = new Font("Segoe UI", Font.BOLD, 15);
    public static final Font FONT_BODY     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_SMALL    = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font FONT_BTN      = new Font("Segoe UI", Font.BOLD, 13);

    public static void styleButton(javax.swing.JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BTN);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
    }

    public static void styleField(javax.swing.JTextField field) {
        field.setBackground(BG_DARK);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(TEXT_PRIMARY);
        field.setFont(FONT_BODY);
        field.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(BORDER, 1),
            javax.swing.BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    public static void styleCombo(javax.swing.JComboBox<?> combo) {
        combo.setBackground(BG_DARK);
        combo.setForeground(TEXT_PRIMARY);
        combo.setFont(FONT_BODY);
    }

    public static void styleTable(javax.swing.JTable table) {
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setFont(FONT_BODY);
        table.setRowHeight(32);
        table.setGridColor(BORDER);
        table.setSelectionBackground(ACCENT2);
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(BG_DARK);
        table.getTableHeader().setForeground(ACCENT);
        table.getTableHeader().setFont(FONT_HEADING);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
    }
}
