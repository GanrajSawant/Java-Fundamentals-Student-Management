public class EncapsulationMain {

    public static void main(String[] args) {

        Student s = new Student();

        s.setName("Ganraj");
        s.setAge(21);

        System.out.println("Student Name: " + s.getName());
        System.out.println("Student Age: " + s.getAge());
    }
}