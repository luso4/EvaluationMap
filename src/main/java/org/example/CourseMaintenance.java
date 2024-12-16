package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class CourseMaintenance extends JFrame {
    private JPanel panel1;
    private JComboBox<String> emailComboBox;
    private JButton UserManagement; //Button to redirect to Manager new user
    private JButton Calendar; // Button to redirect to Calendar
    private JButton Exit; // Sign Off Button
    private JButton addButton; // Add Button
    private JButton removeButton; // Remove Button
    private JLabel emailLabel; // Email Label
    public User user;

    public CourseMaintenance(User user) {
        this.user = user;

        setTitle("Course Maintenance");
        setSize(400, 400);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        panel1 = new JPanel(new GridBagLayout());
        UserManagement = new JButton("User Management");
        Calendar = new JButton("Calendar");
        Exit = new JButton("Sign Off");
        addButton = new JButton("Add Course");
        removeButton = new JButton("Remove Course");
        emailLabel = new JLabel("Email:");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(new JLabel("Select the email of the user " ), gbc); // Example of using user data


        gbc.gridy = 1;
        // Create and populate the JComboBox with emails from the database
        emailComboBox = new JComboBox<>();
        populateEmailComboBox();
        gbc.gridx = 0;
        panel1.add(emailComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel1.add(addButton, gbc);

        gbc.gridx = 1;
        panel1.add(removeButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel1.add(Exit, gbc);

        setContentPane(panel1);
        setMinimumSize(new Dimension(400, 300));

        // Action listener for UserManagement button
        UserManagement.addActionListener(e -> {
            new UserManagement(user);
            dispose();
        });

        // Action listener for Calendar button
        Calendar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Calendar clicked");
        });

        // Add where the Exit button is clicked
        Exit.addActionListener(e -> {
            new Options(user);
            dispose();
        });

        // Action listener for Add button
        addButton.addActionListener(e -> {

                // Create a panel for the input and checkbox
                JPanel inputPanel = new JPanel();
                inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

                // Add the course name label and input field to the panel
                inputPanel.add(new JLabel("Course:"));
                JTextField courseField = new JTextField();
                inputPanel.add(courseField);

                // Add the "Mixed" checkbox to the panel
                JCheckBox mixedCheckBox = new JCheckBox("Mixed");
                inputPanel.add(mixedCheckBox);

                // Show a custom dialog with the input panel and the checkbox
                int result = JOptionPane.showConfirmDialog(panel1, inputPanel, "Add New Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    String courseName = courseField.getText();
                    boolean isMixed = mixedCheckBox.isSelected();
                    String selectedEmail = (String) emailComboBox.getSelectedItem();
                    if (!courseName.isEmpty()) {
                        addCourseToUser(selectedEmail, courseName, isMixed);
                        emailComboBox.addItem(courseName);
                    }
                }

        });

        // Action listener for Remove button
        removeButton.addActionListener(e -> {
            String selectedEmail = (String) emailComboBox.getSelectedItem();
            if (selectedEmail != null) {
                // Fetch the courses associated with the selected email directly from the database
                ArrayList<String> courses = new ArrayList<>();
                String sql = "SELECT course_course FROM course WHERE email_course = ?";

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                     PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, selectedEmail);
                    ResultSet rs = stmt.executeQuery();

                    // Populate the courses list with the results
                    while (rs.next()) {
                        courses.add(rs.getString("course_course"));
                    }

                    // If no courses were found
                    if (courses.isEmpty()) {
                        JOptionPane.showMessageDialog(panel1, "No courses found for this email.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Create a panel with checkboxes for each course
                    JPanel coursePanel = new JPanel();
                    coursePanel.setLayout(new BoxLayout(coursePanel, BoxLayout.Y_AXIS));

                    // Create an array of checkboxes for each course
                    JCheckBox[] checkBoxes = new JCheckBox[courses.size()];
                    for (int i = 0; i < courses.size(); i++) {
                        checkBoxes[i] = new JCheckBox(courses.get(i));
                        coursePanel.add(checkBoxes[i]);
                    }

                    // Show the dialog with the checkboxes
                    int result = JOptionPane.showConfirmDialog(panel1, coursePanel, "Select courses to remove", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    // If the user clicks OK
                    if (result == JOptionPane.OK_OPTION) {
                        // Loop through the checkboxes and remove the selected courses
                        for (int i = 0; i < checkBoxes.length; i++) {
                            if (checkBoxes[i].isSelected()) {
                                String courseName = checkBoxes[i].getText();
                                removeCourseFromDatabase(selectedEmail, courseName); // Remove selected course from the database
                            }
                        }
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel1, "Failed to fetch courses from the database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });




        // Set the window visible
        setVisible(true);
    }

    public String DB_URL = "jdbc:mariadb://192.168.43.151:3306/evaluationmap";
    public String DB_USER = "root";
    public String DB_PASS = "";
    // Method to populate the JComboBox with courses from the database
    private void populateEmailComboBox() {

        String sql = "SELECT email FROM users";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ArrayList<String> emails = new ArrayList<>();
            while (rs.next()) {
                emails.add(rs.getString("email"));
            }

            for (String email : emails) {
                emailComboBox.addItem(email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to fetch emails from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add a new email to the database
    private void addCourseToUser(String email, String course, boolean mixed) {

        String sql = "INSERT INTO course (email_course, course_course, Mixed_course) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, course);
            stmt.setInt(3, mixed ? 1 : 0);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(panel1, "Course added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to add course.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to remove an email from the database
    private void removeCourseFromDatabase(String email, String courseName) {
        String sql = "DELETE FROM course WHERE email_course = ? AND course_course = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, courseName);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(panel1, "Course removed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to remove course.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


}
