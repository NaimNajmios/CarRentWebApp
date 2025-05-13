package Database;

import User.Client;
import User.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public boolean validateUser(String username, String password) throws NoSuchAlgorithmException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isValid = false;

        try {
            // Hash the plain password using SHA-256
            String hashedPassword = hashPasswordSHA256(password);

            // Get database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to check if the username and hashed password match
            String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);

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

    // Method to check whether user is using temporary password
    public boolean isUsingTemporaryPassword(String userID) throws NoSuchAlgorithmException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean isUsing = false;

        try {
            // Get database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to check if the username and hashed password match
            String sql = "SELECT * FROM user WHERE userID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userID);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Compare to hashed temp of Temp123!
            if (resultSet.next()) {
                String hashedTemp = resultSet.getString("password");
                String hashedTemp123 = hashPasswordSHA256("Temp123!");
                if (hashedTemp.equals(hashedTemp123)) {
                    isUsing = true;
                } else {
                    isUsing = false;
                }
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

        return isUsing;
    }

    // Method to reset temporary password
    public boolean resetTemporaryPassword(String userID, String newPassword) throws NoSuchAlgorithmException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isReset = false;

        try {
            // Get database connection
            connection = DatabaseConnection.getConnection();

            // Hash the plain password using SHA-256
            String hashedPassword = hashPasswordSHA256(newPassword);

            // SQL query to check if the username and hashed password match
            String sql = "UPDATE user SET password = ? WHERE userID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, userID);

            // Execute the query
            int rowsAffected = preparedStatement.executeUpdate();

            // If a record is found, the credentials are valid
            if (rowsAffected > 0) {
                isReset = true;
                System.out.println("Password reset successful!");
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error during user validation: " + e.getMessage());
        } finally {
            // Close resources
            try {
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

        return isReset;
    }

    public boolean registerUser(String username, String plainPassword, String email,
            String fullName, String address, String phoneNumber)
            throws SQLException, Exception {

        Connection connection = null;
        boolean registrationSuccess = false;
        long newUserId = -1;

        String sqlCheckUser = "SELECT userID FROM user WHERE username = ?";
        String sqlInsertUser = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        String sqlInsertClient = "INSERT INTO client (userID, name, address, phonenumber, email) VALUES (?, ?, ?, ?, ?)";

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement psCheck = connection.prepareStatement(sqlCheckUser)) {
                psCheck.setString(1, username);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        connection.rollback();
                        System.out.println("Username '" + username + "' already exists.");
                        return false;
                    }
                }
            }

            // 🔐 Hash the plain password using SHA-256
            String hashedPassword = hashPasswordSHA256(plainPassword);

            try (PreparedStatement psUser = connection.prepareStatement(sqlInsertUser,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, username);
                psUser.setString(2, hashedPassword); // Store hashed password
                psUser.setString(3, "Client");

                int userRowsAffected = psUser.executeUpdate();
                if (userRowsAffected == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                try (ResultSet generatedKeys = psUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newUserId = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            if (newUserId <= 0) {
                throw new SQLException("Failed to get valid userID after user insert.");
            }

            try (PreparedStatement psClient = connection.prepareStatement(sqlInsertClient)) {
                psClient.setLong(1, newUserId);
                psClient.setString(2, fullName);
                psClient.setString(3, address);
                psClient.setString(4, phoneNumber);
                psClient.setString(5, email);

                int clientRowsAffected = psClient.executeUpdate();
                if (clientRowsAffected == 0) {
                    throw new SQLException("Creating client details failed, no rows affected.");
                }
            }

            connection.commit();
            registrationSuccess = true;
            System.out.println("User '" + username + "' registered successfully with userID: " + newUserId);

        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database error during registration for user " + username + ": " + e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    System.err.println("Rolling back transaction due to error.");
                    connection.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            if (e instanceof ClassNotFoundException) {
                throw new SQLException("Database Driver Error", e);
            }
            throw new Exception("Registration failed due to an unexpected error.", e);

        } catch (Exception e) {
            System.err.println("Non-DB error during registration for user " + username + ": " + e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    System.err.println("Rolling back transaction due to non-DB error.");
                    connection.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    DatabaseConnection.closeConnection(connection);
                } catch (SQLException e) {
                    System.err.println("Error closing connection/resetting autoCommit: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return registrationSuccess;
    }

    public boolean registerWalkInCustomer(String fullName, String email) throws SQLException, Exception {
        Connection connection = null;
        boolean registrationSuccess = false;
        long newUserId = -1;

        // You can generate a username (e.g., email prefix + timestamp) or leave it up to your design
        String username = email.split("@")[0] + System.currentTimeMillis(); // e.g., "john.1684222345"
        String plainPassword = "Temp123!"; // Default temporary password
        String hashedPassword = hashPasswordSHA256(plainPassword);
        String role = "Client";

        String sqlInsertUser = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        String sqlInsertClient = "INSERT INTO client (userID, name, address, phonenumber, email) VALUES (?, ?, ?, ?, ?)";

        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            // Insert into 'user' table
            try (PreparedStatement psUser = connection.prepareStatement(sqlInsertUser, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, username);
                psUser.setString(2, hashedPassword);
                psUser.setString(3, role);

                int rows = psUser.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("User insert failed, no rows affected.");
                }

                try (ResultSet generatedKeys = psUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newUserId = generatedKeys.getLong(1);
                    } else {
                        throw new SQLException("No userID returned.");
                    }
                }
            }

            // Insert into 'client' table with minimal info
            try (PreparedStatement psClient = connection.prepareStatement(sqlInsertClient)) {
                psClient.setLong(1, newUserId);
                psClient.setString(2, fullName);
                psClient.setString(3, ""); // Address not provided
                psClient.setString(4, ""); // Phone not provided
                psClient.setString(5, email);

                int clientRows = psClient.executeUpdate();
                if (clientRows == 0) {
                    throw new SQLException("Client insert failed.");
                }
            }

            connection.commit();
            registrationSuccess = true;
            System.out.println("Walk-in customer registered successfully: " + fullName + " (" + email + ")");
            System.out.println("Temporary Username: " + username + " | Password: " + plainPassword);

        } catch (Exception e) {
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);
                DatabaseConnection.closeConnection(connection);
            }
        }

        return registrationSuccess;
    }

// Helper method to hash a password with SHA-256
    private String hashPasswordSHA256(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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
                client.setProfileImagePath(resultSet.getString("profileImagePath"));
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
