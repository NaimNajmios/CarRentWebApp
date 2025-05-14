<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Database.AdminOperation"%>
<%@ page import="Database.DBOperation"%>
<%@ page import="User.Admin"%>
<%@ page import="User.User"%>

<%-- Get admin data from session --%>
<%
    AdminOperation dbo = new AdminOperation();
    User user = null;
    Admin admin = null;

    String adminID = "";
    String userid = "";
    String adminName = "";
    String adminEmail = "";
    String profilePicturePath = "";

    // Reassign object passed in session
    user = (User) session.getAttribute("user");
    admin = (Admin) session.getAttribute("admin");

    if (admin != null) {
        adminID = admin.getAdminID();
        userid = admin.getUserID();
        adminName = admin.getName();
        adminEmail = admin.getEmail();
        profilePicturePath = admin.getProfileImagePath() != null && !admin.getProfileImagePath().isEmpty()
                ? admin.getProfileImagePath()
                : "/images/profile/default_profile.jpg";
    } else {
        // Redirect to login if admin is not logged in
        response.sendRedirect(request.getContextPath() + "/index.jsp?error=logout");
        return;
    }

    // Handle messages
    String message = request.getParameter("message");
    String messageText = "";
    String messageClass = "";
    if (message != null) {
        if (message.equals("changesuccess")) {
            messageText = "Profile updated successfully.";
            messageClass = "text-success";
        } else if (message.equals("error")) {
            messageText = "Failed to update profile. Please try again.";
            messageClass = "text-danger";
        }
    }
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>CarRent Admin - My Profile</title>
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
                cursor: pointer;
                transition: border-color 0.3s ease;
            }
            .profile-picture:hover {
                border-color: #007bff;
            }
            .profile-picture-actions {
                display: flex;
                gap: 0.5rem;
                justify-content: center;
            }
            .btn-sm {
                font-size: 0.875rem;
                padding: 0.25rem 0.5rem;
            }
            .message {
                font-size: 1rem;
                margin-bottom: 1rem;
                text-align: center;
            }
        </style>
    </head>
    <body data-spy="scroll" data-target=".site-navbar-target" data-offset="300">
        <div class="site-wrap" id="profile-section">
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
                                <h3 class="mb-0">My Profile</h3>
                            </div>
                            <div class="card-body">
                                <form action="<%= request.getContextPath()%>/UpdateAdmin" method="post" enctype="multipart/form-data">
                                    <%-- Message --%>
                                    <% if (!messageText.isEmpty()) {%>
                                    <div class="message <%= messageClass%>">
                                        <%= messageText%>
                                    </div>
                                    <% }%>
                                    <%-- Profile Picture Section --%>
                                    <div class="form-group mb-3 profile-picture-container">
                                        <label for="profilePicture">Profile Picture</label><br>
                                        <img src="<%= request.getContextPath()%>/<%= profilePicturePath%>" alt="Profile Picture" 
                                             class="profile-picture" id="profilePreview" onclick="document.getElementById('profilePicture').click()">
                                        <div class="profile-picture-actions">
                                            <input type="file" class="form-control-file" id="profilePicture" name="profilePicture" accept="image/*" 
                                                   onchange="previewImage(this)" style="display: none;">
                                            <button type="button" class="btn btn-outline-primary btn-sm" onclick="document.getElementById('profilePicture').click()">
                                                Change
                                            </button>
                                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="resetProfilePicture()">
                                                Reset
                                            </button>
                                        </div>
                                        <small class="form-text text-muted d-block text-center mt-2">Recommended size: 300x300 pixels. Max file size: 5MB</small>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label for="adminID">Admin ID</label>
                                        <input type="text" class="form-control" id="adminID" name="adminID" value="<%= adminID%>" disabled>
                                        <input type="hidden" name="adminID" value="<%= adminID%>">
                                    </div>
                                    <%-- User ID --%>
                                    <div class="form-group mb-3">
                                        <label for="userID">User ID</label>
                                        <input type="text" class="form-control" id="userID" name="userID" value="<%= userid%>" disabled>
                                        <input type="hidden" name="userID" value="<%= userid%>">
                                    </div>
                                    <%-- Name --%>
                                    <div class="form-group mb-3">
                                        <label for="name">Name</label>
                                        <input type="text" class="form-control" id="name" name="name" value="<%= adminName%>" required>
                                    </div>
                                    <div class="form-group mb-3">
                                        <label for="email">Email</label>
                                        <input type="email" class="form-control" id="email" name="email" value="<%= adminEmail%>" required>
                                    </div>
                                    <button type="submit" class="btn btn-primary">Save Changes</button>
                                    <a href="user-management.jsp" class="btn btn-outline-secondary">Cancel</a>
                                </form>
                            </div>
                        </div>
                    </main>
                </div>
            </div>
        </div>
        <!-- Fixed Paths for JS -->
        <%@ include file="../include/admin-js.html" %>
        <script>
            function previewImage(input) {
                if (input.files && input.files[0]) {
                    const reader = new FileReader();
                    reader.onload = function (e) {
                        document.getElementById('profilePreview').src = e.target.result;
                    }
                    reader.readAsDataURL(input.files[0]);
                }
            }

            function resetProfilePicture() {
                document.getElementById('profilePicture').value = '';
                document.getElementById('profilePreview').src = '<%= request.getContextPath()%>/<%= profilePicturePath%>';
                    }
        </script>
    </body>
</html>