/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Database.AdminOperation;
import Database.DBOperation;
import User.Client;
import User.User;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Naim Najmi
 */
public class RegisterWalkInServlet extends HttpServlet {

    AdminOperation db = new AdminOperation();
    User user = new User();
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
            throws ServletException, IOException, Exception {
        
            // Get the form data from the request
            String username = request.getParameter("username");
            
            String email = request.getParameter("email");
            String fullname = request.getParameter("full-name");
            String address = request.getParameter("address");
            String phoneNumber = request.getParameter("phone-number");

            // Set the user details in the user object
            user.setUsername(username);

            // Set the client details in the client object
            client.setEmail(email);
            client.setName(fullname);
            client.setAddress(address);
            client.setPhoneNumber(phoneNumber);

            // Logging for debug
            System.out.println("Username: " + username);
            System.out.println("Email: " + email);
            System.out.println("Full Name: " + fullname);
            System.out.println("Address: " + address);
            System.out.println("Phone Number: " + phoneNumber);

            // Add the user to the database
            boolean success = db.registerWalkInCustomer(client, user);

            if (success) {
                // User registration successful, redirect to login page
                response.sendRedirect(request.getContextPath() + "/admin/user-management.jsp?div_message=changesuccess");
            } else {
                // User registration failed, redirect back to register page
                response.sendRedirect(request.getContextPath() + "/admin/user-management.jsp?div_message=error");
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
        } catch (Exception ex) {
            Logger.getLogger(RegisterWalkInServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception ex) {
            Logger.getLogger(RegisterWalkInServlet.class.getName()).log(Level.SEVERE, null, ex);
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
