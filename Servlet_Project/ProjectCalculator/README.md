# Project calculator (Servlet + MySQL + Gantt chart, Tomcat 10.1 / Jakarta version)

You give a **start date**, a **last date** and (optionally) a **check date**. The calculator
splits the project tasks over the working days, tells you what to finish each day, how much
of the project is done by the check date, and draws a Gantt chart that redraws on every change.

Default dates: 8 Nov 2026 to 31 Jan 2027 (85 calendar days, 61 working days with Friday and
Saturday off, 152 hours of work, about 2.5 hours per working day).

## File structure

```
ProjectCalculator/
|-- pom.xml                                   Maven: Servlet API, MySQL driver, Gson, JUnit
|-- README.md
|-- sql/
|   `-- schema.sql                            database, 3 tables, 17 sample tasks
`-- src/
    |-- main/
    |   |-- java/bd/mbstu/planner/
    |   |   |-- model/
    |   |   |   |-- Task.java                 one task (phase, name, hours)
    |   |   |   |-- PlanResult.java           calculation result sent as JSON
    |   |   |   `-- PlanSummary.java          row of the saved-plans list
    |   |   |-- util/
    |   |   |   |-- DBConnection.java         JDBC connection from db.properties
    |   |   |   `-- Json.java                 JSON response helper
    |   |   |-- dao/
    |   |   |   |-- TaskDao.java              select / insert / delete tasks
    |   |   |   `-- PlanDao.java              save plan (transaction) + history
    |   |   |-- service/
    |   |   |   `-- SchedulerService.java     THE CALCULATOR (pure Java)
    |   |   `-- servlet/
    |   |       |-- PlanRequest.java          reads request parameters
    |   |       |-- PlanServlet.java          GET/POST /api/plan
    |   |       |-- TaskServlet.java          GET/POST /api/tasks
    |   |       `-- HistoryServlet.java       GET /api/history
    |   |-- resources/
    |   |   `-- db.properties                 MySQL url, user, password  <-- edit this
    |   `-- webapp/
    |       |-- index.html                    the page
    |       |-- css/style.css
    |       |-- js/app.js                     calls the servlets, draws the Gantt chart
    |       `-- WEB-INF/web.xml
    `-- test/java/bd/mbstu/planner/
        `-- SchedulerServiceTest.java         JUnit 5 tests
```

## Run it step by step

1. Install JDK 17, MySQL 8 and Apache **Tomcat 10.1** (this version uses `jakarta.*`; Tomcat 9 will NOT work with it).
2. Run `sql/schema.sql` in MySQL Workbench (or `mysql -u root -p < sql/schema.sql`).
3. Put your MySQL password in `src/main/resources/db.properties`.
4. IntelliJ IDEA: **File > Open**, choose the `ProjectCalculator` folder (the one with `pom.xml`), wait for Maven to download dependencies.
5. **Run > Edit Configurations > + > Tomcat Server > Local**. Select your Tomcat 9 folder.
6. **Deployment** tab > **+ > Artifact** > `project-calculator:war exploded`. Set *Application context* to `/project-calculator`.
7. Run. Open http://localhost:8080/project-calculator/
8. Change any date: the numbers and the Gantt chart update at once. "Save this plan" stores the calculation in MySQL.

## How the calculator works (SchedulerService)

1. Count working days between start and last date (skip off days and holidays) = `N`.
2. `hoursPerDay = totalHours / N`, so every working day carries the same amount of work.
3. Place tasks one after another: a task that needs 10 hours takes `10 / hoursPerDay` days.
4. Check date: `hoursDone = workingDaysUpToCheckDate * hoursPerDay`, then the percentage, finished tasks and the task in progress.

## API

| Method | URL | Purpose |
|---|---|---|
| GET | `/api/plan?start=2026-11-08&end=2027-01-31&asOf=2026-12-20&offDays=FRIDAY,SATURDAY&holidays=` | calculate |
| POST | `/api/plan` (same parameters) | calculate and save |
| GET | `/api/tasks` | list tasks |
| POST | `/api/tasks` `action=add` or `action=delete` | add / delete a task |
| GET | `/api/history` | saved plans |
