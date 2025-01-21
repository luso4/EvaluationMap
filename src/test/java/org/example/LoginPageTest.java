package org.example;

import javax.swing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

public class LoginPageTest {

    private JFrame parentFrame;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        parentFrame = new JFrame(); // Real JFrame instance
        loginPage = spy(new LoginPage(parentFrame)); // Create a spy on the real LoginPage

        // Mocking showErrorMessage using the spy
        doNothing().when(loginPage).showErrorMessage(anyString()); // No action for this method
    }

    @Test
    public void testEmptyCredentials() {
        String email = "";
        String password = "";

        loginPage.getAuthenticatedUser(email, password);

        // Verify that showErrorMessage() is called with the expected message
        verify(loginPage).showErrorMessage(eq("Please enter both email and password."));
    }
    @Test
    public void testInvalidCredentials() {
        String email = "4";
        String password = "test";
        loginPage.getAuthenticatedUser(email, password);
        verify(loginPage).showErrorMessage(eq("Invalid username or password"));
    }
    @Test
    public void testValidCredentials() {
        String email = "4"; // Valid username
        String password = "4"; // Valid password

        loginPage.getAuthenticatedUser(email, password);

        // No error message should be shown for valid credentials, so verify it isn't called
        verify(loginPage, never()).showErrorMessage(anyString());
    }

    // Other test methods...
}
