# Java Multithreading — Race Condition

This project demonstrates a race condition in Java multithreading and two common ways to solve it:

synchronized
AtomicInteger

---

## 📌 Concept

Three threads share a single counter. Each thread increments the counter 100,000 times.

Number of threads = 3

Increments per thread = 100,000

Expected count = 3 × 100,000

               = 300,000
              
---

## 🔴 Race Condition

The following operation is not thread-safe:

count++;

Although it looks like one operation, it involves reading, modifying, and writing the value.

When multiple threads execute it concurrently, some increments may be lost.

Example:

Expected count = 300000

Actual count   = 247831

The actual result can vary between executions.

---

# 🟢 Solution 1 — synchronized

The increment() method can be synchronized:

static synchronized void increment() {
    count++;
}
This ensures that only one thread at a time can execute the method.

## M1.java

public class M1 extends Thread {

    static int count = 0;

    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            increment();
        }
    }

    static synchronized void increment() {
        count++;
    }
}

## Main.java

public class Main {
    public static void main(String[] args) throws InterruptedException {

        M1 t1 = new M1();
        M1 t2 = new M1();
        M1 t3 = new M1();

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Expected count = " + 300000);
        System.out.println("Actual count = " + M1.count);
    }
}

## Result

Expected count = 300000

Actual count = 300000

---

# 🟢 Solution 2 — AtomicInteger

Java provides AtomicInteger for thread-safe operations on integers.

Instead of:

static int count = 0;
we use:

static AtomicInteger count = new AtomicInteger(0);
And instead of:

count++;
we use:

count.incrementAndGet();

## M1.java

import java.util.concurrent.atomic.AtomicInteger;

public class M1 extends Thread {

    static AtomicInteger count = new AtomicInteger(0);

    @Override
    public void run() {
        for (int i = 0; i < 100000; i++) {
            count.incrementAndGet();
        }
    }
}

## Main.java

public class Main {
    public static void main(String[] args) throws InterruptedException {

        M1 t1 = new M1();
        M1 t2 = new M1();
        M1 t3 = new M1();

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        System.out.println("Expected count = " + 300000);
        System.out.println("Actual count = " + M1.count.get());
    }
}

## Result

Expected count = 300000

Actual count = 300000

---

# 🔍 Important Methods

## start()

Starts a thread and causes its run() method to execute.

t1.start();

##join()

Makes the main thread wait until the specified thread finishes.

t1.join();

join() does not solve the race condition. It only ensures that the threads have finished before we print the final result.

##synchronized

Allows only one thread at a time to execute the synchronized method.

static synchronized void increment()

##AtomicInteger

Provides thread-safe operations on an integer.

count.incrementAndGet();

To retrieve the current value:

count.get();

---


# 🧠 Key Takeaways

1.Multiple threads accessing shared data can cause a race condition.
2.count++ is not atomic.
3.join() waits for threads to finish but does not prevent race conditions.
4.synchronized provides mutual exclusion using a lock.
5.AtomicInteger provides atomic operations without explicitly using synchronized.
6.Both approaches can safely produce the expected result of 300000.

## Important distinction


join()
  ↓
Wait for thread to finish

synchronized
  ↓
Allow only one thread at a time

AtomicInteger
  ↓
Perform integer operations atomically

