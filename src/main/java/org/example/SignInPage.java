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
    public User user;

    public SignInPage(User user) {
        this.user = user;

        setTitle("Sign Up");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Initialize components
        panel1 = new JPanel(new GridBagLayout());
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        usernameField = new JTextField(20);
        signInButton = new JButton("Sign Up");
        homePageButton = new JButton("Home Page");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        panel1.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel1.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        panel1.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel1.add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        panel1.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel1.add(signInButton, gbc);

        gbc.gridx = 1;
        panel1.add(homePageButton, gbc);

        // Action of the Sign in Button
        signInButton.addActionListener(e -> handleSignUp());

        homePageButton.addActionListener(e -> {
            new Options(user);
            dispose();
        });

        add(panel1);
        setVisible(true);
    }

    private void handleSignUp() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String username = usernameField.getText().trim();

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill all fields to sign up.",
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (createUser(email, password, username)) {
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

    private boolean createUser(String email, String password, String username) {
        final String DB_URL = "jdbc:mariadb://192.168.43.151:3306/evaluationmap";
        final String DB_USER = "root";
        final String DB_PASS = "";

        String sql = "INSERT INTO users (email, password, name) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, username);

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
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
        }
    }
}
