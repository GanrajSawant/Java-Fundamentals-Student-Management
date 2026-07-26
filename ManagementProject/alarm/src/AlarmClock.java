import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Scanner;

public class AlarmClock implements Runnable {

    private final LocalTime alarmTime;
    private final String filePath;

    public AlarmClock(LocalTime alarmTime, String filePath) {
        this.alarmTime = alarmTime;
        this.filePath = filePath;
    }

    @Override
    public void run() {

        while (LocalTime.now().isBefore(alarmTime)) {

            LocalTime now = LocalTime.now();

            System.out.printf("\rCurrent Time: %02d:%02d:%02d",
                    now.getHour(),
                    now.getMinute(),
                    now.getSecond());

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Thread Interrupted");
            }
        }

        System.out.println("\n\n*** ALARM RINGING ***");

        playSound(filePath);
    }

    private void playSound(String filePath) {

        File audioFile = new File(filePath);

        if (!audioFile.exists()) {
            System.out.println("Alarm file not found!");
            System.out.println(audioFile.getAbsolutePath());
            return;
        }

        try (AudioInputStream audioStream =
                     AudioSystem.getAudioInputStream(audioFile)) {

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            // Play continuously
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();

            System.out.println("Press Enter to stop the alarm...");

            // Separate scanner so previous Enter key isn't consumed
            Scanner stopScanner = new Scanner(System.in);
            stopScanner.nextLine();

            clip.stop();
            clip.close();

            System.out.println("Alarm stopped.");

        } catch (UnsupportedAudioFileException e) {
            System.out.println("Unsupported audio file.");
        } catch (LineUnavailableException e) {
            System.out.println("Audio line unavailable.");
        } catch (IOException e) {
            System.out.println("Error reading audio file.");
        }
    }
}
