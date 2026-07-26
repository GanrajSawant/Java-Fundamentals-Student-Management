import java.util.HashSet;

public class HashSetExample {
    public static void main(String[] args) {

        HashSet<String> fruits = new HashSet<>();

        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Mango");
        fruits.add("Apple");

        System.out.println(fruits);
    }
}