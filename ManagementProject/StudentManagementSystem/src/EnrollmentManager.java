import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnrollmentManager {

    // Link a student to a course
    public static boolean enrollStudent(int studentId, int courseId) {
        String sql = "INSERT INTO Enrollments (student_id, course_id) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            pstmt.setInt(2, courseId);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error during enrollment: " + e.getMessage());
            return false;
        }
    }

    // Assign a grade to an enrollment record
    public static boolean assignGrade(int studentId, int courseId, String grade) {
        String sql = "UPDATE Enrollments SET grade = ? WHERE student_id = ? AND course_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, grade);
            pstmt.setInt(2, studentId);
            pstmt.setInt(3, courseId);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error assigning grade: " + e.getMessage());
            return false;
        }
    }

    // View which student is enrolled in what course, joining tables together
    public static void viewAllEnrollments() {
        String sql = "SELECT e.student_id, s.first_name, s.last_name, c.course_name, c.course_code, e.grade " +
                "FROM Enrollments e " +
                "JOIN Students s ON e.student_id = s.student_id " +
                "JOIN Courses c ON e.course_id = c.course_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n--- Course Enrollments & Grades ---");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                String grade = rs.getString("grade");
                if (grade == null) grade = "Not Assigned Yet";

                System.out.printf("StudentID: %d | Student: %s %s | Course: %s (%s) | Grade: %s\n",
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("course_name"),
                        rs.getString("course_code"),
                        grade);
            }
            if (!hasData) System.out.println("No enrollment entries found.");

        } catch (SQLException e) {
            System.out.println("❌ Error fetching enrollments: " + e.getMessage());
        }
    }
}