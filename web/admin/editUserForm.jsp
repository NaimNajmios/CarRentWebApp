<%-- Import required classes --%>
<%@ page import="Database.AdminOperation"%>
<%@ page import="User.Client" %>
<%@ page import="User.User" %>
<%@ page import="User.Admin" %>

<%-- Get user data by ID --%>
<%

    String userID = request.getParameter("userID");

    // Get user and client data
    AdminOperation dbo = new AdminOperation();
    User user = null;
    Client client = null;

    String clientID = "";
    String clientName = "";
    String clientAddress = "";
    String clientEmail = "";
    String clientPhone = "";

    // Retrieve user data 
    client = dbo.getClientDataByClientID(userID);

    // // Check if the user exists
    if (client == null) {
        // User not found
        response.sendRedirect("user-management.jsp");
        return;
    } else {
        // Assign to variables
        clientID = client.getClientID();
        clientName = client.getName();
        clientEmail = client.getEmail();
        clientPhone = client.getPhoneNumber();
        clientAddress = client.getAddress();
    }

%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>CarRent Admin - Edit User</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link href="https://fonts.googleapis.com/css?family=DM+Sans:300,400,700&display=swap" rel="stylesheet">
        <%-- Include start here --%>
        <!-- Fixed Paths for CSS -->
        <%@ include file="../include/admin-styling.html" %>

    </head>
    <body data-spy="scroll" data-target=".site-navbar-target" data-offset="300">
        <div class="site-wrap" id="user-section">
            <header class="site-header site-navbar-target" role="banner">
                <div class="container">
                    <div class="row align-items-center">
                        <div class="col-6 col-md-3">
                            <div class="site-logo">
                                <a href="user-management.jsp">CarRent Admin</a>
                            </div>
                        </div>
                        <div class="col-6 col-md-9 text-right">
                            <a href="<%= request.getContextPath()%>/Logout" class="btn btn-light">Logout</a>
                        </div>
                    </div>
                </div>
            </header>

            <div class="container-fluid">
                <div class="row">
                    <%@ include file="../include/sidebar.jsp" %>
                    <%-- Include end here --%>
                    <main class="col-md-9 ms-sm-auto col-lg-10 content">
                        <div class="card">
                            <div class="card-header">
                                <h3 class="mb-0">Edit User</h3>
                            </div>
                            <div class="card-body">
                                <a href="user-management.jsp" class="btn btn-secondary btn-sm mb-3">Back to User Management</a>
                                <form action="<%= request.getContextPath()%>/UpdateClient" method="post">
                                    <%-- Message --%>
                                    <div id="div_message" class="text-center mb-4" style="color: blue;"></div>
                                    <div class="form-group mb-3">
                                        <label for="clientID">Client ID</label>
                                        <input type="text" class="form-control" id="clientID" name="clientID" value="<% out.print(clientID); %>" disabled>
                                        <input type="hidden" name="clientID" value="<% out.print(clientID); %>">
                                    </div>
                                    <div class="form-group mb-3">
                                        <label for="name">Name</label>
                                        <input type="text" class="form-control" id="name" name="name" value="<% out.print(clientName); %>" required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label for="email">Email</label>
                                        <input type="email" class="form-control" id="email" name="email" value="<% out.print(clientEmail); %>" required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label for="role">Role</label>
                                        <select class="form-control" id="role" name="role" required>
                                            <option value="Client" ${user.role == 'Client' ? 'selected' : ''}>Client</option>
                                            <option value="Admin" ${user.role == 'Admin' ? 'selected' : ''}>Admin</option>
                                        </select>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label for="phone">Phone</label>
                                        <input type="tel" class="form-control" id="phone" name="phone" value="<% out.print(clientPhone); %>">
                                    </div>
                                    <div class="form-group mb-3">
                                        <label for="address">Address</label>
                                        <%-- Textarea --%>
                                        <textarea class="form-control" id="address" name="address" rows="3"><% out.print(clientAddress); %></textarea>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Save Changes</button>
                                    <a href="viewUserDetails.jsp?userID=<% out.print(clientID);%>" class="btn btn-outline-secondary">Cancel</a>
                                </form>
                            </div>
                        </div>
                    </main>
                </div>
            </div>
        </div>
        <!-- Fixed Paths for JS -->
        <%@ include file="../include/admin-js.html" %>
    </body>
</html>