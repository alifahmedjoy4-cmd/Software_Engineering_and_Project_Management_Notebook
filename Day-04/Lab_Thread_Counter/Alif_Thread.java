import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/*
 * Lab SEPM-001: Static vs Non-Static counters in multithreading.
 * NOTE: Rename the class AND the file to YourFirstname_Thread
 * (e.g. class Rahim_Thread  ->  file Rahim_Thread.java).
 */
public class Alif_Thread {

    // Experiment A: thread-safe shared (static) counter
    static final AtomicLong atomicStatic = new AtomicLong(0);

    // Experiment B: ordinary static counter, NO synchronization (race condition)
    static long unsafeStatic = 0;

    // Each thread gets its own CounterTask => its own non-static counter
    static class CounterTask implements Runnable {
        private final long increments;
        private final boolean threadSafe;
        long instanceCount = 0; // non-static (per object)

        CounterTask(long increments, boolean threadSafe) {
            this.increments = increments;
            this.threadSafe = threadSafe;
        }

        @Override
        public void run() {
            for (long i = 0; i < increments; i++) {
                instanceCount++;                 // only this thread touches it
                if (threadSafe) {
                    atomicStatic.incrementAndGet();
                } else {
                    unsafeStatic++;              // lost updates possible
                }
            }
        }
    }

    // Runs one experiment, returns {staticCount, nonStaticTotal}
    static long[] runExperiment(int threads, long increments, boolean threadSafe)
            throws InterruptedException {
        atomicStatic.set(0);
        unsafeStatic = 0;

        List<CounterTask> tasks = new ArrayList<>();
        List<Thread> pool = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            CounterTask t = new CounterTask(increments, threadSafe);
            tasks.add(t);
            pool.add(new Thread(t));
        }
        for (Thread t : pool) t.start();
        for (Thread t : pool) t.join();

        long nonStaticTotal = 0;
        for (CounterTask t : tasks) nonStaticTotal += t.instanceCount;

        long staticCount = threadSafe ? atomicStatic.get() : unsafeStatic;
        return new long[] { staticCount, nonStaticTotal };
    }

    static String percent(long diff, long nonStatic) {
        if (nonStatic == 0) return (diff == 0) ? "0.0000%" : "undefined";
        return String.format("%.4f%%", (diff * 100.0) / nonStatic);
    }

    static void report(int threads, long increments, boolean threadSafe)
            throws InterruptedException {
        long[] r = runExperiment(threads, increments, threadSafe);
        long expected = (long) threads * increments;
        long diff = Math.abs(r[0] - r[1]);

        System.out.println("Mode                : " + (threadSafe ? "Thread-safe (AtomicLong)" : "Unsynchronized (long)"));
        System.out.println("Threads (N)         : " + threads);
        System.out.println("Increments/thread(K): " + increments);
        System.out.println("Expected (N x K)    : " + expected);
        System.out.println("Static count        : " + r[0]);
        System.out.println("Non-static total    : " + r[1]);
        System.out.println("Absolute difference : " + diff);
        System.out.println("Percentage diff     : " + percent(diff, r[1]));
    }

    static String line(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append('-');
        return sb.toString();
    }

    // Runs all 7 test cases and prints results as tables
    static void runAllTestCases() throws InterruptedException {
        int[] threads = { 1, 2, 5, 10, 20, 50, 100 };
        long[] incs = { 1000, 10000, 10000, 50000, 50000, 50000, 50000 };

        // ---------- Table 1: Expected behavior (thread-safe) ----------
        System.out.println("=== Expected behavior: Thread-safe (AtomicLong) ===");
        String f1 = "| %-4s | %-7s | %-12s | %-18s | %-12s | %-10s |%n";
        System.out.println(line(82));
        System.out.printf(f1, "Test", "Threads", "Expected", "Thread-safe static", "Non-static", "Diff (%)");
        System.out.println(line(82));
        for (int i = 0; i < threads.length; i++) {
            long[] r = runExperiment(threads[i], incs[i], true);
            long diff = Math.abs(r[0] - r[1]);
            System.out.printf(f1, "TC" + (i + 1), threads[i], (long) threads[i] * incs[i],
                    r[0], r[1], percent(diff, r[1]));
        }
        System.out.println(line(82));

        // ---------- Table 2: Unsynchronized, 5 runs each ----------
        System.out.println();
        System.out.println("=== Unsynchronized (5 runs each) ===");
        String f2 = "| %-7s | %-3s | %-12s | %-12s | %-12s | %-10s | %-10s |%n";
        System.out.println(line(88));
        System.out.printf(f2, "Threads", "Run", "Expected", "Static(unsafe)", "Non-static", "Abs diff", "Diff (%)");
        System.out.println(line(88));

        long[] avgStatic = new long[threads.length];
        long[] avgNonStatic = new long[threads.length];
        long[] avgDiff = new long[threads.length];
        double[] avgPct = new double[threads.length];

        for (int i = 0; i < threads.length; i++) {
            long expected = (long) threads[i] * incs[i];
            long sS = 0, sN = 0, sD = 0;
            double sP = 0;
            for (int run = 1; run <= 5; run++) {
                long[] r = runExperiment(threads[i], incs[i], false);
                long diff = Math.abs(r[0] - r[1]);
                double pct = (r[1] == 0) ? 0 : (diff * 100.0) / r[1];
                sS += r[0]; sN += r[1]; sD += diff; sP += pct;
                System.out.printf(f2, threads[i], run, expected, r[0], r[1], diff,
                        String.format("%.4f", pct));
            }
            avgStatic[i] = sS / 5; avgNonStatic[i] = sN / 5; avgDiff[i] = sD / 5; avgPct[i] = sP / 5;
            System.out.println(line(88));
        }

        // ---------- Table 3: Result recording sheet (averages of 5 runs) ----------
        System.out.println();
        System.out.println("=== Result recording sheet (average of 5 unsynchronized runs) ===");
        String f3 = "| %-7s | %-14s | %-18s | %-16s | %-19s | %-14s |%n";
        System.out.println(line(103));
        System.out.printf(f3, "Threads", "Expected count", "Static count(unsafe)", "Non-static total",
                "Absolute difference", "Difference (%)");
        System.out.println(line(103));
        for (int i = 0; i < threads.length; i++) {
            System.out.printf(f3, threads[i], (long) threads[i] * incs[i], avgStatic[i], avgNonStatic[i],
                    avgDiff[i], String.format("%.4f", avgPct[i]));
        }
        System.out.println(line(103));
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length == 0) {          // no arguments -> run every test case
            runAllTestCases();
            return;
        }
        if (args.length != 3) {
            System.out.println("Usage: java Firstname_Thread <threads> <incrementsPerThread> <true|false>");
            System.out.println("   or: java Firstname_Thread        (runs all test cases)");
            return;
        }
        int threads = Integer.parseInt(args[0]);
        long increments = Long.parseLong(args[1]);
        boolean threadSafe = Boolean.parseBoolean(args[2]);
        report(threads, increments, threadSafe);
    }
}
