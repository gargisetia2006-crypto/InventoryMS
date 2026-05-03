package ui;

import dao.InvoiceDAO;
import model.Invoice;
import model.InvoiceItem;
import util.Theme;
import javax.swing.*;
import java.util.List;
import java.awt.*;

public class InvoicePanel extends BaseCRUDPanel {
    private final InvoiceDAO dao = new InvoiceDAO();
    private List<Invoice> currentList;

    public InvoicePanel() { super("🧾 Invoice Management"); }

    protected String[] getColumns() {
        return new String[]{"ID","Invoice #","Customer | Date","Total (₹)","Discount","Tax","Payment","Status"};
    }

    protected void loadData() { currentList = dao.getAll(); fillTable(currentList); }
    protected void onSearch(String kw) { currentList = kw.isEmpty() ? dao.getAll() : dao.search(kw); fillTable(currentList); }

    private void fillTable(List<Invoice> list) {
        tableModel.setRowCount(0);
        for (Invoice inv : list)
            tableModel.addRow(new Object[]{inv.getId(), inv.getInvoiceNumber(), inv.getCreatedAt(),
                String.format("%.2f", inv.getTotalAmount()), String.format("%.2f", inv.getDiscount()),
                String.format("%.2f", inv.getTax()), inv.getPaymentMethod(), inv.getStatus()});
    }

    protected void showAddDialog() {
        JOptionPane.showMessageDialog(this, "Use the Sales module to create invoices.", "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showEditDialog(int row) {
        Invoice inv = currentList.get(row);
        List<InvoiceItem> items = dao.getItemsByInvoiceId(inv.getId());
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice: ").append(inv.getInvoiceNumber()).append("\n\n");
        sb.append(String.format("%-30s %5s %10s %10s\n","Product","Qty","Price","Subtotal"));
        sb.append("-".repeat(58)).append("\n");
        for (InvoiceItem it : items)
            sb.append(String.format("%-30s %5d %10.2f %10.2f\n", it.getProductName(), it.getQuantity(), it.getUnitPrice(), it.getSubtotal()));
        sb.append("-".repeat(58)).append("\n");
        sb.append(String.format("Discount: %.2f | Tax: %.2f | TOTAL: %.2f", inv.getDiscount(), inv.getTax(), inv.getTotalAmount()));
        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setEditable(false);
        ta.setBackground(Theme.BG_DARK); ta.setForeground(Theme.TEXT_PRIMARY);
        JScrollPane sp = new JScrollPane(ta);
        sp.setPreferredSize(new Dimension(550, 300));
        JOptionPane.showMessageDialog(this, sp, "Invoice Details", JOptionPane.PLAIN_MESSAGE);
    }

    protected void deleteRow(int row) {
        JOptionPane.showMessageDialog(this, "Invoice deletion is not allowed for audit integrity.");
    }
}
