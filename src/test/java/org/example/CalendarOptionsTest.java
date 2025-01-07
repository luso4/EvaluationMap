package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CalendarOptionsTest {

    @Mock
    private User mockUser;
    private CalendarOptions calendarOptions;
    private JButton CalendarSemesterButton;
    private JButton CalendarCourseButton;
    private JButton ExitButton;

    // Mock the ActionListener for buttons
    @Mock
    private ActionListener mockActionListener;

    @BeforeEach
    void setUp() {
        // Mock the User object
        MockitoAnnotations.openMocks(this);

        // Mock the User object to return a specific director value
        when(mockUser.getDirector()).thenReturn(1);

        // Create the CalendarOptions instance
        calendarOptions = new CalendarOptions(mockUser);

        // Access the buttons by direct references
        CalendarSemesterButton = calendarOptions.CalendarSemester;
        CalendarCourseButton = calendarOptions.CalendarCourse;
        ExitButton = calendarOptions.Exit;

        // Add the mock action listener to the buttons
        CalendarSemesterButton.addActionListener(mockActionListener);
        CalendarCourseButton.addActionListener(mockActionListener);
        ExitButton.addActionListener(mockActionListener);
    }

    // Test to check if the buttons are initialized correctly
    @Test
    void testButtonsInitialization() {
        assertNotNull(CalendarSemesterButton, "CalendarSemester button should be initialized");
        assertNotNull(CalendarCourseButton, "CalendarCourse button should be initialized");
        assertNotNull(ExitButton, "Exit button should be initialized");
    }

    // Test the action of the Calendar Semester Button
    @Test
    void testCalendarSemesterButtonAction() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        CalendarSemesterButton.getActionListeners()[0].actionPerformed(actionEvent);
        verify(mockActionListener, times(1)).actionPerformed(actionEvent);
    }

    // Test the action of the Calendar Course Button
    @Test
    void testCalendarCourseButtonAction() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        CalendarCourseButton.getActionListeners()[0].actionPerformed(actionEvent);
        verify(mockActionListener, times(1)).actionPerformed(actionEvent);
    }

    // Test the action of the Options Button
    @Test
    void testOptionsButtonAction() {
        ActionEvent actionEvent = mock(ActionEvent.class);
        ExitButton.getActionListeners()[0].actionPerformed(actionEvent);
        verify(mockActionListener, times(1)).actionPerformed(actionEvent);

    }

}
