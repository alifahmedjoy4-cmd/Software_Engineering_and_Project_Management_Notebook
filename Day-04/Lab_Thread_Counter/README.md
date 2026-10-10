# 🧵 Static vs Non-Static Variables in Multithreading

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk&logoColor=white)
![Lab](https://img.shields.io/badge/Lab-SEPM--001-blue)
![Type](https://img.shields.io/badge/Topic-Concurrency-green)

> A Java lab project that shows how a **static (shared)** counter and a **non-static (per-object)** counter behave when many threads update them, and why `static` does **not** mean thread-safe.

---

## 📌 Overview

Each thread runs a `CounterTask` object and increments **two counters**:

| Counter | Scope | Who updates it |
|---|---|---|
| **Static counter** | Shared by the whole class | All threads at the same time |
| **Non-static counter** | Belongs to one object | Only its own thread |

After all threads finish, the program compares the two totals and reports the **absolute difference** and **percentage difference**.

## 🎯 Learning Objectives

- Understand the difference between static and non-static variables
- Create and run multiple threads
- Observe shared vs instance-level data
- Calculate absolute and percentage difference
- Understand race conditions and thread safety

## 🧪 Two Experiments

| Mode | Argument | Static counter type | Expected result |
|---|---|---|---|
| **A: Thread-safe** | `true` | `AtomicLong` | Both counts equal, difference = **0%** |
| **B: Unsynchronized** | `false` | plain `static long` | Static count may be **lower** (lost updates) |

Expected count for every run: **N × K**
(N = number of threads, K = increments per thread)

**Formulas**

```
Absolute difference   = | static count − non-static total |
Percentage difference = (absolute difference / non-static total) × 100
```

If the non-static total is 0: the difference is 0% when both counts are 0, otherwise it is undefined.

## 🗂️ Project Structure

```
.
├── Firstname_Thread.java   # Single-class solution (rename to your first name)
└── README.md
```

> ⚠️ The class name and file name must match. Replace `Alif` with your real first name in both places.

## ⚙️ Requirements

- JDK 17 or newer (check with `javac -version`)
- Windows CMD, PowerShell, or any terminal

## ▶️ How to Run

**1. Compile**

```bash
javac Alif_Thread.java
```

**2. Run all 7 test cases (prints the tables)**

```bash
java Alif_Thread
```

**3. Run a single experiment**

```bash
# Thread-safe (AtomicLong)
java Alif_Thread 10 100000 true

# Unsynchronized (race condition)
java Alif_Thread 10 100000 false
```

Usage: `java Alif_Thread <threads> <incrementsPerThread> <true|false>`

**4. Save the output to a file (optional)**

```bash
java Alif_Thread > result.txt
```

## 📋 Test Cases

| Test | Threads (N) | Increments per thread (K) | Expected (N × K) |
|---|---|---|---|
| TC1 | 1 | 1,000 | 1,000 |
| TC2 | 2 | 10,000 | 20,000 |
| TC3 | 5 | 10,000 | 50,000 |
| TC4 | 10 | 50,000 | 500,000 |
| TC5 | 20 | 50,000 | 1,000,000 |
| TC6 | 50 | 50,000 | 2,500,000 |
| TC7 | 100 | 50,000 | 5,000,000 |

Running the program with no arguments executes the thread-safe mode once and the unsynchronized mode **5 times** per test case, then prints three tables:

1. Expected behavior (thread-safe)
2. Unsynchronized: every run listed separately
3. Result recording sheet: average of the 5 runs

## 🔍 Key Observations

- **Thread-safe mode:** static count equals non-static total, so the difference stays at **0%**. This is the control case.
- **Unsynchronized mode:** `static long++` is not atomic (read → add → write), so threads overwrite each other and increments are lost.
- **Non-static counter:** always exact, because each thread owns its object.
- **1 thread:** no concurrency, so no lost updates even without synchronization.
- The percentage difference does **not** always grow with thread count. It depends on thread scheduling, hardware, and the JVM, so results change from run to run.

## ✅ Conclusion

`static` vs non-static describes **variable ownership**, not **thread safety**. A static variable is shared, so concurrent updates still need `AtomicLong`, `synchronized`, or another proper synchronization mechanism. A non-static counter is safe in this experiment only because each thread has its own object.

## 🛠️ Troubleshooting

| Error | Fix |
|---|---|
| `'javac' is not recognized` | Install the JDK and add it to PATH |
| `class ... is public, should be declared in a file named ...` | Make the class name and file name identical |
| `Could not find or load main class` | Run `java Firstname_Thread` (no `.java`) after compiling |
