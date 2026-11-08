package bd.mbstu.planner.model;

/** One row of the saved-plans list. */
public class PlanSummary {
    public int id, totalWorkDays;
    public double totalHours;
    public String startDate, endDate, offDays, holidays, createdAt;
}
