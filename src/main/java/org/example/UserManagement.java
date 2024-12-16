package org.example;

import javax.swing.*;
import java.awt.*;

public class UserManagement extends JFrame {
    private JPanel panel1;
    private JButton SignIn; //Button to redirect to Manager new user
    private JButton Course; // Button to redirect to Course
    private JButton Exit; // Sign Off Button
    public User user;

    public UserManagement(User user) {
        this.user = user;  // Store the user object for future use

        setTitle("User Management");
        setSize(400, 300);
        setLocationRelativeTo(null);


        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        panel1 = new JPanel(new GridBagLayout());
        SignIn = new JButton("Sign In");
        Course = new JButton("Associate course");
        Exit = new JButton("Return to Options");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(new JLabel("User Management"), gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        panel1.add(SignIn, gbc);


        gbc.gridy = 2;
        panel1.add(Course, gbc);

        gbc.gridy = 3;
        panel1.add(Exit, gbc);

        // Set the content pane
        setContentPane(panel1);
        setMinimumSize(new Dimension(400, 200));

        // Action listener for UserManagement button
        SignIn.addActionListener(e -> {
            new SignInPage(user);
            dispose();
        });

        // Action listener for Course Maintenance button
        Course.addActionListener(e -> {
           new CourseMaintenance(user);
           dispose();
        });

        Exit.addActionListener(e -> {
            dispose();
        });

        setVisible(true);
    }


}
