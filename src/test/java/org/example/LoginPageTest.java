package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.*;

public class sLoginPageTest {

    @Test
    public void testGetAuthenticatedUser_ValidCredentials() throws SQLException {

        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true); // Simulate that a valid user was found
        when(rs.getString("email")).thenReturn("user@example.com");
        when(rs.getString("name")).thenReturn("John Doe");

        LoginPage loginPage = new LoginPage(null);
        User user = loginPage.getAuthenticatedUser("user@example.com", "password");

        assertNotNull(user);
        assertEquals("user@example.com", user.getEmail());
        assertEquals("John Doe", user.getName());
    }

    @Test
    public void testGetAuthenticatedUser_InvalidCredentials() throws SQLException {
        Connection conn = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(conn.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false); // Simulate no matching user

        LoginPage loginPage = new LoginPage(null);
        User user = loginPage.getAuthenticatedUser("invalid@example.com", "wrongpassword");

        assertNull(user);
    }
}
