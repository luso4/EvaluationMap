package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class SelectionCourse extends JFrame {
    private JPanel panel1;
    private JComboBox<Course> courseComboBox;
    private JButton Exit; // Sign Off Button
    private JButton Select;
    public User user;

    public SelectionCourse(User user) {
        this.user = user;

        setTitle("Selection Course");
        setSize(400, 400);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        panel1 = new JPanel(new GridBagLayout());
        Exit = new JButton("Calendar Options");
        Select = new JButton("Select Course");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(new JLabel("Select the course of the user " + user.getName()), gbc);

        gbc.gridy = 1;
        // Create and populate the JComboBox with courses from the database of the user
        courseComboBox = new JComboBox<>();
        populateCourseComboBox();
        gbc.gridx = 0;
        panel1.add(courseComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel1.add(Select, gbc);

        gbc.gridx = 1;
        panel1.add(Exit, gbc);

        setContentPane(panel1);
        setMinimumSize(new Dimension(400, 300));

        Select.addActionListener(e -> {
            // Get the selected Course object from the JComboBox
            Course selectedCourse = (Course) courseComboBox.getSelectedItem();

            if (selectedCourse != null) {
                // Pass the user object and the selected Course to the CreateAssessment constructor
                new AssessmentManagement(user, selectedCourse);  // Assuming CreateAssessment has a constructor that accepts User and Course
                dispose();  // Close the current window
            } else {
                JOptionPane.showMessageDialog(panel1, "Please select a course first.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add where the Exit button is clicked
        Exit.addActionListener(e -> {
            new CalendarOptions(user);
            dispose();
        });


        setVisible(true);
    }

    public String DB_URL = "jdbc:mariadb://192.168.131.151:3306/evaluationmap";
    public String DB_USER = "userSQL";
    public String DB_PASS = "password1";
    // Method to populate the JComboBox with courses from the database
    public void populateCourseComboBox() {
        // SQL query with a placeholder for the email
        String sql = "SELECT course_course, course_number_assessment, number_student_course, mixed_course, assessment_mandatory_number_course, percentage_course  FROM course WHERE email_course = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             // Use PreparedStatement to safely insert the email
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the email dynamically from the User object
            pstmt.setString(1, user.getEmail());  // user.getEmail() gets the email of the user

            try (ResultSet rs = pstmt.executeQuery()) {
                ArrayList<Course> courses = new ArrayList<>();

                // Loop through the result set and create Course objects
                while (rs.next()) {
                    String courseCourse = rs.getString("course_course");
                    int courseAssessmentNr = rs.getInt("course_number_assessment");
                    int studentNrCourse = rs.getInt("number_student_course");
                    int mixedCourse =rs.getInt("mixed_course");
                    int assessmentMandatoryNumberCourse =rs.getInt("assessment_mandatory_number_course");
                    int percentageCourse = rs.getInt("percentage_course");

                    // Create a new Course object and add it to the list
                    Course course = new Course(courseCourse, courseAssessmentNr, studentNrCourse, mixedCourse, assessmentMandatoryNumberCourse, percentageCourse);
                    courses.add(course);
                }

                // Update the ComboBox on the EDT to ensure UI responsiveness
                SwingUtilities.invokeLater(() -> {
                    for (Course course : courses) {
                        // Add the course to the combo box (show course name but store the whole object)
                        courseComboBox.addItem(course);  // Add Course object to combo box
                    }
                });

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to fetch courses from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
