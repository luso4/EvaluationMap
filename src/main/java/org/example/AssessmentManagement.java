package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
        if (course.getcourseAssessmentNr() == 0)
        {
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel1.add(new JLabel("You have no assessments. You must create x assessments."), gbc);
        }
        //when there is some assessments
        else {


            // Add the header label
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel1.add(new JLabel("Create a new Assessment"), gbc);

            // Add the combo box for assessments (assuming you have a list of assessments to populate it)
            assessmentcombobox = new JComboBox<>();
            // Add dummy data for testing purposes; replace this with actual data
            assessmentcombobox.addItem("Assessment 1");
            assessmentcombobox.addItem("Assessment 2");
            assessmentcombobox.addItem("Assessment 3");

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel1.add(assessmentcombobox, gbc);
        }

        //Used in the case of there being no or some assessments
        // Add the buttons
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel1.add(select, gbc);

        gbc.gridy = 3;
        panel1.add(create, gbc);

        gbc.gridy = 4;
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
}
