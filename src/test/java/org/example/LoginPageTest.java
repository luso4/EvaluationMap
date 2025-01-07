import static org.mockito.Mockito.*;
import org.example.LoginPage;

import javax.swing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

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

        // Directly mock JOptionPane methods in the test setup
        doReturn(mockOptionPane).when(loginPage).getOptionPane();
    }

    @Test
    public void testEmptyCredentials() {
        String email = "";
        String password = "";

        // Call the method with empty credentials
        loginPage.getAuthenticatedUser(email, password);

        // Verify that a warning message dialog is shown for empty credentials
        verify(mockOptionPane).showMessageDialog(eq(loginPage), eq("Please enter both email and password."), eq("Error"), eq(JOptionPane.WARNING_MESSAGE));
    }

    // Other test methods...
}
