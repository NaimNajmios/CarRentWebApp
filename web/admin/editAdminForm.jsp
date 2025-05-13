<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Database.AdminOperation"%>
<%@ page import="Database.DBOperation"%>
<%@ page import="User.Client" %>
<%@ page import="User.User" %>
<%@ page import="User.Admin" %>

<%-- Get user data by ID --%>
<%
    String userID = request.getParameter("userID");

    // Get user and client data
    AdminOperation dbo = new AdminOperation();
    User user = null;
    Admin admin = null;

    String adminID = "";
    String userid = "";
    String adminName = "";
    String adminEmail = "";
    String profilePicturePath = "";

    // Retrieve user data 
    admin = dbo.getAdminDataByAdminID(userID);

    // Check if the user exists
    if (admin == null) {
        // User not found
        response.sendRedirect("user-management.jsp");
        return;
    } else {
        // Assign to variables
        adminID = admin.getAdminID();
        userid = admin.getUserID();
        adminName = admin.getName();
        adminEmail = admin.getEmail();
        profilePicturePath = admin.getProfileImagePath() != null && !admin.getProfileImagePath().isEmpty() 
                            ? admin.getProfileImagePath() 
                            : "images/profile/default_profile.jpg";
    }
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>CarRent Admin - Edit User</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link href="https://fonts.googleapis.com/css?family=DM+Sans:300,400,700&display=swap" rel="stylesheet">
        <!-- Fixed Paths for CSS -->
        <%@ include file="../include/admin-styling.html" %>
        <style>
            .profile-picture-container {
                text-align: center;
                margin-bottom: 1.5rem;
            }
            .profile-picture {
                width: 150px;
                height: 150px;
                object-fit: cover;
                border-radius: 50%;
                border: 2px solid #ddd;
                margin-bottom: 1rem;
            }
        </style>
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
                            <a href="<%= request.getContextPath()%>/Logout" class="btn btn-light" onclick="return confirm('Are you sure you want to logout?')">Logout</a>
                        </div>
                    </div>
                </div>
            </header>

            <div class="container-fluid">
                <div class="row">
                    <%@ include file="../include/sidebar.jsp" %>
                    <main class="col-md-9 ms-sm-auto col-lg-10 content">
                        <div class="card">
                            <div class="card-header">
                                <h3 class="mb-0">Edit User</h3>
                            </div>
                            <div class="card-body">
                                <a href="user-management.jsp" class="btn btn-secondary btn-sm mb-3">Back to User Management</a>
                                <form action="<%= request.getContextPath()%>/UpdateAdmin" method="post" enctype="multipart/form-data">
                                    <%-- Message --%>
                                    <div id="div_message" class="text-center mb-4" style="color: blue;"></div>
                                    <%-- Profile Picture Section --%>
                                    <div class="form-group mb-3 profile-picture-container">
                                        <label for="profilePicture">Profile Picture</label><br>
                                        <img src="<%= request.getContextPath()%>/<%= profilePicturePath %>" alt="Profile Picture of <%= adminName %>" class="profile-picture">
                                        <div>
                                            <input type="file" class="form-control-file" id="profilePicture" name="profilePicture" accept="image/*">
                                        </div>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label for="adminID">Admin ID</label>
                                        <input type="text" class="form-control" id="adminID" name="adminID" value="<% out.print(adminID); %>" disabled>
                                        <input type="hidden" name="adminID" value="<% out.print(adminID); %>">
                                    </div>
                                    <%-- User ID --%>
                                    <div class="form-group mb-3">
                                        <label for="userID">User ID</label>
                                        <input type="text" class="form-control" id="userID" name="userID" value="<% out.print(userid); %>" disabled>
                                        <input type="hidden" name="userID" value="<% out.print(userid); %>">
                                    </div>
                                    <%-- Name --%>
                                    <div class="form-group mb-3">
                                        <label for="name">Name</label>
                                        <input type="text" class="form-control" id="name" name="name" value="<% out.print(adminName); %>" required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label for="email">Email</label>
                                        <input type="email" class="form-control" id="email" name="email" value="<% out.print(adminEmail); %>" disabled>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Save Changes</button>
                                    <a href="viewUserDetails.jsp?userID=<% out.print(adminID);%>" class="btn btn-outline-secondary">Cancel</a>
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