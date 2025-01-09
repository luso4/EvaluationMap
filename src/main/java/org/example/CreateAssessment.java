package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class CreateAssessment extends JFrame {
    private JPanel panel1;
    private JComboBox<String> assessmentComboBox;
    private JButton Exit; // Sign Off Button
    private JButton Select;
    public User user;

    public CreateAssessment(User user, String course) {
        this.user = user;

        setTitle("Selection Course");
        setSize(720,480);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        panel1 = new JPanel(new GridBagLayout());
        Exit = new JButton("Calendar Options");
        //Select = new JButton("Select Course");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(new JLabel("Create a new Assessment for the course " + course), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel1.add(new JLabel("Select the type of Assessment "), gbc);

        // Create and populate the JComboBox with Assessments from the database
        assessmentComboBox = new JComboBox<>();
        populateAssessmentComboBox();
        gbc.gridx = 1;
        panel1.add(assessmentComboBox, gbc);

        //Option to define the percentage
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel1.add(new JLabel("Percentage: "), gbc);

        setContentPane(panel1);  // Set the content pane
        setMinimumSize(new Dimension(720,480));

        setVisible(true);
    }

    public String DB_URL = "jdbc:mariadb://192.168.1.248:3306/evaluationmap";
    public String DB_USER = "root";
    public String DB_PASS = "";
    // Method to populate the JComboBox with courses from the database
    public void populateAssessmentComboBox() {
        // SQL query with a placeholder for the email
        String sql = "SELECT assessment_name FROM assessment";

        //CHANGE THIS PART
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             // Using PreparedStatement even though there's no parameter now
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                ArrayList<String> assessments = new ArrayList<>();

                // Loop through the result set and add the courses to the list
                while (rs.next()) {
                    String assessment = rs.getString("assessment_name");
                    assessments.add(assessment);  // Add the course to the list
                }

                // Update the ComboBox on the EDT to ensure UI responsiveness
                SwingUtilities.invokeLater(() -> {
                    for (String assessment : assessments) {
                        assessmentComboBox.addItem(assessment);  // Add assessment to the combo box
                    }
                });

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to fetch assessment from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
