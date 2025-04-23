package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Database URL, username, and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/vehiclerentaldb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "admin";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        // Load the MySQL JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    // Method to print db details
    public void printDBDetails() {
        System.out.println("Database URL: " + DB_URL);
        System.out.println("Database User: " + DB_USER);
    }

    // Test the database connection
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            System.out.println("Database connection successful!");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
    
}
