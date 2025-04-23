/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Database.DBOperation;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Naim Najmi
 */
public class RegisterServlet extends HttpServlet {

    DBOperation db = new DBOperation();

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

        // 1. Retrieve and trim all parameters from the form
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeat-password");
        String email = request.getParameter("email");
        String fullName = request.getParameter("full-name");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phone-number");

        // Trim inputs to remove leading/trailing spaces
        if (username != null) {
            username = username.trim();
        }
        if (email != null) {
            email = email.trim();
        }
        if (fullName != null) {
            fullName = fullName.trim();
        }
        if (address != null) {
            address = address.trim();
        }
        if (phoneNumber != null) {
            phoneNumber = phoneNumber.trim();
        }

        // 2. Server-Side Validation
        if (username == null || username.isEmpty()
                || password == null || password.isEmpty()
                || repeatPassword == null || repeatPassword.isEmpty()
                || email == null || email.isEmpty()
                || fullName == null || fullName.isEmpty()
                || address == null || address.isEmpty()
                || phoneNumber == null || phoneNumber.isEmpty()) {

            System.out.println("Validation failed: One or more fields are empty.");
            response.sendRedirect("register.jsp?error=empty");
            return;
        }

        // 3. Check if passwords match
        if (!password.equals(repeatPassword)) {
            System.out.println("Validation failed: Passwords do not match.");
            response.sendRedirect("register.jsp?error=mismatch");
            return;
        }

        // 4. Check password strength (basic)
        if (password.length() < 8
                || !password.matches(".*[A-Z].*")
                || !password.matches(".*[a-z].*")
                || !password.matches(".*\\d.*")) {
            System.out.println("Validation failed: Weak password.");
            response.sendRedirect("register.jsp?error=weakpassword");
            return;
        }

        // 5. Attempt registration via DBOperation
        boolean registrationSuccess = false;
        String redirectUrl = "register.jsp"; // Default fallback

        try {
            // Assuming db is an instance of a DBOperation class
            registrationSuccess = db.registerUser(username, password, email, fullName, address, phoneNumber);

            if (registrationSuccess) {
                redirectUrl = "login.jsp?success=register";
            } else {
                redirectUrl = "register.jsp?error=duplicate";
            }

        } catch (SQLException sqle) {
            if ("23505".equals(sqle.getSQLState())) { // Unique constraint violation (may vary by DBMS)
                redirectUrl = "register.jsp?error=duplicate";
            } else {
                redirectUrl = "register.jsp?error=server";
                System.err.println("SQL Error during registration: " + sqle.getMessage());
                sqle.printStackTrace();
            }

        } catch (Exception e) {
            redirectUrl = "register.jsp?error=server";
            System.err.println("General Error during registration: " + e.getMessage());
            e.printStackTrace();
        }

        // 6. Redirect the user
        response.sendRedirect(redirectUrl);
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
