<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Database.AdminOperation"%>
<%@ page import="User.Client" %>
<%@ page import="User.User" %>

<%
    // Get user and client data
    AdminOperation dbo = new AdminOperation();
    Client client = dbo.getClientDataByClientID(client.getClientID());

    // Assign to variables
    String clientID = client.getClientID();
    String clientName = client.getName();
    String clientEmail = client.getEmail();
    String clientPhone = client.getPhoneNumber();
    String clientAddress = client.getAddress();
    String profileImagePath = client.getProfileImagePath() != null ? client.getProfileImagePath() : "images/default-profile.jpg"; // Default image if none exists
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <title>CarRent - My Profile</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css?family=DM+Sans:300,400,700&display=swap" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <style>
        body {
            font-family: 'DM Sans', sans-serif;
        }
        .profile-container {
            max-width: 800px;
            margin: 0 auto;
            padding: 2rem 0;
        }
        .profile-card {
            background: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            padding: 2rem;
        }
        .profile-image {
            width: 150px;
            height: 150px;
            object-fit: cover;
            border-radius: 50%;
            margin-bottom: 1rem;
            border: 2px solid #ddd;
        }
        .profile-info h2 {
            font-size: 1.75rem;
            font-weight: 700;
            margin-bottom: 1rem;
        }
        .profile-info p {
            font-size: 1rem;
            margin-bottom: 0.5rem;
        }
    </style>
</head>
<body>
    <!-- Header Include -->
    <%@ include file="../include/client-header.jsp" %>

    <!-- Profile Section -->
    <div class="container profile-container">
        <div class="profile-card">
            <div class="text-center">
                <img src="<%= profileImagePath %>" alt="Profile Image" class="profile-image">
            </div>
            <div class="profile-info">
                <h2>My Profile</h2>
                <p><strong>Client ID:</strong> <%= clientID %></p>
                <p><strong>Name:</strong> <%= clientName %></p>
                <p><strong>Email:</strong> <%= clientEmail %></p>
                <p><strong>Phone:</strong> <%= clientPhone != null ? clientPhone : "Not provided" %></p>
                <p><strong>Address:</strong> <%= clientAddress != null ? clientAddress : "Not provided" %></p>
            </div>
            <div class="text-center mt-4">
                <a href="editProfile.jsp?clientID=<%= clientID %>" class="btn btn-primary">Edit Profile</a>
            </div>
        </div>
    </div>

    <!-- Footer Include -->
    <%@ include file="../include/client-footer.jsp" %>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>