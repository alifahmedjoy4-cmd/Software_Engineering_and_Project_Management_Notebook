package bd.mbstu.planner.servlet;

import bd.mbstu.planner.dao.TaskDao;
import bd.mbstu.planner.model.Task;
import bd.mbstu.planner.util.Json;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * GET  /api/tasks                                   -> list of tasks
 * POST /api/tasks  action=add&phase=&name=&hours=   -> add a task
 * POST /api/tasks  action=delete&id=                -> delete a task
 */
@WebServlet("/api/tasks")
public class TaskServlet extends HttpServlet {
    private final TaskDao dao = new TaskDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            List<Task> tasks = dao.findAll();
            Json.write(resp, 200, tasks.stream().map(t -> Map.of(
                    "id", t.getId(), "phase", t.getPhase(), "name", t.getName(), "hours", t.getHours())).toList());
        } catch (SQLException e) {
            Json.error(resp, 500, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        try {
            if ("add".equals(action)) {
                String phase = req.getParameter("phase");
                String name = req.getParameter("name");
                double hours = Double.parseDouble(req.getParameter("hours"));
                if (phase == null || phase.isBlank() || name == null || name.isBlank() || hours <= 0)
                    throw new IllegalArgumentException("Phase, name and hours (> 0) are required.");
                dao.add(phase.trim(), name.trim(), hours);
            } else if ("delete".equals(action)) {
                dao.delete(Integer.parseInt(req.getParameter("id")));
            } else {
                throw new IllegalArgumentException("Unknown action.");
            }
            Json.write(resp, 200, Map.of("ok", true));
        } catch (IllegalArgumentException e) {      // includes NumberFormatException
            Json.error(resp, 400, "Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            Json.error(resp, 500, "Database error: " + e.getMessage());
        }
    }
}
