package bd.mbstu.planner.dao;

import bd.mbstu.planner.model.PlanResult;
import bd.mbstu.planner.model.PlanSummary;
import bd.mbstu.planner.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Saves calculations (plans + plan_items) and lists the saved ones. */
public class PlanDao {

    /** Saves a plan and its schedule in one transaction. Returns the new plan id. */
    public int save(PlanResult r, String offDays, String holidays) throws SQLException {
        try (Connection c = DBConnection.get()) {
            c.setAutoCommit(false);
            try {
                int planId;
                String sql = "INSERT INTO plans (start_date, end_date, off_days, holidays, total_work_days, total_hours) "
                           + "VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setDate(1, Date.valueOf(r.startDate));
                    ps.setDate(2, Date.valueOf(r.endDate));
                    ps.setString(3, offDays);
                    ps.setString(4, holidays);
                    ps.setInt(5, r.totalWorkDays);
                    ps.setDouble(6, r.totalHours);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        planId = keys.getInt(1);
                    }
                }
                String itemSql = "INSERT INTO plan_items (plan_id, task_name, phase, start_date, end_date, work_days, hours) "
                               + "VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = c.prepareStatement(itemSql)) {
                    for (PlanResult.ScheduledTask t : r.tasks) {
                        ps.setInt(1, planId);
                        ps.setString(2, t.name);
                        ps.setString(3, t.phase);
                        ps.setDate(4, Date.valueOf(t.startDate));
                        ps.setDate(5, Date.valueOf(t.endDate));
                        ps.setInt(6, t.workDays);
                        ps.setDouble(7, t.hours);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                c.commit();
                return planId;
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    /** The 20 most recent saved plans. */
    public List<PlanSummary> history() throws SQLException {
        List<PlanSummary> list = new ArrayList<>();
        String sql = "SELECT id, start_date, end_date, off_days, holidays, total_work_days, total_hours, created_at "
                   + "FROM plans ORDER BY id DESC LIMIT 20";
        try (Connection c = DBConnection.get();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                PlanSummary p = new PlanSummary();
                p.id = rs.getInt("id");
                p.startDate = rs.getDate("start_date").toString();
                p.endDate = rs.getDate("end_date").toString();
                p.offDays = rs.getString("off_days");
                p.holidays = rs.getString("holidays");
                p.totalWorkDays = rs.getInt("total_work_days");
                p.totalHours = rs.getDouble("total_hours");
                p.createdAt = rs.getTimestamp("created_at").toString();
                list.add(p);
            }
        }
        return list;
    }
}
