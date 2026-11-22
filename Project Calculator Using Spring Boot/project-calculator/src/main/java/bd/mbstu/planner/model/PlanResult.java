package bd.mbstu.planner.model;

import java.util.ArrayList;
import java.util.List;

/** Result of one calculation. Sent to the browser as JSON (dates are ISO strings). */
public class PlanResult {
    public String startDate, endDate;
    public int totalCalendarDays, totalWorkDays;
    public double totalHours, hoursPerDay;
    public List<String> offDays = new ArrayList<>();
    public List<String> holidays = new ArrayList<>();
    public List<ScheduledTask> tasks = new ArrayList<>();
    public List<DailyEntry> daily = new ArrayList<>();
    public Progress progress;            // null when no check date was given

    /** A task placed on the calendar. */
    public static class ScheduledTask {
        public int id;
        public String phase, name, startDate, endDate;
        public double hours, percentOfProject, progressPercent;
        public int workDays, calendarDays;
    }

    /** What to do on one working day. */
    public static class DailyEntry {
        public String date, weekday;
        public double hours, cumulativePercent;
        public List<DailyItem> items = new ArrayList<>();
    }

    public static class DailyItem {
        public String task;
        public double hours;
        public DailyItem(String task, double hours) { this.task = task; this.hours = hours; }
    }

    /** How much work can be finished up to the check date. */
    public static class Progress {
        public String asOf, currentTask, message;
        public int workDaysElapsed, workDaysRemaining;
        public double percentDone, hoursDone;
        public List<String> completedTasks = new ArrayList<>();
    }
}
