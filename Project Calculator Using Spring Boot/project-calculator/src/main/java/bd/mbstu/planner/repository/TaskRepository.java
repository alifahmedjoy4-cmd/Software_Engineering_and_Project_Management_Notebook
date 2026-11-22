package bd.mbstu.planner.repository;

import bd.mbstu.planner.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/** select / insert / delete for the tasks table (replaces TaskDao). */
@Repository
public class TaskRepository {
    private final JdbcTemplate jdbc;

    public TaskRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Task> findAll() {
        return jdbc.query("SELECT id, phase, name, hours FROM tasks ORDER BY sort_order, id",
                (rs, i) -> new Task(rs.getInt("id"), rs.getString("phase"),
                        rs.getString("name"), rs.getDouble("hours")));
    }

    public int count() {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM tasks", Integer.class);
        return n == null ? 0 : n;
    }

    public void add(String phase, String name, double hours) {
        jdbc.update("INSERT INTO tasks (phase, name, hours, sort_order) "
                + "SELECT ?, ?, ?, COALESCE(MAX(sort_order), 0) + 1 FROM tasks", phase, name, hours);
    }

    public void delete(int id) {
        jdbc.update("DELETE FROM tasks WHERE id = ?", id);
    }
}
