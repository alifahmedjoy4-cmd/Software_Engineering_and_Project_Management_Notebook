package bd.mbstu.planner.servlet;

import bd.mbstu.planner.dao.PlanDao;
import bd.mbstu.planner.util.Json;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/** GET /api/history -> the saved plans (newest first). */
@WebServlet("/api/history")
public class HistoryServlet extends HttpServlet {
    private final PlanDao dao = new PlanDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Json.write(resp, 200, dao.history());
        } catch (SQLException e) {
            Json.error(resp, 500, "Database error: " + e.getMessage());
        }
    }
}
