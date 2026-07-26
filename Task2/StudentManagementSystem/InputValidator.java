import java.util.regex.Pattern;

public class InputValidator {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println(" Validation Error: Name field cannot be empty.");
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        if (email == null || !emailPattern.matcher(email).matches()) {
            System.out.println(" Validation Error: Invalid email format.");
            return false;
        }
        return true;
    }

    public static boolean isValidCourseCode(String code) {
        if (code == null || code.trim().isEmpty() || code.length() < 3) {
            System.out.println(" Validation Error: Course code must be at least 3 characters.");
            return false;
        }
        return true;
    }

    public static boolean isValidCredits(int credits) {
        if (credits <= 0 || credits > 6) {
            System.out.println(" Validation Error: Credits must be between 1 and 6.");
            return false;
        }
        return true;
    }

    public static boolean isValidGrade(String grade) {
        if (grade == null) return false;
        String g = grade.toUpperCase().trim();
        return g.equals("A") || g.equals("B") || g.equals("C") || g.equals("D") || g.equals("F");
    }
}
