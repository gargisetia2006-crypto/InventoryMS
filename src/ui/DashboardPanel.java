package ui;

import dao.*;
import util.Theme;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import model.Product;

public class DashboardPanel extends JPanel {
    private final ProductDAO productDAO = new ProductDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final SupplierDAO supplierDAO = new SupplierDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();

    public DashboardPanel() {
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0, 20));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        build();
    }

    private void build() {
        removeAll();
        // Header
        JLabel title = new JLabel("Dashboard Overview");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        add(title, BorderLayout.NORTH);

        // Stats cards
        JPanel cards = new JPanel(new GridLayout(2, 3, 15, 15));
        cards.setOpaque(false);

        int products = productDAO.getAll().size();
        int customers = customerDAO.getAll().size();
        int suppliers = supplierDAO.getAll().size();
        int employees = employeeDAO.getAll().size();
        int invoices = invoiceDAO.getTotalInvoices();
        double revenue = invoiceDAO.getTotalRevenue();
        List<Product> lowStock = productDAO.getLowStock();

        cards.add(statCard("📦 Products", String.valueOf(products), Theme.ACCENT));
        cards.add(statCard("👥 Customers", String.valueOf(customers), Theme.SUCCESS));
        cards.add(statCard("🏭 Suppliers", String.valueOf(suppliers), Theme.ACCENT2));
        cards.add(statCard("👷 Employees", String.valueOf(employees), new Color(156, 106, 222)));
        cards.add(statCard("🧾 Total Invoices", String.valueOf(invoices), Theme.WARNING));
        cards.add(statCard("💰 Total Revenue", String.format("₹ %.2f", revenue), Theme.SUCCESS));

        // Center: cards + low stock
        JPanel center = new JPanel(new BorderLayout(0, 20));
        center.setOpaque(false);
        center.add(cards, BorderLayout.NORTH);

        // Low stock warning
        if (!lowStock.isEmpty()) {
            JPanel lowPanel = new JPanel(new BorderLayout(0, 8));
            lowPanel.setBackground(Theme.BG_CARD);
            lowPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.DANGER, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
            ));
            JLabel warn = new JLabel("⚠️  Low Stock Alert (" + lowStock.size() + " items)");
            warn.setFont(Theme.FONT_HEADING);
            warn.setForeground(Theme.DANGER);
            lowPanel.add(warn, BorderLayout.NORTH);

            String[] cols = {"Product", "Category", "Current Stock", "Min Stock", "Unit"};
            Object[][] data = new Object[lowStock.size()][5];
            for (int i = 0; i < lowStock.size(); i++) {
                Product p = lowStock.get(i);
                data[i] = new Object[]{p.getName(), p.getCategory(), p.getQuantity(), p.getMinStock(), p.getUnit()};
            }
            JTable tbl = new JTable(data, cols) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            Theme.styleTable(tbl);
            JScrollPane sp = new JScrollPane(tbl);
            sp.setBackground(Theme.BG_CARD);
            sp.getViewport().setBackground(Theme.BG_CARD);
            sp.setBorder(BorderFactory.createEmptyBorder());
            sp.setPreferredSize(new Dimension(0, Math.min(lowStock.size() * 33 + 30, 180)));
            lowPanel.add(sp, BorderLayout.CENTER);
            center.add(lowPanel, BorderLayout.CENTER);
        }

        add(center, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private JPanel statCard(String label, String value, Color accent) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
            BorderFactory.createEmptyBorder(18, 16, 18, 16)
        ));

        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_BODY);
        lbl.setForeground(Theme.TEXT_MUTED);

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 26));
        val.setForeground(accent);

        card.add(lbl, BorderLayout.NORTH);
        card.add(val, BorderLayout.CENTER);
        return card;
    }

    public void refresh() { build(); }
}
