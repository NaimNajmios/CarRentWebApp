import Database.AdminOperation;
import Database.DBOperation;
import User.Admin;
import User.Client;
import User.User;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Naim Najmi
 */
public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    private final DBOperation db = new DBOperation();
    private final AdminOperation ao = new AdminOperation();
    private final User user = new User();
    private Admin admin = new Admin();
    private Client client = new Client();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws NoSuchAlgorithmException if the hashing algorithm is not available
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, NoSuchAlgorithmException {

        LOGGER.log(Level.INFO, "Processing login request.");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        LOGGER.log(Level.FINE, "Attempting login for username: {0}", username);

        if (db.validateUser(username, password)) {
            LOGGER.log(Level.INFO, "Login successful for user: {0}", username);

            User loggedInUser = db.getUser(username);
            HttpSession session = request.getSession();
            session.setAttribute("user", loggedInUser);
            LOGGER.log(Level.FINE, "User object stored in session: {0}", loggedInUser);

            if (loggedInUser.getRole().equals("Administrator")) {
                admin = ao.getAdminDetailsByUserID(loggedInUser.getUserID());
                session.setAttribute("admin", admin);
                LOGGER.log(Level.FINE, "Admin object stored in session: {0}", admin);
                response.sendRedirect("admin/user-management.jsp");
                LOGGER.log(Level.INFO, "Redirecting administrator to: admin/user-management.jsp");
            } else {
                if (db.isUsingTemporaryPassword(loggedInUser.getUserID())) {
                    client = db.getClientDataByID(loggedInUser.getUserID());
                    session.setAttribute("client", client);
                    LOGGER.log(Level.FINE, "Client object stored in session (temporary password): {0}", client);
                    response.sendRedirect("reset-temp-password.jsp");
                    LOGGER.log(Level.INFO, "Redirecting client with temporary password to: reset-temp-password.jsp");
                } else {
                    client = db.getClientDataByID(loggedInUser.getUserID());
                    session.setAttribute("client", client);
                    LOGGER.log(Level.FINE, "Client object stored in session: {0}", client);
                    response.sendRedirect("client/index.jsp");
                    LOGGER.log(Level.INFO, "Redirecting client to: client/index.jsp");
                }
            }

        } else {
            LOGGER.log(Level.WARNING, "Login failed for username: {0}", username);
            response.sendRedirect("index.jsp?error=invalid");
            LOGGER.log(Level.INFO, "Redirecting to login page with error.");
        }
    }

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
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.log(Level.SEVERE, "Error during GET request", ex);
            // Consider setting an error attribute in the request and forwarding to an error page
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
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
        } catch (NoSuchAlgorithmException ex) {
            LOGGER.log(Level.SEVERE, "Error during POST request", ex);
            // Consider setting an error attribute in the request and forwarding to an error page
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Handles user login authentication and redirection.";
    }
}