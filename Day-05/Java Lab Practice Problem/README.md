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

---

# Java Lab Practice-02: Stack Frames, Heap Objects, and Inner Classes

## Problem Description

This Java program demonstrates Stack Frames, Heap Objects, Instance Variables, Local Variables, and Local Inner Classes. It defines an `Employee` class with a `salary` instance variable and a `display()` method. Inside `display()`, it declares a local variable `bonus` and creates a local inner class `Report` with a `show()` method.

## Concepts Used

* **Class and Object:** Creates `Employee` and `Report` objects.
* **Instance Variable:** Stores the employee's salary.
* **Local Variable:** Stores the bonus inside the `display()` method.
* **Local Inner Class:** Defines the `Report` class inside the `display()` method.
* **Identity Hash Code:** Uses `System.identityHashCode()` to display object identity hash codes.
* **Stack and Heap Memory:** Demonstrates method execution frames, local variables, and objects.

---

## Code

```java
import java.util.Scanner;

class Employee {
    int salary;

    void display() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter salary: ");
        salary = sc.nextInt();

        System.out.print("Enter bonus: ");
        int bonus = sc.nextInt();

        System.out.println("\nInside display() Method:");
        System.out.println("Salary (Instance Variable): " + salary);
        System.out.println("Bonus (Local Variable): " + bonus);

        class Report {
            void show() {
                System.out.println("\nReport Object Created");
                System.out.println("Report Identity Hash Code: "
                        + System.identityHashCode(this));
                System.out.println("Report: Employee salary is " + salary);
            }
        }

        Report r = new Report();
        r.show();
    }

    public static void main(String[] args) {
        Employee e = new Employee();

        System.out.println("Employee Object Created");
        System.out.println("Employee Identity Hash Code: "
                + System.identityHashCode(e));

        e.display();

        System.out.println("\nMemory Concept:");
        System.out.println("Employee object -> Heap");
        System.out.println("Report object -> Heap");
        System.out.println("salary -> Instance field");
        System.out.println("bonus -> Local variable in display()");
        System.out.println("show() -> Executes in its own method frame");
    }
}
```

---


## Sample Input

```text
Enter salary: 30000
Enter bonus: 5000
```

## Sample Output

```text
Employee Object Created
Employee Identity Hash Code: 12345678

Inside display() Method:
Salary (Instance Variable): 30000
Bonus (Local Variable): 5000

Report Object Created
Report Identity Hash Code: 87654321
Report: Employee salary is 30000

Memory Concept:
Employee object -> Heap
Report object -> Heap
salary -> Instance field
bonus -> Local variable in display()
show() -> Executes in its own method frame
```

## Purpose

To understand Java memory concepts, object identity, instance and local variables, and the use of local inner classes.

