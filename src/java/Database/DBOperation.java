package Database;

import User.Client;
import User.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

public class DBOperation {

    // Instance of DatabaseConnection to manage database connections
    private DatabaseConnection db;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    public DBOperation() {
        // Constructor
        this.db = new DatabaseConnection();
    }

    // DB Connection details print out method
    public void printDBDetails() {
        this.db.printDBDetails();
    }

    // Method to validate user login credentials
    public boolean validateUser(String username, String password) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isValid = false;

        try {
            // Get database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to check if the username and hashed password match
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // If a record is found, the credentials are valid
            if (resultSet.next()) {
                isValid = true;
                System.out.println("Login successful!");
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error during user validation: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    DatabaseConnection.closeConnection(connection);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }

        return isValid;
    }

    public boolean registerUser(String username, String plainPassword, String email,
            String fullName, String address, String phoneNumber)
            throws SQLException, Exception { // Declare specific exceptions

        Connection connection = null;
        boolean registrationSuccess = false;
        long newUserId = -1; // To store the generated userID from the 'user' table insert

        // SQL statements (adjust table and column names as needed)
        String sqlCheckUser = "SELECT userID FROM user WHERE username = ?";
        String sqlInsertUser = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        // Ensure client table columns match: userID (FK), username, address,
        // phonenumber, email, fullname
        String sqlInsertClient = "INSERT INTO client (userID, name, address, phonenumber, email) VALUES (?, ?, ?, ?, ?)";

        try {
            connection = DatabaseConnection.getConnection();
            // *** Start Transaction ***
            connection.setAutoCommit(false);

            // 1. Check if username already exists
            try (PreparedStatement psCheck = connection.prepareStatement(sqlCheckUser)) {
                psCheck.setString(1, username);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        // Username exists - rollback (though nothing done yet) and return false
                        connection.rollback();
                        System.out.println("Username '" + username + "' already exists.");
                        return false; // Indicate username is taken
                    }
                }
            }

            // 3. Insert into 'user' table
            try (PreparedStatement psUser = connection.prepareStatement(sqlInsertUser,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, username);
                psUser.setString(2, plainPassword);
                psUser.setString(3, "Client"); // Set role explicitly

                int userRowsAffected = psUser.executeUpdate();

                if (userRowsAffected == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                // Retrieve the generated userID
                try (ResultSet generatedKeys = psUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newUserId = generatedKeys.getLong(1); // Assuming userID is the first generated key (long)
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            // 4. Insert into 'client' table using the obtained userID
            if (newUserId <= 0) {
                throw new SQLException("Failed to get valid userID after user insert.");
            }

            try (PreparedStatement psClient = connection.prepareStatement(sqlInsertClient)) {
                psClient.setLong(1, newUserId); // The userID obtained from user insert
                psClient.setString(2, fullName); // Username (can be useful in client table too)
                psClient.setString(3, address);
                psClient.setString(4, phoneNumber);
                psClient.setString(5, email);

                int clientRowsAffected = psClient.executeUpdate();

                if (clientRowsAffected == 0) {
                    throw new SQLException("Creating client details failed, no rows affected.");
                }
            }

            // *** Commit Transaction ***
            connection.commit();
            registrationSuccess = true;
            System.out.println("User '" + username + "' registered successfully with userID: " + newUserId);

        } catch (SQLException | ClassNotFoundException e) {
            // Log the error server-side
            System.err.println("Database error during registration for user " + username + ": " + e.getMessage());
            e.printStackTrace(); // For detailed debugging logs
            if (connection != null) {
                try {
                    System.err.println("Rolling back transaction due to error.");
                    connection.rollback(); // *** Rollback Transaction ***
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                    ex.printStackTrace(); // Log rollback failure too
                }
            }
            // Re-throw SQLException so the servlet can handle DB errors
            if (e instanceof SQLException)
                throw (SQLException) e;
            if (e instanceof ClassNotFoundException)
                throw new SQLException("Database Driver Error", e);
            // Fallback for other caught exceptions (like from hashing)
            throw new Exception("Registration failed due to an unexpected error.", e);

        } catch (Exception e) { // Catch potential hashing errors
            System.err.println("Non-DB error during registration for user " + username + ": " + e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    System.err.println("Rolling back transaction due to non-DB error.");
                    connection.rollback(); // *** Rollback Transaction ***
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            // Re-throw the exception
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true); // Restore default mode
                    DatabaseConnection.closeConnection(connection); // Close/return connection to pool
                } catch (SQLException e) {
                    System.err.println("Error closing connection/resetting autoCommit: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return registrationSuccess;
    }

    // Get user information by username
    public User getUser(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            // Get database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to retrieve user information by username
            String sql = "SELECT * FROM user WHERE username = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // If a record is found, create a User object and return it
            if (resultSet.next()) {
                user = new User();
                user.setUserID(resultSet.getString("userID"));
                user.setUsername(resultSet.getString("username"));
                user.setRole(resultSet.getString("role"));
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while retrieving user: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    DatabaseConnection.closeConnection(connection);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }
        return user;
    }

    // Fetch user data by userID
    public User getUserDataByID(String userID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;

        try {
            // Get database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to retrieve user information by userID
            String sql = "SELECT * FROM user WHERE userID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userID);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // If a record is found, create a User object and return it
            if (resultSet.next()) {
                user.setUserID(resultSet.getString("userID"));
                user.setUsername(resultSet.getString("username"));
                user.setRole(resultSet.getString("role"));

            } else {
                System.out.println("No user found with userID: " + userID);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while retrieving user: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    DatabaseConnection.closeConnection(connection);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }
        System.out.println(user);
        return user;
    }

    // Fetch user data by userID
    public Client getClientDataByID(String userID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Client client = null;

        try {
            // Get database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to retrieve user information by userID
            String sql = "SELECT * FROM client WHERE userID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userID);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // If a record is found, create a Client object and populate it
            if (resultSet.next()) {
                client = new Client(); // Instantiate the Client object
                client.setUserID(resultSet.getString("userID"));
                client.setClientID(resultSet.getString("clientID"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
                client.setPhoneNumber(resultSet.getString("phoneNumber"));
                client.setEmail(resultSet.getString("email"));
            } else {
                System.out.println("No user found with userID: " + userID);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while retrieving user: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    DatabaseConnection.closeConnection(connection);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }

        System.out.println(client);
        return client; // Returns populated Client object or null if not found
    }

    // Get client data by clientID
    public Client getClientDataByClientID(String clientID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Client client = null;

        try {
            // Get database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to retrieve client information by clientID
            String sql = "SELECT * FROM client WHERE clientID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clientID);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // If a record is found, create a Client object and populate it
            if (resultSet.next()) {
                client = new Client(); // Instantiate the Client object
                client.setUserID(resultSet.getString("userID"));
                client.setClientID(resultSet.getString("clientID"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
                client.setPhoneNumber(resultSet.getString("phoneNumber"));
                client.setEmail(resultSet.getString("email"));
            } else {
                System.out.println("No client found with clientID: " + clientID);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while retrieving client: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    DatabaseConnection.closeConnection(connection);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }

        System.out.println(client);
        return client; // Returns populated Client object or null if not found
    }

}
