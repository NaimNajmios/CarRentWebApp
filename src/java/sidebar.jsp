<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav class="col-md-3 col-lg-2 sidebar d-md-block">
    <ul class="nav flex-column mt-3">
        <li class="nav-item">
            <a class="nav-link <%= request.getRequestURI().contains("user-management.jsp") || request.getRequestURI().contains("viewUserDetails.jsp") || request.getRequestURI().contains("editUserForm.jsp") ? "active" : "" %>" href="user-management.jsp">User Management</a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getRequestURI().contains("car-management.jsp") ? "active" : "" %>" href="car-management.jsp">Car Management</a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getRequestURI().contains("maintenance.jsp") ? "active" : "" %>" href="maintenance.jsp">Vehicle Maintenance</a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getRequestURI().contains("reports.jsp") ? "active" : "" %>" href="reports.jsp">Reports</a>
        </li>
        <li class="nav-item">
            <a class="nav-link <%= request.getRequestURI().contains("payments") ? "active" : "" %>" href="payment-verification.jsp">Payment Verification</a>
        </li>
    </ul>
</nav>