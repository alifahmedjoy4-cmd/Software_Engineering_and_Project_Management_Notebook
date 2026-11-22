package bd.mbstu.planner.repository;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/** Inserts the 17 sample tasks on the first run (only when the tasks table is empty). */
@Component
public class SampleDataLoader implements ApplicationRunner {
    private final TaskRepository tasks;

    public SampleDataLoader(TaskRepository tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (tasks.count() > 0) return;
        Object[][] rows = {
            {"Planning", "Requirement analysis", 8.0},
            {"Planning", "Proposal and feasibility", 6.0},
            {"Design",   "Database design and ERD", 6.0},
            {"Design",   "UML diagrams and UI mockup", 8.0},
            {"Setup",    "IntelliJ, Maven and Tomcat setup", 4.0},
            {"Setup",    "MySQL schema and sample data", 4.0},
            {"Backend",  "Model classes and DB connection", 6.0},
            {"Backend",  "DAO layer (JDBC)", 10.0},
            {"Backend",  "Scheduler logic (calculator)", 16.0},
            {"Backend",  "Servlets and JSON API", 10.0},
            {"Frontend", "HTML and CSS layout", 10.0},
            {"Frontend", "Gantt chart", 16.0},
            {"Frontend", "JavaScript and API integration", 8.0},
            {"Testing",  "Unit tests", 8.0},
            {"Testing",  "System testing and bug fixing", 12.0},
            {"Report",   "Final report", 12.0},
            {"Report",   "Slides and demo", 8.0},
        };
        for (Object[] r : rows) tasks.add((String) r[0], (String) r[1], (Double) r[2]);
    }
}
