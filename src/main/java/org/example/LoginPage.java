package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginPage extends JDialog {
    private JTextField userField;
    private JPanel panel1;
    private JPasswordField passwordField;
    private JButton logginButton;
    private JButton cancelButton;
    public User user;
    //JSL
    public LoginPage(JFrame parent) {
        super(parent);
        setTitle("Login");

        // Initialize components
        panel1 = new JPanel(new GridBagLayout());
        userField = new JTextField(20);
        passwordField = new JPasswordField(20);
        logginButton = new JButton("Login");
        cancelButton = new JButton("Cancel");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        panel1.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel1.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        panel1.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel1.add(logginButton, gbc);
        gbc.gridx = 1;
        panel1.add(cancelButton, gbc);

        setContentPane(panel1);
        setMinimumSize(new Dimension(400, 200));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Log in button action
        logginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = userField.getText();
                String password = new String(passwordField.getPassword());

                if (email.isEmpty() || password.isEmpty()) {
                    showErrorMessage("Please enter both email and password."); //JSL 07-01-2025
                    return;
                }

                user = getAuthenticatedUser(email, password);

                if (user != null) {
                    // Here call the HomePage/Options
                    new Options(user);
                    dispose();
                } else {
                    showErrorMessage("Invalid username or password"); //JSL 07-01-2025
                }
            }
        });

        // Cancel button action
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    // Add a separate method to show error messages. This will allow us to mock it in tests.
    public void showErrorMessage(String message) { //JSL 07-01-2025
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.WARNING_MESSAGE);
    }

    // Get the authenticated user from the database
   public User getAuthenticatedUser(String email, String password) {
        User user = null;


        final String DB_URL = "jdbc:mariadb://192.168.131.151:3306/evaluationmap";
        final String USER = "userSQL";
        final String PASS = "password1";

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setEmail(rs.getString("email"));
                    user.setName(rs.getString("name"));
                    user.setDirector(rs.getInt("director"));
                    user.setDepartment(rs.getString("department"));
                    user.setAdmin(rs.getInt("admin"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorMessage("Database connection error. Please try again."); //JSL 07-01-2025
        }

        return user;
    }

    // Main method to run the login page
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LoginPage loginPage = new LoginPage(null);
                User user = loginPage.user;
                if (user != null) {
                    System.out.println("Successfully logged in as: " + user.getEmail());
                } else {
                    System.out.println("Failed to log in.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}