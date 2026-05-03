package dao;

import model.Employee;
import util.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class EmployeeDAO {

    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        try (Connection c = DatabaseConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery("SELECT * FROM employees ORDER BY name")) {
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Employee> search(String keyword) {
        List<Employee> list = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE name LIKE ? OR position LIKE ? OR phone LIKE ? ORDER BY name";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            String k = "%" + keyword + "%";
            ps.setString(1, k); ps.setString(2, k); ps.setString(3, k);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(map(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(Employee e) {
        String sql = "INSERT INTO employees(name,email,phone,position,salary,hire_date) VALUES(?,?,?,?,?,?)";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getName()); ps.setString(2, e.getEmail());
            ps.setString(3, e.getPhone()); ps.setString(4, e.getPosition());
            ps.setDouble(5, e.getSalary()); ps.setString(6, e.getHireDate());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public boolean update(Employee e) {
        String sql = "UPDATE employees SET name=?,email=?,phone=?,position=?,salary=?,hire_date=? WHERE id=?";
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, e.getName()); ps.setString(2, e.getEmail());
            ps.setString(3, e.getPhone()); ps.setString(4, e.getPosition());
            ps.setDouble(5, e.getSalary()); ps.setString(6, e.getHireDate());
            ps.setInt(7, e.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    public boolean delete(int id) {
        try (Connection c = DatabaseConnection.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM employees WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private Employee map(ResultSet rs) throws SQLException {
        return new Employee(rs.getInt("id"), rs.getString("name"), rs.getString("email"),
            rs.getString("phone"), rs.getString("position"), rs.getDouble("salary"), rs.getString("hire_date"));
    }
}
