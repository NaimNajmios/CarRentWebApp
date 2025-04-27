<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Include Font Awesome for icons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<style>
    .nav-link i {
        margin-right: 15px; /* Custom spacing between icon and text */
    }
</style>
<nav class="col-md-3 col-lg-2 sidebar d-md-block">
    <ul class="nav flex-column mt-3">
        <%-- My profile --%>
        <li class="nav-item">
            <a class="nav-link" href="admin-profile.jsp">
                <i class="fas fa-user"></i>My Profile
            </a>
        </li>

        <li class="nav-item">
            <a class="nav-link <%= request.getRequestURI().contains("user-management.jsp") || request.getRequestURI().contains("viewUserDetails.jsp") || request.getRequestURI().contains("editUserForm.jsp") ? "active" : ""%>" href="user-management.jsp">
                <i class="fas fa-users"></i>User Management
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getRequestURI().contains("car-management.jsp") ? "active" : ""%>" href="car-management.jsp">
                <i class="fas fa-car"></i>Car Management
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getRequestURI().contains("maintenance.jsp") ? "active" : ""%>" href="maintenance.jsp">
                <i class="fas fa-wrench"></i>Vehicle Maintenance
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getRequestURI().contains("reports.jsp") ? "active" : ""%>" href="reports.jsp">
                <i class="fas fa-chart-bar"></i>Reports
            </a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getRequestURI().contains("payments") ? "active" : ""%>" href="payment-verification.jsp">
                <i class="fas fa-credit-card"></i>Payment Verification
            </a>
        </li>
    </ul>
</nav>