package ui;

import dao.CustomerDAO;
import model.Customer;
import util.Theme;
import javax.swing.*;
import java.util.List;

public class CustomerPanel extends BaseCRUDPanel {

    private final CustomerDAO dao = new CustomerDAO();
    private List<Customer> currentList;

    public CustomerPanel() {
        super("👥 Customer Management");
        loadData(); // ✅ VERY IMPORTANT (fixes crash)
    }

    protected String[] getColumns() {
        return new String[]{"ID","Name","Email","Phone","City","Address"};
    }

    protected void loadData() {
        currentList = dao.getAll();
        fillTable(currentList);
    }

    protected void onSearch(String kw) {
        currentList = kw.isEmpty() ? dao.getAll() : dao.search(kw);
        fillTable(currentList);
    }

    private void fillTable(List<Customer> list) {
        tableModel.setRowCount(0);
        for (Customer c : list) {
            tableModel.addRow(new Object[]{
                c.getId(),
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                c.getCity(),
                c.getAddress()
            });
        }
    }

    protected void showAddDialog() {
        JPanel form = makeFormPanel();
        var gbc = defaultGbc();

        JTextField name    = labeledField("Name *",   form, gbc, 0);
        JTextField email   = labeledField("Email",    form, gbc, 1);
        JTextField phone   = labeledField("Phone",    form, gbc, 2);
        JTextField city    = labeledField("City",     form, gbc, 3);
        JTextField address = labeledField("Address",  form, gbc, 4);

        int res = JOptionPane.showConfirmDialog(this, form, "Add Customer",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res != JOptionPane.OK_OPTION) return;

        if (name.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name required.");
            return;
        }

        Customer c = new Customer();
        c.setName(name.getText().trim());
        c.setEmail(email.getText().trim());
        c.setPhone(phone.getText().trim());
        c.setCity(city.getText().trim());
        c.setAddress(address.getText().trim());

        if (dao.insert(c)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Customer added!");
        }
    }

    protected void showEditDialog(int row) {
        Customer c = currentList.get(row);

        JPanel form = makeFormPanel();
        var gbc = defaultGbc();

        JTextField name    = labeledField("Name *",  form, gbc, 0);
        JTextField email   = labeledField("Email",   form, gbc, 1);
        JTextField phone   = labeledField("Phone",   form, gbc, 2);
        JTextField city    = labeledField("City",    form, gbc, 3);
        JTextField address = labeledField("Address", form, gbc, 4);

        name.setText(c.getName());
        email.setText(c.getEmail());
        phone.setText(c.getPhone());
        city.setText(c.getCity());
        address.setText(c.getAddress());

        int res = JOptionPane.showConfirmDialog(this, form, "Edit Customer",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res != JOptionPane.OK_OPTION) return;

        c.setName(name.getText().trim());
        c.setEmail(email.getText().trim());
        c.setPhone(phone.getText().trim());
        c.setCity(city.getText().trim());
        c.setAddress(address.getText().trim());

        if (dao.update(c)) {
            loadData();
            JOptionPane.showMessageDialog(this, "Customer updated!");
        }
    }

    protected void deleteRow(int row) {
        Customer c = currentList.get(row);

        if (dao.delete(c.getId())) {
            loadData();
            JOptionPane.showMessageDialog(this, "Customer deleted.");
        }
    }
}