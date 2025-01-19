package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

import com.toedter.calendar.JDateChooser;

public class CreateAssessment extends JFrame {
    private JPanel panel1;
    private JComboBox<String> assessmentComboBox;
    private JSpinner PercentageTextField;
    private JComboBox<String> roomComboBox;
    private JRadioButton yesComputer, noComputer;
    private ButtonGroup computerRequired;
    private JRadioButton yesMandatory, noMandatory;
    private ButtonGroup mandatory;
    private JRadioButton yesRoom, noRoom;
    private ButtonGroup roomMandatory;
    private JButton createAssessmentButton;
    private JButton calendarOptions;
    private JDateChooser dateChooser;
    public User user;
    public Course course;

    public CreateAssessment(User user, Course course) {
        this.user = user;
        this.course = course;

        setTitle("Create Assessment");
        setSize(720,480);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


        panel1 = new JPanel(new GridBagLayout());
        createAssessmentButton = new JButton("Create Assessment");
        calendarOptions = new JButton("Return to Calendar Options");

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


        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 100, 1);
        PercentageTextField = new JSpinner(model);
        PercentageTextField.setPreferredSize(new Dimension(75, 25));
        gbc.gridx = 1;
        panel1.add(PercentageTextField, gbc);

        // Button for the dates
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel1.add(new JLabel("Date"), gbc);

        // Create a JDateChooser component for selecting the date
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");  // Set the date format
        gbc.gridx = 1;
        panel1.add(dateChooser, gbc);


        //Option for if the assessment needs a room
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel1.add(new JLabel("Is it required a room?"), gbc);

        // Create the JRadioButtons
        yesRoom = new JRadioButton("Yes");
        noRoom = new JRadioButton("No");

        // Create a ButtonGroup to ensure only one radio button is selected at a time
        roomMandatory = new ButtonGroup();
        roomMandatory.add(yesRoom);
        roomMandatory.add(noRoom);

        // Add radio buttons to the panel
        gbc.gridx = 1;
        gbc.gridy = 4;
        panel1.add(yesRoom, gbc);

        gbc.gridx = 2;
        panel1.add(noRoom, gbc);

        // Create and initialize the container panel for room-related components
        JPanel roomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints roomGbc = new GridBagConstraints();
        roomGbc.insets = new Insets(10, 10, 10, 10);

        // Room related components
        roomPanel.setVisible(true); // Initially visible
        roomGbc.gridx = 0;
        roomGbc.gridy = 0;
        roomPanel.add(new JLabel("Is it required a room with computers?"), roomGbc);


        // Create the JRadioButtons
        yesComputer = new JRadioButton("Yes");
        noComputer = new JRadioButton("No");

        // Create a ButtonGroup to ensure only one radio button is selected at a time
        computerRequired = new ButtonGroup();
        computerRequired.add(yesComputer);
        computerRequired.add(noComputer);

        // Add radio buttons to the panel
        roomGbc.gridx = 1;
        roomGbc.gridy = 1;
        roomPanel.add(yesComputer, roomGbc);

        roomGbc.gridx = 2;
        roomPanel.add(noComputer, roomGbc);


        //Selection of the Room
        roomGbc.gridx = 0;
        roomGbc.gridy = 2;
        roomPanel.add(new JLabel("Room"), roomGbc);

        // Create and populate the JComboBox with Assessments from the database
        roomComboBox = new JComboBox<>();
        populateRoomComboBox();
        roomGbc.gridx = 1;
        roomPanel.add(roomComboBox, roomGbc);

