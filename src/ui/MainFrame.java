package ui;

import util.Theme;
import util.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
    private JPanel contentArea;
    private CardLayout cardLayout;
    private DashboardPanel dashboardPanel;

    public MainFrame() {
        setTitle("Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout());

        buildSidebar();
        buildContent();
        showPanel("Dashboard");
    }

    private void buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(Theme.BG_SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Logo
        JPanel logo = new JPanel(new BorderLayout());
        logo.setBackground(new Color(15, 20, 31));
        logo.setMaximumSize(new Dimension(200, 70));
        logo.setPreferredSize(new Dimension(200, 70));
        JLabel logoTxt = new JLabel("📋 InvMS", JLabel.CENTER);
        logoTxt.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoTxt.setForeground(Theme.ACCENT);
        logo.add(logoTxt, BorderLayout.CENTER);
        sidebar.add(logo);

        // Nav items
        String[][] items = {
            {"🏠", "Dashboard"},
            {"🛒", "Sales"},
            {"👥", "Customers"},
            {"🏭", "Suppliers"},
            {"👷", "Employees"},
            {"📦", "Products"},
            {"🧾", "Invoices"},
            {"📊", "Reports"}
        };

        sidebar.add(Box.createVerticalStrut(10));
        ButtonGroup bg = new ButtonGroup();
        for (String[] item : items) {
            JToggleButton btn = makeNavBtn(item[0], item[1]);
            bg.add(btn);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(2));
            btn.addActionListener(e -> showPanel(item[1]));
        }

        sidebar.add(Box.createVerticalGlue());

        // Version label
        JLabel ver = new JLabel("v1.0  |  SQLite", JLabel.CENTER);
        ver.setFont(Theme.FONT_SMALL);
        ver.setForeground(Theme.TEXT_MUTED);
        ver.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        ver.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(ver);

        add(sidebar, BorderLayout.WEST);
    }

    private JToggleButton makeNavBtn(String icon, String label) {
        JToggleButton btn = new JToggleButton(icon + "  " + label);
        btn.setFont(Theme.FONT_BODY);
        btn.setForeground(Theme.TEXT_MUTED);
        btn.setBackground(Theme.BG_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));
        btn.setMaximumSize(new Dimension(200, 44));
        btn.setMinimumSize(new Dimension(200, 44));
        btn.setPreferredSize(new Dimension(200, 44));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addItemListener(e -> {
            if (btn.isSelected()) {
                btn.setBackground(new Color(45, 55, 80));
                btn.setForeground(Theme.ACCENT);
            } else {
                btn.setBackground(Theme.BG_SIDEBAR);
                btn.setForeground(Theme.TEXT_MUTED);
            }
        });
        return btn;
    }

    private void buildContent() {
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(Theme.BG_DARK);

        dashboardPanel = new DashboardPanel();
        contentArea.add(dashboardPanel, "Dashboard");
        contentArea.add(new SalesPanel(), "Sales");
        contentArea.add(new CustomerPanel(), "Customers");
        contentArea.add(new SupplierPanel(), "Suppliers");
        contentArea.add(new EmployeePanel(), "Employees");
        contentArea.add(new ProductPanel(), "Products");
        contentArea.add(new InvoicePanel(), "Invoices");
        contentArea.add(new ReportsPanel(), "Reports");

        add(contentArea, BorderLayout.CENTER);
    }

    private void showPanel(String name) {
        if ("Dashboard".equals(name)) dashboardPanel.refresh();
        cardLayout.show(contentArea, name);
    }
}
