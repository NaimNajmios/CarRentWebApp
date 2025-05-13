/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import User.Client;
import User.User;
import User.Admin;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Naim Najmi
 */
public class AdminOperation {

    private User user;
    private Client client;
    private Admin admin;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    // Database connection
    private DatabaseConnection db = new DatabaseConnection();

    // Constructor
    public AdminOperation() {
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    // Get all client level users in List
    public List<Client> getAllClientLevelUsers() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        Client client = null;

        try {
            // Get database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to retrieve all client level users
            String sql = "SELECT * FROM client WHERE isDeleted = FALSE;";
            preparedStatement = connection.prepareStatement(sql);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Create a list to hold the Client objects
            List<Client> clientList = new java.util.ArrayList<>();

            // Iterate through the result set and create Client objects
            while (resultSet.next()) {
                client = new Client();
                client.setUserID(resultSet.getString("userID"));
                client.setClientID(resultSet.getString("clientID"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
                client.setPhoneNumber(resultSet.getString("phoneNumber"));
                client.setEmail(resultSet.getString("email"));

                // Add the Client object to the list
                clientList.add(client); // Add the Client object to the list
            }

            // Return the list of Client objects
            return clientList;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while retrieving users: " + e.getMessage());
        } finally {
            // Close resources
            db.closeConnection(connection);
        }
    }

    // Get all admin users level in List
    public List<Admin> getAllAdminLevelUsers() throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        Admin admin = null;

        try {
            // Get database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to retrieve all admin level users
            String sql = "SELECT * FROM administrator WHERE isDeleted = FALSE;";
            preparedStatement = connection.prepareStatement(sql);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Create a list to hold the Admin objects
            List<Admin> adminList = new java.util.ArrayList<>();

            // Iterate through the result set and create Admin objects
            while (resultSet.next()) {
                admin = new Admin();
                admin.setUserID(resultSet.getString("userID"));
                admin.setAdminID(resultSet.getString("adminID"));
                admin.setName(resultSet.getString("name"));
                admin.setEmail(resultSet.getString("email"));

                // Add the Admin object to the list
                adminList.add(admin); // Add the Admin object to the list
            }

            // Return the list of Admin objects
            return adminList;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while retrieving users: " + e.getMessage());
        } finally {
            // Close resources
            db.closeConnection(connection);
        }
    }

    // Get admin data by adminID
    public Admin getAdminDataByAdminID(String adminID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Admin admin = null;

        try {
            // Get database connection
            connection = DatabaseConnection.getConnection();

            // SQL query to retrieve admin information by adminID
            String sql = "SELECT * FROM administrator WHERE adminID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, adminID);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // If a record is found, create an Admin object and populate it
            if (resultSet.next()) {
                admin = new Admin(); // Instantiate the Admin object
                admin.setUserID(resultSet.getString("userID"));
                admin.setAdminID(resultSet.getString("adminID"));
                admin.setName(resultSet.getString("name"));
                admin.setEmail(resultSet.getString("email"));
            } else {
                System.out.println("No admin found with adminID: " + adminID);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while retrieving admin: " + e.getMessage());
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

        System.out.println(admin);
        return admin; // Returns populated Admin object or null if not found
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
            if (e instanceof SQLException) {
                throw (SQLException) e;
            }
            if (e instanceof ClassNotFoundException) {
                throw new SQLException("Database Driver Error", e);
            }
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

    // Method to register walk in customer, parameter of Client and User object
    public boolean registerWalkInCustomer(Client client, User user) throws SQLException, Exception {

        Connection connection = null;
        boolean registrationSuccess = false;
        long newUserId = -1;

        // User info
        String userName = user.getUsername();
        String plainPassword = "Temp123!"; // Default temporary password
        String hashedPassword = hashPasswordSHA256(plainPassword);
        String role = "Client";

        // Client details
        String clientName = client.getName();
        String address = client.getAddress();
        String phoneNumber = client.getPhoneNumber();
        String email = client.getEmail();

        try {
            // *** Start Transaction ***
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            // 1. Insert into 'user' table
            String sqlInsertUser = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement psUser = connection.prepareStatement(sqlInsertUser, PreparedStatement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, userName);
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
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                System.err.println("Database error during user insert: " + e.getMessage());
                e.printStackTrace();
                connection.rollback(); // *** Rollback Transaction ***
                throw e; // Rethrow the SQLException
            }

            // 2. Insert into 'client' table
            String sqlInsertClient = "INSERT INTO client (userID, name, address, phonenumber, email) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement psClient = connection.prepareStatement(sqlInsertClient)) {
                psClient.setLong(1, newUserId);
                psClient.setString(2, clientName);
                psClient.setString(3, address);
                psClient.setString(4, phoneNumber);
                psClient.setString(5, email);

                int rows = psClient.executeUpdate();
                if (rows == 0) {
                    throw new SQLException("Client insert failed, no rows affected.");
                }
            } catch (SQLException e) {
                System.err.println("Database error during client insert: " + e.getMessage());
                e.printStackTrace();
                connection.rollback(); // *** Rollback Transaction ***
                throw e; // Rethrow the SQLException
            }

            // *** Commit Transaction ***
            connection.commit();
            registrationSuccess = true;
        } catch (SQLException e) {
            System.err.println("Database error during registration for user " + userName + ": " + e.getMessage());
            e.printStackTrace();
            if (connection != null) {
                try {
                    System.err.println("Rolling back transaction due to DB error.");
                    connection.rollback(); // *** Rollback Transaction ***
                } catch (SQLException ex) {
                    System.err.println("Rollback failed: " + ex.getMessage());
                    ex.printStackTrace();
                }
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

    // Update client details by Client object
    public boolean updateClientDetails(Client client) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // Log
        System.out.println("Updating client details for userID: " + client.getUserID());

        try {
            connection = DatabaseConnection.getConnection();

            // SQL query to update client details
            String sql = "UPDATE client SET name = ?, address = ?, phonenumber = ?, email = ? WHERE clientID = ?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getAddress());
            preparedStatement.setString(3, client.getPhoneNumber());
            preparedStatement.setString(4, client.getEmail());
            preparedStatement.setString(5, client.getUserID());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0; // Returns true if update was successful

        } catch (SQLException | ClassNotFoundException e) {
            // Log the error server-side
            throw new RuntimeException("Database error while updating client details: " + e.getMessage());

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

    }

    // Delete client by clientID
    public boolean deleteClientByClientID(String clientID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            // SQL query to delete client by clientID
            // SQL to delete from client table
            String sql = "DELETE FROM client WHERE clientID = ?";
            // SQl to delete from user table
            String sql2 = "DELETE FROM user WHERE userID = (SELECT userID FROM client WHERE clientID = ?)";

            // Prepare statement for client deletion
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clientID);

            int rowsAffected = preparedStatement.executeUpdate();

            // Prepare statement for user deletion
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
            preparedStatement2.setString(1, clientID);
            int rowsAffected2 = preparedStatement2.executeUpdate();

            // Check if any rows were affected
            if (rowsAffected > 0 && rowsAffected2 > 0) {
                System.out.println("Client with clientID " + clientID + " deleted successfully.");
                return true; // Client deleted successfully
            } else {
                System.out.println("No client found with clientID: " + clientID);
                return false; // No client found with the given clientID
            } // Close resources
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while deleting client: " + e.getMessage());
        } finally {
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

    }

    // Method to (soft) delete client by clientID
    public boolean softDeleteClientByClientID(String clientID) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            // SQL query to soft delete client by clientID
            String sql = "UPDATE client SET isDeleted = TRUE WHERE clientID = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clientID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Client with clientID " + clientID + " soft deleted successfully.");
                return true; // Client soft deleted successfully
            } else {
                System.out.println("No client found with clientID: " + clientID);
                return false; // No client found with the given clientID
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while soft deleting client: " + e.getMessage());
        } finally {
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
    }

    // Method to soft delete admin by adminID
    public boolean softDeleteAdminByAdminID(String adminID) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DatabaseConnection.getConnection();

            // SQL query to soft delete admin by adminID
            String sql = "UPDATE administrator SET isDeleted = TRUE WHERE adminID = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, adminID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Admin with adminID " + adminID + " soft deleted successfully.");
                return true; // Admin soft deleted successfully
            } else {
                System.out.println("No admin found with adminID: " + adminID);
                return false; // No admin found with the given adminID
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while soft deleting admin: " + e.getMessage());
        } finally {
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
    }

