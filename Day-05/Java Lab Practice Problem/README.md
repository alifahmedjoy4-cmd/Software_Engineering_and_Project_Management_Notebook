# Java Lab Practice-01: Stack and Heap Memory

## Problem Description

This Java program demonstrates Stack and Heap Memory concepts using a `Student` class. It takes the student's age and marks as keyboard input and displays the instance variable, local variable, and object identity hash code.

## Concepts Used

* **Class and Object:** Creating a `Student` class and its object.
* **Instance Variable:** Stores the student's age.
* **Local Variable:** Stores marks inside the `display()` method.
* **Keyboard Input:** Uses the `Scanner` class to take user input.
* **Object Identity Hash Code:** Uses `System.identityHashCode()` to display the object's identity hash code.

---

## Code

```java
import java.util.Scanner;

class Student {
    int age;

    void display() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter Age: ");
        age = sc.nextInt();

        System.out.print("Enter Marks: ");
        int marks = sc.nextInt();

        System.out.println("Instance Variable (age): " + age);
        System.out.println("Local Variable (marks): " + marks);
    }

    public static void main(String[] args) {
        Student s = new Student();

        System.out.println("Student Object Created");
        System.out.println("Object Identity Hash Code: "
                + System.identityHashCode(s));

        s.display();
    }
}
```

---

## Sample Input

```text
Enter Age: 20
Enter Marks: 85
```

## Sample Output

```text
Student Object Created
Object Identity Hash Code: 12345678
Instance Variable (age): 20
Local Variable (marks): 85
```

## Purpose

To understand how Java objects, instance variables, local variables, and object identity hash codes work in relation to Stack and Heap Memory.
