package bd.mbstu.planner.model;

/** One piece of project work with an effort estimate in hours. */
public class Task {
    private final int id;
    private final String phase;
    private final String name;
    private final double hours;

    public Task(int id, String phase, String name, double hours) {
        this.id = id;
        this.phase = phase;
        this.name = name;
        this.hours = hours;
    }

    public int getId()        { return id; }
    public String getPhase()  { return phase; }
    public String getName()   { return name; }
    public double getHours()  { return hours; }
}
