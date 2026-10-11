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
