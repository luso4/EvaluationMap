package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class SignInPageTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @InjectMocks
    private SignInPage signInPage;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser_Success() throws SQLException {
        String email = "test@example.com";
        String password = "password123";
        String username = "testuser";
        boolean isDirector = true;
        String course = "Math 101";
        boolean isMixed = false;

        // Simulate the creation of a PreparedStatement
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        when(mockPreparedStatement.executeUpdate()).thenReturn(1);

        boolean result = signInPage.createUser(email, password, username, isDirector, course, isMixed, mockConnection);

        assertTrue(result); // Assert that the result is true (user creation was successful)

        // Verify that executeUpdate() was called for both statements
        verify(mockPreparedStatement, times(1)).executeUpdate();
        verify(mockConnection, times(1)).commit(); // Ensure commit was called once
    }



    /*@Test
    public void testCreateUser_Failure_EmailAlreadyExists() throws SQLException {
        // Arrange
        String email = "test56@example.com";
        String password = "password123";
        String username = "testuser";
        boolean isDirector = true;
        String course = "Math 101";
        boolean isMixed = false;

        // When preparing the statement, simulate the SQLIntegrityConstraintViolationException for duplicate email
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLIntegrityConstraintViolationException("Duplicate entry"));

        // Act
        boolean result = signInPage.createUser(email, password, username, isDirector, course, isMixed);

        // Assert
        assertFalse(result);
        // Verify rollback was called once when the exception is thrown
        verify(mockConnection, times(1)).rollback();
    }*/
    /*
    @Test
    public void testCreateUser_UnexpectedError() throws SQLException {
        // Arrange
        String email = "test56@example.com";
        String password = "password123";
        String username = "testuser";
        boolean isDirector = true;
        String course = "Math 101";
        boolean isMixed = false;

        // Simulate an unexpected SQLException during execution
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Unexpected error"));

        // Act
        boolean result = signInPage.createUser(email, password, username, isDirector, course, isMixed);

        // Assert
        assertFalse(result);
        // Ensure rollback is not called in case of an unexpected error
        verify(mockConnection, never()).rollback();
    }*/
}

