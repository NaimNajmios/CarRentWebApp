
import Database.AdminOperation;
import User.Client;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Naim Najmi
 */
public class UpdateClientServlet extends HttpServlet {

    AdminOperation adminOperation = new AdminOperation();
    Client client = new Client();

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

        // Retrieve all parameters from the form
        String clientID = request.getParameter("clientID");
        String clientUsername = request.getParameter("name");
        String clientEmail = request.getParameter("email");
        String clientRole = request.getParameter("role");
        String clientPhone = request.getParameter("phone");
        String clientAddress = request.getParameter("address");

        // Log
        System.out.println("Editing Client ID: " + clientID);

        // Set the updated user details in the client object
        client.setUserID(clientID);
        client.setName(clientUsername);
        client.setEmail(clientEmail);
        client.setPhoneNumber(clientPhone);
        client.setAddress(clientAddress);

        // Log
        System.out.println(client);

        // Update the user details in the database
        boolean updateSuccess = adminOperation.updateClientDetails(client);

        if (updateSuccess) {
            // Log
            System.out.println("User details updated successfully");
            // User details updated successfully
            response.sendRedirect(request.getContextPath() + "/admin/viewUserDetails.jsp?userID=" + clientID + "&div_message=changesuccess");
        } else {
            // Log
            System.out.println("User details update failed");
            // User details update failed
            response.sendRedirect(request.getContextPath() + "/admin/editUserForm.jsp?userID=" + clientID + "&div_message=changeinvalid");
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
