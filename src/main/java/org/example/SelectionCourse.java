package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class SelectionCourse extends JFrame {
    private JPanel panel1;
    private JComboBox<String> courseComboBox;
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
            // Get the selected course from the JComboBox
            String selectedCourse = (String) courseComboBox.getSelectedItem();

            if (selectedCourse != null) {
                // Pass the user object and the selected course to the CreateAssessment constructor
                new CreateAssessment(user, selectedCourse);  // Assuming CreateAssessment has a constructor that accepts User and String
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

    public String DB_URL = "jdbc:mariadb://192.168.1.248:3306/evaluationmap";
    public String DB_USER = "root";
    public String DB_PASS = "";
    // Method to populate the JComboBox with courses from the database
    public void populateCourseComboBox() {
        // SQL query with a placeholder for the email
        String sql = "SELECT course_course, Mixed_course FROM course WHERE email_course = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             // Use PreparedStatement to safely insert the email
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the email dynamically from the User object
            pstmt.setString(1, user.getEmail());  // user.getEmail() gets the email of the user

            try (ResultSet rs = pstmt.executeQuery()) {
                ArrayList<String> courses = new ArrayList<>();

                // Loop through the result set and add the courses to the list
                while (rs.next()) {
                    String course = rs.getString("course_course");
                    courses.add(course);  // Add the course to the list
                }

                // Update the ComboBox on the EDT to ensure UI responsiveness
                SwingUtilities.invokeLater(() -> {
                    for (String course : courses) {
                        courseComboBox.addItem(course);  // Add email to the combo box
                    }
                });

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to fetch courses from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
