package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
//JSL
public class CourseMaintenance extends JFrame {
    private JPanel panel1;
    private JComboBox<String> emailComboBox;
    private JButton Calendar; // Button to redirect to Calendar
    private JButton Exit; // Sign Off Button
    private JButton addButton; // Add Button
    private JButton removeButton; // Remove Button
    private JLabel emailLabel; // Email Label
    private JSpinner yearSpinner;
    private JSpinner numberSpinner;
    public User user;

    public CourseMaintenance(User user) {
        this.user = user;

        setTitle("Course Maintenance");
        setSize(400, 400);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        panel1 = new JPanel(new GridBagLayout());
        Calendar = new JButton("Calendar");
        Exit = new JButton("Options");
        addButton = new JButton("Add Course");
        removeButton = new JButton("Remove Course");
        emailLabel = new JLabel("Email:");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(new JLabel("Select the email of the user "), gbc);


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
            inputPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.anchor = GridBagConstraints.WEST; // Align labels to the left

            // Create the course name label and input field
            gbc2.gridx = 0;
            gbc2.gridy = 0;
            inputPanel.add(new JLabel("Course:"), gbc2);

            gbc2.gridx = 1;
            JTextField courseField = new JTextField(20);
            inputPanel.add(courseField, gbc2);

            // Create the "Mixed" checkbox
            gbc2.gridx = 0;
            gbc2.gridy = 1;
            inputPanel.add(new JLabel("Mixed:"), gbc2);

            gbc2.gridx = 1;
            JCheckBox mixedCheckBox = new JCheckBox();
            inputPanel.add(mixedCheckBox, gbc2);

            // Create the Year label and spinner
            gbc2.gridx = 0;
            gbc2.gridy = 2;
            inputPanel.add(new JLabel("Year:"), gbc2);


            gbc2.gridx = 1;
            SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 4, 1);
            JSpinner yearSpinner = new JSpinner(model);
            inputPanel.add(yearSpinner, gbc2);

            gbc2.gridx = 0;
            gbc2.gridy = 3;
            inputPanel.add(new JLabel("NumberOfStudents:"), gbc2);
            gbc2.gridx = 1;
            SpinnerNumberModel Stmodel = new SpinnerNumberModel(1, 1, 400, 1);
            JSpinner numberSpinner = new JSpinner(Stmodel);
            inputPanel.add(numberSpinner, gbc2);



            // Show a custom dialog with the input panel
            int result = JOptionPane.showConfirmDialog(panel1, inputPanel, "Add New Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String courseName = courseField.getText();
                boolean isMixed = mixedCheckBox.isSelected();
                String selectedEmail = (String) emailComboBox.getSelectedItem();
                int year = (Integer) yearSpinner.getValue();
                String department = user.getDepartment();
                int numberStudent = (Integer) numberSpinner.getValue();
                if (!courseName.isEmpty()) {
                    addCourseToUser(selectedEmail, courseName, isMixed, year, department, numberStudent);
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

    public String DB_URL = "jdbc:mariadb://192.168.21.151:3306/evaluationmap";
    public String DB_USER = "userSQL";
    public String DB_PASS = "password1";
    // Method to populate the JComboBox with emails from the database
    public void populateEmailComboBox() {
        String sql = "Select email from users";
        if (user.getDirector() == 1)
        {
            String department = user.getDepartment();
            sql = "Select email from users where department = '" + department + "'";
        }
        else
        {
            String email = user.getEmail();
            sql = "SELECT email FROM users where email = '" + email + "'";
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            ArrayList<String> emails = new ArrayList<>();

            // Loop through the result set and add the emails to the list
            while (rs.next()) {
                String email = rs.getString("email");
                emails.add(email);  // Add email to the list
            }

            // Debugging: Print the emails to verify they're being fetched
            System.out.println("Emails fetched: " + emails);

            // Update the ComboBox on the EDT to ensure UI responsiveness
            SwingUtilities.invokeLater(() -> {
                for (String email : emails) {
                    emailComboBox.addItem(email);  // Add email to the combo box
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to fetch emails from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Method to add a new email to the database
    public void addCourseToUser(String email, String course, boolean mixed, int year, String department, int number) {

        String sql = "INSERT INTO course (email_course, course_course, Mixed_course, course_year, department_course, course_number_assessment, number_student_course) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, course);
            stmt.setInt(3, mixed ? 1 : 0);
            stmt.setInt(4, year);
            stmt.setString(5, department);
            stmt.setInt(6, 0);
            stmt.setInt(7, number);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(panel1, "Course added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to add course.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to remove an email from the database
    public void removeCourseFromDatabase(String email, String courseName) {
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
