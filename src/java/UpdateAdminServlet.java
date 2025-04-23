
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Database.AdminOperation;
import User.Admin;

/**
 *
 * @author Naim Najmi
 */
public class UpdateAdminServlet extends HttpServlet {

    AdminOperation adminOperation = new AdminOperation();
    Admin admin = new Admin();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get request parameters
        String adminID = request.getParameter("adminID");
        String userID = request.getParameter("userID");
        String adminName = request.getParameter("name");
        String adminEmail = request.getParameter("email");

        // Set the updated admin details in the admin object
        admin.setAdminID(adminID);
        admin.setUserID(userID);
        admin.setName(adminName);
        admin.setEmail(adminEmail);

        // Update the admin details in the database
        boolean updated = adminOperation.updateAdminDetails(admin);

        if (updated) {
            // Log
            System.out.println("Admin details updated successfully.");
            // Redirect to a success page or display a success message
            response.sendRedirect(request.getContextPath() + "/admin/viewAdminDetails.jsp?userID=" + adminID + "&div_message=changesuccess");
        } else {
            // Log 
            System.out.println("Failed to update admin details.");
            // Redirect to an error page or display an error message
            response.sendRedirect("user-management.jsp?div_message=true");
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
        processRequest(request, response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
