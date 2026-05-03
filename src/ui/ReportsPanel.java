package ui;

import dao.*;
import model.Product;
import util.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ReportsPanel extends JPanel {
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();

    public ReportsPanel() {
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        build();
    }

    private void build() {
        JLabel title = new JLabel("📊 Reports & Analytics");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(Theme.BG_CARD);
        tabs.setForeground(Theme.TEXT_PRIMARY);
        tabs.setFont(Theme.FONT_BODY);

        tabs.addTab("💰 Sales Summary", salesSummaryPanel());
        tabs.addTab("📦 Inventory Report", inventoryPanel());
        tabs.addTab("⚠️  Low Stock", lowStockPanel());

        add(tabs, BorderLayout.CENTER);
    }

    private JPanel salesSummaryPanel() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(Theme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Summary cards
        JPanel cards = new JPanel(new GridLayout(1, 3, 15, 0));
        cards.setOpaque(false);
        double revenue = invoiceDAO.getTotalRevenue();
        int invoices   = invoiceDAO.getTotalInvoices();
        double avg     = invoices > 0 ? revenue / invoices : 0;
        cards.add(miniCard("Total Revenue", String.format("₹ %.2f", revenue), Theme.SUCCESS));
        cards.add(miniCard("Total Invoices", String.valueOf(invoices), Theme.ACCENT));
        cards.add(miniCard("Avg. Invoice Value", String.format("₹ %.2f", avg), Theme.WARNING));
        p.add(cards, BorderLayout.NORTH);

        // Last 7 days table
        Map<String, Double> daily = invoiceDAO.getDailySales(7);
        String[] cols = {"Date", "Sales (₹)"};
        Object[][] data = daily.entrySet().stream()
            .map(e -> new Object[]{e.getKey(), String.format("%.2f", e.getValue())})
            .toArray(Object[][]::new);
        JTable tbl = new JTable(new DefaultTableModel(data, cols) { public boolean isCellEditable(int r, int c){ return false; } });
        Theme.styleTable(tbl);
        JScrollPane sp = new JScrollPane(tbl);
        sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Theme.BORDER), "Last 7 Days Sales",
            0, 0, Theme.FONT_BODY, Theme.ACCENT));
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private JPanel inventoryPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        List<Product> products = productDAO.getAll();
        String[] cols = {"Name","Category","Stock","Min Stock","Unit","Selling Price","Cost Price","Stock Value"};
        Object[][] data = products.stream().map(pr -> new Object[]{
            pr.getName(), pr.getCategory(), pr.getQuantity(), pr.getMinStock(), pr.getUnit(),
            String.format("%.2f", pr.getPrice()), String.format("%.2f", pr.getCostPrice()),
            String.format("%.2f", pr.getQuantity() * pr.getCostPrice())
        }).toArray(Object[][]::new);
        JTable tbl = new JTable(new DefaultTableModel(data, cols){ public boolean isCellEditable(int r,int c){return false;} });
        Theme.styleTable(tbl);
        double totalValue = products.stream().mapToDouble(pr -> pr.getQuantity() * pr.getCostPrice()).sum();
        JLabel footer = new JLabel("  Total Inventory Value: ₹ " + String.format("%.2f", totalValue));
        footer.setFont(Theme.FONT_HEADING);
        footer.setForeground(Theme.SUCCESS);
        footer.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        JScrollPane sp = new JScrollPane(tbl);
        sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        p.add(sp, BorderLayout.CENTER);
        p.add(footer, BorderLayout.SOUTH);
        return p;
    }

    private JPanel lowStockPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Theme.BG_DARK);
        p.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        List<Product> lowStock = productDAO.getLowStock();
        String[] cols = {"Name","Category","Current Stock","Min Stock","Unit","Status"};
        Object[][] data = lowStock.stream().map(pr -> new Object[]{
            pr.getName(), pr.getCategory(), pr.getQuantity(), pr.getMinStock(), pr.getUnit(),
            pr.getQuantity() == 0 ? "OUT OF STOCK" : "LOW STOCK"
        }).toArray(Object[][]::new);
        JTable tbl = new JTable(new DefaultTableModel(data, cols){ public boolean isCellEditable(int r,int c){return false;} });
        Theme.styleTable(tbl);
        JLabel hdr = new JLabel("  " + lowStock.size() + " item(s) need restocking");
        hdr.setFont(Theme.FONT_HEADING);
        hdr.setForeground(Theme.DANGER);
        hdr.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        JScrollPane sp = new JScrollPane(tbl);
        sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        p.add(hdr, BorderLayout.NORTH);
        p.add(sp, BorderLayout.CENTER);
        return p;
    }

    private JPanel miniCard(String label, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(Theme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, color),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        JLabel l = new JLabel(label);
        l.setFont(Theme.FONT_SMALL);
        l.setForeground(Theme.TEXT_MUTED);
        JLabel v = new JLabel(value);
        v.setFont(new Font("Segoe UI", Font.BOLD, 18));
        v.setForeground(color);
        card.add(l, BorderLayout.NORTH);
        card.add(v, BorderLayout.CENTER);
        return card;
    }
}
