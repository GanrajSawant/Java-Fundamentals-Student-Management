import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CourseManager {

    // Method to add a new course offering
    public static boolean addCourse(String courseName, String courseCode, int credits) {
        String sql = "INSERT INTO Courses (course_name, course_code, credits) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, courseName);
            pstmt.setString(2, courseCode);
            pstmt.setInt(3, credits);

            int rows = pstmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("❌ Error adding course: " + e.getMessage());
            return false;
        }
    }

    // Method to view all active courses
    public static void viewAllCourses() {
        String sql = "SELECT * FROM Courses";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("\n--- Available Courses ---");
            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                System.out.printf("ID: %d | Code: %s | Title: %s | Credits: %d\n",
                        rs.getInt("course_id"),
                        rs.getString("course_code"),
                        rs.getString("course_name"),
                        rs.getInt("credits"));
            }
            if (!hasData) System.out.println("No courses available yet.");

        } catch (SQLException e) {
            System.out.println("❌ Error fetching courses: " + e.getMessage());
        }
    }
}