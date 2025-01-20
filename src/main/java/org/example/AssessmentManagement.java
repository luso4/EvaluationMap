package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.sql.Date;
import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Map;

import javax.swing.SwingUtilities;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

public class AssessmentManagement extends JFrame {
    private JPanel panel1;
    private JComboBox<Assessment> assessmentcombobox;  // Assuming this is for assessments related to the course
    private JButton select;
    private JButton exit;
    private JButton create;
    public User user;
    public Course course;
    public Assessment assessment;

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
            assessmentcombobox = new JComboBox<Assessment>();
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
                Assessment selectedAssessment = (Assessment) assessmentcombobox.getSelectedItem();

                if (selectedAssessment != null) {
                    // Open CreateAssessment with the selected assessment's data
                    new CreateAssessment(user, course, selectedAssessment);
                    dispose();  // Close the current window
                } else {
                    JOptionPane.showMessageDialog(panel1, "Assessment not found!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Create button listener
        create.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle create assessment action
                new CreateAssessment(user, course, null);  // Assuming CreateAssessment has a constructor that accepts User and Course
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
    public String DB_URL = "jdbc:mariadb://192.168.131.151:3306/evaluationmap";
    public String DB_USER = "userSQL";
    public String DB_PASS = "password1";
    // Method to populate the JComboBox with courses from the database

    public void populateAssessmentCombobox() {
        // SQL query to fetch the assessments based on the course and user email
        String sql = "SELECT assessment_course_email_user, assessment_course_course, assessment_course_assessment, assessment_course_percentage, assessment_course_date, assessment_course_required_room, assessment_course_required_room_computer, assessment_course_room, assessment_course_mandatory FROM assessmentcourse WHERE assessment_course_course = ? AND assessment_course_email_user = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set the parameters for the course and user email
            pstmt.setString(1, course.getcourseCourse());  // course.getcourseCourse() gets the course name
            pstmt.setString(2, user.getEmail());  // user.getEmail() gets the user's email

            try (ResultSet rs = pstmt.executeQuery()) {
                ArrayList<Assessment> assessments = new ArrayList<>();
                Map<String, Assessment> dateToAssessmentMap = new HashMap<>(); // Map to store date to assessment

                // Loop through the result set and create Assessment objects
                while (rs.next()) {
                    String assessmentCourseEmailUser = rs.getString("assessment_course_email_user");
                    String assessmentCourseCourse = rs.getString("assessment_course_course");
                    String assessmentCourseAssessment = rs.getString("assessment_course_assessment");
                    int assessmentCoursePercentage = rs.getInt("assessment_course_percentage");

                    // Get the date as a java.sql.Date
                    Date sqlDate = rs.getDate("assessment_course_date");

                    // Format the Date as a String
                    if (sqlDate != null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Change format as needed
                        String formattedDate = sdf.format(sqlDate);

                        // Create the Assessment object
                        Assessment assessment1 = new Assessment(
                                assessmentCourseEmailUser,
                                assessmentCourseCourse,
                                assessmentCourseAssessment,
                                assessmentCoursePercentage,
                                formattedDate,
                                rs.getInt("assessment_course_required_room"),
                                rs.getInt("assessment_course_required_room_computer"),
                                rs.getString("assessment_course_room"),
                                rs.getInt("assessment_course_mandatory")
                        );

                        assessments.add(assessment1);  // Store the full Assessment object
                        dateToAssessmentMap.put(formattedDate, assessment1);  // Map the formatted date to the Assessment object
                    }
                }

                // Update the ComboBox on the EDT to ensure UI responsiveness
                SwingUtilities.invokeLater(() -> {
                    // Clear existing items from ComboBox before adding new ones
                    assessmentcombobox.removeAllItems();

                    // Add each date to the ComboBox
                    for (Assessment assessment : assessments) {
                        // Add formatted date to the ComboBox (show date but store full Assessment object)
                        assessmentcombobox.addItem(assessment);  // Add formatted date to combo box
                    }
                });

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to fetch assessments from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
