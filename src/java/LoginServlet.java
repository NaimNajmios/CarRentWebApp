/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import Database.AdminOperation;
import Database.DBOperation;
import User.Admin;
import User.Client;
import User.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
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
public class LoginServlet extends HttpServlet {

    DBOperation db = new DBOperation();
    AdminOperation ao = new AdminOperation();
    User user = new User();
    Admin admin = new Admin();
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
            throws ServletException, IOException, NoSuchAlgorithmException {

        // Handles login form submission
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (db.validateUser(username, password)) {
            // Login successful, redirect to home page
            User user = db.getUser(username);

            // Console log for debugging, username
            System.out.println("Username: " + username);

            // Set session attributes object for user
            request.getSession().setAttribute("user", user);

            System.out.println("User: " + user);

            // Check if user is admin or client
            if (user.getRole().equals("Administrator")) {
                // Set session attributes object if admin
                admin = ao.getAdminDetailsByUserID(user.getUserID());
                request.getSession().setAttribute("admin", admin);
                response.sendRedirect("admin/user-management.jsp");
                System.out.println("Admin: " + admin);
            } else {
                // Check if user has a temporary password
                if (db.isUsingTemporaryPassword(user.getUserID())) {
                    // Set session attributes object if client
                    client = db.getClientDataByID(user.getUserID());
                    request.getSession().setAttribute("client", client);                    
                    // User has a temporary password, redirect to reset password page
                    response.sendRedirect("reset-temp-password.jsp");
                } else {
                    // Set session attributes object if client
                    client = db.getClientDataByID(user.getUserID());
                    request.getSession().setAttribute("client", client);                    
                    // User does not have a temporary password, redirect to client home page
                    response.sendRedirect("clientHome.jsp");
                    System.out.println("Client: " + client);
                }
            }

        } else {
            // Login failed, redirect to login page with error message
            System.out.println("Login failed!");
            response.sendRedirect("login.html?error=true");
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
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
