package org.example;

import javax.swing.*;
import java.awt.*;

public class CalendarOptions extends JFrame {
    private JPanel panel1;
    public JButton CalendarSemester; //Button to redirect to Manager new user
    public JButton CalendarCourse; // Button to redirect to Calendar
    public JButton Exit; // Sign Off Button
    public User user;
    public JButton AssessmentButton; // Button to redirect to Assesement JSL 08-01-2025

    public CalendarOptions(User user) {
        this.user = user;  // Store the user object for future use

        setTitle("Calendar options");
        setSize(400, 300);
        setLocationRelativeTo(null);


        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // Initialize the panel and buttons
        panel1 = new JPanel(new GridBagLayout());
        CalendarSemester = new JButton("Define Semester");
        CalendarCourse = new JButton("Define Course");
        AssessmentButton = new JButton("New Assesement"); //JSL 08-01-2025
        Exit = new JButton("Options");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        if (user.getDirector() == 1) {
            gbc.gridx = 0;
            gbc.gridy = 1;
            panel1.add(CalendarSemester, gbc);
        }

        gbc.gridy = 2;
        panel1.add(CalendarCourse, gbc);

        gbc.gridy = 3;
        panel1.add(Exit, gbc);

        //JSL 08-01-2025
        gbc.gridy = 4;
        panel1.add(AssessmentButton, gbc);

        setContentPane(panel1);
        setMinimumSize(new Dimension(400, 200));

        CalendarSemester.addActionListener(e -> {
            // Cria e exibe o diálogo CalendarSemester
            CalendarSemester dialog = new CalendarSemester();
            dialog.pack(); // Ajusta o tamanho do diálogo
            dialog.setLocationRelativeTo(null); // Centraliza na tela
            dialog.setVisible(true); // Torna o diálogo visível
        });


        // Action listener for Calendar button
        CalendarCourse.addActionListener(e -> {
            new SelectionCourse(user);
            dispose();
        });

        // Add Exit button
        Exit.addActionListener(e -> {
            new Options(user);
            dispose();
        });
        //JSL 08-01-2025
        AssessmentButton.addActionListener(e -> {
            //new CreateAssessment (user);
            dispose();
        });

        setVisible(true);
    }


}
