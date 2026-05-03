package ui;

import dao.ProductDAO;
import model.Product;
import util.Theme;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductPanel extends BaseCRUDPanel {
    private final ProductDAO dao = new ProductDAO();
    private List<Product> currentList;

    public ProductPanel() { super("📦 Product Management"); }

    protected String[] getColumns() {
        return new String[]{"ID", "Name", "Category", "Price (₹)", "Cost (₹)", "Stock", "Min Stock", "Unit"};
    }

    protected void loadData() {
        currentList = dao.getAll();
        fillTable(currentList);
    }

    protected void onSearch(String kw) {
        currentList = kw.isEmpty() ? dao.getAll() : dao.search(kw);
        fillTable(currentList);
    }

    private void fillTable(List<Product> list) {
        tableModel.setRowCount(0);
        for (Product p : list)
            tableModel.addRow(new Object[]{p.getId(), p.getName(), p.getCategory(),
                String.format("%.2f", p.getPrice()), String.format("%.2f", p.getCostPrice()),
                p.getQuantity(), p.getMinStock(), p.getUnit()});
    }

    protected void showAddDialog() {
        JPanel form = makeFormPanel();
        GridBagConstraints gbc = defaultGbc();
        JTextField name = labeledField("Name *", form, gbc, 0);
        JTextField cat  = labeledField("Category", form, gbc, 1);
        JTextField price = labeledField("Selling Price *", form, gbc, 2);
        JTextField cost  = labeledField("Cost Price", form, gbc, 3);
        JTextField qty   = labeledField("Quantity", form, gbc, 4);
        JTextField min   = labeledField("Min Stock", form, gbc, 5);
        JTextField unit  = labeledField("Unit (pcs/kg/L)", form, gbc, 6);
        JTextField desc  = labeledField("Description", form, gbc, 7);

        int res = JOptionPane.showConfirmDialog(this, form, "Add Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;
        if (name.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Name is required."); return; }
        try {
            Product p = new Product();
            p.setName(name.getText().trim());
            p.setCategory(cat.getText().trim());
            p.setDescription(desc.getText().trim());
            p.setPrice(price.getText().trim().isEmpty() ? 0 : Double.parseDouble(price.getText().trim()));
            p.setCostPrice(cost.getText().trim().isEmpty() ? 0 : Double.parseDouble(cost.getText().trim()));
            p.setQuantity(qty.getText().trim().isEmpty() ? 0 : Integer.parseInt(qty.getText().trim()));
            p.setMinStock(min.getText().trim().isEmpty() ? 5 : Integer.parseInt(min.getText().trim()));
            p.setUnit(unit.getText().trim().isEmpty() ? "pcs" : unit.getText().trim());
            if (dao.insert(p)) { loadData(); JOptionPane.showMessageDialog(this, "Product added!"); }
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid number format."); }
    }

    protected void showEditDialog(int row) {
        Product p = currentList.get(row);
        JPanel form = makeFormPanel();
        GridBagConstraints gbc = defaultGbc();
        JTextField name = labeledField("Name *", form, gbc, 0);
        JTextField cat  = labeledField("Category", form, gbc, 1);
        JTextField price = labeledField("Selling Price *", form, gbc, 2);
        JTextField cost  = labeledField("Cost Price", form, gbc, 3);
        JTextField qty   = labeledField("Quantity", form, gbc, 4);
        JTextField min   = labeledField("Min Stock", form, gbc, 5);
        JTextField unit  = labeledField("Unit", form, gbc, 6);
        JTextField desc  = labeledField("Description", form, gbc, 7);

        name.setText(p.getName()); cat.setText(p.getCategory());
        price.setText(String.valueOf(p.getPrice())); cost.setText(String.valueOf(p.getCostPrice()));
        qty.setText(String.valueOf(p.getQuantity())); min.setText(String.valueOf(p.getMinStock()));
        unit.setText(p.getUnit()); desc.setText(p.getDescription());

        int res = JOptionPane.showConfirmDialog(this, form, "Edit Product", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            p.setName(name.getText().trim()); p.setCategory(cat.getText().trim());
            p.setDescription(desc.getText().trim());
            p.setPrice(Double.parseDouble(price.getText().trim()));
            p.setCostPrice(cost.getText().trim().isEmpty() ? 0 : Double.parseDouble(cost.getText().trim()));
            p.setQuantity(Integer.parseInt(qty.getText().trim()));
            p.setMinStock(Integer.parseInt(min.getText().trim()));
            p.setUnit(unit.getText().trim());
            if (dao.update(p)) { loadData(); JOptionPane.showMessageDialog(this, "Product updated!"); }
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid number format."); }
    }

    protected void deleteRow(int row) {
        Product p = currentList.get(row);
        if (dao.delete(p.getId())) { loadData(); JOptionPane.showMessageDialog(this, "Product deleted."); }
    }
}
