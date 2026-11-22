# Project calculator (Spring Boot + MySQL + Gantt chart)

Same project, same page, same design as the Servlet/Tomcat version. Only the back end changed:
Servlets became Spring `@RestController`s, JDBC DAOs became `JdbcTemplate` repositories, and the
war + external Tomcat became one runnable Spring Boot jar with Tomcat built in.

You give a **start date**, a **last date** and (optionally) a **check date**. The calculator splits
the tasks over the working days, tells you what to finish each day, how much is done by the check
date, and redraws the Gantt chart on every change.

## Run it (PowerShell)

Needs: JDK 17 or newer, MySQL 8 running on port 3306.

```powershell
cd C:\path\to\project-calculator
$env:DB_PASSWORD = "your_mysql_password"
.\mvnw.cmd spring-boot:run
```

Open http://localhost:8080

The first start creates the `project_calculator` database, the 3 tables, and the 17 sample tasks by itself.
You do not have to run `sql/schema.sql` (it is kept only for people who prefer MySQL Workbench).

## Run it in IntelliJ IDEA

1. **File > Open**, choose the `project-calculator` folder (the one with `pom.xml`). Wait for Maven to finish.
2. Open `ProjectCalculatorApplication.java` and click the green run arrow.
3. If your MySQL password is not `your_mysql_password`: **Run > Edit Configurations > Environment variables**
   and add `DB_PASSWORD=yourpassword` (or edit `src/main/resources/application.properties`).
4. Open http://localhost:8080

## File structure

```
project-calculator/
|-- pom.xml                                   Spring Boot 3.3, JDBC, MySQL driver, JUnit
|-- mvnw, mvnw.cmd, .mvn/                     Maven wrapper (no Maven install needed)
|-- sql/schema.sql                            optional manual script for MySQL Workbench
`-- src/
    |-- main/
    |   |-- java/bd/mbstu/planner/
    |   |   |-- ProjectCalculatorApplication.java   start class
    |   |   |-- model/        Task, PlanResult, PlanSummary          (unchanged)
    |   |   |-- service/      SchedulerService                        THE CALCULATOR (unchanged logic)
    |   |   |-- repository/   TaskRepository, PlanRepository, SampleDataLoader
    |   |   `-- controller/   PlanController, TaskController, HistoryController,
    |   |                     PlanRequest, ApiExceptionHandler
    |   `-- resources/
    |       |-- application.properties          port + MySQL settings   <-- edit the password
    |       |-- schema.sql                      creates tables if missing
    |       `-- static/       index.html, css/style.css, js/app.js      (unchanged)
    `-- test/java/bd/mbstu/planner/SchedulerServiceTest.java            JUnit 5 (unchanged)
```

## API (unchanged)

| Method | URL | Purpose |
|---|---|---|
| GET | `/api/plan?start=2026-11-08&end=2027-01-31&asOf=2026-12-20&offDays=FRIDAY,SATURDAY&holidays=` | calculate |
| POST | `/api/plan` (same parameters) | calculate and save |
| GET | `/api/tasks` | list tasks |
| POST | `/api/tasks` `action=add` or `action=delete` | add / delete a task |
| GET | `/api/history` | saved plans |

## Useful commands

```powershell
.\mvnw.cmd test                                   # run the unit tests (no database needed)
.\mvnw.cmd clean package -DskipTests              # build target\project-calculator.jar
java -jar .\target\project-calculator.jar         # run the jar
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.arguments=--server.port=9090"   # another port
```
