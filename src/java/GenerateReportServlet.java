/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Database.DatabaseConnection;
import java.io.IOException;
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
        String role = request.getParameter("role"); // For user reports
        String bookingStatus = request.getParameter("bookingStatus"); // For booking reports
        String vehicleId = request.getParameter("vehicleId"); // For booking and vehiclemaintenance reports
        String category = request.getParameter("category"); // For vehicle reports
        String availability = request.getParameter("availability"); // For vehicle reports
        String paymentType = request.getParameter("paymentType"); // For payment reports
        String paymentStatus = request.getParameter("paymentStatus"); // For payment reports
        String maintenanceStatus = request.getParameter("maintenanceStatus"); // For vehiclemaintenance reports

        // Log form parameters
        LOGGER.log(Level.INFO, "Form Parameters: reportCategory={0}, reportType={1}, dateRangeType={2}, startDate={3}, endDate={4}, role={5}, bookingStatus={6}, vehicleId={7}, category={8}, availability={9}, paymentType={10}, paymentStatus={11}, maintenanceStatus={12}",
                new Object[]{reportCategory, reportType, dateRangeType, startDate, endDate, role, bookingStatus, vehicleId, category, availability, paymentType, paymentStatus, maintenanceStatus});

        // Validate report category and type
        if (reportCategory == null || reportType == null) {
            LOGGER.log(Level.WARNING, "Missing report category or type: reportCategory={0}, reportType={1}",
                    new Object[]{reportCategory, reportType});
            request.setAttribute("error", "Report category and type are required.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Validate and parse dates
        LocalDate start, end;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate today = LocalDate.now();
        LOGGER.log(Level.INFO, "Today's date: {0}", today);

        switch (dateRangeType != null ? dateRangeType : "custom") {
            case "today":
                start = today;
                end = today;
                break;
            case "yesterday":
                start = today.minusDays(1);
                end = today.minusDays(1);
                break;
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

        // Build query and collect parameters based on report category and type
        String query = "";
        String reportTitle = "";
        Map<String, String> headers = new HashMap<>();
        List<Map<String, Object>> results = new ArrayList<>();

        Connection conn = null;
        try {
            // Get database connection
            LOGGER.log(Level.INFO, "Establishing database connection...");
            conn = DatabaseConnection.getConnection();
            LOGGER.log(Level.INFO, "Database connection established successfully.");

            // Handle different report categories and types
            switch (reportCategory) {
                case "user":
                    switch (reportType) {
                        case "detailed_registrations":
                            reportTitle = "USER - DETAILED REGISTRATIONS";
                            if (role != null && !role.isEmpty()) {
                                params.add(role);
                            }
                            query = "SELECT u.userID, u.username, u.password, u.role, u.createdAt, "
                                    + "c.name, c.email, c.phoneNumber, a.email AS adminEmail "
                                    + "FROM user u "
                                    + "LEFT JOIN client c ON u.userID = c.userID "
                                    + "LEFT JOIN administrator a ON u.userID = a.userID "
                                    + "WHERE u.createdAt BETWEEN ? AND ? "
                                    + (role != null && !role.isEmpty() ? "AND u.role = ?" : "");
                            headers.put("userID", "User ID");
                            headers.put("username", "Username");
                            headers.put("password", "Password");
                            headers.put("role", "Role");
                            headers.put("createdAt", "Created At");
                            headers.put("name", "Name");
                            headers.put("email", "Client Email");
                            headers.put("phoneNumber", "Phone Number");
                            headers.put("adminEmail", "Admin Email");
                            break;

                        case "summary":
                            reportTitle = "USER - SUMMARY";
                            query = "SELECT u.role, COUNT(*) AS userCount "
                                    + "FROM user u "
                                    + "WHERE u.createdAt BETWEEN ? AND ? "
                                    + "GROUP BY u.role";
                            headers.put("role", "Role");
                            headers.put("userCount", "User Count");
                            break;

                        default:
                            LOGGER.log(Level.WARNING, "Invalid report type for user category: {0}", reportType);
                            request.setAttribute("error", "Invalid report type for user category.");
                            request.getRequestDispatcher("/error.jsp").forward(request, response);
                            return;
                    }
                    break;

                case "vehicle":
                    switch (reportType) {
                        case "vehicle_list":
                        case "detailed_inventory": // Treat vehicle_list as detailed_inventory for consistency
                            reportTitle = "VEHICLE - DETAILED INVENTORY";
                            StringBuilder vehicleQuery = new StringBuilder(
                                    "SELECT v.vehicleID, v.model, v.brand, v.manufacturingYear, v.availability, "
                                    + "v.category, v.fuelType, v.transmissionType, v.mileage, v.ratePerDay "
                                    + "FROM vehicles v "
                                    + "WHERE 1=1 ");
                            // No createdAt in vehicles table, so date range filtering is not applicable here
                            // If you meant to filter by a different date (e.g., booking dates), adjust accordingly
                            if (category != null && !category.isEmpty()) {
                                vehicleQuery.append(" AND v.category = ?");
                                params.add(category);
                            }
                            if (availability != null && !availability.isEmpty()) {
                                vehicleQuery.append(" AND v.availability = ?");
                                params.add(availability.equals("1") ? 1 : 0);
                            }
                            query = vehicleQuery.toString();
                            headers.put("vehicleID", "Vehicle ID");
                            headers.put("model", "Model");
                            headers.put("brand", "Brand");
                            headers.put("manufacturingYear", "Manufacturing Year");
                            headers.put("availability", "Availability");
                            headers.put("category", "Category");
                            headers.put("fuelType", "Fuel Type");
                            headers.put("transmissionType", "Transmission Type");
                            headers.put("mileage", "Mileage");
                            headers.put("ratePerDay", "Rate Per Day");
                            break;

                        case "availability_summary":
                            reportTitle = "VEHICLE - AVAILABILITY SUMMARY";
                            query = "SELECT v.availability, COUNT(*) AS vehicleCount "
                                    + "FROM vehicles v "
                                    + "WHERE 1=1 "
                                    + (category != null && !category.isEmpty() ? "AND v.category = ? " : "")
                                    + "GROUP BY v.availability";
                            if (category != null && !category.isEmpty()) {
                                params.add(category);
                            }
                            headers.put("availability", "Availability");
                            headers.put("vehicleCount", "Vehicle Count");
                            break;

                        case "usage_summary":
                            reportTitle = "VEHICLE - USAGE SUMMARY";
                            StringBuilder usageQuery = new StringBuilder(
                                    "SELECT v.vehicleID, v.model, v.category, COUNT(bv.bookingID) AS bookingCount "
                                    + "FROM vehicles v "
                                    + "LEFT JOIN bookingvehicle bv ON v.vehicleID = bv.vehicleID "
                                    + "LEFT JOIN booking b ON bv.bookingID = b.bookingID "
                                    + "AND b.startDate BETWEEN ? AND ? "
                                    + "WHERE 1=1 ");
                            if (category != null && !category.isEmpty()) {
                                usageQuery.append("AND v.category = ? ");
                                params.add(category);
                            }
                            if (availability != null && !availability.isEmpty()) {
                                usageQuery.append("AND v.availability = ? ");
                                params.add(availability.equals("1") ? 1 : 0);
                            }
                            usageQuery.append("GROUP BY v.vehicleID, v.model, v.category");
                            query = usageQuery.toString();
                            headers.put("vehicleID", "Vehicle ID");
                            headers.put("model", "Model");
                            headers.put("category", "Category");
                            headers.put("bookingCount", "Booking Count");
                            break;

                        case "mileage_report":
                            reportTitle = "VEHICLE - MILEAGE REPORT";
                            StringBuilder mileageQuery = new StringBuilder(
                                    "SELECT v.vehicleID, v.model, v.category, v.mileage "
                                    + "FROM vehicles v "
                                    + "WHERE 1=1 ");
                            if (category != null && !category.isEmpty()) {
                                mileageQuery.append("AND v.category = ? ");
                                params.add(category);
                            }
                            if (availability != null && !availability.isEmpty()) {
                                mileageQuery.append("AND v.availability = ? ");
                                params.add(availability.equals("1") ? 1 : 0);
                            }
                            query = mileageQuery.toString();
                            headers.put("vehicleID", "Vehicle ID");
                            headers.put("model", "Model");
                            headers.put("category", "Category");
                            headers.put("mileage", "Mileage");
                            break;

                        case "rental_performance":
                            reportTitle = "VEHICLE - RENTAL PERFORMANCE";
                            StringBuilder perfQuery = new StringBuilder(
                                    "SELECT v.vehicleID, v.model, v.category, SUM(b.totalCost) AS totalRevenue, COUNT(bv.bookingID) AS bookingCount "
                                    + "FROM vehicles v "
                                    + "LEFT JOIN bookingvehicle bv ON v.vehicleID = bv.vehicleID "
                                    + "LEFT JOIN booking b ON bv.bookingID = b.bookingID "
                                    + "AND b.startDate BETWEEN ? AND ? "
                                    + "WHERE 1=1 ");
                            if (category != null && !category.isEmpty()) {
                                perfQuery.append("AND v.category = ? ");
                                params.add(category);
                            }
                            if (availability != null && !availability.isEmpty()) {
                                perfQuery.append("AND v.availability = ? ");
                                params.add(availability.equals("1") ? 1 : 0);
                            }
                            perfQuery.append("GROUP BY v.vehicleID, v.model, v.category");
                            query = perfQuery.toString();
                            headers.put("vehicleID", "Vehicle ID");
                            headers.put("model", "Model");
                            headers.put("category", "Category");
                            headers.put("totalRevenue", "Total Revenue");
                            headers.put("bookingCount", "Booking Count");
                            break;

                        default:
                            LOGGER.log(Level.WARNING, "Invalid report type for vehicle category: {0}", reportType);
                            request.setAttribute("error", "Invalid report type for vehicle category.");
                            request.getRequestDispatcher("/error.jsp").forward(request, response);
                            return;
                    }
                    break;

                case "booking":
                    switch (reportType) {
                        case "detailed_bookings":
                            reportTitle = "BOOKING - DETAILED BOOKINGS";
                            StringBuilder bookingQuery = new StringBuilder(
                                    "SELECT b.bookingID, bv.vehicleID, v.model, b.clientID, c.name AS clientName, "
                                    + "b.startDate, b.endDate, b.totalCost, b.bookingStatus "
                                    + "FROM booking b "
                                    + "JOIN bookingvehicle bv ON b.bookingID = bv.bookingID "
                                    + "JOIN vehicles v ON bv.vehicleID = v.vehicleID "
                                    + "JOIN client c ON b.clientID = c.clientID "
                                    + "WHERE b.startDate BETWEEN ? AND ? ");
                            if (bookingStatus != null && !bookingStatus.isEmpty()) {
                                bookingQuery.append("AND b.bookingStatus = ? ");
                                params.add(bookingStatus);
                            }
                            if (vehicleId != null && !vehicleId.isEmpty()) {
                                bookingQuery.append("AND bv.vehicleID = ? ");
                                params.add(vehicleId);
                            }
                            query = bookingQuery.toString();
                            headers.put("bookingID", "Booking ID");
                            headers.put("vehicleID", "Vehicle ID");
                            headers.put("model", "Vehicle Model");
                            headers.put("clientID", "Client ID");
                            headers.put("clientName", "Client Name");
                            headers.put("startDate", "Start Date");
                            headers.put("endDate", "End Date");
                            headers.put("totalCost", "Total Cost");
                            headers.put("bookingStatus", "Status");
                            break;

                        case "booking_summary":
                            reportTitle = "BOOKING - SUMMARY";
                            StringBuilder summaryQuery = new StringBuilder(
                                    "SELECT b.bookingStatus, COUNT(*) AS bookingCount "
                                    + "FROM booking b "
                                    + "JOIN bookingvehicle bv ON b.bookingID = bv.bookingID "
                                    + "WHERE b.startDate BETWEEN ? AND ? ");
                            if (vehicleId != null && !vehicleId.isEmpty()) {
                                summaryQuery.append("AND bv.vehicleID = ? ");
                                params.add(vehicleId);
                            }
                            summaryQuery.append("GROUP BY b.bookingStatus");
                            query = summaryQuery.toString();
                            headers.put("bookingStatus", "Status");
                            headers.put("bookingCount", "Booking Count");
                            break;

                        case "bookings_by_client":
                            reportTitle = "BOOKING - BOOKINGS BY CLIENT";
                            StringBuilder clientQuery = new StringBuilder(
                                    "SELECT b.clientID, c.name AS clientName, COUNT(b.bookingID) AS bookingCount, SUM(b.totalCost) AS totalSpent "
                                    + "FROM booking b "
                                    + "JOIN bookingvehicle bv ON b.bookingID = bv.bookingID "
                                    + "JOIN client c ON b.clientID = c.clientID "
                                    + "WHERE b.startDate BETWEEN ? AND ? ");
                            if (bookingStatus != null && !bookingStatus.isEmpty()) {
                                clientQuery.append("AND b.bookingStatus = ? ");
                                params.add(bookingStatus);
                            }
                            if (vehicleId != null && !vehicleId.isEmpty()) {
                                clientQuery.append("AND bv.vehicleID = ? ");
                                params.add(vehicleId);
                            }
                            clientQuery.append("GROUP BY b.clientID, c.name");
                            query = clientQuery.toString();
                            headers.put("clientID", "Client ID");
                            headers.put("clientName", "Client Name");
                            headers.put("bookingCount", "Booking Count");
                            headers.put("totalSpent", "Total Spent");
                            break;

                        default:
                            LOGGER.log(Level.WARNING, "Invalid report type for booking category: {0}", reportType);
                            request.setAttribute("error", "Invalid report type for booking category.");
                            request.getRequestDispatcher("/error.jsp").forward(request, response);
                            return;
                    }
                    break;

                case "payment":
                    switch (reportType) {
                        case "payment_details":
                            reportTitle = "PAYMENT - DETAILS";
                            StringBuilder paymentQuery = new StringBuilder(
                                    "SELECT p.paymentID, p.bookingID, b.clientID, c.name AS clientName, p.paymentDate, "
                                    + "p.amount, p.paymentType, p.paymentStatus "
                                    + "FROM payment p "
                                    + "JOIN booking b ON p.bookingID = b.bookingID "
                                    + "JOIN client c ON b.clientID = c.clientID "
                                    + "WHERE p.paymentDate BETWEEN ? AND ? ");
                            if (paymentType != null && !paymentType.isEmpty()) {
                                paymentQuery.append("AND p.paymentType = ? ");
                                params.add(paymentType);
                            }
                            if (paymentStatus != null && !paymentStatus.isEmpty()) {
                                paymentQuery.append("AND p.paymentStatus = ? ");
                                params.add(paymentStatus);
                            }
                            query = paymentQuery.toString();
                            headers.put("paymentID", "Payment ID");
                            headers.put("bookingID", "Booking ID");
                            headers.put("clientID", "Client ID");
                            headers.put("clientName", "Client Name");
                            headers.put("paymentDate", "Payment Date");
                            headers.put("amount", "Amount");
                            headers.put("paymentType", "Payment Type");
                            headers.put("paymentStatus", "Status");
                            break;

                        case "payment_summary":
                            reportTitle = "PAYMENT - SUMMARY";
                            StringBuilder paymentSummaryQuery = new StringBuilder(
                                    "SELECT p.paymentStatus, COUNT(*) AS paymentCount, SUM(p.amount) AS totalAmount "
                                    + "FROM payment p "
                                    + "WHERE p.paymentDate BETWEEN ? AND ? ");
                            if (paymentType != null && !paymentType.isEmpty()) {
                                paymentSummaryQuery.append("AND p.paymentType = ? ");
                                params.add(paymentType);
                            }
                            paymentSummaryQuery.append("GROUP BY p.paymentStatus");
                            query = paymentSummaryQuery.toString();
                            headers.put("paymentStatus", "Status");
                            headers.put("paymentCount", "Payment Count");
                            headers.put("totalAmount", "Total Amount");
                            break;

                        case "revenue_by_payment_type":
                            reportTitle = "PAYMENT - REVENUE BY PAYMENT TYPE";
                            query = "SELECT p.paymentType, SUM(p.amount) AS totalRevenue "
                                    + "FROM payment p "
                                    + "WHERE p.paymentDate BETWEEN ? AND ? "
                                    + (paymentStatus != null && !paymentStatus.isEmpty() ? "AND p.paymentStatus = ? " : "")
                                    + "GROUP BY p.paymentType";
                            if (paymentStatus != null && !paymentStatus.isEmpty()) {
                                params.add(paymentStatus);
                            }
                            headers.put("paymentType", "Payment Type");
                            headers.put("totalRevenue", "Total Revenue");
                            break;

                        default:
                            LOGGER.log(Level.WARNING, "Invalid report type for payment category: {0}", reportType);
                            request.setAttribute("error", "Invalid report type for payment category.");
                            request.getRequestDispatcher("/error.jsp").forward(request, response);
                            return;
                    }
                    break;

                case "maintenance":
                    switch (reportType) {
                        case "maintenance_schedule":
                            reportTitle = "MAINTENANCE - SCHEDULE";
                            StringBuilder scheduleQuery = new StringBuilder(
                                    "SELECT m.maintenanceID, m.vehicleID, v.model, m.maintenanceDate, m.maintenanceDescription, m.maintenanceStatus "
                                    + "FROM vehiclemaintenance m "
                                    + "JOIN vehicles v ON m.vehicleID = v.vehicleID "
                                    + "WHERE m.maintenanceDate BETWEEN ? AND ? ");
                            if (maintenanceStatus != null && !maintenanceStatus.isEmpty()) {
                                scheduleQuery.append("AND m.maintenanceStatus = ? ");
                                params.add(maintenanceStatus);
                            }
                            if (vehicleId != null && !vehicleId.isEmpty()) {
                                scheduleQuery.append("AND m.vehicleID = ? ");
                                params.add(vehicleId);
                            }
                            query = scheduleQuery.toString();
                            headers.put("maintenanceID", "Maintenance ID");
                            headers.put("vehicleID", "Vehicle ID");
                            headers.put("model", "Vehicle Model");
                            headers.put("maintenanceDate", "Maintenance Date");
                            headers.put("maintenanceDescription", "Description");
                            headers.put("maintenanceStatus", "Status");
                            break;

                        case "maintenance_history":
                            reportTitle = "MAINTENANCE - HISTORY";
                            StringBuilder historyQuery = new StringBuilder(
                                    "SELECT m.maintenanceID, m.vehicleID, v.model, m.maintenanceDate, m.maintenanceDescription, m.maintenanceCost, m.maintenanceStatus "
                                    + "FROM vehiclemaintenance m "
                                    + "JOIN vehicles v ON m.vehicleID = v.vehicleID "
                                    + "WHERE m.maintenanceDate BETWEEN ? AND ? ");
                            if (maintenanceStatus != null && !maintenanceStatus.isEmpty()) {
                                historyQuery.append("AND m.maintenanceStatus = ? ");
                                params.add(maintenanceStatus);
                            }
                            if (vehicleId != null && !vehicleId.isEmpty()) {
                                historyQuery.append("AND m.vehicleID = ? ");
                                params.add(vehicleId);
                            }
                            query = historyQuery.toString();
                            headers.put("maintenanceID", "Maintenance ID");
                            headers.put("vehicleID", "Vehicle ID");
                            headers.put("model", "Vehicle Model");
                            headers.put("maintenanceDate", "Maintenance Date");
                            headers.put("maintenanceDescription", "Description");
                            headers.put("maintenanceCost", "Cost");
                            headers.put("maintenanceStatus", "Status");
                            break;

                        case "cost_analysis":
                            reportTitle = "MAINTENANCE - COST ANALYSIS";
                            StringBuilder costQuery = new StringBuilder(
                                    "SELECT m.vehicleID, v.model, SUM(m.maintenanceCost) AS totalCost, COUNT(m.maintenanceID) AS maintenanceCount "
                                    + "FROM vehiclemaintenance m "
                                    + "JOIN vehicles v ON m.vehicleID = v.vehicleID "
                                    + "WHERE m.maintenanceDate BETWEEN ? AND ? ");
                            if (maintenanceStatus != null && !maintenanceStatus.isEmpty()) {
                                costQuery.append("AND m.maintenanceStatus = ? ");
                                params.add(maintenanceStatus);
                            }
                            if (vehicleId != null && !vehicleId.isEmpty()) {
                                costQuery.append("AND m.vehicleID = ? ");
                                params.add(vehicleId);
                            }
                            costQuery.append("GROUP BY m.vehicleID, v.model");
                            query = costQuery.toString();
                            headers.put("vehicleID", "Vehicle ID");
                            headers.put("model", "Vehicle Model");
                            headers.put("totalCost", "Total Cost");
                            headers.put("maintenanceCount", "Maintenance Count");
                            break;

                        default:
                            LOGGER.log(Level.WARNING, "Invalid report type for maintenance category: {0}", reportType);
                            request.setAttribute("error", "Invalid report type for maintenance category.");
                            request.getRequestDispatcher("/error.jsp").forward(request, response);
                            return;
                    }
                    break;

                case "rental": // Legacy category, update to use booking
                    switch (reportType) {
                        case "detailed_rentals":
                            reportTitle = "RENTAL - DETAILED RENTALS";
                            query = "SELECT b.bookingID, bv.vehicleID, v.model, b.clientID, c.name AS clientName, "
                                    + "b.startDate, b.endDate, b.totalCost "
                                    + "FROM booking b "
                                    + "JOIN bookingvehicle bv ON b.bookingID = bv.bookingID "
                                    + "JOIN vehicles v ON bv.vehicleID = v.vehicleID "
                                    + "JOIN client c ON b.clientID = c.clientID "
                                    + "WHERE b.startDate BETWEEN ? AND ?";
                            headers.put("bookingID", "Booking ID");
                            headers.put("vehicleID", "Vehicle ID");
                            headers.put("model", "Vehicle Model");
                            headers.put("clientID", "Client ID");
                            headers.put("clientName", "Client Name");
                            headers.put("startDate", "Start Date");
                            headers.put("endDate", "End Date");
                            headers.put("totalCost", "Total Cost");
                            break;

                        case "revenue_summary":
                            reportTitle = "RENTAL - REVENUE SUMMARY";
                            query = "SELECT DATE(b.startDate) AS bookingDay, SUM(b.totalCost) AS totalRevenue "
                                    + "FROM booking b "
                                    + "WHERE b.startDate BETWEEN ? AND ? "
                                    + "GROUP BY DATE(b.startDate)";
                            headers.put("bookingDay", "Booking Day");
                            headers.put("totalRevenue", "Total Revenue");
                            break;

                        default:
                            LOGGER.log(Level.WARNING, "Invalid report type for rental category: {0}", reportType);
                            request.setAttribute("error", "Invalid report type for rental category.");
                            request.getRequestDispatcher("/error.jsp").forward(request, response);
                            return;
                    }
                    break;

                default:
                    LOGGER.log(Level.WARNING, "Invalid report category: {0}", reportCategory);
                    request.setAttribute("error", "Invalid report category.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
            }

            // Log the query and parameters
            LOGGER.log(Level.INFO, "Executing query: {0}", query);
            LOGGER.log(Level.INFO, "Query parameters: {0}", params);

            // Execute query and collect results
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                for (int i = 0; i < params.size(); i++) {
                    stmt.setObject(i + 1, params.get(i));
                }

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Map<String, Object> row = new HashMap<>();
                        for (String column : headers.keySet()) {
                            Object value = rs.getObject(column);
                            // Handle availability display
                            if (column.equals("availability")) {
                                value = (value != null && ((Integer) value) == 1) ? "Available" : "Not Available";
                            }
                            row.put(column, value);
                        }
                        results.add(row);
                    }
                }
            }

            // Log the number of results
            LOGGER.log(Level.INFO, "Query executed successfully. Number of results: {0}", results.size());

            // Set attributes and forward to JSP for rendering
            request.setAttribute("reportTitle", reportTitle);
            request.setAttribute("headers", headers);
            request.setAttribute("results", results);
            LOGGER.log(Level.INFO, "reportTitle set to: {0}", reportTitle);
            LOGGER.log(Level.INFO, "headers set to: {0}", headers);
            LOGGER.log(Level.INFO, "results set to: {0}", results);
            LOGGER.log(Level.INFO, "Forwarding to /admin/reportResults.jsp with attributes set");
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQLException in doGet: {0}", ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "SQLException in doPost: {0}", ex.getMessage());
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for generating reports";
    }
}
