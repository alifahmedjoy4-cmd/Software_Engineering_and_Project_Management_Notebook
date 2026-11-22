package bd.mbstu.planner.repository;

import bd.mbstu.planner.model.PlanResult;
import bd.mbstu.planner.model.PlanSummary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/** Saves calculations (plans + plan_items) and lists the saved ones (replaces PlanDao). */
@Repository
public class PlanRepository {
    private final JdbcTemplate jdbc;

    public PlanRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Saves a plan and its schedule in one transaction. Returns the new plan id. */
    @Transactional
    public int save(PlanResult r, String offDays, String holidays) {
        String sql = "INSERT INTO plans (start_date, end_date, off_days, holidays, total_work_days, total_hours) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keys = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, Date.valueOf(r.startDate));
            ps.setDate(2, Date.valueOf(r.endDate));
            ps.setString(3, offDays);
            ps.setString(4, holidays);
            ps.setInt(5, r.totalWorkDays);
            ps.setDouble(6, r.totalHours);
            return ps;
        }, keys);
        final int planId = keys.getKey().intValue();

        String itemSql = "INSERT INTO plan_items (plan_id, task_name, phase, start_date, end_date, work_days, hours) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbc.batchUpdate(itemSql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                PlanResult.ScheduledTask t = r.tasks.get(i);
                ps.setInt(1, planId);
                ps.setString(2, t.name);
                ps.setString(3, t.phase);
                ps.setDate(4, Date.valueOf(t.startDate));
                ps.setDate(5, Date.valueOf(t.endDate));
                ps.setInt(6, t.workDays);
                ps.setDouble(7, t.hours);
            }

            @Override
            public int getBatchSize() {
                return r.tasks.size();
            }
        });
        return planId;
    }

    /** The 20 most recent saved plans. */
    public List<PlanSummary> history() {
        String sql = "SELECT id, start_date, end_date, off_days, holidays, total_work_days, total_hours, created_at "
                   + "FROM plans ORDER BY id DESC LIMIT 20";
        return jdbc.query(sql, (rs, i) -> {
            PlanSummary p = new PlanSummary();
            p.id = rs.getInt("id");
            p.startDate = rs.getDate("start_date").toString();
            p.endDate = rs.getDate("end_date").toString();
            p.offDays = rs.getString("off_days");
            p.holidays = rs.getString("holidays");
            p.totalWorkDays = rs.getInt("total_work_days");
            p.totalHours = rs.getDouble("total_hours");
            p.createdAt = rs.getTimestamp("created_at").toString();
            return p;
        });
    }
}
