package bd.mbstu.planner.servlet;

import bd.mbstu.planner.dao.PlanDao;
import bd.mbstu.planner.dao.TaskDao;
import bd.mbstu.planner.model.PlanResult;
import bd.mbstu.planner.service.SchedulerService;
import bd.mbstu.planner.util.Json;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.util.Map;

/**
 * GET  /api/plan?start=&end=&asOf=&offDays=&holidays=  -> calculates the plan (JSON)
 * POST /api/plan  (same parameters)                    -> calculates and saves it in MySQL
 */
@WebServlet("/api/plan")
public class PlanServlet extends HttpServlet {
    private final TaskDao taskDao = new TaskDao();
    private final PlanDao planDao = new PlanDao();
    private final SchedulerService scheduler = new SchedulerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Json.write(resp, 200, calculate(PlanRequest.from(req)));
        } catch (IllegalArgumentException | DateTimeException e) {
            Json.error(resp, 400, "Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            Json.error(resp, 500, "Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        try {
            PlanRequest q = PlanRequest.from(req);
            PlanResult result = calculate(q);
            int id = planDao.save(result, q.offDaysText, q.holidaysText);
            Json.write(resp, 200, Map.of("id", id));
        } catch (IllegalArgumentException | DateTimeException e) {
            Json.error(resp, 400, "Invalid input: " + e.getMessage());
        } catch (SQLException e) {
            Json.error(resp, 500, "Database error: " + e.getMessage());
        }
    }

    private PlanResult calculate(PlanRequest q) throws SQLException {
        return scheduler.plan(taskDao.findAll(), q.start, q.end, q.offDays, q.holidays, q.asOf);
    }
}
