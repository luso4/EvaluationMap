package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class SignInPage extends JDialog {
    private JTextField emailField;
    private JPanel panel1;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private JButton signInButton;
    private JButton homePageButton;
    private JCheckBox directorCheckBox;
    private JTextField courseField;
    private JCheckBox mixedCheckBox;
    public User user;

    private int yPosition = 0; // Position of y in the form

    public SignInPage(User user) {
        this.user = user;

        setTitle("Sign Up");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);



        // Initialize components
        panel1 = new JPanel(new GridBagLayout());
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        usernameField = new JTextField(20);
        directorCheckBox = new JCheckBox("Director");
        courseField = new JTextField(20);
        mixedCheckBox = new JCheckBox("Mixed");
        signInButton = new JButton("Sign Up");
        homePageButton = new JButton("User Management");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = yPosition;
        panel1.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        panel1.add(emailField, gbc);

        yPosition++;

        gbc.gridx = 0;
        gbc.gridy = yPosition;
        panel1.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        panel1.add(passwordField, gbc);

        yPosition++;

        gbc.gridx = 0;
        gbc.gridy = yPosition;
        panel1.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        panel1.add(usernameField, gbc);

        yPosition++;

        gbc.gridx = 0;
        gbc.gridy = yPosition;
        panel1.add(directorCheckBox, gbc);

        yPosition++;

        // Add "Course" label and field
        gbc.gridx = 0;
        gbc.gridy = yPosition;
        panel1.add(new JLabel("Course:"), gbc);

        gbc.gridx = 1;
        panel1.add(courseField, gbc);

        yPosition++;

        // Add "Mixed" checkbox
        gbc.gridx = 0;
        gbc.gridy = yPosition;
        panel1.add(mixedCheckBox, gbc);

        yPosition++;

        gbc.gridx = 0;
        gbc.gridy = yPosition;
        panel1.add(signInButton, gbc);

        gbc.gridx = 1;
        panel1.add(homePageButton, gbc);

        // Action of the Sign in Button
        signInButton.addActionListener(e -> handleSignUp());

        homePageButton.addActionListener(e -> {
            new UserManagement(user);
            dispose();
        });

        add(panel1);
        setVisible(true);
    }



    private void handleSignUp() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String username = usernameField.getText().trim();
        boolean isDirector = directorCheckBox.isSelected();
        String course = courseField.getText().trim();
        boolean isMixed = !mixedCheckBox.isSelected();

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields to sign up.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (createUser(email, password, username, isDirector, course, isMixed)) {
            JOptionPane.showMessageDialog(this,
                    "Account successfully created!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Go to the Option Page

            new Options(user);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to create account. The email may already exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean createUser(String email, String password, String username, boolean isDirector, String course, boolean isMixed) {
        final String DB_URL = "jdbc:mariadb://192.168.1.248:3306/evaluationmap";
        final String DB_USER = "root";
        final String DB_PASS = "";

        String sql = "INSERT INTO users (email, password, name, director) VALUES (?, ?, ?, ?)";
        String sqlCourse = "INSERT INTO course (email_course, course_course, Mixed_course) VALUES (?, ?, ?)";

        Connection conn = null;

        // Start connection and transaction handling
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // Disable auto-commit to start a transaction
            conn.setAutoCommit(false);

            // Prepare the first SQL statement (for users)
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                preparedStatement.setString(3, username);
                preparedStatement.setInt(4, isDirector ? 1 : 0);  // Set director field as 1 if true, else 0

                preparedStatement.executeUpdate();
            }

            // Prepare the second SQL statement (for courses)
            try (PreparedStatement preparedStatementCourse = conn.prepareStatement(sqlCourse)) {
                preparedStatementCourse.setString(1, email);
                preparedStatementCourse.setString(2, course);
                preparedStatementCourse.setInt(3, isMixed ? 1 : 0);

                preparedStatementCourse.executeUpdate();
            }

            // Commit the transaction if both statements succeed
            conn.commit();

            return true;
        } catch (SQLException e) {
            try {
                // Rollback the transaction in case of an error
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace(); // Handle rollback failure
            }

            if (e.getSQLState().equals("23000")) {
                JOptionPane.showMessageDialog(this,
                        "The email is already registered.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "An unexpected error occurred.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
            return false;
        } finally {
            try {
                // Restore auto-commit after transaction handling
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
