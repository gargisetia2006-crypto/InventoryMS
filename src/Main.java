import ui.MainFrame;
import util.DatabaseConnection;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize database first
        DatabaseConnection.initializeDatabase();

        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            UIManager.put("Panel.background", new java.awt.Color(28, 35, 49));
            UIManager.put("OptionPane.background", new java.awt.Color(35, 44, 61));
            UIManager.put("OptionPane.messageForeground", new java.awt.Color(226, 232, 240));
            UIManager.put("Button.background", new java.awt.Color(99, 179, 237));
            UIManager.put("Button.foreground", java.awt.Color.WHITE);
            UIManager.put("TextField.background", new java.awt.Color(28, 35, 49));
            UIManager.put("TextField.foreground", new java.awt.Color(226, 232, 240));
            UIManager.put("TextField.caretForeground", new java.awt.Color(226, 232, 240));
            UIManager.put("ComboBox.background", new java.awt.Color(28, 35, 49));
            UIManager.put("ComboBox.foreground", new java.awt.Color(226, 232, 240));
            UIManager.put("ScrollPane.background", new java.awt.Color(35, 44, 61));
            UIManager.put("Viewport.background", new java.awt.Color(35, 44, 61));
            UIManager.put("TabbedPane.background", new java.awt.Color(35, 44, 61));
            UIManager.put("TabbedPane.foreground", new java.awt.Color(226, 232, 240));
            UIManager.put("TabbedPane.selected", new java.awt.Color(28, 35, 49));
            UIManager.put("Label.foreground", new java.awt.Color(226, 232, 240));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