    // Method to get admin details by userID
    public Admin getAdminDetailsByUserID(String userID) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Admin admin = null;

        try {
            connection = DatabaseConnection.getConnection();

            // SQL query to retrieve admin details by userID
            String sql = "SELECT * FROM administrator WHERE userID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userID);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // If a record is found, create an Admin object and populate it
            if (resultSet.next()) {
                admin = new Admin(); // Instantiate the Admin object
                admin.setUserID(resultSet.getString("userID"));
                admin.setAdminID(resultSet.getString("adminID"));
                admin.setName(resultSet.getString("name"));
                admin.setEmail(resultSet.getString("email"));
                admin.setProfileImagePath(resultSet.getString("profileImagePath"));
            } else {
                System.out.println("No admin found with userID: " + userID);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while retrieving admin details: " + e.getMessage());
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

        return admin;
    }

    // Method to update admin details using Admin object
    public boolean updateAdminDetails(Admin admin) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        // Log
        System.out.println("Updating admin details for adminID: " + admin.getAdminID());

        try {
            connection = DatabaseConnection.getConnection();

            // SQL query to update admin details
            String sql = "UPDATE administrator SET name = ? WHERE adminID = ?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, admin.getName());
            preparedStatement.setString(2, admin.getAdminID());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Admin details updated successfully.");
                return true;
            } else {
                System.out.println("No admin found with adminID: " + admin.getAdminID());
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Database error while updating admin details: " + e.getMessage());
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
