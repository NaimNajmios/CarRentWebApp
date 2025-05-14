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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Naim Najmi
 */
public class AdminOperation {

    private static final Logger LOGGER = Logger.getLogger(AdminOperation.class.getName());

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
            LOGGER.log(Level.INFO, "Retrieving all client level users from the database.");
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

                LOGGER.log(Level.FINE, "Found client: {0}", client);
                // Add the Client object to the list
                clientList.add(client);
            }

            LOGGER.log(Level.INFO, "Successfully retrieved {0} client level users.", clientList.size());
            // Return the list of Client objects
            return clientList;

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving client level users: {0}", e.getMessage());
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
            LOGGER.log(Level.INFO, "Retrieving all admin level users from the database.");
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

                LOGGER.log(Level.FINE, "Found admin: {0}", admin);
                // Add the Admin object to the list
                adminList.add(admin);
            }

            LOGGER.log(Level.INFO, "Successfully retrieved {0} admin level users.", adminList.size());
            // Return the list of Admin objects
            return adminList;

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving admin level users: {0}", e.getMessage());
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
            LOGGER.log(Level.INFO, "Retrieving admin data for adminID: {0}", adminID);
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
                admin = new Admin();
                admin.setUserID(resultSet.getString("userID"));
                admin.setAdminID(resultSet.getString("adminID"));
                admin.setName(resultSet.getString("name"));
                admin.setEmail(resultSet.getString("email"));
                LOGGER.log(Level.FINE, "Admin found: {0}", admin);
            } else {
                LOGGER.log(Level.WARNING, "No admin found with adminID: {0}", adminID);
            }

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving admin data for adminID {0}: {1}",
                    new Object[] { adminID, e.getMessage() });
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
                LOGGER.log(Level.SEVERE, "Error closing database resources: {0}", e.getMessage());
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }

        return admin;
    }

    public boolean registerUser(String username, String plainPassword, String email,
            String fullName, String address, String phoneNumber)
            throws SQLException, Exception {

        Connection connection = null;
        boolean registrationSuccess = false;
        long newUserId = -1;

        // SQL statements
        String sqlCheckUser = "SELECT userID FROM user WHERE username = ?";
        String sqlInsertUser = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
        String sqlInsertClient = "INSERT INTO client (userID, name, address, phonenumber, email) VALUES (?, ?, ?, ?, ?)";

        try {
            LOGGER.log(Level.INFO, "Registering new user with username: {0}", username);
            connection = DatabaseConnection.getConnection();
            // Start Transaction
            connection.setAutoCommit(false);

            // 1. Check if username already exists
            try (PreparedStatement psCheck = connection.prepareStatement(sqlCheckUser)) {
                psCheck.setString(1, username);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        LOGGER.log(Level.WARNING, "Username already exists: {0}", username);
                        connection.rollback();
                        return false;
                    }
                }
            }

            // 2. Insert into 'user' table
            try (PreparedStatement psUser = connection.prepareStatement(sqlInsertUser,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
                psUser.setString(1, username);
                psUser.setString(2, plainPassword);
                psUser.setString(3, "Client");

                int userRowsAffected = psUser.executeUpdate();

                if (userRowsAffected == 0) {
                    throw new SQLException("Creating user failed, no rows affected.");
                }

                // Retrieve the generated userID
                try (ResultSet generatedKeys = psUser.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newUserId = generatedKeys.getLong(1);
                        LOGGER.log(Level.FINE, "Generated userID: {0}", newUserId);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            }

            // 3. Insert into 'client' table using the obtained userID
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

            // Commit Transaction
            connection.commit();
            registrationSuccess = true;
            LOGGER.log(Level.INFO, "User registered successfully with userID: {0}", newUserId);

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Database error during registration for user {0}: {1}",
                    new Object[] { username, e.getMessage() });
            if (connection != null) {
                try {
                    LOGGER.log(Level.WARNING, "Rolling back transaction due to error for user: {0}", username);
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed for user {0}: {1}",
                            new Object[] { username, ex.getMessage() });
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
            LOGGER.log(Level.SEVERE, "Non-DB error during registration for user {0}: {1}",
                    new Object[] { username, e.getMessage() });
            if (connection != null) {
                try {
                    LOGGER.log(Level.WARNING, "Rolling back transaction due to non-DB error for user: {0}", username);
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed for user {0}: {1}",
                            new Object[] { username, ex.getMessage() });
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    DatabaseConnection.closeConnection(connection);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing connection/resetting autoCommit: {0}", e.getMessage());
                }
            }
        }

        return registrationSuccess;
    }

    // Method to register walk-in customer, parameter of Client and User object
    public boolean registerWalkInCustomer(Client client, User user) throws SQLException, Exception {
        Connection connection = null;
        boolean registrationSuccess = false;
        long newUserId = -1;

        // User info
        String userName = user.getUsername();
        String plainPassword = "Temp123!";
        String hashedPassword = hashPasswordSHA256(plainPassword);
        String role = "Client";

        // Client details
        String clientName = client.getName();
        String address = client.getAddress();
        String phoneNumber = client.getPhoneNumber();
        String email = client.getEmail();

        try {
            LOGGER.log(Level.INFO, "Registering walk-in customer with username: {0}", userName);
            // Start Transaction
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);

            // 1. Insert into 'user' table
            String sqlInsertUser = "INSERT INTO user (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement psUser = connection.prepareStatement(sqlInsertUser,
                    PreparedStatement.RETURN_GENERATED_KEYS)) {
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
                        LOGGER.log(Level.FINE, "Generated userID for walk-in customer: {0}", newUserId);
                    } else {
                        throw new SQLException("Creating user failed, no ID obtained.");
                    }
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Database error during user insert for walk-in customer {0}: {1}",
                        new Object[] { userName, e.getMessage() });
                connection.rollback();
                throw e;
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
                LOGGER.log(Level.SEVERE, "Database error during client insert for walk-in customer {0}: {1}",
                        new Object[] { userName, e.getMessage() });
                connection.rollback();
                throw e;
            }

            // Commit Transaction
            connection.commit();
            registrationSuccess = true;
            LOGGER.log(Level.INFO, "Walk-in customer registered successfully with userID: {0}", newUserId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during registration for walk-in customer {0}: {1}",
                    new Object[] { userName, e.getMessage() });
            if (connection != null) {
                try {
                    LOGGER.log(Level.WARNING, "Rolling back transaction due to DB error for user: {0}", userName);
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed for user {0}: {1}",
                            new Object[] { userName, ex.getMessage() });
                }
            }
            throw e;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Non-DB error during registration for walk-in customer {0}: {1}",
                    new Object[] { userName, e.getMessage() });
            if (connection != null) {
                try {
                    LOGGER.log(Level.WARNING, "Rolling back transaction due to non-DB error for user: {0}", userName);
                    connection.rollback();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Rollback failed for user {0}: {1}",
                            new Object[] { userName, ex.getMessage() });
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    DatabaseConnection.closeConnection(connection);
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing connection/resetting autoCommit: {0}", e.getMessage());
                }
            }
        }
        return registrationSuccess;
    }

    // Helper method to hash a password with SHA-256
    private String hashPasswordSHA256(String password) throws NoSuchAlgorithmException {
        LOGGER.log(Level.FINE, "Hashing password for registration.");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        String hashedPassword = sb.toString();
        LOGGER.log(Level.FINE, "Password hashed successfully.");
        return hashedPassword;
    }

    // Update client details by Client object
    public boolean updateClientDetails(Client client) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            LOGGER.log(Level.INFO, "Updating client details for userID: {0}", client.getUserID());
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

            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Client details updated successfully for userID: {0}", client.getUserID());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No client found with userID: {0}", client.getUserID());
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error updating client details for userID {0}: {1}",
                    new Object[] { client.getUserID(), e.getMessage() });
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
                LOGGER.log(Level.SEVERE, "Error closing database resources: {0}", e.getMessage());
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }
    }

    // Delete client by clientID
    public boolean deleteClientByClientID(String clientID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            LOGGER.log(Level.INFO, "Deleting client with clientID: {0}", clientID);
            connection = DatabaseConnection.getConnection();

            // SQL to delete from client table
            String sql = "DELETE FROM client WHERE clientID = ?";
            // SQL to delete from user table
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
                LOGGER.log(Level.INFO, "Client deleted successfully with clientID: {0}", clientID);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No client found with clientID: {0}", clientID);
                return false;
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error deleting client with clientID {0}: {1}",
                    new Object[] { clientID, e.getMessage() });
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
                LOGGER.log(Level.SEVERE, "Error closing database resources: {0}", e.getMessage());
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }
    }

    // Method to (soft) delete client by clientID
    public boolean softDeleteClientByClientID(String clientID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            LOGGER.log(Level.INFO, "Soft deleting client with clientID: {0}", clientID);
            connection = DatabaseConnection.getConnection();

            // SQL query to soft delete client by clientID
            String sql = "UPDATE client SET isDeleted = TRUE WHERE clientID = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, clientID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Client soft deleted successfully with clientID: {0}", clientID);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No client found with clientID: {0}", clientID);
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error soft deleting client with clientID {0}: {1}",
                    new Object[] { clientID, e.getMessage() });
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
                LOGGER.log(Level.SEVERE, "Error closing database resources: {0}", e.getMessage());
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }
    }

    // Method to soft delete admin by adminID
    public boolean softDeleteAdminByAdminID(String adminID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            LOGGER.log(Level.INFO, "Soft deleting admin with adminID: {0}", adminID);
            connection = DatabaseConnection.getConnection();

            // SQL query to soft delete admin by adminID
            String sql = "UPDATE administrator SET isDeleted = TRUE WHERE adminID = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, adminID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Admin soft deleted successfully with adminID: {0}", adminID);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No admin found with adminID: {0}", adminID);
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error soft deleting admin with adminID {0}: {1}",
                    new Object[] { adminID, e.getMessage() });
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
                LOGGER.log(Level.SEVERE, "Error closing database resources: {0}", e.getMessage());
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
            LOGGER.log(Level.INFO, "Retrieving admin details for userID: {0}", userID);
            connection = DatabaseConnection.getConnection();

            // SQL query to retrieve admin details by userID
            String sql = "SELECT * FROM administrator WHERE userID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userID);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // If a record is found, create an Admin object and populate it
            if (resultSet.next()) {
                admin = new Admin();
                admin.setUserID(resultSet.getString("userID"));
                admin.setAdminID(resultSet.getString("adminID"));
                admin.setName(resultSet.getString("name"));
                admin.setEmail(resultSet.getString("email"));
                admin.setProfileImagePath(resultSet.getString("profileImagePath"));
                LOGGER.log(Level.FINE, "Admin found: {0}", admin);
            } else {
                LOGGER.log(Level.WARNING, "No admin found with userID: {0}", userID);
            }

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving admin details for userID {0}: {1}",
                    new Object[] { userID, e.getMessage() });
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
                LOGGER.log(Level.SEVERE, "Error closing database resources: {0}", e.getMessage());
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }

        return admin;
    }

    // Method to update admin details using Admin object
    public boolean updateAdminDetails(Admin admin) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            LOGGER.log(Level.INFO, "Updating admin details for adminID: {0}", admin.getAdminID());
            connection = DatabaseConnection.getConnection();

            // SQL query to update admin details
            String sql = "UPDATE administrator SET name = ?, email = ?, profileImagePath = ? WHERE adminID = ?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, admin.getName());
            preparedStatement.setString(2, admin.getEmail());
            preparedStatement.setString(3, admin.getProfileImagePath());
            preparedStatement.setString(4, admin.getAdminID());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Admin details updated successfully for adminID: {0}", admin.getAdminID());
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No admin found with adminID: {0}", admin.getAdminID());
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error updating admin details for adminID {0}: {1}",
                    new Object[] { admin.getAdminID(), e.getMessage() });
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
                LOGGER.log(Level.SEVERE, "Error closing database resources: {0}", e.getMessage());
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
            LOGGER.log(Level.INFO, "Retrieving client data for clientID: {0}", clientID);
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
                client = new Client();
                client.setUserID(resultSet.getString("userID"));
                client.setClientID(resultSet.getString("clientID"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
                client.setPhoneNumber(resultSet.getString("phoneNumber"));
                client.setEmail(resultSet.getString("email"));
                client.setProfileImagePath(resultSet.getString("profileImagePath"));
                LOGGER.log(Level.FINE, "Client found: {0}", client);
            } else {
                LOGGER.log(Level.WARNING, "No client found with clientID: {0}", clientID);
            }

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving client data for clientID {0}: {1}",
                    new Object[] { clientID, e.getMessage() });
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
                LOGGER.log(Level.SEVERE, "Error closing database resources: {0}", e.getMessage());
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }

        return client;
    }

    // Update admin profile picture
    public boolean updateAdminProfilePicture(String adminID, String profileImagePath) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            LOGGER.log(Level.INFO, "Updating admin profile picture for adminID: {0}", adminID);
            connection = DatabaseConnection.getConnection();

            // SQL query to update admin profile picture
            String sql = "UPDATE administrator SET profileImagePath = ? WHERE adminID = ?";

            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, profileImagePath);
            preparedStatement.setString(2, adminID);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                LOGGER.log(Level.INFO, "Admin profile picture updated successfully for adminID: {0}", adminID);
                return true;
            } else {
                LOGGER.log(Level.WARNING, "No admin found with adminID: {0}", adminID);
                return false;
            }

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error updating admin profile picture for adminID {0}: {1}",
                    new Object[] { adminID, e.getMessage() });
            throw new RuntimeException("Database error while updating admin profile picture: " + e.getMessage());
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
                LOGGER.log(Level.SEVERE, "Error closing database resources: {0}", e.getMessage());
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }
    }

    // Get admin profile picture
    public String getAdminProfilePicture(String adminID) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String profileImagePath = null;

        try {
            LOGGER.log(Level.INFO, "Retrieving admin profile picture for adminID: {0}", adminID);
            connection = DatabaseConnection.getConnection();

            // SQL query to retrieve admin profile picture
            String sql = "SELECT profileImagePath FROM administrator WHERE adminID = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, adminID);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                profileImagePath = resultSet.getString("profileImagePath");
                LOGGER.log(Level.FINE, "Profile picture path retrieved for adminID {0}: {1}",
                        new Object[] { adminID, profileImagePath });
            } else {
                LOGGER.log(Level.WARNING, "No profile picture found for adminID: {0}", adminID);
            }
        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving admin profile picture for adminID {0}: {1}",
                    new Object[] { adminID, e.getMessage() });
            throw new RuntimeException("Database error while retrieving admin profile picture: " + e.getMessage());
        } finally {
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
                LOGGER.log(Level.SEVERE, "Error closing database resources: {0}", e.getMessage());
                throw new RuntimeException("Error closing database resources: " + e.getMessage());
            }
        }
        return profileImagePath;
    }
}