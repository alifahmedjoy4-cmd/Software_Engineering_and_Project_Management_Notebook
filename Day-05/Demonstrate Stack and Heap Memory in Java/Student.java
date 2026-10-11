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
