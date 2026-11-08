package bd.mbstu.planner.service;

import bd.mbstu.planner.model.PlanResult;
import bd.mbstu.planner.model.Task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * The calculator. Pure Java (no servlet, no database) so it is easy to test.
 *
 * Idea: every task has an effort in hours. The working days between the start
 * date and the end date (minus weekly off-days and holidays) share the total
 * effort equally, so each working day carries the same amount of work
 * (hoursPerDay = totalHours / workingDays). Tasks are placed one after another.
 */
public class SchedulerService {
    private static final double EPS = 1e-9;
    private static final DateTimeFormatter NICE = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.ENGLISH);

    /**
     * @param asOf the "check date" (may be null): how much work fits up to this date
     */
    public PlanResult plan(List<Task> tasks, LocalDate start, LocalDate end,
                           Set<DayOfWeek> offDays, Set<LocalDate> holidays, LocalDate asOf) {
        if (tasks == null || tasks.isEmpty())
            throw new IllegalArgumentException("No tasks found. Add at least one task.");
        if (start == null || end == null)
            throw new IllegalArgumentException("Start date and end date are required.");
        if (end.isBefore(start))
            throw new IllegalArgumentException("End date must be on or after the start date.");

        // 1. Working days
        List<LocalDate> work = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
            if (!offDays.contains(d.getDayOfWeek()) && !holidays.contains(d)) work.add(d);
        }
        if (work.isEmpty())
            throw new IllegalArgumentException("There is no working day between these dates.");
        int n = work.size();

        // 2. Work per day
        double total = 0;
        for (Task t : tasks) total += t.getHours();
        double perDay = total / n;

        PlanResult r = new PlanResult();
        r.startDate = start.toString();
        r.endDate = end.toString();
        r.totalCalendarDays = (int) (ChronoUnit.DAYS.between(start, end) + 1);
        r.totalWorkDays = n;
        r.totalHours = round2(total);
        r.hoursPerDay = round2(perDay);
        for (DayOfWeek d : offDays) r.offDays.add(d.name());
        for (LocalDate h : holidays) r.holidays.add(h.toString());

        // 3. Work done up to the check date
        int elapsed = 0;
        double done = 0;
        if (asOf != null) {
            for (LocalDate d : work) if (!d.isAfter(asOf)) elapsed++;
            done = Math.min(total, elapsed * perDay);
        }

        // 4. Place each task on the calendar
        double[] ts = new double[tasks.size()];
        double[] te = new double[tasks.size()];
        double cum = 0;
        int k = 0;
        for (Task t : tasks) {
            double s = cum, e = cum + t.getHours();
            cum = e;
            ts[k] = s;
            te[k] = e;
            int si = (int) Math.floor(s / perDay + EPS);
            int ei = (int) Math.ceil(e / perDay - EPS) - 1;
            si = Math.min(si, n - 1);
            ei = Math.max(si, Math.min(ei, n - 1));

            PlanResult.ScheduledTask st = new PlanResult.ScheduledTask();
            st.id = t.getId();
            st.phase = t.getPhase();
            st.name = t.getName();
            st.hours = t.getHours();
            st.startDate = work.get(si).toString();
            st.endDate = work.get(ei).toString();
            st.workDays = ei - si + 1;
            st.calendarDays = (int) (ChronoUnit.DAYS.between(work.get(si), work.get(ei)) + 1);
            st.percentOfProject = round1(t.getHours() * 100 / total);
            st.progressPercent = asOf == null ? 0
                    : round1(Math.max(0, Math.min(100, (done - s) * 100 / t.getHours())));
            r.tasks.add(st);
            k++;
        }

        // 5. Day-by-day plan (which task, how many hours, each working day)
        for (int i = 0; i < n; i++) {
            double dayStart = i * perDay, dayEnd = (i + 1) * perDay;
            PlanResult.DailyEntry de = new PlanResult.DailyEntry();
            de.date = work.get(i).toString();
            de.weekday = work.get(i).getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            de.hours = round2(perDay);
            de.cumulativePercent = round1((i + 1) * 100.0 / n);
            for (int j = 0; j < tasks.size(); j++) {
                double overlap = Math.min(dayEnd, te[j]) - Math.max(dayStart, ts[j]);
                if (overlap > 1e-6) de.items.add(new PlanResult.DailyItem(tasks.get(j).getName(), round2(overlap)));
            }
            r.daily.add(de);
        }

        // 6. Progress summary for the check date
        if (asOf != null) {
            PlanResult.Progress p = new PlanResult.Progress();
            p.asOf = asOf.toString();
            p.workDaysElapsed = elapsed;
            p.workDaysRemaining = n - elapsed;
            p.hoursDone = round1(done);
            p.percentDone = round1(done * 100 / total);
            for (int j = 0; j < tasks.size(); j++) {
                if (te[j] <= done + EPS) p.completedTasks.add(tasks.get(j).getName());
                else if (p.currentTask == null && elapsed > 0) p.currentTask = tasks.get(j).getName();
            }
            if (elapsed == 0) {
                p.message = "Not started yet: there are no working days up to "
                        + asOf.format(NICE) + ". The plan starts on " + start.format(NICE) + ".";
            } else if (done >= total - EPS) {
                p.message = String.format(Locale.ENGLISH,
                        "By %s (%d working days) you can finish all the work (%.1f hours).",
                        asOf.format(NICE), elapsed, total);
            } else {
                p.message = String.format(Locale.ENGLISH,
                        "By %s: %d working days, about %.1f hours of work (%.1f%%) can be done. "
                                + "%d tasks finished, current task: %s. %d working days left.",
                        asOf.format(NICE), elapsed, done, p.percentDone,
                        p.completedTasks.size(), p.currentTask, p.workDaysRemaining);
            }
            r.progress = p;
        }
        return r;
    }

    private static double round1(double v) { return Math.round(v * 10.0) / 10.0; }
    private static double round2(double v) { return Math.round(v * 100.0) / 100.0; }
}