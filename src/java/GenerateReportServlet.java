/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Database.DatabaseConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Naim Najmi
 */
public class GenerateReportServlet extends HttpServlet {

    // Initialize logger for the servlet
    private static final Logger LOGGER = Logger.getLogger(GenerateReportServlet.class.getName());

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws SQLException if a database error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        // Log incoming request details
        LOGGER.log(Level.INFO, "Processing request: Method={0}, URI={1}",
                new Object[]{request.getMethod(), request.getRequestURI()});

        // Extract form parameters
        String reportCategory = request.getParameter("reportCategory");
        String reportType = request.getParameter("reportType");
        String dateRangeType = request.getParameter("dateRangeType");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String role = request.getParameter("role");

        // Log form parameters
        LOGGER.log(Level.INFO, "Form Parameters: reportCategory={0}, reportType={1}, dateRangeType={2}, startDate={3}, endDate={4}, role={5}",
                new Object[]{reportCategory, reportType, dateRangeType, startDate, endDate, role});

        // Validate report category
        if (!"user".equals(reportCategory)) {
            LOGGER.log(Level.WARNING, "Invalid report category: {0}. Expected 'user'.", reportCategory);
            request.setAttribute("error", "This servlet only handles user reports.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Validate and parse dates
        LocalDate start, end;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LOGGER.log(Level.INFO, "Today's date: {0}", today);

        switch (dateRangeType) {
            case "this_month":
                start = today.withDayOfMonth(1);
                end = today;
                break;
            case "last_month":
                LocalDate lastMonth = today.minusMonths(1);
                start = lastMonth.withDayOfMonth(1);
                end = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());
                break;
            case "last_7_days":
                start = today.minusDays(7);
                end = today;
                break;
            case "custom":
            default:
                try {
                start = LocalDate.parse(startDate, formatter);
                end = LocalDate.parse(endDate, formatter);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to parse dates: startDate={0}, endDate={1}, Error={2}",
                        new Object[]{startDate, endDate, e.getMessage()});
                request.setAttribute("error", "Invalid date format. Please use YYYY-MM-DD.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            break;
        }

        // Log calculated date range
        LOGGER.log(Level.INFO, "Calculated date range: start={0}, end={1}", new Object[]{start, end});

        // Prepare parameters for SQL query
        List<Object> params = new ArrayList<>();
        params.add(start.toString());
        params.add(end.toString());

        // Build query and collect parameters for "Detailed Registrations"
        String query = "";
        Map<String, String> headers = new HashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();

        Connection conn = null;
        try {
            // Get database connection
            LOGGER.log(Level.INFO, "Establishing database connection...");
            conn = DatabaseConnection.getConnection();
            LOGGER.log(Level.INFO, "Database connection established successfully.");

            // Handle "Detailed Registrations" report
            if (!role.isEmpty()) {
                params.add(role);
            }

            query = "SELECT u.userID, u.username, u.password, u.role, u.createdAt, "
                    + "c.name, c.email, c.phoneNumber, a.email AS adminEmail "
                    + "FROM user u "
                    + "LEFT JOIN client c ON u.userID = c.userID "
                    + "LEFT JOIN administrator a ON u.userID = a.userID "
                    + "WHERE u.createdAt BETWEEN ? AND ? "
                    + (role.isEmpty() ? "" : "AND u.role = ?");

            // Log the query and parameters
            LOGGER.log(Level.INFO, "Executing query: {0}", query);
            LOGGER.log(Level.INFO, "Query parameters: {0}", params);

            headers.put("userID", "User ID");
            headers.put("username", "Username");
            headers.put("password", "Password");
            headers.put("role", "Role");
            headers.put("createdAt", "Created At");
            headers.put("name", "Name");
            headers.put("email", "Client Email");
            headers.put("phoneNumber", "Phone Number");
            headers.put("adminEmail", "Admin Email");

            // Execute query and collect results
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (String column : headers.keySet()) {
                            row.put(column, rs.getObject(column));
                        }
                        results.add(row);
                    }
                }
            }

            // Log the number of results
            LOGGER.log(Level.INFO, "Query executed successfully. Number of results: {0}", results.size());

            // Set attributes and forward to JSP for rendering
            request.setAttribute("reportTitle", "USER - DETAILED REGISTRATIONS");
            request.setAttribute("headers", headers);
            request.setAttribute("results", results);
            LOGGER.log(Level.INFO, "Forwarding to /admin/reportResults.jsp");
            // Log to be fetched data
            LOGGER.log(Level.INFO, "reportTitle set to: {0}", request.getAttribute("reportTitle"));
            LOGGER.log(Level.INFO, "headers set to: {0}", headers);
            LOGGER.log(Level.INFO, "results set to: {0}", results);
            request.getRequestDispatcher("/admin/reportResults.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Database error occurred: {0}", e.getMessage());
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } finally {
            if (conn != null) {
                try {
                    LOGGER.log(Level.INFO, "Closing database connection...");
                    DatabaseConnection.closeConnection(conn);
                    LOGGER.log(Level.INFO, "Database connection closed successfully.");
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Error closing database connection: {0}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQLException in doGet: {0}", ex.getMessage());
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQLException in doPost: {0}", ex.getMessage());
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for generating user reports";
    }// </editor-fold>
}
