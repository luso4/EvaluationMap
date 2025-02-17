package org.example;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JCalendar;
//import com.toedter.calendar.SelectableDateFilter;



public class CreateAssessment extends JFrame {
    private JPanel panel1;
    private JComboBox<String> assessmentComboBox;
    private JSpinner PercentageTextField;
    private JComboBox<String> roomComboBox;
    private JRadioButton yesComputer, noComputer, yesMore, noMore;
    private ButtonGroup computerRequired, moreRequired;
    private JRadioButton yesMandatory, noMandatory;
    private ButtonGroup mandatory;
    private JRadioButton yesRoom, noRoom;
    private ButtonGroup roomMandatory;
    private JButton createAssessmentButton;
    private JButton calendarOptions;
    private JDateChooser dateChooser;
    private User user;
    private Course course;
    private Assessment assessment;
    private JComboBox<Integer> hourComboBox;
    private JComboBox<Integer> minuteComboBox;

    public CreateAssessment(User user, Course course, Assessment assessment) {
        this.user = user;
        this.course = course;
        this.assessment = assessment;

        initComponents();
        if (assessment != null) {
            assessmentComboBox.setSelectedItem(assessment.getAssessment_course_assessment());
            PercentageTextField.setValue(assessment.getAssessment_course_percentage());
            roomComboBox.setSelectedItem(assessment.getAssessment_course_room());
            int roomRequired = assessment.getAssessment_course_required_room();
            if (roomRequired == 1) {
                yesRoom.setSelected(true);  // If room is required, select "Yes"
            } else {
                noRoom.setSelected(true);   // If room is not required, select "No"
            }
            int roomRequiredComputer = assessment.getAssessment_course_required_room_computer();
            if (roomRequiredComputer == 1) {
                yesComputer.setSelected(true);  // If computer is required, select "Yes"
            } else {
                noComputer.setSelected(true);   // If computer is not required, select "No"
            }
            int mandatory = assessment.getAssessment_course_mandatory();
            if (mandatory == 1) {
                yesMandatory.setSelected(true);  // If it is mandatory, select "Yes"
            } else {
                noMandatory.setSelected(true);   // If it is not mandatory, select "No"
            }
        }

        setTitle("Create Assessment");

        setSize(1280, 800);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void initComponents() {
        panel1 = new JPanel(new GridBagLayout());
        createAssessmentButton = new JButton("Create Assessment");
        calendarOptions = new JButton("Return to Course Management");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add components to panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel1.add(new JLabel("Create a new Assessment for the course " + course), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel1.add(new JLabel("Select the type of Assessment "), gbc);

        assessmentComboBox = new JComboBox<>();
        populateAssessmentComboBox();
        gbc.gridx = 1;
        panel1.add(assessmentComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel1.add(new JLabel("Percentage: "), gbc);

        SpinnerNumberModel model = new SpinnerNumberModel(1, 1, 100, 1);
        PercentageTextField = new JSpinner(model);
        PercentageTextField.setPreferredSize(new Dimension(75, 25));
        gbc.gridx = 1;
        panel1.add(PercentageTextField, gbc);

        // Create date chooser
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel1.add(new JLabel("Date"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        gbc.gridx = 1;
        panel1.add(dateChooser, gbc);


        // Create hour JComboBox (0-24)
        gbc.gridx = 0;
        gbc.gridy = 5;
        panel1.add(new JLabel("Hour"), gbc);

        hourComboBox = new JComboBox<>();
        for (int i = 0; i <= 24; i++) {
            hourComboBox.addItem(i);
        }
        gbc.gridx = 1;
        panel1.add(hourComboBox, gbc);

        // Create minute JComboBox (0-60)
        gbc.gridx = 0;
        gbc.gridy = 6;
        panel1.add(new JLabel("Minute"), gbc);

        minuteComboBox = new JComboBox<>();
        for (int i = 0; i < 60; i++) {
            minuteComboBox.addItem(i);
        }
        gbc.gridx = 1;
        panel1.add(minuteComboBox, gbc);



        // Room related components
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel1.add(new JLabel("Is it required a room?"), gbc);

        yesRoom = new JRadioButton("Yes");
        noRoom = new JRadioButton("No");
        roomMandatory = new ButtonGroup();
        roomMandatory.add(yesRoom);
        roomMandatory.add(noRoom);

        gbc.gridx = 1;
        panel1.add(yesRoom, gbc);
        gbc.gridx = 2;
        panel1.add(noRoom, gbc);

        // Room panel
        JPanel roomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints roomGbc = new GridBagConstraints();
        roomGbc.insets = new Insets(10, 10, 10, 10);

        roomPanel.setVisible(true);
        roomGbc.gridx = 0;
        roomGbc.gridy = 0;
        roomPanel.add(new JLabel("Is it required a room with computers?"), roomGbc);

        yesComputer = new JRadioButton("Yes");
        noComputer = new JRadioButton("No");
        computerRequired = new ButtonGroup();
        computerRequired.add(yesComputer);
        computerRequired.add(noComputer);

        roomGbc.gridx = 1;
        roomGbc.gridy = 1;
        roomPanel.add(yesComputer, roomGbc);
        roomGbc.gridx = 2;
        roomPanel.add(noComputer, roomGbc);


        //Selection of the Room
        roomGbc.gridx = 0;
        roomGbc.gridy = 2;
        roomPanel.add(new JLabel("Room"), roomGbc);

        roomComboBox = new JComboBox<>();
        populateRoomComboBox();
        roomGbc.gridx = 1;
        roomPanel.add(roomComboBox, roomGbc);

        // Add room panel to the main panel

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 3;
        panel1.add(roomPanel, gbc);


        gbc.gridx = 0;
        gbc.gridy = 9;
        panel1.add(new JLabel("Is it mandatory?"), gbc);

        yesMandatory = new JRadioButton("Yes");
        noMandatory = new JRadioButton("No");
        mandatory = new ButtonGroup();
        mandatory.add(yesMandatory);
        mandatory.add(noMandatory);

        gbc.gridx = 1;
        gbc.gridy = 9;
        panel1.add(yesMandatory, gbc);
        gbc.gridx = 2;
        panel1.add(noMandatory, gbc);

        gbc.gridx = 0;
        gbc.gridy = 10;
        panel1.add(createAssessmentButton, gbc);
        gbc.gridx = 1;
        panel1.add(calendarOptions, gbc);

        setContentPane(panel1);
        setMinimumSize(new Dimension(720, 480));
        setVisible(true);

        yesComputer.addActionListener(e -> populateRoomComboBox());
        noComputer.addActionListener(e -> populateRoomComboBox());
        yesRoom.addActionListener(e -> roomPanel.setVisible(true));
        noRoom.addActionListener(e -> roomPanel.setVisible(false));

        createAssessmentButton.addActionListener(e -> addAssessmentCourse());
        calendarOptions.addActionListener(e -> {
            new CalendarOptions(user);
            dispose();
        });
    }



    // Database URL, user, and password
    public static final String DB_URL = "jdbc:mariadb://192.168.1.248:3306/evaluationmap";


    public static final String DB_USER = "userSQL";
    public static final String DB_PASS = "password1";

    // Populate the JComboBox with assessments
    private void populateAssessmentComboBox() {
        String sql = "SELECT assessment_name FROM assessment";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                ArrayList<String> assessments = new ArrayList<>();
                while (rs.next()) {
                    assessments.add(rs.getString("assessment_name"));
                }
                SwingUtilities.invokeLater(() -> {
                    for (String assessment : assessments) {
                        assessmentComboBox.addItem(assessment);
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
        String sql = "";

        if (yesComputer.isSelected()) {
            type_of_material = " AND room_type_of_material = 'Computadores'";  // For rooms with computers
        } else if (noComputer.isSelected()) {
            type_of_material = "";
        }

        sql = "SELECT room_room FROM room WHERE room_seats >= " + course.getstudentNrCourse() + type_of_material;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                roomComboBox.removeAllItems();
                while (rs.next()) {
                    roomComboBox.addItem(rs.getString("room_room"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to fetch room from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void addAssessmentCourse() {
        String email_user = user.getEmail();
        String course_course = course.getcourseCourse();
        String assessmentCombo = (String) assessmentComboBox.getSelectedItem();
        int percentage = (Integer) PercentageTextField.getValue();
        String date = "2025-01-05";  // Sample date
        int required_room = yesRoom.isSelected() ? 1 : 0;
        int required_room_computer = yesComputer.isSelected() ? 1 : 0;
        String selectedRoom = (String) roomComboBox.getSelectedItem();
        int course_room = Integer.parseInt(selectedRoom);
        int assessment_mandatory = yesMandatory.isSelected() ? 1 : 0;
        int assessment_hour = (Integer) hourComboBox.getSelectedItem();
        int assessment_minute = (Integer) minuteComboBox.getSelectedItem();

        // IF it is being altered
        if (assessment != null) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String updateQuery = "UPDATE assessmentcourse SET " +
                        "assessment_course_email_user = ?, " +
                        "assessment_course_course = ?, " +
                        "assessment_course_assessment = ?, " +
                        "assessment_course_percentage = ?, " +
                        "assessment_course_date = ?, " +
                        "assessment_course_required_room = ?, " +
                        "assessment_course_required_room_computer = ?, " +
                        "assessment_course_room = ?, " +
                        "assessment_course_mandatory = ? " +
                        "assessment_course_hour = ?, " +
                        "assessment_course_minute = ?, " +
                        "WHERE assessment_course_date = ?";

                try (PreparedStatement pstmt1 = conn.prepareStatement(updateQuery)) {
                    pstmt1.setString(1, email_user);
                    pstmt1.setString(2, course_course);
                    pstmt1.setString(3, assessmentCombo);
                    pstmt1.setInt(4, percentage);
                    pstmt1.setString(5, date);
                    pstmt1.setInt(6, required_room);
                    pstmt1.setInt(7, required_room_computer);
                    pstmt1.setInt(8, course_room);
                    pstmt1.setInt(9, assessment_mandatory);
                    pstmt1.setInt(10, assessment_hour);
                    pstmt1.setInt(11, assessment_minute);
                    pstmt1.setString(12, assessment.getAssessment_course_date());  // The value for WHERE clause

                    pstmt1.executeUpdate();

                    String sqlInsert2 = "UPDATE course " +
                            "SET course_number_assessment = ?, assessment_mandatory_number_course = ?, percentage_course = ? " +
                            "WHERE course_course = ?";
                    try (PreparedStatement pstmt2 = conn.prepareStatement(sqlInsert2)) {
                        pstmt2.setInt(1, course.getcourseAssessmentNr());
                        if (yesMandatory.isSelected()) {
                            pstmt2.setInt(2, course.getassessmentMandatoryNumberCourse() + 1);
                        } else {
                            pstmt2.setInt(2, course.getassessmentMandatoryNumberCourse() - 1);
                        }

                        pstmt2.setInt(3, course.getpercentageCourse() - assessment.getAssessment_course_percentage() + percentage); // Adjust the global percentage
                        pstmt2.setString(4, course.getcourseCourse());
                        pstmt2.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // If it is being created
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                try (PreparedStatement pstmt1 = conn.prepareStatement("INSERT INTO assessmentcourse " +
                        "(assessment_course_email_user, assessment_course_course, " +
                        "assessment_course_assessment, assessment_course_percentage, " +
                        "assessment_course_date, assessment_course_required_room, " +
                        "assessment_course_required_room_computer, assessment_course_room, " +
                        "assessment_course_mandatory, assessment_course_hour, assessment_course_minute) VALUES (?,?,?,?,?,?,?,?,?)")) {

                    pstmt1.setString(1, email_user);
                    pstmt1.setString(2, course_course);
                    pstmt1.setString(3, assessmentCombo);
                    pstmt1.setInt(4, percentage);
                    pstmt1.setString(5, date);
                    pstmt1.setInt(6, required_room);
                    pstmt1.setInt(7, required_room_computer);
                    pstmt1.setInt(8, course_room);
                    pstmt1.setInt(9, assessment_mandatory);
                    pstmt1.setInt(10, assessment_hour);
                    pstmt1.setInt(11, assessment_minute);

                    pstmt1.executeUpdate();
                }

                // Second SQL update statement
                String sqlInsert2 = "UPDATE course " +
                        "SET course_number_assessment = ?, assessment_mandatory_number_course = ?, percentage_course = ? " +
                        "WHERE course_course = ?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(sqlInsert2)) {
                    pstmt2.setInt(1, course.getcourseAssessmentNr() + 1);
                    if (yesMandatory.isSelected()) {
                        pstmt2.setInt(2, course.getassessmentMandatoryNumberCourse() + 1);
                    } else {
                        pstmt2.setInt(2, course.getassessmentMandatoryNumberCourse());
                    }
                    pstmt2.setInt(3, course.getpercentageCourse() + percentage);
                    pstmt2.setString(4, course.getcourseCourse());
                    pstmt2.executeUpdate();
                }

                JOptionPane.showMessageDialog(null, "Assessment successfully created!");

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error while inserting assessment: " + e.getMessage());
            }
        }
    }

    /* private void disableSelectedDates() {
        String sql = "SELECT ac.assessment_course_date " +
                "FROM assessmentcourse ac " +
                "JOIN course c ON ac.assessment_course_email_user = c.email_course " +
                "AND ac.assessment_course_course = c.course_course " +
                "WHERE c.course_year = ? AND c.department_course = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, course.getyearCourse());
            pstmt.setString(2, course.getDepartmentCourse());

            try (ResultSet rs = pstmt.executeQuery()) {
                ArrayList<Date> unavailableDates = new ArrayList<>();
                while (rs.next()) {
                    Date takenDate = rs.getDate("assessment_course_date");
                    unavailableDates.add(takenDate);
                }



                JCalendar jCalendar = dateChooser.getJCalendar();
                jCalendar.setSelectableDateFilter(date -> {

                        for (Date unavailableDate : unavailableDates) {

                            if (date.equals(unavailableDate)) {
                                return false;
                            }
                        }
                        return true;
                    }
                });

            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel1, "Failed to fetch unavailable dates from the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/
}