# PC#1 - ICT 21 Batch | Java Problem Solutions

| | |
|---|---|
| **Name** | Alif Ahmed Joy |
| **Student ID** | IT24015 |
| **Department** | Information and Communication Technology (ICT) |
| **University** | Mawlana Bhashani Science and Technology University (MBSTU) |
| **Year / Semester** | 3rd Year, 1st Semester (Session 2023-24) |
| **Contest** | PC#1 - ICT 21 Batch (HackerRank) |
| **Language** | Java (Basic) |

---

## Problems Solved

| # | Problem | Difficulty | Max Score | Status |
|---|---------|-----------|-----------|--------|
| 1 | Prime Checker | Medium | 3 | Solved |
| 2 | Java Primality Test | Easy | 2 | Solved |

---

## Problem 1: Prime Checker

**Task:** Check whether the given number(s) are prime using a Java class and its methods.

**Approach:**
1. Handle the base cases (numbers less than 2 are not prime).
2. Test divisibility from 2 up to the square root of the number.
3. If no divisor is found, the number is prime.

**Time Complexity:** O(√n) per number

**Solution:** [`PrimeChecker.java`](./PrimeChecker.java)

**Result:** All test cases passed.

![Prime Checker Result](./images/Problem1.png)

---

## Problem 2: Java Primality Test

**Task:** Determine whether a given number is prime or not.

**Approach:**
1. Use Java's built-in `BigInteger.isProbablePrime()` method, or check divisibility up to √n.
2. Print `prime` or `not prime` accordingly.

**Solution:** [`PrimalityTest.java`](./PrimalityTest.java)

**Result:** All test cases passed.

![Primality Test Result](./images/Problem2.png)

---

## Contest Overview

Both problems show **Solved** status on the contest page.

![Contest Overview](./images/Leader_board.png)

### Leaderboard

![Leaderboard](./images/image.png)

---

## Repository Structure

```
.
├── README.md
├── PrimeChecker.java
├── PrimalityTest.java
└── images/
    ├── Problem1.png
    ├── Problem2.png
    ├── Leader_board.png
    └── image.png
```

---
<img width="1050" height="720" alt="Problem1" src="https://github.com/user-attachments/assets/f76c2cfe-2727-4028-9c07-c04dafac3095" />
<img width="1042" height="706" alt="Problem2" src="https://github.com/user-attachments/assets/21697bea-9ae2-47fa-a9b1-24a46e99bb27" />
<img width="1496" height="552" alt="Leader board" src="https://github.com/user-attachments/assets/770bdc18-da08-4421-838c-5e8e365b5e8f" />
<img width="1198" height="823" alt="Score" src="https://github.com/user-attachments/assets/6ef89a03-dc0e-45a0-a895-4f37c2bd32ad" />

## How to Run

```bash
javac PrimeChecker.java
java PrimeChecker

javac PrimalityTest.java
java PrimalityTest
```

---

**Author:** Alif Ahmed Joy (IT24015), Dept. of ICT, MBSTU
