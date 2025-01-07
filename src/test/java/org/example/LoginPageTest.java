package org.example;

import javax.swing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;

public class LoginPageTest {

    @Mock
    private JFrame parentFrame;

    @Mock
    private JOptionPane mockOptionPane;

    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        loginPage = new LoginPage(parentFrame);

        // Mocking showErrorMessage directly (instead of trying to mock JOptionPane)
        doNothing().when(loginPage).showErrorMessage(anyString());
    }

    @Test
    public void testEmptyCredentials() {
        String email = "";
        String password = "";

        // Use Mockito to mock JOptionPane
        JOptionPane mockOptionPane = mock(JOptionPane.class);

        // Use Mockito to simulate the JOptionPane behavior
        doNothing().when(mockOptionPane).showMessageDialog(any(), anyString(), anyString(), anyInt());

        loginPage.getAuthenticatedUser(email, password);

        // Verify JOptionPane's showMessageDialog is called
        verify(mockOptionPane).showMessageDialog(any(), eq("Please enter both email and password."), eq("Error"), eq(JOptionPane.ERROR_MESSAGE));
    }

    // Other test methods...
}
