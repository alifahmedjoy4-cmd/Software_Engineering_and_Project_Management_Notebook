
class MyThread extends Thread {
    public void run() {
        System.out.println("Thread is running");
        Thread.yield();
    }
}

public class ThreadMethodsDemo {
    public static void main(String[] args) throws Exception {
        MyThread t = new MyThread();

        t.setName("MyWorker");
        t.setPriority(Thread.MAX_PRIORITY);

        System.out.println("Name: " + t.getName());
        System.out.println("Priority: " + t.getPriority());
        System.out.println("Current Thread: " + Thread.currentThread().getName());
        System.out.println("State: " + t.getState());
        System.out.println("Alive: " + t.isAlive());

        t.start();
        System.out.println("Alive: " + t.isAlive());
        t.join();

        System.out.println("Final State: " + t.getState());

        Thread.sleep(1000);
        System.out.println("Sleep tested");

        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted");
            }
        });

        t2.start();
        t2.interrupt();
        t2.join();

        System.out.println("Interrupted: " + t2.isInterrupted());
        System.out.println("Active Threads: " + Thread.activeCount());
    }
}