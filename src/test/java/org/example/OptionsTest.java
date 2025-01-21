package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OptionsTest {

    @Mock
    private User user;
    private Options options;
    private JButton UserManagement;
    private JButton Calendar;
    private JButton Exit;

    @Mock
    private ActionListener mockActionListener;

    @BeforeEach
    public void setUp() {
        // Initialize mock objects
        MockitoAnnotations.openMocks(this); // This line is essential!

        // Create a mock user
        user = new User();
        user.setName("Director Test");
        user.setDirector(1);  // 1 means Department Director for this test

        options = new Options(user);

        // Access the buttons by their reference from panel1
        //JSL 01-07-2025
        UserManagement = (JButton) options.panel1.getComponent(1); // Accessing panel1 directly
        Calendar = (JButton) options.panel1.getComponent(2);
        Exit = (JButton) options.panel1.getComponent(3);

        // Add the mock action listener to the buttons
        UserManagement.addActionListener(mockActionListener);
        Calendar.addActionListener(mockActionListener);
        Exit.addActionListener(mockActionListener);
    }

    @Test
    public void testStatusLabel() {
        JLabel statusLabel = (JLabel) options.panel1.getComponent(0);
        String expectedLabel = "Welcome, Department Director Director Test";
        assertEquals(expectedLabel, statusLabel.getText());
    }

    @Test
    public void testUserManagementButtonVisibleForDirector() {
        assertEquals("User Management", UserManagement.getText());
    }

    @Test
    public void testCalendarButton() {
        assertEquals("Course Management", Calendar.getText());
    }

    @Test
    public void testExitButton() {
        assertEquals("Sign Off", Exit.getText());
    }

    // Doesn´t check when the button is pressed
    @Test
    public void testUserManagementButtonAction() {
        ActionEvent actionEvent = mock(ActionEvent.class);

        // Simulate a button click
        UserManagement.getActionListeners()[0].actionPerformed(actionEvent);

        // Verify that the action listener is triggered once
        verify(mockActionListener, times(1)).actionPerformed(actionEvent);
    }

    /* it in comments and not developed as the button hasn´t been developed in the program
    @Test
    public void testCalendarButtonAction() {
    }
    */

    @Test
    public void testExitButtonAction() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        Exit.getActionListeners()[0].actionPerformed(actionEvent);
        verify(mockActionListener, times(1)).actionPerformed(actionEvent);
    }

    @Test
    public void testStatusLabelForNonDirector() {
        // Set the director to 0 (non-director) to test the course coordinator
        user.setName("Coordenator Test");
        user.setDirector(0);

        options = new Options(user);

        JLabel statusLabel = (JLabel) options.panel1.getComponent(0);

        // Verify the text of the label
        String expectedLabel = "Welcome, Course Coordinator Coordenator Test";
        assertEquals(expectedLabel, statusLabel.getText());
    }

    // New test to verify if User Management is not visible to the course Coordinator (user.setDirector == 0)
    @Test
    public void testUserManagementButtonNotVisibleForNonDirector() {
        // Set the director to 0 (non-director)
        user.setDirector(0);

        options = new Options(user);

        boolean userManagementButtonFound = false;
        for (int i = 0; i < options.panel1.getComponentCount(); i++) {
            if (options.panel1.getComponent(i) instanceof JButton) {
                JButton button = (JButton) options.panel1.getComponent(i);
                if (button.getText().equals("User Management")) {
                    userManagementButtonFound = true;
                    break;
                }
            }
        }

        assertFalse(userManagementButtonFound, "The User Management button should not be visible for non-directors.");
    }

    @Test
    public void testExitButtonActionDisposesFrame() {
        Exit.doClick();
        assertFalse(options.isVisible(), "The frame should be disposed when the exit button is clicked.");
    }
}
