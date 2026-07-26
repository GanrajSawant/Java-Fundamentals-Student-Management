import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentManager {

    // Method to add a new student
    public static boolean addStudent(String firstName, String lastName, String email) {
        String sql = "INSERT INTO Students (first_name, last_name, email) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(" Error adding student: " + e.getMessage());
            return false;
        }
    }

    // Method to view all registered students
    public static void viewAllStudents() {
        String sql = "SELECT * FROM Students";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n--- Registered Students ---");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("ID: %d | Name: %s %s | Email: %s | Enrolled: %s\n",
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getDate("enrollment_date"));
            }
            if (!hasData) System.out.println("No students found in the system.");

        } catch (SQLException e) {
            System.out.println(" Error fetching students: " + e.getMessage());
        }
    }

    // Method to delete a student by their ID
    public static boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM Students WHERE student_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(" Error deleting student: " + e.getMessage());
            return false;
        }
    }
}