package org.example;

import javax.swing.*;
import java.awt.*;

public class Options extends JFrame {
    private JPanel panel1;
    private JButton UserManagement; //Button to redirect to Manager new user
    private JButton Calendar; // Button to redirect to Calendar
    private JButton Exit; // Sign Off Button
    public User user;

    public Options(User user) {
        this.user = user;  // Store the user object for future use

        setTitle("Options");
        setSize(400, 300);
        setLocationRelativeTo(null);


        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Initialize the panel and buttons
        panel1 = new JPanel(new GridBagLayout());
        UserManagement = new JButton("User Management");
        Calendar = new JButton("Calendar");
        Exit = new JButton("Sign Off");

        // Add components to the panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Display the user's name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(new JLabel("Welcome, " + user.getName() + "!"), gbc); // Example of using user data

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel1.add(UserManagement, gbc);

        gbc.gridy = 2;
        panel1.add(Calendar, gbc);

        gbc.gridy = 3;
        panel1.add(Exit, gbc);

        // Set the content pane
        setContentPane(panel1);
        setMinimumSize(new Dimension(400, 200));

        // Action listener for UserManagement button
        UserManagement.addActionListener(e -> {
            // Handle Option 1 action (e.g., show some action related to Option 1)
            JOptionPane.showMessageDialog(this, "Option 1 clicked");
        });

        // Action listener for Option2 button
        Calendar.addActionListener(e -> {
            // Handle Option 1 action (e.g., show some action related to Option 1)
            JOptionPane.showMessageDialog(this, "Option 2 clicked");
        });
        Exit.addActionListener(e -> {
            // Handle Cancel action (e.g., close the Options form)
            dispose();  // Close the Options form when Cancel is clicked
        });

        // Set the window visible
        setVisible(true);
    }


}
