package bd.mbstu.planner.servlet;

import jakarta.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;
import java.util.TreeSet;

/** Reads and validates the query parameters of /api/plan. */
final class PlanRequest {
    static final LocalDate DEFAULT_START = LocalDate.of(2026, 11, 8);
    static final LocalDate DEFAULT_END = LocalDate.of(2027, 1, 31);

    LocalDate start, end, asOf;
    Set<DayOfWeek> offDays;
    Set<LocalDate> holidays;
    String offDaysText, holidaysText;

    static PlanRequest from(HttpServletRequest req) {
        PlanRequest q = new PlanRequest();
        q.start = date(req.getParameter("start"), DEFAULT_START);
        q.end = date(req.getParameter("end"), DEFAULT_END);
        q.asOf = date(req.getParameter("asOf"), null);

        // Bangladesh weekend (Friday + Saturday) unless the page sends something else
        String off = req.getParameter("offDays");
        q.offDaysText = off == null ? "FRIDAY,SATURDAY" : off.trim();
        q.offDays = EnumSet.noneOf(DayOfWeek.class);
        for (String s : q.offDaysText.split(",")) {
            if (!s.isBlank()) q.offDays.add(DayOfWeek.valueOf(s.trim().toUpperCase()));
        }

        String hol = req.getParameter("holidays");
        q.holidaysText = hol == null ? "" : hol.trim();
        q.holidays = new TreeSet<>();
        for (String s : q.holidaysText.split(",")) {
            if (!s.isBlank()) q.holidays.add(LocalDate.parse(s.trim()));
        }
        return q;
    }

    private static LocalDate date(String s, LocalDate fallback) {
        return s == null || s.isBlank() ? fallback : LocalDate.parse(s.trim());
    }
}
