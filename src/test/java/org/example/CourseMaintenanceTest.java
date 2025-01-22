
package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.swing.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CourseMaintenanceTest {

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private User mockUser;
    @Mock
    private JComboBox<String> mockEmailComboBox; // Mocked combo box for emails
    private CourseMaintenance courseMaintenance;

    @BeforeEach
    void setUp() throws SQLException, NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        // Mocking database behavior
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Mocking the behavior of the combo box for emails
        when(mockEmailComboBox.getSelectedItem()).thenReturn("test@example.com");

        // Creating instance of CourseMaintenance with mocked user
        courseMaintenance = new CourseMaintenance(mockUser);

        try (MockedStatic<DriverManager> driverManagerMock = mockStatic(DriverManager.class)) {
            driverManagerMock.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConnection);

            // Now the populateEmailComboBox() method should use the mocked connection
            courseMaintenance.populateEmailComboBox();
        }

        // Verifying interaction with the mock
        verify(mockResultSet, times(1)).getString("email");
    }

    // Test for adding a course to the user
    @Test
    void testAddCourseToUser() throws SQLException {
        // Given
        String email = "test@example.com";
        String password = "password123";
        String username = "testuser";
        boolean isDirector = true;
        String courseName = "Math 101";
        boolean isMixed = false;
        String department = "Informatics";
        int year = 1;
        int numberSt = 1;

        courseMaintenance.addCourseToUser(email, courseName, isMixed, year, department, numberSt);

        verify(mockPreparedStatement, times(1)).setString(1, email);
        verify(mockPreparedStatement, times(1)).setString(2, courseName);
        verify(mockPreparedStatement, times(1)).setInt(3, 1);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testRemoveCourseFromDatabase() throws SQLException {
        String email = "test@example.com";
        String courseName = "Math 101";

        courseMaintenance.removeCourseFromDatabase(email, courseName);

        verify(mockPreparedStatement, times(1)).setString(1, email);
        verify(mockPreparedStatement, times(1)).setString(2, courseName);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    // Test for populating the email combo box
    @Test
    void testPopulateEmailComboBox() throws SQLException, NoSuchFieldException, IllegalAccessException {
        String sql = "SELECT email FROM users";
        ArrayList<String> emails = new ArrayList<>();
        emails.add("test@example.com");
        emails.add("user@example.com");

        when(mockResultSet.next()).thenReturn(true, true, false); // Simulate 2 emails in DB
        when(mockResultSet.getString("email")).thenReturn("test@example.com", "user@example.com");

        courseMaintenance.populateEmailComboBox();

        verify(mockResultSet, times(2)).getString("email");

        Field emailComboBoxField = CourseMaintenance.class.getDeclaredField("emailComboBox");
        emailComboBoxField.setAccessible(true);
        JComboBox<String> emailComboBox = (JComboBox<String>) emailComboBoxField.get(courseMaintenance);

        assertTrue(emailComboBox.getItemCount() > 0);
    }

    // Test for the case when add course button is clicked with empty input
    @Test
    void testAddCourseButtonWithEmptyInput() throws NoSuchFieldException, IllegalAccessException {
        JPanel inputPanel = new JPanel();
        JTextField courseField = new JTextField();
        inputPanel.add(courseField);
        JCheckBox mixedCheckBox = new JCheckBox("Mixed");
        inputPanel.add(mixedCheckBox);

        // Simulating the user adding a course with an empty name
        courseField.setText("");

        // Access the private panel1 field using reflection
        Field panel1Field = CourseMaintenance.class.getDeclaredField("panel1");
        panel1Field.setAccessible(true);
        JPanel panel1 = (JPanel) panel1Field.get(courseMaintenance);

        int result = JOptionPane.showConfirmDialog(panel1, inputPanel, "Add New Course", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            assertThrows(SQLException.class, () -> courseMaintenance.addCourseToUser("test@example.com", "Java", true,1,"informatics",50));
        }
    }

    // Test for when no courses are found for a given email in remove course function
    @Test
    void testRemoveCourseNoCoursesFound() throws SQLException {
        String selectedEmail = "test@example.com";
        ArrayList<String> courses = new ArrayList<>();

        when(mockResultSet.next()).thenReturn(false); // No courses found

        courseMaintenance.removeCourseFromDatabase(selectedEmail, "Math 101");

        assertTrue(courses.isEmpty());
        verify(mockPreparedStatement, times(0)).executeUpdate(); // Make sure the delete doesn't happen
    }

    // Test the behavior of a valid remove course action
    @Test
    void testRemoveCourseSuccess() throws SQLException {
        String email = "test@example.com";
        String courseName = "Math 101";

        courseMaintenance.removeCourseFromDatabase(email, courseName);

        verify(mockPreparedStatement).setString(1, email);
        verify(mockPreparedStatement).setString(2, courseName);
        verify(mockPreparedStatement).executeUpdate();
    }
}
