import java.util.ArrayList;

public class ArrayListExample {
    public static void main(String[] args) {

        ArrayList<String> students = new ArrayList<>();

        students.add("Ganraj");
        students.add("Rahul");
        students.add("Amit");

        System.out.println("Student List:");
        System.out.println(students);

        students.set(1, "Rohan");

        students.remove("Amit");

        System.out.println("Updated List:");
        System.out.println(students);
    }
}