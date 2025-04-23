<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="Database.AdminOperation"%>
<%@ page import="User.Client" %>
<%@ page import="User.User" %>
<%@ page import="User.Admin" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>CarRent Admin - User Management</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

        <!-- Google Fonts -->
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
                            <a href="Logout" class="btn btn-light">Logout</a>
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
                            <div class="card-header d-flex justify-content-between align-items-center">
                                <h3 class="mb-0">User Management</h3>
                                <button class="btn btn-primary" data-toggle="modal" data-target="#addUserModal">Add User</button>
                            </div>
                            <div class="card-body">
                                <ul class="nav nav-tabs" id="userTabs" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link active" id="client-tab" data-toggle="tab" href="#client-users" role="tab" aria-controls="client-users" aria-selected="true">Client Users</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="admin-tab" data-toggle="tab" href="#admin-users" role="tab" aria-controls="admin-users" aria-selected="false">Admin Users</a>
                                    </li>
                                </ul>
                                <div id="div_message" class="text-center mb-4" style="color: blue;"></div>

                                <div class="tab-content" id="userTabsContent">
                                    <div class="tab-pane fade show active" id="client-users" role="tabpanel" aria-labelledby="client-tab">
                                        <table class="table table-bordered mt-3">
                                            <thead>
                                                <tr>
                                                    <th>User ID</th>
                                                    <th>Name</th>
                                                    <th>Email</th>
                                                    <th>Phone Number</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                    AdminOperation adminOp = new AdminOperation();
                                                    List<Client> clientUsers = adminOp.getAllClientLevelUsers();

                                                    if (clientUsers != null && !clientUsers.isEmpty()) {
                                                        for (Client client : clientUsers) {
                                                %>
                                                <tr>
                                                    <td><%= client.getClientID()%></td>
                                                    <td><%= client.getName()%></td>
                                                    <td><%= client.getEmail()%></td>
                                                    <td><%= client.getPhoneNumber()%></td>
                                                    <td>
                                                        <a href="viewUserDetails.jsp?userID=<%= client.getClientID()%>" class="btn btn-sm btn-info">View</a>
                                                        <a href="editUserForm.jsp?userID=<%= client.getClientID()%>" class="btn btn-sm btn-warning">Edit</a>
                                                        <br><br>
                                                        <form action="DeleteClientServlet" method="post" onsubmit="return confirm('Are you sure you want to delete this user?');">
                                                            <input type="hidden" name="clientID" value="<%= client.getClientID()%>">
                                                            <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                                        </form>
                                                    </td>
                                                </tr>
                                                <%
                                                    }
                                                } else {
                                                %>
                                                <tr>
                                                    <td colspan="5" class="text-center">No client users found.</td>
                                                </tr>
                                                <%
                                                    }
                                                %>
                                            </tbody>
                                        </table>
                                    </div>

                                    <!-- Admin Users Tab -->
                                    <div class="tab-pane fade" id="admin-users" role="tabpanel" aria-labelledby="admin-tab">
                                        <table class="table table-bordered mt-3">
                                            <thead>
                                                <tr>
                                                    <th>Admin ID</th>
                                                    <th>User ID</th>
                                                    <th>Name</th>
                                                    <th>Email</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <%
                                                    List<Admin> adminUsers = adminOp.getAllAdminLevelUsers();

                                                    if (adminUsers != null && !adminUsers.isEmpty()) {
                                                        for (Admin admin : adminUsers) {
                                                %>
                                                <tr>
                                                    <td><%= admin.getAdminID()%></td>
                                                    <td><%= admin.getUserID()%></td>
                                                    <td><%= admin.getName()%></td>
                                                    <td><%= admin.getEmail()%></td>
                                                    <td>
                                                        <a href="viewAdminDetails.jsp?userID=<%= admin.getAdminID()%>" class="btn btn-sm btn-info">View</a>
                                                        <a href="editAdminForm.jsp?userID=<%= admin.getAdminID()%>" class="btn btn-sm btn-warning">Edit</a>
                                                        <br><br>
                                                        <form action="" method="post" onsubmit="return confirm('Are you sure you want to delete this user?');">
                                                            <input type="hidden" name="adminID" value="<%= admin.getAdminID()%>">
                                                            <button type="submit" class="btn btn-sm btn-danger">Delete</button>
                                                        </form>
                                                    </td>
                                                </tr>
                                                <%
                                                    }
                                                } else {
                                                %>
                                                <tr>
                                                    <td colspan="5" class="text-center">No admin users found.</td>
                                                </tr>
                                                <%
                                                    }
                                                %>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>
                </div>
            </div>

            <!-- Modal for Adding New User -->
            <div class="modal fade" id="addUserModal" tabindex="-1" aria-labelledby="addUserModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="addUserModalLabel">Add New User</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">×</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <form action="AddUserServlet" method="post">
                                <div class="form-group">
                                    <label for="userName">Name</label>
                                    <input type="text" class="form-control" id="userName" name="name" required>
                                </div>
                                <div class="form-group">
                                    <label for="userEmail">Email</label>
                                    <input type="email" class="form-control" id="userEmail" name="email" required>
                                </div>
                                <div class="form-group">
                                    <label for="userRole">Role</label>
                                    <select class="form-control" id="userRole" name="role" required>
                                        <option value="Customer">Customer</option>
                                        <option value="Admin">Admin</option>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label for="userPassword">Password</label>
                                    <input type="password" class="form-control" id="userPassword" name="password" required>
                                </div>
                                <button type="submit" class="btn btn-primary">Save</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Fixed Paths for JS -->
        <%@ include file="../include/admin-js.html" %>
    </body>
</html>
