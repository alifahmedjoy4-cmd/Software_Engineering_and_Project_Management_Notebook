
class CounterThread extends Thread {
    static volatile boolean running;
    static long staticCount;
    long count;

    public void run() {
        while (running) {
            staticCount++;
            count++;
        }
    }
}

public class Counter {
    public static void main(String[] args) throws Exception {
        int[] times = {1, 5, 15};

        for (int min : times) {
            CounterThread.staticCount = 0;
            CounterThread.running = true;

            CounterThread[] t = new CounterThread[100];

            for (int i = 0; i < 100; i++) {
                t[i] = new CounterThread();
                t[i].start();
            }

            Thread.sleep(min * 60L * 1000);
            CounterThread.running = false;

            long total = 0;

            for (CounterThread x : t) {
                x.join();
                total += x.count;
            }

            System.out.println("\nTime: " + min + " minute(s)");
            System.out.println("Static Count: " + CounterThread.staticCount);
            System.out.println("Non-static Total: " + total);
        }

        int[] a = {10, 25, 8, 40, 30};
        int max = Integer.MIN_VALUE, second = Integer.MIN_VALUE;

        for (int x : a) {
            if (x > max) {
                second = max;
                max = x;
            } else if (x > second && x < max) {
                second = x;
            }
        }

        System.out.println("\nSecond Largest: " + second);
    }
}