package bd.mbstu.planner.dao;

import bd.mbstu.planner.model.Task;
import bd.mbstu.planner.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** CRUD for the tasks table. */
public class TaskDao {

    public List<Task> findAll() throws SQLException {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT id, phase, name, hours FROM tasks ORDER BY sort_order, id";
        try (Connection c = DBConnection.get();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Task(rs.getInt("id"), rs.getString("phase"),
                        rs.getString("name"), rs.getDouble("hours")));
            }
        }
        return list;
    }

    public void add(String phase, String name, double hours) throws SQLException {
        String sql = "INSERT INTO tasks (phase, name, hours, sort_order) "
                   + "SELECT ?, ?, ?, COALESCE(MAX(sort_order), 0) + 1 FROM tasks";
        try (Connection c = DBConnection.get(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, phase);
            ps.setString(2, name);
            ps.setDouble(3, hours);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        try (Connection c = DBConnection.get();
             PreparedStatement ps = c.prepareStatement("DELETE FROM tasks WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
