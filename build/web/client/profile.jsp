<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="Database.DBOperation"%>
<%@ page import="User.Client" %>
<%@ page import="User.User" %>

<%
    // Get user and client data from session
    Client client = (Client) session.getAttribute("client");

    if (client == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    // Get client data from database
    DBOperation dbo = new DBOperation();
    client = dbo.getClientDataByClientID(client.getClientID());

    // Print client data
    System.out.println("Client ID: " + client.getClientID());
    System.out.println("Client Name: " + client.getName());
    System.out.println("Client Email: " + client.getEmail());
    System.out.println("Client Phone: " + client.getPhoneNumber());
    System.out.println("Client Address: " + client.getAddress());
    System.out.println("Profile Image Path: " + client.getProfileImagePath());

    // Assign to variables
    String clientID = client.getClientID();
    String clientName = client.getName();
    String clientEmail = client.getEmail();
    String clientPhone = client.getPhoneNumber();
    String clientAddress = client.getAddress();
    String profileImagePath = client.getProfileImagePath();

    // Check if in edit mode
    boolean isEditing = "true".equals(request.getParameter("edit"));
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>CarRent - My Profile</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://fonts.googleapis.com/css?family=DM+Sans:300,400,700&display=swap" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
        <style>
            body {
                font-family: 'DM Sans', sans-serif;
                background-color: #f8f9fa;
                display: flex;
                flex-direction: column;
                min-height: 100vh;
                margin: 0;
            }
            .profile-hero {
                background: linear-gradient(rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.6)), url('images/profile_bg.jpg') no-repeat center center/cover;
                color: white;
                padding: 3rem 0;
                text-align: center;
                margin-bottom: 2rem;
            }
            .profile-hero h1 {
                font-size: 2.5rem;
                font-weight: 700;
                margin-bottom: 0.5rem;
            }
            .profile-hero p {
                font-size: 1.1rem;
                opacity: 0.9;
            }
            .container {
                flex: 1 0 auto;
                padding: 0 1.5rem 3rem;
            }
            .profile-card {
                background: #ffffff;
                border-radius: 15px;
                box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
                padding: 2.5rem;
                max-width: 700px;
                margin: -3rem auto 0;
                position: relative;
                z-index: 10;
            }
            .profile-avatar {
                width: 100px; /* Or your desired fixed width */
                height: 100px; /* Or your desired fixed height */
                background-color: #007bff;
                color: white;
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 2.5rem;
                margin: -70px auto 1.5rem;
                border: 5px solid white;
                box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
                overflow: hidden; /* Important to clip the image if it overflows the circle */
            }
            .profile-avatar img {
                width: 100%;
                height: auto; /* Maintain aspect ratio */
                display: block; /* Remove extra space below the image */
                object-fit: cover; /* Ensures the image covers the area without distortion, may crop */
            }
            .profile-header {
                text-align: center;
                margin-bottom: 2rem;
            }
            .profile-header h2 {
                font-size: 1.8rem;
                font-weight: 700;
                color: #2c3e50;
                margin-bottom: 0.5rem;
            }
            .profile-header p {
                color: #6c757d;
                margin-bottom: 0;
            }
            .profile-info {
                background-color: #f8f9fa;
                border-radius: 10px;
                padding: 1.5rem;
                margin-bottom: 1.5rem;
            }
            .profile-info-item {
                margin-bottom: 1rem;
                display: flex;
                align-items: center;
                padding: 0.5rem 0;
                border-bottom: 1px solid #e9ecef;
            }
            .profile-info-item:last-child {
                border-bottom: none;
                margin-bottom: 0;
            }
            .profile-info-item i {
                font-size: 1.2rem;
                color: #007bff;
                margin-right: 1rem;
                width: 20px;
                text-align: center;
            }
            .profile-info-item strong {
                color: #495057;
                margin-right: 0.75rem;
                display: inline-block;
                width: 80px;
                font-weight: 500;
            }
            .profile-info-item span {
                color: #343a40;
                flex: 1;
            }
            .form-label {
                font-weight: 500;
                color: #495057;
            }
            .form-control {
                font-size: 1rem;
                color: #34495e;
                border-color: #ced4da;
                padding: 0.75rem 1rem;
                border-radius: 8px;
            }
            .form-control:focus {
                border-color: #007bff;
                box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.15);
            }
            .button-group {
                display: flex;
                justify-content: center;
                gap: 1rem;
                margin-top: 2rem;
            }
            .btn {
                font-size: 1rem;
                padding: 0.7rem 2rem;
                border-radius: 8px;
                transition: all 0.3s ease;
            }
            .btn-primary {
                background-color: #007bff;
                border: none;
                box-shadow: 0 4px 6px rgba(0, 123, 255, 0.15);
            }
            .btn-primary:hover {
                background-color: #0069d9;
                transform: translateY(-2px);
                box-shadow: 0 6px 8px rgba(0, 123, 255, 0.2);
            }
            .btn-secondary {
                background-color: #6c757d;
                border: none;
                box-shadow: 0 4px 6px rgba(108, 117, 125, 0.15);
            }
            .btn-secondary:hover {
                background-color: #5a6268;
                transform: translateY(-2px);
                box-shadow: 0 6px 8px rgba(108, 117, 125, 0.2);
            }
            .edit-icon {
                margin-right: 0.5rem;
            }
            footer.sticky-footer {
                flex-shrink: 0;
                padding: 1rem 0;
                background-color: #343a40;
                color: #fff;
                text-align: center;
            }
            @media (max-width: 768px) {
                .profile-card {
                    padding: 2rem 1.5rem;
                }
                .profile-hero {
                    padding: 2rem 0;
                }
                .profile-hero h1 {
                    font-size: 2rem;
                }
                .profile-avatar {
                    width: 80px;
                    height: 80px;
                    font-size: 2rem;
                    margin-top: -50px;
                }
                .profile-info-item {
                    flex-direction: column;
                    align-items: flex-start;
                }
                .profile-info-item i {
                    margin-bottom: 0.5rem;
                }
                .profile-info-item strong {
                    width: auto;
                    margin-bottom: 0.25rem;
                }
            }
        </style>
    </head>
    <body>
        <%@ include file="../include/client-header.jsp" %>

        <div class="profile-hero">
            <div class="container">
                <h1>My Account</h1>
                <p>Manage your profile and personal information</p>
            </div>
        </div>

        <div class="container">
            <div class="profile-card">
                <div class="profile-avatar">
                    <img src="<%= request.getContextPath()%>/<%= profileImagePath%>" alt="Profile Image">
                </div>
                <div class="profile-header">
                    <h2><%= clientName%></h2>
                    <p>Member since <%= java.time.LocalDate.now().getYear()%></p>
                </div>

                <% if (!isEditing) {%>
                <div class="profile-info">
                    <div class="profile-info-item">
                        <i class="bi bi-person-badge"></i>
                        <strong>ID:</strong>
                        <span><%= clientID%></span>
                    </div>
                    <div class="profile-info-item">
                        <i class="bi bi-person-fill"></i>
                        <strong>Name:</strong>
                        <span><%= clientName%></span>
                    </div>
                    <div class="profile-info-item">
                        <i class="bi bi-envelope-fill"></i>
                        <strong>Email:</strong>
                        <span><%= clientEmail%></span>
                    </div>
                    <div class="profile-info-item">
                        <i class="bi bi-telephone-fill"></i>
                        <strong>Phone:</strong>
                        <span><%= clientPhone != null ? clientPhone : "Not provided"%></span>
                    </div>
                    <div class="profile-info-item">
                        <i class="bi bi-geo-alt-fill"></i>
                        <strong>Address:</strong>
                        <span><%= clientAddress != null ? clientAddress : "Not provided"%></span>
                    </div>
                </div>
                <div class="button-group">
                    <a href="profile.jsp?edit=true" class="btn btn-primary">
                        <i class="bi bi-pencil-square edit-icon"></i>Edit Profile
                    </a>
                </div>
                <% } else {%>
                <form action="<%= request.getContextPath()%>/UpdateClient" method="post">
                    <input type="hidden" name="clientID" value="<%= clientID%>">
                    <div class="mb-3">
                        <label for="name" class="form-label">Name</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-person-fill"></i></span>
                            <input type="text" class="form-control" id="name" name="name" value="<%= clientName%>" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-envelope-fill"></i></span>
                            <input type="email" class="form-control" id="email" name="email" value="<%= clientEmail%>" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="phone" class="form-label">Phone</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-telephone-fill"></i></span>
                            <input type="tel" class="form-control" id="phone" name="phone" value="<%= clientPhone != null ? clientPhone : ""%>" placeholder="Enter your phone number">
                        </div>
                    </div>
                    <div class="mb-3">
                        <label for="address" class="form-label">Address</label>
                        <div class="input-group">
                            <span class="input-group-text"><i class="bi bi-geo-alt-fill"></i></span>
                            <textarea class="form-control" id="address" name="address" rows="3" placeholder="Enter your address"><%= clientAddress != null ? clientAddress : ""%></textarea>
                        </div>
                    </div>
                    <div class="button-group">
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-check-lg edit-icon"></i>Save Changes
                        </button>
                        <a href="profile.jsp" class="btn btn-secondary">
                            <i class="bi bi-x-lg edit-icon"></i>Cancel
                        </a>
                    </div>
                </form>
                <% }%>
            </div>
        </div>

        <%@ include file="../include/client-footer.jsp" %>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html> 