        // Add room panel to the main panel
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 10; // Ensure it spans across the columns
        panel1.add(roomPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel1.add(new JLabel("Is it mandatory?"), gbc);

        // Create the JRadioButtons
        yesMandatory = new JRadioButton("Yes");
        noMandatory = new JRadioButton("No");

        // Create a ButtonGroup to ensure only one radio button is selected at a time
        mandatory = new ButtonGroup();
        mandatory.add(yesMandatory);
        mandatory.add(noMandatory);

        // Add radio buttons to the panel
        gbc.gridx = 1;
        gbc.gridy = 6;
        panel1.add(yesMandatory, gbc);

        gbc.gridx = 2;
        panel1.add(noMandatory, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        panel1.add(createAssessmentButton, gbc);
        gbc.gridx = 1;
        panel1.add(calendarOptions, gbc);

        setContentPane(panel1);  // Set the content pane
        setMinimumSize(new Dimension(720,480));

        setVisible(true);

        yesComputer.addActionListener(e -> {
            populateRoomComboBox();
        });

        noComputer.addActionListener(e -> {
            populateRoomComboBox();
            JOptionPane.showMessageDialog(panel1, "You selected the option without computers. Please select a room without computers as there are fewer.", "Information", JOptionPane.INFORMATION_MESSAGE);
        });

        // Action listeners to handle visibility of roomPanel
        yesRoom.addActionListener(e -> {
            roomPanel.setVisible(true); // Show the room panel
            panel1.revalidate(); // Revalidate the layout to update visibility
            panel1.repaint();    // Repaint the panel to reflect the changes
        });

        noRoom.addActionListener(e -> {
            roomPanel.setVisible(false); // Hide the room panel
            panel1.revalidate(); // Revalidate the layout to update visibility
            panel1.repaint();    // Repaint the panel to reflect the changes
        });

        //Action listeners for the buttons
        createAssessmentButton.addActionListener(e ->{
            addAssessmentCourse();
        });
        calendarOptions.addActionListener(e -> {
            new CalendarOptions(user);
            dispose();
        });
    }

    public String DB_URL = "jdbc:mariadb://192.168.153.151:3306/evaluationmap";
    public String DB_USER = "userSQL";
    public String DB_PASS = "password1";
    // Method to populate the JComboBox with courses from the database
    public void populateAssessmentComboBox() {
        // SQL query with a placeholder for the email
        String sql = "SELECT assessment_name FROM assessment";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
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

    public void populateRoomComboBox() {
        String type_of_material = "";
        if (yesComputer.isSelected()){
            type_of_material = "where room_type_of_material = 'Computadores'";  // For rooms with computers
        } else if (noComputer.isSelected()){
            type_of_material = "";  // For rooms without computers
        }

        String sql = "SELECT room_room FROM room " + type_of_material;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                // Clear existing items in the ComboBox before adding new ones
                roomComboBox.removeAllItems();

                // Loop through the result set and add the rooms to the ComboBox
                while (rs.next()) {
                    String room = rs.getString("room_room");
                    roomComboBox.addItem(room);  // Add room to the combo box
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to fetch room from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void addAssessmentCourse() {
        String email_user = user.getEmail();
        String course_course = course.getcourseCourse();
        String assessment = (String) assessmentComboBox.getSelectedItem();
        int percentage = (Integer) PercentageTextField.getValue();
        // Captura a data selecionada
        /*java.util.Date selectedDate = dateChooser.getDate();
        java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());
        String date = sqlDate.toString();  // Converte para o formato de data SQL*/

        String date = "2025-01-01";
        int required_room = yesRoom.isSelected() ? 1 : 0;
        int required_room_computer = yesComputer.isSelected() ? 1 : 0;
        String selectedRoom = (String) roomComboBox.getSelectedItem();
        Integer course_room = Integer.parseInt(selectedRoom);  // Parse the room number
        int assessment_mandatory = yesMandatory.isSelected() ? 1 : 0;

        // Format the SQL string with actual values
        String sqlLog = String.format("INSERT INTO assessmentcourse (assessment_course_email_user, assessment_course_course, " +
                        "assessment_course_assessment, assessment_course_percentage, assessment_course_date, " +
                        "assessment_course_required_room, assessment_course_required_room_computer, assessment_course_room, " +
                        "assessment_course_mandatory) VALUES ('%s', '%s', '%s', %d, '%s', %d, %d, %d, %d);",
                email_user, course_course, assessment, percentage, date, required_room, required_room_computer, course_room, assessment_mandatory);

        // Show the SQL query in a dialog box (Only show this once)
        JOptionPane.showMessageDialog(null, "SQL to be executed: \n" + sqlLog);

        try {
            // Disable the button to prevent multiple submissions
            createAssessmentButton.setEnabled(false);

            // Execute the first insert SQL
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                try (PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO assessmentcourse " +
                        "(assessment_course_email_user, assessment_course_course, " +
                        "assessment_course_assessment, assessment_course_percentage, " +
                        "assessment_course_date, assessment_course_required_room, " +
                        "assessment_course_required_room_computer, assessment_course_room, " +
                        "assessment_course_mandatory) VALUES (?,?,?,?,?,?,?,?,?)")) {

                    pstmt1.setString(1, email_user);
                    pstmt1.setString(2, course_course);
                    pstmt1.setString(3, assessment);
                    pstmt1.setInt(4, percentage);
                    pstmt1.setString(5, date);
                    pstmt1.setInt(6, required_room);
                    pstmt1.setInt(7, required_room_computer);
                    pstmt1.setInt(8, course_room);
                    pstmt1.setInt(9, assessment_mandatory);

                    // Execute the first insert statement
                    pstmt1.executeUpdate();
                }

                // Second SQL insert - Example into another table, e.g., 'assessment_details'
                String sqlInsert2 = "UPDATE course " +
                        "SET course_number_assessment = ?, assessment_mandatory_number_course = ?, percentage_course = ? " +
                        "WHERE course_course = ?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(sqlInsert2)) {
                    pstmt2.setInt(1, course.getcourseAssessmentNr() + 1 );
                    pstmt2.setInt(2, course.getassessmentMandatoryNumberCourse() + 1);
                    pstmt2.setInt(3,course.getpercentageCourse() + percentage);
                    pstmt2.setString(4,course.getcourseCourse());


                    // Execute the second insert statement
                    pstmt2.executeUpdate();
                }

                // Confirm success
                JOptionPane.showMessageDialog(null, "Assessment successfully created and additional record added!");

            } catch (SQLException e) {
                // Handle any exceptions
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error while inserting assessment: " + e.getMessage());
            }

        } finally {
            // Enable the button after the operation is complete
            createAssessmentButton.setEnabled(true);
        }
    }

}




