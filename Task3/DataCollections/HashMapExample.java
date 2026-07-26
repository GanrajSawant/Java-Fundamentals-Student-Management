import java.util.HashMap;

public class HashMapExample {
    public static void main(String[] args) {

        HashMap<Integer, String> students = new HashMap<>();

        students.put(101, "Ganraj");
        students.put(102, "Rahul");
        students.put(103, "Amit");

        System.out.println(students);

        System.out.println("Student ID 102: " + students.get(102));

        students.put(102, "Rohan");

        students.remove(103);

        System.out.println(students);
    }
}
