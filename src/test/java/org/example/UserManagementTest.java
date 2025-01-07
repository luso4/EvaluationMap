package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.swing.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserManagementTest {

    @Mock
    private User user;
    @Mock
    private SignInPage signInPage;
    @Mock
    private CourseMaintenance courseMaintenance;
    @Mock
    private Options options;

    private UserManagement userManagement;

    @BeforeEach
    public void setUp() {
        // Initialize the mocks
        MockitoAnnotations.openMocks(this);

        // Set up user mock
        when(user.getName()).thenReturn("Test Name");
        when(user.getDirector()).thenReturn(1);

        // Initialize UserManagement with the mocked user
        userManagement = new UserManagement(user);
    }

    @Test
    public void testSignInButton() {
        JButton signInButton = (JButton) userManagement.getPanel1().getComponent(1);
        assertEquals("Sign In", signInButton.getText());
    }

    @Test
    public void testAssociateCourseButton() {
        JButton courseButton = (JButton) userManagement.getPanel1().getComponent(2);
        assertEquals("Associate course", courseButton.getText());
    }

    @Test
    public void testReturnButton() {
        JButton exitButton = (JButton) userManagement.getPanel1().getComponent(3);
        assertEquals("Return to Options", exitButton.getText());
    }

    @Test
    public void testSignInButtonAction() {
        // Create a mock for the signInPage
        SignInPage signInPage = mock(SignInPage.class);

        // Get the sign-in button
        JButton signInButton = (JButton) userManagement.getPanel1().getComponent(1);

        signInButton.addActionListener(e -> {
            signInPage.setVisible(true);
        });

        signInButton.doClick();
        verify(signInPage, times(1)).setVisible(true); // Ensure the method was called once
    }

    @Test
    public void testCourseButtonAction() {
        // Create a mock for courseMaintenance
        CourseMaintenance courseMaintenance = mock(CourseMaintenance.class);
        JButton courseButton = (JButton) userManagement.getPanel1().getComponent(2);
        courseButton.addActionListener(e -> {
            courseMaintenance.setVisible(true);
        });

        courseButton.doClick();

        verify(courseMaintenance, times(1)).setVisible(true);
    }

    @Test
    public void testExitButtonAction() {
        JButton exitButton = (JButton) userManagement.getPanel1().getComponent(3);
        Options options = mock(Options.class);
        exitButton.addActionListener(e -> {
            options.setVisible(true);
        });

        exitButton.doClick();
        verify(options, times(1)).setVisible(true);
    }
}
