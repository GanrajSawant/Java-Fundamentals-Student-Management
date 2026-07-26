import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StudentDashboard extends JFrame {

    // Student Elements
    private JTable studentTable;
    private DefaultTableModel studentModel;
    private JTextField fNameField, lNameField, emailField;
    private JTextField studentSearchField;
    private int selectedStudentId = -1;

    // Course Elements
    private JTable courseTable;
    private DefaultTableModel courseModel;
    private JTextField courseNameField, courseCodeField, creditsField;
    private int selectedCourseId = -1;

    public StudentDashboard() {
        setTitle("University Admin System");
        setSize(980, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Students Directory", createStudentPanel());
        tabbedPane.addTab("Course Catalog", createCoursePanel());
        add(tabbedPane);

        loadStudentData();
        loadCourseData();
    }

    // --- Tab 1: Student Management Screen ---
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Search Bar Panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        searchPanel.add(new JLabel("🔍 Quick Search (Type Name, ID, or Email): "), BorderLayout.WEST);
        studentSearchField = new JTextField();
        searchPanel.add(studentSearchField, BorderLayout.CENTER);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Table Setup
        String[] columnNames = {"Student ID", "First Name", "Last Name", "Email Address", "Enrollment Date"};
        studentModel = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(studentModel);
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // Row Sorter Filtering Logic
        TableRowSorter<DefaultTableModel> rowSorter = new TableRowSorter<>(studentModel);
        studentTable.setRowSorter(rowSorter);

        studentSearchField.getDocument().addDocumentListener(new DocumentListener() {
            private void searchFilter() {
                String text = studentSearchField.getText().trim();
                if (text.isEmpty()) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
            @Override public void insertUpdate(DocumentEvent e) { searchFilter(); }
            @Override public void removeUpdate(DocumentEvent e) { searchFilter(); }
            @Override public void changedUpdate(DocumentEvent e) { searchFilter(); }
        });

        // Right Sidebar Options
        JPanel sideActions = new JPanel(new GridLayout(2, 1, 10, 10));
        sideActions.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton deleteButton = new JButton(" Delete Selected Student");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setForeground(Color.WHITE);

        JButton resetStudentsButton = new JButton(" Reset Students Only");
        resetStudentsButton.setBackground(new Color(108, 117, 125));
        resetStudentsButton.setForeground(Color.WHITE);

        sideActions.add(deleteButton);
        sideActions.add(resetStudentsButton);
        panel.add(sideActions, BorderLayout.EAST);

        // Master Footer Panel Container
        JPanel bottomContainer = new JPanel(new BorderLayout(10, 10));
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 🛠️ COMPACT GRID ALIGNMENT: Solves horizontal stretching by putting fields on row 1
        JPanel fieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        fNameField = new JTextField(15);
        lNameField = new JTextField(15);
        emailField = new JTextField(22);

        fieldsPanel.add(new JLabel("First Name:"));
        fieldsPanel.add(fNameField);
        fieldsPanel.add(Box.createHorizontalStrut(10)); // Added spacing buffer
        fieldsPanel.add(new JLabel("Last Name:"));
        fieldsPanel.add(lNameField);
        fieldsPanel.add(Box.createHorizontalStrut(10));
        fieldsPanel.add(new JLabel("Email:"));
        fieldsPanel.add(emailField);

        // 🛠️ SEPARATED BUTTONS: Explicit action buttons sitting cleanly on row 2
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        JButton addButton = new JButton(" Add Student");
        JButton updateButton = new JButton(" Update Selected");
        updateButton.setBackground(new Color(40, 167, 69));
        updateButton.setForeground(Color.WHITE);
        JButton refreshButton = new JButton(" Clear / Refresh");

        buttonsPanel.add(addButton);
        buttonsPanel.add(updateButton);
        buttonsPanel.add(refreshButton);

        // Stack them vertically inside the bottom container
        bottomContainer.add(fieldsPanel, BorderLayout.NORTH);
        bottomContainer.add(buttonsPanel, BorderLayout.SOUTH);
        panel.add(bottomContainer, BorderLayout.SOUTH);

        // Student Selection Row Event Handler
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    int modelRow = studentTable.convertRowIndexToModel(selectedRow);
                    selectedStudentId = (int) studentModel.getValueAt(modelRow, 0);
                    fNameField.setText((String) studentModel.getValueAt(modelRow, 1));
                    lNameField.setText((String) studentModel.getValueAt(modelRow, 2));
                    emailField.setText((String) studentModel.getValueAt(modelRow, 3));
                }
            }
        });

        // Click Action: Add Student
        addButton.addActionListener(e -> {
            String fName = fNameField.getText().trim();
            String lName = lNameField.getText().trim();
            String email = emailField.getText().trim();

            if (!InputValidator.isValidName(fName) || !InputValidator.isValidName(lName) || !InputValidator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Validation Failed! Please verify name patterns and email syntax.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (StudentManager.addStudent(fName, lName, email)) {
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                clearStudentForm();
                loadStudentData();
            }
        });

        // Click Action: Update Student
        updateButton.addActionListener(e -> {
            if (selectedStudentId == -1) {
                JOptionPane.showMessageDialog(this, "Please select a student row from the table view first!", "No Row Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String fName = fNameField.getText().trim();
            String lName = lNameField.getText().trim();
            String email = emailField.getText().trim();

            if (!InputValidator.isValidName(fName) || !InputValidator.isValidName(lName) || !InputValidator.isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Validation Failed! Entries cannot be empty or improperly structured.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (updateStudentDatabase(selectedStudentId, fName, lName, email)) {
                JOptionPane.showMessageDialog(this, "Student records modified successfully!");
                clearStudentForm();
                loadStudentData();
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = studentTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please click on a student row first!", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int modelRow = studentTable.convertRowIndexToModel(selectedRow);
            int studentId = (int) studentModel.getValueAt(modelRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete Student ID " + studentId + "?", "Confirm Drop", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (StudentManager.deleteStudent(studentId)) {
                    JOptionPane.showMessageDialog(this, "Student removed successfully.");
                    clearStudentForm();
                    loadStudentData();
                }
            }
        });

        resetStudentsButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    " Warning:\nThis will clear ALL students and enrollment records, setting IDs back to 1.\nYour Courses will NOT be deleted. Proceed?",
                    "Reset Student Directory", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                if (executeStudentOnlyReset()) {
                    JOptionPane.showMessageDialog(this, "Student directory wiped successfully!");
                    clearStudentForm();
                    loadStudentData();
                    loadCourseData();
                }
            }
        });

        refreshButton.addActionListener(e -> {
            clearStudentForm();
            loadStudentData();
        });

        return panel;
    }

    // --- Tab 2: Course Management Screen ---
    private JPanel createCoursePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        courseModel = new DefaultTableModel(new String[]{"Course ID", "Course Title", "Course Code", "Credits"}, 0);
        courseTable = new JTable(courseModel);
        panel.add(new JScrollPane(courseTable), BorderLayout.CENTER);

        JPanel bottomContainer = new JPanel(new BorderLayout(10, 10));
        bottomContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Course Input Layout Row
        JPanel fieldsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        courseNameField = new JTextField(18);
        courseCodeField = new JTextField(10);
        creditsField = new JTextField(6);

        fieldsPanel.add(new JLabel("Course Title:"));
        fieldsPanel.add(courseNameField);
        fieldsPanel.add(Box.createHorizontalStrut(10));
        fieldsPanel.add(new JLabel("Course Code:"));
        fieldsPanel.add(courseCodeField);
        fieldsPanel.add(Box.createHorizontalStrut(10));
        fieldsPanel.add(new JLabel("Credits (1-6):"));
        fieldsPanel.add(creditsField);

        // Course Action Buttons Row
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        JButton addCourseBtn = new JButton(" Add Course");
        JButton updateCourseBtn = new JButton(" Update Selected");
        updateCourseBtn.setBackground(new Color(40, 167, 69));
        updateCourseBtn.setForeground(Color.WHITE);
        JButton refreshCourseBtn = new JButton(" Clear / Refresh");

        buttonsPanel.add(addCourseBtn);
        buttonsPanel.add(updateCourseBtn);
        buttonsPanel.add(refreshCourseBtn);

        bottomContainer.add(fieldsPanel, BorderLayout.NORTH);
        bottomContainer.add(buttonsPanel, BorderLayout.SOUTH);
        panel.add(bottomContainer, BorderLayout.SOUTH);

        // Course Selection Click Listener
        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = courseTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedCourseId = (int) courseModel.getValueAt(selectedRow, 0);
                    courseNameField.setText((String) courseModel.getValueAt(selectedRow, 1));
                    courseCodeField.setText((String) courseModel.getValueAt(selectedRow, 2));
                    creditsField.setText(String.valueOf(courseModel.getValueAt(selectedRow, 3)));
                }
            }
        });

        addCourseBtn.addActionListener(e -> {
            String title = courseNameField.getText().trim();
            String code = courseCodeField.getText().trim();
            int credits;
            try { credits = Integer.parseInt(creditsField.getText().trim()); } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits must be a valid number."); return;
            }
            if (!InputValidator.isValidName(title) || !InputValidator.isValidCourseCode(code) || !InputValidator.isValidCredits(credits)) {
                JOptionPane.showMessageDialog(this, "Validation Failed! Verify course data formatting rules.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (CourseManager.addCourse(title, code, credits)) {
                JOptionPane.showMessageDialog(this, "Course catalog updated successfully!");
                clearCourseForm();
                loadCourseData();
            }
        });

        updateCourseBtn.addActionListener(e -> {
            if (selectedCourseId == -1) {
                JOptionPane.showMessageDialog(this, "Please click an active course entry inside the list catalog first!", "No Selection Found", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String title = courseNameField.getText().trim();
            String code = courseCodeField.getText().trim();
            int credits;
            try { credits = Integer.parseInt(creditsField.getText().trim()); } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits must be a valid integer number."); return;
            }
            if (!InputValidator.isValidName(title) || !InputValidator.isValidCourseCode(code) || !InputValidator.isValidCredits(credits)) {
                JOptionPane.showMessageDialog(this, "Validation Failed! Please check entry rules.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (updateCourseDatabase(selectedCourseId, title, code, credits)) {
                JOptionPane.showMessageDialog(this, "Course criteria updated successfully!");
                clearCourseForm();
                loadCourseData();
            }
        });

        refreshCourseBtn.addActionListener(e -> {
            clearCourseForm();
            loadCourseData();
        });

        return panel;
    }

    private void clearStudentForm() {
        fNameField.setText(""); lNameField.setText(""); emailField.setText("");
        if (studentSearchField != null) studentSearchField.setText("");
        selectedStudentId = -1;
    }

    private void clearCourseForm() {
        courseNameField.setText(""); courseCodeField.setText(""); creditsField.setText("");
        selectedCourseId = -1;
    }

    private boolean updateStudentDatabase(int id, String firstName, String lastName, String email) {
        String query = "UPDATE Students SET first_name = ?, last_name = ?, email = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setInt(4, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private boolean updateCourseDatabase(int id, String title, String code, int credits) {
        String query = "UPDATE Courses SET course_name = ?, course_code = ?, credits = ? WHERE course_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, code);
            pstmt.setInt(3, credits);
            pstmt.setInt(4, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    private boolean executeStudentOnlyReset() {
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 0");
            stmt.executeUpdate("TRUNCATE TABLE Enrollments");
            stmt.executeUpdate("TRUNCATE TABLE Students");
            stmt.executeUpdate("SET FOREIGN_KEY_CHECKS = 1");
            return true;
        } catch (SQLException ex) { ex.printStackTrace(); return false; }
    }

    private void loadStudentData() {
        studentModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Students");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                studentModel.addRow(new Object[]{rs.getInt("student_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("email"), rs.getDate("enrollment_date")});
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadCourseData() {
        courseModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Courses");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                courseModel.addRow(new Object[]{rs.getInt("course_id"), rs.getString("course_name"), rs.getString("course_code"), rs.getInt("credits")});
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDashboard().setVisible(true));
    }
}