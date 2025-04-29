import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Report.Report;

/**
 * Servlet for handling report generation requests.
 *
 * @author Naim Najmi
 */
public class GenerateReportServlet extends HttpServlet {

    // Initialize logger for the servlet
    private static final Logger LOGGER = Logger.getLogger(GenerateReportServlet.class.getName());

    /**
     * Processes requests for both HTTP GET and POST methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        String bookingStatus = request.getParameter("bookingStatus");
        String vehicleId = request.getParameter("vehicleId");
        String category = request.getParameter("category");
        String availability = request.getParameter("availability");
        String paymentType = request.getParameter("paymentType");
        String paymentStatus = request.getParameter("paymentStatus");
        String maintenanceStatus = request.getParameter("maintenanceStatus");

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

        try {
            // Delegate report generation to Report class
            Report report = new Report();
            Report.ReportResult reportResult = report.generateReport(
                    reportCategory, reportType, dateRangeType, startDate, endDate,
                    role, bookingStatus, vehicleId, category, availability,
                    paymentType, paymentStatus, maintenanceStatus);

            // Set attributes and forward to JSP for rendering
            request.setAttribute("reportTitle", reportResult.getReportTitle());
            request.setAttribute("headers", reportResult.getHeaders());
            request.setAttribute("results", reportResult.getResults());
            LOGGER.log(Level.INFO, "Forwarding to /admin/reportResults.jsp with attributes set");
            request.getRequestDispatcher("/admin/reportResults.jsp").forward(request, response);

        } catch (SQLException | ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Error generating report: {0}", e.getMessage());
            request.setAttribute("error", "Error generating report: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for generating reports";
    }
}