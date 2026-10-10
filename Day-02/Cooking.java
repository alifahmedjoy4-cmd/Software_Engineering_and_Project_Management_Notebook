class Cooking extends Thread {

    String name;
    long count = 0;

    Cooking(String name) {
        this.name = name;
    }

    public void run() {

        System.out.println("This is a Cooking Class - " + name);

        long startTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - startTime < 5000) {
            count++;
        }

        System.out.println(name + " ran " + count + " times.");
    }
}

public class Main {

    public static void main(String[] args) {

        Cooking T1 = new Cooking("T1");
        Cooking T2 = new Cooking("T2");
        Cooking T3 = new Cooking("T3");

        T1.start();
        T2.start();
        T3.start();
    }
}
