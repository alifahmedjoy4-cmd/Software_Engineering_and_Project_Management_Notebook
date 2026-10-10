public class Counter {

    static int count = 0;       
    int nonStaticCount = 0;     
    final int finalCount = 1;  

    public static void main(String[] args) {

        Counter obj1 = new Counter();
        Counter obj2 = new Counter();

        obj1.count++;
        obj2.count++;

        obj1.nonStaticCount++;
        obj1.nonStaticCount++;

        obj2.nonStaticCount++;

        System.out.println("Static Count: " + count);
        System.out.println("Obj1 Non-Static Count: " + obj1.nonStaticCount);
        System.out.println("Obj2 Non-Static Count: " + obj2.nonStaticCount);
        System.out.println("Final Count: " + obj1.finalCount);
    }
}