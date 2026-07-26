import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("================================================");
        System.out.println("   Welcome to the Student Management System     ");
        System.out.println("================================================");

        while (running) {
            System.out.println("\n--- MAIN MENU ---");
            System.out.println("1. Add a Student");
            System.out.println("2. Add a Course");
            System.out.println("3. Enroll Student in a Course");
            System.out.println("4. Assign Course Grade");
            System.out.println("5. View All Students");
            System.out.println("6. View All Courses");
            System.out.println("7. View All Enrollments/Grades");
            System.out.println("8. Delete a Student");
            System.out.println("9. Exit System");
            System.out.print("Enter your choice (1-9): ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println(" Invalid input! Enter a valid menu number.");
                scanner.nextLine();
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.println("\n--- Add New Student ---");
                    System.out.print("Enter First Name: ");
                    String fName = scanner.nextLine();
                    System.out.print("Enter Last Name: ");
                    String lName = scanner.nextLine();
                    System.out.print("Enter Email Address: ");
                    String email = scanner.nextLine();

                    if (StudentManager.addStudent(fName, lName, email)) {
                        System.out.println("🚀 Student record added successfully!");
                    }
                    break;

                case 2:
                    System.out.println("\n--- Add New Course ---");
                    System.out.print("Enter Course Title: ");
                    String cName = scanner.nextLine();
                    System.out.print("Enter Course Code: ");
                    String cCode = scanner.nextLine();
                    System.out.print("Enter Credits: ");
                    int credits = scanner.nextInt();
                    scanner.nextLine();

                    if (CourseManager.addCourse(cName, cCode, credits)) {
                        System.out.println("🚀 Course catalog updated successfully!");
                    }
                    break;

                case 3:
                    System.out.println("\n--- Enroll Student in Course ---");
                    System.out.print("Enter Student ID: ");
                    int sId = scanner.nextInt();
                    System.out.print("Enter Course ID: ");
                    int cId = scanner.nextInt();
                    scanner.nextLine();

                    if (EnrollmentManager.enrollStudent(sId, cId)) {
                        System.out.println("🚀 Student enrolled successfully!");
                    }
                    break;

                case 4:
                    System.out.println("\n--- Assign Grade ---");
                    System.out.print("Enter Student ID: ");
                    int gradSId = scanner.nextInt();
                    System.out.print("Enter Course ID: ");
                    int gradCId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Grade (e.g., A, B, C, F): ");
                    String grade = scanner.nextLine();

                    if (EnrollmentManager.assignGrade(gradSId, gradCId, grade)) {
                        System.out.println(" Grade assigned successfully!");
                    }
                    break;

                case 5:
                    StudentManager.viewAllStudents();
                    break;

                case 6:
                    CourseManager.viewAllCourses();
                    break;

                case 7:
                    EnrollmentManager.viewAllEnrollments();
                    break;

                case 8:
                    System.out.println("\n--- Delete Student Record ---");
                    System.out.print("Enter Student ID to remove: ");
                    int idToDelete = scanner.nextInt();
                    scanner.nextLine();

                    if (StudentManager.deleteStudent(idToDelete)) {
                        System.out.println(" Student removed successfully.");
                    } else {
                        System.out.println(" Student ID not found.");
                    }
                    break;

                case 9:
                    System.out.println("\nShutting down system... Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println(" Option not available.");
            }
        }
        scanner.close();
    }
}