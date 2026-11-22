package bd.mbstu.planner.controller;

import bd.mbstu.planner.model.PlanResult;
import bd.mbstu.planner.repository.PlanRepository;
import bd.mbstu.planner.repository.TaskRepository;
import bd.mbstu.planner.service.SchedulerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * GET  /api/plan?start=&end=&asOf=&offDays=&holidays=  -> calculates the plan (JSON)
 * POST /api/plan  (same parameters)                    -> calculates and saves it in MySQL
 */
@RestController
@RequestMapping("/api/plan")
public class PlanController {
    private final TaskRepository tasks;
    private final PlanRepository plans;
    private final SchedulerService scheduler;

    public PlanController(TaskRepository tasks, PlanRepository plans, SchedulerService scheduler) {
        this.tasks = tasks;
        this.plans = plans;
        this.scheduler = scheduler;
    }

    @GetMapping
    public PlanResult calculate(@RequestParam(name = "start", required = false) String start,
                                @RequestParam(name = "end", required = false) String end,
                                @RequestParam(name = "asOf", required = false) String asOf,
                                @RequestParam(name = "offDays", required = false) String offDays,
                                @RequestParam(name = "holidays", required = false) String holidays) {
        return calculate(PlanRequest.of(start, end, asOf, offDays, holidays));
    }

    @PostMapping
    public Map<String, Object> save(@RequestParam(name = "start", required = false) String start,
                                    @RequestParam(name = "end", required = false) String end,
                                    @RequestParam(name = "asOf", required = false) String asOf,
                                    @RequestParam(name = "offDays", required = false) String offDays,
                                    @RequestParam(name = "holidays", required = false) String holidays) {
        PlanRequest q = PlanRequest.of(start, end, asOf, offDays, holidays);
        int id = plans.save(calculate(q), q.offDaysText, q.holidaysText);
        return Map.of("id", id);
    }

    private PlanResult calculate(PlanRequest q) {
        return scheduler.plan(tasks.findAll(), q.start, q.end, q.offDays, q.holidays, q.asOf);
    }
}
