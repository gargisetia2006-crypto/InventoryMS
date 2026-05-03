package ui;

import util.Theme;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public abstract class BaseCRUDPanel extends JPanel {
    protected JTable table;
    protected DefaultTableModel tableModel;
    protected JTextField searchField;
    protected JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    protected String panelTitle;

    public BaseCRUDPanel(String title) {
        this.panelTitle = title;
        setBackground(Theme.BG_DARK);
        setLayout(new BorderLayout(0, 0));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        buildUI();

        // ❌ REMOVED loadData() from here
    }

    private void buildUI() {
        JPanel topBar = new JPanel(new BorderLayout(10, 0));
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel lbl = new JLabel(panelTitle);
        lbl.setFont(Theme.FONT_TITLE);
        lbl.setForeground(Theme.TEXT_PRIMARY);
        topBar.add(lbl, BorderLayout.WEST);

        JPanel rightBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        rightBar.setOpaque(false);

        searchField = new JTextField(18);
        searchField.setToolTipText("Search...");
        Theme.styleField(searchField);
        searchField.putClientProperty("Placeholder", "🔍 Search...");
        rightBar.add(searchField);

        btnRefresh = makeBtn("↻ Refresh", Theme.TEXT_MUTED);
        btnAdd     = makeBtn("+ Add", Theme.SUCCESS);
        btnEdit    = makeBtn("✏ Edit", Theme.ACCENT2);
        btnDelete  = makeBtn("🗑 Delete", Theme.DANGER);

        rightBar.add(btnRefresh);
        rightBar.add(btnAdd);
        rightBar.add(btnEdit);
        rightBar.add(btnDelete);
        topBar.add(rightBar, BorderLayout.EAST);
        add(topBar, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(getColumns(), 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane sp = new JScrollPane(table);
        sp.setBackground(Theme.BG_CARD);
        sp.getViewport().setBackground(Theme.BG_CARD);
        sp.setBorder(BorderFactory.createLineBorder(Theme.BORDER, 1));
        add(sp, BorderLayout.CENTER);

        btnAdd.addActionListener(e -> showAddDialog());

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a row to edit.");
                return;
            }
            showEditDialog(row);
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this record?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) deleteRow(row);
        });

        btnRefresh.addActionListener(e -> {
            searchField.setText("");
            loadData(); // ✅ safe here (after full init)
        });

        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                onSearch(searchField.getText().trim());
            }
        });
    }

    protected JButton makeBtn(String text, Color color) {
        JButton btn = new JButton(text);
        Theme.styleButton(btn, color);
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width + 10, 32));
        return btn;
    }

    protected JTextField labeledField(String label, JPanel panel, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        JLabel lbl = new JLabel(label);
        lbl.setForeground(Theme.TEXT_MUTED);
        lbl.setFont(Theme.FONT_BODY);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        JTextField field = new JTextField(20);
        Theme.styleField(field);
        panel.add(field, gbc);

        return field;
    }

    protected JPanel makeFormPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.BG_CARD);
        form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return form;
    }

    protected GridBagConstraints defaultGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        return gbc;
    }

    protected abstract String[] getColumns();
    protected abstract void loadData();
    protected abstract void onSearch(String keyword);
    protected abstract void showAddDialog();
    protected abstract void showEditDialog(int row);
    protected abstract void deleteRow(int row);
}