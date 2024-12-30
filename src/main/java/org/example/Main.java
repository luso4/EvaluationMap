import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Database credentials
        String url = "jdbc:mariadb://192.168.1.248:3306/evaluationmap"; // Watch out with the IP address 40381
        String username = "root";
        String password = "";
        //teste
        // Establish connection to the database
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            System.out.println("Connected to MariaDB!");

            // Create a statement
            Statement stmt = conn.createStatement();

            // Example query to retrieve data
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String password2 = rs.getString("password"); // Included but not printed
                int director = rs.getInt("director");
                System.out.println("--------------------------");
                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Email: " + email);
                System.out.println("Password: " + password2);
                if (director != 0)
                {
                    System.out.println("Department Director");
                }
                else
                {
                    System.out.println("Course Coordinator");
                }
            }
            System.out.println("Query completed!");

        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}
