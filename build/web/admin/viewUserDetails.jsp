<%-- Import required classes --%>
<%@ page import="Database.AdminOperation"%>
<%@ page import="User.Client" %>
<%@ page import="User.User" %>
<%@ page import="User.Admin" %>

<%-- Get user data by ID --%>
<%
    String clientid = request.getParameter("userID");

    // Get user and client data
    AdminOperation dbo = new AdminOperation();
    Client client = null;

    String clientID = "";
    String userid = "";
    String clientName = "";
    String clientAddress = "";
    String clientEmail = "";
    String clientPhone = "";
    String clientImagePath = "";

    // Retrieve user data 
    client = dbo.getClientDataByClientID(clientid);

    // Log the client data
    System.out.println("Client Data: " + client);

    // Check if the user exists
    if (client == null) {
        // User not found
        response.sendRedirect("user-management.jsp");
        return;
    } else {
        // Assign to variables
        clientID = client.getClientID();
        userid = client.getUserID();
        clientName = client.getName();
        clientEmail = client.getEmail();
        clientPhone = client.getPhoneNumber();
        clientAddress = client.getAddress();
        clientImagePath = client.getProfileImagePath() != null && !client.getProfileImagePath().isEmpty()
                ? client.getProfileImagePath()
                : "images/profile/default_profile.jpg";
    }
%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>CarRent Admin - View User Details</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link href="https://fonts.googleapis.com/css?family=DM+Sans:300,400,700&display=swap" rel="stylesheet">
        <%-- Include start here --%>
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
            .user-details dt {
                font-weight: 500;
                margin-bottom: 0.5rem;
            }
            .user-details dd {
                margin-bottom: 1rem;
                color: #555;
            }
            @media (max-width: 768px) {
                .profile-picture {
                    width: 120px;
                    height: 120px;
                }
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
                    <%-- Include end here --%>
                    <main class="col-md-9 ms-sm-auto col-lg-10 content">
                        <div class="card">
                            <div class="card-header">
                                <h3 class="mb-0">User Details</h3>
                            </div>
                            <div class="card-body">
                                <a href="user-management.jsp" class="btn btn-secondary btn-sm mb-3">Back to User Management</a>
                                <%-- Message --%>
                                <div id="div_message" class="text-center mb-4" style="color: blue;"></div>
                                <%-- Profile Picture Section --%>
                                <div class="profile-picture-container">
                                    <img src="<%= request.getContextPath()%>/<%= clientImagePath%>" alt="Profile Picture of <%= clientName%>" class="profile-picture">
                                </div>
                                <%-- Display user details --%>
                                <dl class="user-details">
                                    <dt>Client ID</dt>
                                    <dd><% out.print(clientID); %></dd>
                                    <dt>User ID</dt>
                                    <dd><% out.print(userid); %></dd>
                                    <dt>Name</dt>
                                    <dd><% out.print(clientName); %></dd>
                                    <dt>Email</dt>
                                    <dd><% out.print(clientEmail); %></dd>
                                    <dt>Phone</dt>
                                    <dd><% out.print(clientPhone); %></dd>
                                    <dt>Address</dt>
                                    <dd><% out.print(clientAddress);%></dd>
                                </dl>
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