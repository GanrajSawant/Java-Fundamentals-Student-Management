import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("HH:mm:ss");

        LocalTime alarmTime = null;
        String filePath = "alarm.wav";

        System.out.println("Current Time: " + LocalTime.now().withNano(0));

        while (alarmTime == null) {
            try {
                System.out.print("Enter an alarm time (HH:mm:ss): ");
                String inputTime = scanner.nextLine();

                alarmTime = LocalTime.parse(inputTime, formatter);

                System.out.println("Alarm set for: " + alarmTime);

            } catch (DateTimeParseException e) {
                System.out.println("Invalid format! Please use HH:mm:ss");
            }
        }

        AlarmClock alarmClock =
                new AlarmClock(alarmTime, filePath);

        Thread alarmThread = new Thread(alarmClock);
        alarmThread.start();
    }
}