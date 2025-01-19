package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class AssessmentManagement extends JFrame {
    private JPanel panel1;
    private JComboBox<String> assessmentcombobox;  // Assuming this is for assessments related to the course
    private JButton select;
    private JButton exit;
    private JButton create;
    public User user;
    public Course course;

    public AssessmentManagement(User user, Course selectedCourse) {
        this.user = user;
        this.course = selectedCourse;

        // Frame setup
        setTitle("Assessment Management");
        setSize(720, 480);
        setLocationRelativeTo(null);  // Center the window
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Initialize the panel
        panel1 = new JPanel(new GridBagLayout());
        select = new JButton("Select Assessment");
        exit = new JButton("Return to Course Maintenance");
        create = new JButton("Create Assessment");

        // Set up GridBagLayout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        //when there is no assessments
        if (course.getassessmentMandatoryNumberCourse() == 0)
        {
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel1.add(new JLabel("You have no assessments. You must create x assessments."), gbc);
        }
        //when there is some assessments
        else {
                //If it is a continuous course
                if (course.getassessmentMandatoryNumberCourse() < 3 && course.getmixedcourse() == 0) {
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    panel1.add(new JLabel("You have " + course.getassessmentMandatoryNumberCourse() + " mandatory assessments. Please insert " + (3 - course.getassessmentMandatoryNumberCourse()) + " more."), gbc);
                }
                //If it is a mixed course
                //NEED TO ADD A CONDITION FOR EXAMS
                else if (course.getassessmentMandatoryNumberCourse() < 2 && course.getmixedcourse() == 1)
                {
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    panel1.add(new JLabel("You have " + course.getassessmentMandatoryNumberCourse() + " mandatory assessments. Please insert " + (3 - course.getassessmentMandatoryNumberCourse()) + " more."), gbc);
                }
                if(course.getpercentageCourse()<= 100)
                {
                    gbc.gridx = 0;
                    gbc.gridy = 1;
                    panel1.add(new JLabel("You have " + course.getpercentageCourse() + "%. Please insert the remaning " + (100 - course.getpercentageCourse()) + "%."), gbc);
                }
            assessmentcombobox = new JComboBox<>();
            populateAssessmentCombobox();

            gbc.gridx = 0;
            gbc.gridy = 2;
            panel1.add(assessmentcombobox, gbc);
        }

        //Used in the case of there being no or some assessments
        // Add the buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel1.add(select, gbc);

        gbc.gridy = 4;
        panel1.add(create, gbc);

        gbc.gridy = 5;
        panel1.add(exit, gbc);

        // Set the panel as the content pane
        setContentPane(panel1);

        // Ensure the window is resizable and set minimum size
        setMinimumSize(new Dimension(400, 300));

        // Make the window visible
        setVisible(true);

        // Add button listeners
        addButtonListeners();
    }

    private void addButtonListeners() {
        // Select button listener
        select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedAssessment = (String) assessmentcombobox.getSelectedItem();
                if (selectedAssessment != null) {
                    // Handle assessment selection
                    JOptionPane.showMessageDialog(panel1, "Selected Assessment: " + selectedAssessment);
                }
            }
        });

        // Create button listener
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle create assessment action
                JOptionPane.showMessageDialog(panel1, "Creating a new assessment...");
                new CreateAssessment(user, course);  // Assuming CreateAssessment has a constructor that accepts User and Course
                dispose();  // Close the current window
            }
        });

        // Exit button listener
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close the window and return to previous screen
                dispose();
            }
        });
    }
    public String DB_URL = "jdbc:mariadb://192.168.153.151:3306/evaluationmap";
    public String DB_USER = "userSQL";
    public String DB_PASS = "password1";
    // Method to populate the JComboBox with courses from the database
    public void populateAssessmentCombobox() {
        String sql = "Select assessment_course_date from assessmentcourse where assessment_course_course = '" + course.getcourseCourse() + "' and assessment_course_email_user = '" + user.getEmail() + "'";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                ArrayList<String> assessments = new ArrayList<>();

                // Loop through the result set and add the courses to the list
                while (rs.next()) {
                    String assessment = rs.getString("assessment_course_date");
                    assessments.add(assessment);  // Add the course to the list
                }
                // Update the ComboBox on the EDT to ensure UI responsiveness
                SwingUtilities.invokeLater(() -> {
                    for (String assessment : assessments) {
                        assessmentcombobox.addItem(assessment);  // Add assessment to the combo box
                    }
                });

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to fetch assessment from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
