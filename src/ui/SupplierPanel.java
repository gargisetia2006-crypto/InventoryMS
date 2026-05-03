package ui;

import dao.SupplierDAO;
import model.Supplier;
import javax.swing.*;
import java.util.List;

public class SupplierPanel extends BaseCRUDPanel {
    private final SupplierDAO dao = new SupplierDAO();
    private List<Supplier> currentList;

    public SupplierPanel() { super("🏭 Supplier Management"); }

    protected String[] getColumns() { return new String[]{"ID","Company Name","Contact Person","Email","Phone","City","Address"}; }

    protected void loadData() { currentList = dao.getAll(); fillTable(currentList); }
    protected void onSearch(String kw) { currentList = kw.isEmpty() ? dao.getAll() : dao.search(kw); fillTable(currentList); }

    private void fillTable(List<Supplier> list) {
        tableModel.setRowCount(0);
        for (Supplier s : list)
            tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getContactPerson(), s.getEmail(), s.getPhone(), s.getCity(), s.getAddress()});
    }

    protected void showAddDialog() {
        JPanel form = makeFormPanel();
        var gbc = defaultGbc();
        JTextField name    = labeledField("Company Name *",  form, gbc, 0);
        JTextField contact = labeledField("Contact Person",  form, gbc, 1);
        JTextField email   = labeledField("Email",           form, gbc, 2);
        JTextField phone   = labeledField("Phone",           form, gbc, 3);
        JTextField city    = labeledField("City",            form, gbc, 4);
        JTextField address = labeledField("Address",         form, gbc, 5);

        int res = JOptionPane.showConfirmDialog(this, form, "Add Supplier", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;
        if (name.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Company name required."); return; }
        Supplier s = new Supplier(); s.setName(name.getText().trim()); s.setContactPerson(contact.getText().trim());
        s.setEmail(email.getText().trim()); s.setPhone(phone.getText().trim()); s.setCity(city.getText().trim()); s.setAddress(address.getText().trim());
        if (dao.insert(s)) { loadData(); JOptionPane.showMessageDialog(this, "Supplier added!"); }
    }

    protected void showEditDialog(int row) {
        Supplier s = currentList.get(row);
        JPanel form = makeFormPanel();
        var gbc = defaultGbc();
        JTextField name    = labeledField("Company Name *", form, gbc, 0);
        JTextField contact = labeledField("Contact Person", form, gbc, 1);
        JTextField email   = labeledField("Email",          form, gbc, 2);
        JTextField phone   = labeledField("Phone",          form, gbc, 3);
        JTextField city    = labeledField("City",           form, gbc, 4);
        JTextField address = labeledField("Address",        form, gbc, 5);
        name.setText(s.getName()); contact.setText(s.getContactPerson()); email.setText(s.getEmail());
        phone.setText(s.getPhone()); city.setText(s.getCity()); address.setText(s.getAddress());
        int res = JOptionPane.showConfirmDialog(this, form, "Edit Supplier", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;
        s.setName(name.getText().trim()); s.setContactPerson(contact.getText().trim());
        s.setEmail(email.getText().trim()); s.setPhone(phone.getText().trim());
        s.setCity(city.getText().trim()); s.setAddress(address.getText().trim());
        if (dao.update(s)) { loadData(); JOptionPane.showMessageDialog(this, "Supplier updated!"); }
    }

    protected void deleteRow(int row) {
        Supplier s = currentList.get(row);
        if (dao.delete(s.getId())) { loadData(); JOptionPane.showMessageDialog(this, "Supplier deleted."); }
    }
}
