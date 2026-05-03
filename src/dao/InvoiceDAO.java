package dao;

import model.Invoice;
import model.InvoiceItem;
import util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class InvoiceDAO {

    public List<Invoice> getAll() {
        List<Invoice> list = new ArrayList<>();
        String sql = """
            SELECT i.*, c.name as cname FROM invoices i
            LEFT JOIN customers c ON i.customer_id = c.id
            ORDER BY i.created_at DESC
        """;
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Invoice> search(String keyword) {
        List<Invoice> list = new ArrayList<>();
        String sql = """
            SELECT i.*, c.name as cname FROM invoices i
            LEFT JOIN customers c ON i.customer_id = c.id
            WHERE i.invoice_number LIKE ? OR c.name LIKE ?
            ORDER BY i.created_at DESC
        """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            ps.setString(1, k); ps.setString(2, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insertWithItems(Invoice invoice) {
        String invSql = "INSERT INTO invoices(invoice_number,customer_id,employee_id,total_amount,discount,tax,payment_method,status) VALUES(?,?,?,?,?,?,?,?)";
        String itemSql = "INSERT INTO invoice_items(invoice_id,product_id,quantity,unit_price,subtotal) VALUES(?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            int invoiceId;
            try (PreparedStatement ps = conn.prepareStatement(invSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, invoice.getInvoiceNumber());
                ps.setInt(2, invoice.getCustomerId());
                ps.setInt(3, invoice.getEmployeeId());
                ps.setDouble(4, invoice.getTotalAmount());
                ps.setDouble(5, invoice.getDiscount());
                ps.setDouble(6, invoice.getTax());
                ps.setString(7, invoice.getPaymentMethod());
                ps.setString(8, invoice.getStatus());
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                keys.next();
                invoiceId = keys.getInt(1);
            }

            ProductDAO productDAO = new ProductDAO();
            for (InvoiceItem item : invoice.getItems()) {
                try (PreparedStatement ps = conn.prepareStatement(itemSql)) {
                    ps.setInt(1, invoiceId);
                    ps.setInt(2, item.getProductId());
                    ps.setInt(3, item.getQuantity());
                    ps.setDouble(4, item.getUnitPrice());
                    ps.setDouble(5, item.getSubtotal());
                    ps.executeUpdate();
                }
                // Decrease stock
                productDAO.updateQuantity(item.getProductId(), -item.getQuantity(), conn);
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); } catch (SQLException ex) {}
        }
    }

    public List<InvoiceItem> getItemsByInvoiceId(int invoiceId) {
        List<InvoiceItem> items = new ArrayList<>();
        String sql = """
            SELECT ii.*, p.name as pname FROM invoice_items ii
            JOIN products p ON ii.product_id = p.id
            WHERE ii.invoice_id = ?
        """;
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, invoiceId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                InvoiceItem item = new InvoiceItem();
                item.setId(rs.getInt("id"));
                item.setInvoiceId(rs.getInt("invoice_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setProductName(rs.getString("pname"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnitPrice(rs.getDouble("unit_price"));
                item.setSubtotal(rs.getDouble("subtotal"));
                items.add(item);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return items;
    }

    public Map<String, Double> getDailySales(int days) {
        Map<String, Double> map = new LinkedHashMap<>();
        String sql = "SELECT date(created_at) as day, SUM(total_amount) as total FROM invoices WHERE created_at >= date('now', '-" + days + " days') GROUP BY day ORDER BY day";
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) map.put(rs.getString("day"), rs.getDouble("total"));
        } catch (SQLException e) { e.printStackTrace(); }
        return map;
    }

    public double getTotalRevenue() {
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COALESCE(SUM(total_amount),0) as t FROM invoices")) {
            return rs.next() ? rs.getDouble("t") : 0;
        } catch (SQLException e) { e.printStackTrace(); return 0; }
    }

    public int getTotalInvoices() {
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT COUNT(*) as t FROM invoices")) {
            return rs.next() ? rs.getInt("t") : 0;
        } catch (SQLException e) { e.printStackTrace(); return 0; }
    }

    private Invoice map(ResultSet rs) throws SQLException {
        Invoice inv = new Invoice();
        inv.setId(rs.getInt("id"));
        inv.setInvoiceNumber(rs.getString("invoice_number"));
        inv.setCustomerId(rs.getInt("customer_id"));
        inv.setEmployeeId(rs.getInt("employee_id"));
        inv.setTotalAmount(rs.getDouble("total_amount"));
        inv.setDiscount(rs.getDouble("discount"));
        inv.setTax(rs.getDouble("tax"));
        inv.setPaymentMethod(rs.getString("payment_method"));
        inv.setStatus(rs.getString("status"));
        inv.setCreatedAt(rs.getString("created_at"));
        try { inv.setCreatedAt(rs.getString("cname") + " | " + rs.getString("created_at")); } catch(Exception ignored){}
        return inv;
    }
}
