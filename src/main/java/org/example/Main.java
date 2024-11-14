package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//teste 40381
//test 44107
public class Main {
    public static void main(String[] args) {
        // Replace these with your actual MySQL database details
        String url = "jdbc:mysql://localhost:3306/evaluationmap";
        String username = "your_username";
        String password = "your_password";

        // Attempt to connect to the database
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (connection != null) {
                System.out.println("Connected to the database successfully!");
            } else {
                System.out.println("Failed to make a connection!");
            }
        } catch (SQLException e) {
            System.out.println("An error occurred while connecting to the database:");
            e.printStackTrace();
        }
    }
}
