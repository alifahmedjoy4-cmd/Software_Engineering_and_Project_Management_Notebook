package bd.mbstu.planner.controller;

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

    static PlanRequest of(String start, String end, String asOf, String offDays, String holidays) {
        PlanRequest q = new PlanRequest();
        q.start = date(start, DEFAULT_START);
        q.end = date(end, DEFAULT_END);
        q.asOf = date(asOf, null);

        // Bangladesh weekend (Friday + Saturday) unless the page sends something else
        q.offDaysText = offDays == null ? "FRIDAY,SATURDAY" : offDays.trim();
        q.offDays = EnumSet.noneOf(DayOfWeek.class);
        for (String s : q.offDaysText.split(",")) {
            if (!s.isBlank()) q.offDays.add(DayOfWeek.valueOf(s.trim().toUpperCase()));
        }

        q.holidaysText = holidays == null ? "" : holidays.trim();
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
