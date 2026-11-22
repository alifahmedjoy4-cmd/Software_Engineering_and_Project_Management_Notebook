package bd.mbstu.planner.controller;

import bd.mbstu.planner.repository.TaskRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * GET  /api/tasks                                   -> list of tasks
 * POST /api/tasks  action=add&phase=&name=&hours=   -> add a task
 * POST /api/tasks  action=delete&id=                -> delete a task
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskRepository tasks;

    public TaskController(TaskRepository tasks) {
        this.tasks = tasks;
    }

    @GetMapping
    public List<Map<String, Object>> list() {
        return tasks.findAll().stream().map(t -> Map.<String, Object>of(
                "id", t.getId(), "phase", t.getPhase(), "name", t.getName(), "hours", t.getHours())).toList();
    }

    @PostMapping
    public Map<String, Object> change(@RequestParam(name = "action", required = false) String action,
                                      @RequestParam(name = "phase", required = false) String phase,
                                      @RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "hours", required = false) String hours,
                                      @RequestParam(name = "id", required = false) String id) {
        if ("add".equals(action)) {
            if (hours == null) throw new IllegalArgumentException("Phase, name and hours (> 0) are required.");
            double h = Double.parseDouble(hours);
            if (phase == null || phase.isBlank() || name == null || name.isBlank() || h <= 0)
                throw new IllegalArgumentException("Phase, name and hours (> 0) are required.");
            tasks.add(phase.trim(), name.trim(), h);
        } else if ("delete".equals(action)) {
            if (id == null) throw new IllegalArgumentException("Task id is required.");
            tasks.delete(Integer.parseInt(id));
        } else {
            throw new IllegalArgumentException("Unknown action.");
        }
        return Map.of("ok", true);
    }
}
