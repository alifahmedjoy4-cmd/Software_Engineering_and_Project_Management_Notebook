package bd.mbstu.planner;

import bd.mbstu.planner.model.PlanResult;
import bd.mbstu.planner.model.Task;
import bd.mbstu.planner.service.SchedulerService;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SchedulerServiceTest {
    private final SchedulerService s = new SchedulerService();
    private final Set<DayOfWeek> friSat = EnumSet.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
    private final List<Task> tasks = List.of(
            new Task(1, "A", "Task one", 10), new Task(2, "A", "Task two", 20), new Task(3, "B", "Task three", 10));

    @Test
    void countsWorkingDaysWithoutFridayAndSaturday() {
        // Sunday 8 Nov 2026 .. Sunday 31 Jan 2027 = 85 days, 24 of them are Fri/Sat
        PlanResult r = s.plan(tasks, LocalDate.of(2026, 11, 8), LocalDate.of(2027, 1, 31), friSat, Set.of(), null);
        assertEquals(85, r.totalCalendarDays);
        assertEquals(61, r.totalWorkDays);
    }

    @Test
    void tasksCoverTheWholeRangeInOrder() {
        PlanResult r = s.plan(tasks, LocalDate.of(2026, 11, 8), LocalDate.of(2027, 1, 31), friSat, Set.of(), null);
        assertEquals("2026-11-08", r.tasks.get(0).startDate);
        assertEquals("2027-01-31", r.tasks.get(2).endDate);
    }

    @Test
    void checkDateGivesProgress() {
        PlanResult r = s.plan(tasks, LocalDate.of(2026, 11, 8), LocalDate.of(2027, 1, 31), friSat, Set.of(),
                LocalDate.of(2026, 12, 20));
        assertNotNull(r.progress);
        assertTrue(r.progress.percentDone > 0 && r.progress.percentDone < 100);
    }

    @Test
    void rejectsEndBeforeStart() {
        assertThrows(IllegalArgumentException.class, () ->
                s.plan(tasks, LocalDate.of(2027, 1, 31), LocalDate.of(2026, 11, 8), friSat, Set.of(), null));
    }

    @Test
    void dailyHoursAddUpToTotal() {
        PlanResult r = s.plan(tasks, LocalDate.of(2026, 11, 8), LocalDate.of(2026, 11, 19), friSat, Set.of(), null);
        double sum = r.daily.stream().flatMap(d -> d.items.stream()).mapToDouble(i -> i.hours).sum();
        assertEquals(40.0, sum, 0.5);
    }
}
