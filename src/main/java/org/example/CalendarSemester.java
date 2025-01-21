package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import com.toedter.calendar.JDateChooser;

public class CalendarSemester extends JFrame {
    private JDateChooser startDateChooser;
    private JDateChooser endDateChooser;
    private JDateChooser examStartDateChooser;
    private JDateChooser examEndDateChooser;
    private JDateChooser appealExamStartDateChooser;
    private JDateChooser appealExamEndDateChooser;
    private JDateChooser specialExamStartDateChooser;
    private JDateChooser specialExamEndDateChooser;
    private JButton submitButton;

    public CalendarSemester() {
        setTitle("Define Semester dates");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(9, 2, 10, 10));

        // Initialization of the date components
        startDateChooser = new JDateChooser();
        endDateChooser = new JDateChooser();
        examStartDateChooser = new JDateChooser();
        examEndDateChooser = new JDateChooser();
        appealExamStartDateChooser = new JDateChooser();
        appealExamEndDateChooser = new JDateChooser();
        specialExamStartDateChooser = new JDateChooser();
        specialExamEndDateChooser = new JDateChooser();
        submitButton = new JButton("Confirme dates");

        // Adicionando rótulos e campos à interface
        add(new JLabel("Semester start date:"));
        add(startDateChooser);

        add(new JLabel("Semester end date:"));
        add(endDateChooser);

        add(new JLabel("Regular exams start date:"));
        add(examStartDateChooser);

        add(new JLabel("Regular exams end date:"));
        add(examEndDateChooser);

        add(new JLabel("Appeal exams start date:"));
        add(appealExamStartDateChooser);

        add(new JLabel("Appeal exams end date:"));
        add(appealExamEndDateChooser);

        add(new JLabel("Special season exams start date:"));
        add(specialExamStartDateChooser);

        add(new JLabel("Special season exams end date:"));
        add(specialExamEndDateChooser);

        add(new JLabel("")); // Empty spac for aligning
        add(submitButton);

        // Listener for submission button
        submitButton.addActionListener(this::handleSubmission);

        setVisible(true);
    }

    private void handleSubmission(ActionEvent e) {
        if (!validateDates()) {
            return;
        }
        saveDatesToDatabase();
    }

    private boolean validateDates() {
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();
        Date examStartDate = examStartDateChooser.getDate();
        Date examEndDate = examEndDateChooser.getDate();
        Date appealExamStartDate = appealExamStartDateChooser.getDate();
        Date appealExamEndDate = appealExamEndDateChooser.getDate();
        Date specialExamStartDate = specialExamStartDateChooser.getDate();
        Date specialExamEndDate = specialExamEndDateChooser.getDate();

        if (startDate == null || endDate == null || examStartDate == null || examEndDate == null ||
                appealExamStartDate == null || appealExamEndDate == null || specialExamStartDate == null ||
                specialExamEndDate == null) {
            JOptionPane.showMessageDialog(this, "Please enter all the dates.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (endDate.before(new Date(startDate.getTime() + 30L * 24 * 60 * 60 * 1000))) {
            JOptionPane.showMessageDialog(this, "The end date must be at least 1 month after the starting date.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (examStartDate.before(startDate)) {
            JOptionPane.showMessageDialog(this, "Regular exams start date can not be before the start of the semester.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (examEndDate.before(new Date(examStartDate.getTime() + 7L * 24 * 60 * 60 * 1000))) {
            JOptionPane.showMessageDialog(this, "Regular exames end date must be at least 1 week after the starting date.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (appealExamStartDate.before(examEndDate)) {
            JOptionPane.showMessageDialog(this, "Appeal exams date can not be befoe the regular exams ending date.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (appealExamEndDate.before(new Date(appealExamStartDate.getTime() + 14L * 24 * 60 * 60 * 1000))) {
            JOptionPane.showMessageDialog(this, "Appeal exams end date should be at least 2 weeks after the starting date.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (specialExamStartDate.before(appealExamEndDate)) {
            JOptionPane.showMessageDialog(this, "Special season exams star date can not be before appeal exams ending date.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (specialExamEndDate.before(new Date(specialExamStartDate.getTime() + 14L * 24 * 60 * 60 * 1000))) {
            JOptionPane.showMessageDialog(this, "Special season exams end date must be at least 2 weeks after the starting date.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private void saveDatesToDatabase() {
        // Database connection config
        String url = "jdbc:mariadb://192.168.76.151:3306/evaluationmap";
        String username = "userSQL";
        String password = "password1";

        // Consults SQL to insert dates
        String insertQuery = "INSERT INTO tabela_das_datas (descricao, data) VALUES (?, ?)";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            saveDate(preparedStatement, "Semesters Start", startDateChooser.getDate());
            saveDate(preparedStatement, "Semesters End", endDateChooser.getDate());
            saveDate(preparedStatement, "Regular exams Start", examStartDateChooser.getDate());
            saveDate(preparedStatement, "Regular exams End", examEndDateChooser.getDate());
            saveDate(preparedStatement, "Appeal exams Start", appealExamStartDateChooser.getDate());
            saveDate(preparedStatement, "Appeal exams End", appealExamEndDateChooser.getDate());
            saveDate(preparedStatement, "Special season Start", specialExamStartDateChooser.getDate());
            saveDate(preparedStatement, "Special season End", specialExamEndDateChooser.getDate());

            JOptionPane.showMessageDialog(this, "Success inserting date in the database!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving dates to the database!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveDate(PreparedStatement preparedStatement, String description, Date date) throws SQLException {
        if (date != null) {
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            preparedStatement.setString(1, description);
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.executeUpdate();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalendarSemester::new);
    }
}
