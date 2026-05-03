package ui;

import dao.*;
import model.*;
import util.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class SalesPanel extends JPanel {
    private final InvoiceDAO invoiceDAO = new InvoiceDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    // Cart
    private DefaultTableModel cartModel;
    private final List<InvoiceItem> cartItems = new ArrayList<>();
    private JLabel lblTotal, lblTax, lblGrand;
    private JComboBox<Customer> cbCustomer;
    private JComboBox<Employee> cbEmployee;
    private JComboBox<Product> cbProduct;
    private JTextField txtQty, txtDiscount, txtPayment;
    private JComboBox<String> cbPaymentMethod;

    public SalesPanel() {
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(15, 0));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        build();
    }

    private void build() {
        // Title
        JLabel title = new JLabel("🛒 New Sale / Invoice");
        title.setFont(Theme.FONT_TITLE);
        title.setForeground(Theme.TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // LEFT: form
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(320, 0));

        left.add(sectionLabel("Customer & Employee"));
        cbCustomer = new JComboBox<>();
        cbEmployee = new JComboBox<>();
        styleCombo(cbCustomer); styleCombo(cbEmployee);
        customerDAO.getAll().forEach(c -> cbCustomer.addItem(c));
        employeeDAO.getAll().forEach(e -> cbEmployee.addItem(e));
        left.add(labeledRow("Customer:", cbCustomer));
        left.add(labeledRow("Employee:", cbEmployee));
        left.add(Box.createVerticalStrut(15));

        left.add(sectionLabel("Add Product to Cart"));
        cbProduct = new JComboBox<>();
        styleCombo(cbProduct);
        productDAO.getAll().forEach(p -> cbProduct.addItem(p));
        txtQty = new JTextField("1", 6);
        Theme.styleField(txtQty);
        left.add(labeledRow("Product:", cbProduct));
        left.add(labeledRow("Qty:", txtQty));
        JButton btnAdd = new JButton("+ Add to Cart");
        Theme.styleButton(btnAdd, Theme.SUCCESS);
        btnAdd.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        left.add(Box.createVerticalStrut(6));
        left.add(btnAdd);
        left.add(Box.createVerticalStrut(15));

        left.add(sectionLabel("Payment"));
        cbPaymentMethod = new JComboBox<>(new String[]{"Cash", "Card", "UPI", "Bank Transfer", "Credit"});
        styleCombo(cbPaymentMethod);
        txtDiscount = new JTextField("0", 6);
        Theme.styleField(txtDiscount);
        left.add(labeledRow("Payment:", cbPaymentMethod));
        left.add(labeledRow("Discount (₹):", txtDiscount));
        left.add(Box.createVerticalStrut(15));

        lblTotal = infoLabel("Subtotal: ₹ 0.00");
        lblTax   = infoLabel("Tax (5%): ₹ 0.00");
        lblGrand = infoLabel("GRAND TOTAL: ₹ 0.00");
        lblGrand.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblGrand.setForeground(Theme.SUCCESS);
        left.add(lblTotal); left.add(lblTax); left.add(lblGrand);
        left.add(Box.createVerticalStrut(15));

        JButton btnSave = new JButton("💾 Save Invoice");
        Theme.styleButton(btnSave, Theme.ACCENT2);
        btnSave.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        left.add(btnSave);

        // RIGHT: Cart table
        String[] cols = {"Product", "Qty", "Unit Price", "Subtotal", ""};
        cartModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable cartTable = new JTable(cartModel);
        Theme.styleTable(cartTable);

        JButton btnRemove = new JButton("Remove Selected");
        Theme.styleButton(btnRemove, Theme.DANGER);

        JPanel right = new JPanel(new BorderLayout(0, 8));
        right.setOpaque(false);
        JLabel cartLbl = new JLabel("🛒 Cart Items");
        cartLbl.setFont(Theme.FONT_HEADING);
        cartLbl.setForeground(Theme.ACCENT);
        right.add(cartLbl, BorderLayout.NORTH);
        JScrollPane sp = new JScrollPane(cartTable);
        sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER));
        right.add(sp, BorderLayout.CENTER);
        right.add(btnRemove, BorderLayout.SOUTH);

        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);

        // Events
        btnAdd.addActionListener(e -> addToCart());
        btnRemove.addActionListener(e -> {
            int row = cartTable.getSelectedRow();
            if (row >= 0) { cartItems.remove(row); refreshCart(); }
        });
        btnSave.addActionListener(e -> saveInvoice());
    }

    private void addToCart() {
        Product p = (Product) cbProduct.getSelectedItem();
        if (p == null) return;
        int qty;
        try { qty = Integer.parseInt(txtQty.getText().trim()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid quantity."); return; }
        if (qty <= 0) { JOptionPane.showMessageDialog(this, "Quantity must be > 0."); return; }
        if (qty > p.getQuantity()) { JOptionPane.showMessageDialog(this, "Insufficient stock! Available: " + p.getQuantity()); return; }
        // Check if already in cart
        for (InvoiceItem item : cartItems) {
            if (item.getProductId() == p.getId()) { item.setQuantity(item.getQuantity() + qty); refreshCart(); return; }
        }
        cartItems.add(new InvoiceItem(p.getId(), p.getName(), qty, p.getPrice()));
        refreshCart();
    }

    private void refreshCart() {
        cartModel.setRowCount(0);
        double subtotal = 0;
        for (InvoiceItem item : cartItems) {
            cartModel.addRow(new Object[]{item.getProductName(), item.getQuantity(),
                String.format("%.2f", item.getUnitPrice()), String.format("%.2f", item.getSubtotal()), "❌"});
            subtotal += item.getSubtotal();
        }
        double disc = 0;
        try { disc = Double.parseDouble(txtDiscount.getText().trim()); } catch (Exception ignored) {}
        double tax = (subtotal - disc) * 0.05;
        double grand = subtotal - disc + tax;
        lblTotal.setText(String.format("Subtotal: ₹ %.2f", subtotal));
        lblTax.setText(String.format("Tax (5%%): ₹ %.2f", tax));
        lblGrand.setText(String.format("GRAND TOTAL: ₹ %.2f", grand));
    }

    private void saveInvoice() {
        if (cartItems.isEmpty()) { JOptionPane.showMessageDialog(this, "Cart is empty."); return; }
        Customer cust = (Customer) cbCustomer.getSelectedItem();
        Employee emp  = (Employee) cbEmployee.getSelectedItem();
        if (cust == null || emp == null) { JOptionPane.showMessageDialog(this, "Select customer and employee."); return; }

        double subtotal = cartItems.stream().mapToDouble(InvoiceItem::getSubtotal).sum();
        double disc = 0;
        try { disc = Double.parseDouble(txtDiscount.getText().trim()); } catch (Exception ignored) {}
        double tax = (subtotal - disc) * 0.05;
        double grand = subtotal - disc + tax;

        Invoice inv = new Invoice();
        inv.setInvoiceNumber("INV-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        inv.setCustomerId(cust.getId());
        inv.setEmployeeId(emp.getId());
        inv.setTotalAmount(grand);
        inv.setDiscount(disc);
        inv.setTax(tax);
        inv.setPaymentMethod((String) cbPaymentMethod.getSelectedItem());
        inv.setStatus("Paid");
        inv.setItems(new ArrayList<>(cartItems));

        if (invoiceDAO.insertWithItems(inv)) {
            JOptionPane.showMessageDialog(this, "✅ Invoice saved!\nInvoice #: " + inv.getInvoiceNumber() + "\nTotal: ₹ " + String.format("%.2f", grand));
            cartItems.clear();
            refreshCart();
            // Refresh product combo
            cbProduct.removeAllItems();
            productDAO.getAll().forEach(p -> cbProduct.addItem(p));
        } else {
            JOptionPane.showMessageDialog(this, "❌ Error saving invoice.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.FONT_HEADING);
        lbl.setForeground(Theme.ACCENT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        lbl.setBorder(BorderFactory.createEmptyBorder(5, 0, 3, 0));
        return lbl;
    }

    private JLabel infoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(Theme.FONT_BODY);
        lbl.setForeground(Theme.TEXT_PRIMARY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JPanel labeledRow(String labelText, JComponent comp) {
        JPanel row = new JPanel(new BorderLayout(8, 0));
        row.setOpaque(false);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(Theme.FONT_BODY);
        lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setPreferredSize(new Dimension(100, 30));
        row.add(lbl, BorderLayout.WEST);
        row.add(comp, BorderLayout.CENTER);
        row.setBorder(BorderFactory.createEmptyBorder(3, 0, 3, 0));
        return row;
    }

    private void styleCombo(JComboBox<?> cb) {
        cb.setBackground(Theme.BG_DARK);
        cb.setForeground(Theme.TEXT_PRIMARY);
        cb.setFont(Theme.FONT_BODY);
    }
}
