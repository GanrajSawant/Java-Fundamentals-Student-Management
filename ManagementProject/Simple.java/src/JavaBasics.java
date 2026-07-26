import java.util.Scanner;

public class JavaBasics {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter a number: ");
        int num = scanner.nextInt();

        // Condition
        if(num % 2 == 0)
            System.out.println(num + " is Even");
        else
            System.out.println(num + " is Odd");

        // Loop
        System.out.println("\nNumbers from 1 to " + num);

        for(int i = 1; i <= num; i++) {
            System.out.print(i + " ");
        }

        // Array
        int[] marks = {75, 82, 90, 68, 88};

        System.out.println("\n\nStudent Marks:");

        for(int mark : marks) {
            System.out.println(mark);
        }

        scanner.close();
    }
}