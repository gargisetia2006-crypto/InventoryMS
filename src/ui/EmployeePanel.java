package ui;

import dao.EmployeeDAO;
import model.Employee;
import javax.swing.*;
import java.util.List;

public class EmployeePanel extends BaseCRUDPanel {
    private final EmployeeDAO dao = new EmployeeDAO();
    private List<Employee> currentList;

    public EmployeePanel() { super("👷 Employee Management"); }

    protected String[] getColumns() { return new String[]{"ID","Name","Position","Email","Phone","Salary (₹)","Hire Date"}; }

    protected void loadData() { currentList = dao.getAll(); fillTable(currentList); }
    protected void onSearch(String kw) { currentList = kw.isEmpty() ? dao.getAll() : dao.search(kw); fillTable(currentList); }

    private void fillTable(List<Employee> list) {
        tableModel.setRowCount(0);
        for (Employee e : list)
            tableModel.addRow(new Object[]{e.getId(), e.getName(), e.getPosition(), e.getEmail(), e.getPhone(), String.format("%.2f", e.getSalary()), e.getHireDate()});
    }

    protected void showAddDialog() {
        JPanel form = makeFormPanel();
        var gbc = defaultGbc();
        JTextField name     = labeledField("Name *",     form, gbc, 0);
        JTextField position = labeledField("Position",   form, gbc, 1);
        JTextField email    = labeledField("Email",      form, gbc, 2);
        JTextField phone    = labeledField("Phone",      form, gbc, 3);
        JTextField salary   = labeledField("Salary (₹)", form, gbc, 4);
        JTextField hireDate = labeledField("Hire Date (YYYY-MM-DD)", form, gbc, 5);

        int res = JOptionPane.showConfirmDialog(this, form, "Add Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;
        if (name.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(this, "Name required."); return; }
        try {
            Employee e = new Employee();
            e.setName(name.getText().trim()); e.setPosition(position.getText().trim());
            e.setEmail(email.getText().trim()); e.setPhone(phone.getText().trim());
            e.setSalary(salary.getText().trim().isEmpty() ? 0 : Double.parseDouble(salary.getText().trim()));
            e.setHireDate(hireDate.getText().trim());
            if (dao.insert(e)) { loadData(); JOptionPane.showMessageDialog(this, "Employee added!"); }
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid salary format."); }
    }

    protected void showEditDialog(int row) {
        Employee e = currentList.get(row);
        JPanel form = makeFormPanel();
        var gbc = defaultGbc();
        JTextField name     = labeledField("Name *",     form, gbc, 0);
        JTextField position = labeledField("Position",   form, gbc, 1);
        JTextField email    = labeledField("Email",      form, gbc, 2);
        JTextField phone    = labeledField("Phone",      form, gbc, 3);
        JTextField salary   = labeledField("Salary (₹)", form, gbc, 4);
        JTextField hireDate = labeledField("Hire Date",  form, gbc, 5);
        name.setText(e.getName()); position.setText(e.getPosition()); email.setText(e.getEmail());
        phone.setText(e.getPhone()); salary.setText(String.valueOf(e.getSalary())); hireDate.setText(e.getHireDate());
        int res = JOptionPane.showConfirmDialog(this, form, "Edit Employee", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (res != JOptionPane.OK_OPTION) return;
        try {
            e.setName(name.getText().trim()); e.setPosition(position.getText().trim());
            e.setEmail(email.getText().trim()); e.setPhone(phone.getText().trim());
            e.setSalary(Double.parseDouble(salary.getText().trim())); e.setHireDate(hireDate.getText().trim());
            if (dao.update(e)) { loadData(); JOptionPane.showMessageDialog(this, "Employee updated!"); }
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid salary format."); }
    }

    protected void deleteRow(int row) {
        Employee e = currentList.get(row);
        if (dao.delete(e.getId())) { loadData(); JOptionPane.showMessageDialog(this, "Employee deleted."); }
    }
}
