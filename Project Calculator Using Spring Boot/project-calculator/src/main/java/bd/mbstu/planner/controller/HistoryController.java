package bd.mbstu.planner.controller;

import bd.mbstu.planner.model.PlanSummary;
import bd.mbstu.planner.repository.PlanRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** GET /api/history -> the saved plans (newest first). */
@RestController
@RequestMapping("/api/history")
public class HistoryController {
    private final PlanRepository plans;

    public HistoryController(PlanRepository plans) {
        this.plans = plans;
    }

    @GetMapping
    public List<PlanSummary> history() {
        return plans.history();
    }
}
