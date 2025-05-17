<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>CarRent Admin - Reports</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link href="https://fonts.googleapis.com/css?family=DM+Sans:300,400,700&display=swap" rel="stylesheet">
        <%@ include file="../include/admin-styling.html" %>
        <%@ include file="../include/admin-report-styling.html" %>
    </head>
    <body data-spy="scroll" data-target=".site-navbar-target" data-offset="300">
        <div class="site-wrap" id="reports-section">
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
                                <h3>Reports</h3>
                            </div>
                            <div class="card-body">
                                <ul class="nav nav-tabs" id="reportTabs" role="tablist">
                                    <li class="nav-item">
                                        <a class="nav-link active" id="booking-tab" data-toggle="tab" href="#booking" role="tab" aria-controls="booking" aria-selected="true">Booking Reports</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="vehicle-tab" data-toggle="tab" href="#vehicle" role="tab" aria-controls="vehicle" aria-selected="false">Vehicle Reports</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="payment-tab" data-toggle="tab" href="#payment" role="tab" aria-controls="payment" aria-selected="false">Payment Reports</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="maintenance-tab" data-toggle="tab" href="#maintenance" role="tab" aria-controls="maintenance" aria-selected="false">Maintenance Reports</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="user-tab" data-toggle="tab" href="#user" role="tab" aria-controls="user" aria-selected="false">User Reports</a>
                                    </li>
                                </ul>
                                <div class="tab-content" id="reportTabsContent">
                                    <%-- Booking Reports Tab --%>
                                    <div class="tab-pane fade show active" id="booking" role="tabpanel" aria-labelledby="booking-tab">
                                        <div class="report-form">
                                            <h5>Booking Reports</h5>
                                            <form action="<%= request.getContextPath()%>/GenerateReport" method="post" target="_blank" id="bookingReportForm">
                                                <input type="hidden" name="reportCategory" value="booking">
                                                <div class="report-type-group">
                                                    <h6>Report Type</h6>
                                                    <div class="form-group">
                                                        <label for="bookingReportType">Select Report Type</label>
                                                        <select class="form-control" id="bookingReportType" name="reportType" required>
                                                            <option value="">Select a report type</option>
                                                            <option value="booking_summary">Booking Summary</option>
                                                            <option value="detailed_bookings">Detailed Bookings</option>
                                                            <option value="bookings_by_client">Bookings by Client</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="duration-group">
                                                    <h6>Duration</h6>
                                                    <div class="date-selection-group">
                                                        <label>Date Range:</label>
                                                        <div>
                                                            <input type="radio" id="bookingToday" name="dateRangeType" value="today" required>
                                                            <label for="bookingToday">Today</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="bookingYesterday" name="dateRangeType" value="yesterday">
                                                            <label for="bookingYesterday">Yesterday</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="bookingLast7Days" name="dateRangeType" value="last_7_days">
                                                            <label for="bookingLast7Days">Last 7 Days</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="bookingThisMonth" name="dateRangeType" value="this_month">
                                                            <label for="bookingThisMonth">This Month</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="bookingLastMonth" name="dateRangeType" value="last_month">
                                                            <label for="bookingLastMonth">Last Month</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="bookingCustom" name="dateRangeType" value="custom">
                                                            <label for="bookingCustom">Custom</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="date-range-group">
                                                    <h6>Custom Date Range</h6>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label for="bookingStartDate">Start Date</label>
                                                            <input type="text" class="form-control datepicker" id="bookingStartDate" name="startDate" placeholder="Select start date" disabled>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <label for="bookingEndDate">End Date</label>
                                                            <input type="text" class="form-control datepicker" id="bookingEndDate" name="endDate" placeholder="Select end date" disabled>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="filter-group">
                                                    <h6>Optional Filters</h6>
                                                    <div class="form-group">
                                                        <label for="bookingStatusFilter">Booking Status</label>
                                                        <select class="form-control" id="bookingStatusFilter" name="bookingStatus">
                                                            <option value="">All</option>
                                                            <option value="Pending">Pending</option>
                                                            <option value="Confirmed">Confirmed</option>
                                                            <option value="Completed">Completed</option>
                                                            <option value="Cancelled">Cancelled</option>
                                                        </select>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="vehicleFilter">Vehicle</label>
                                                        <select class="form-control" id="vehicleFilter" name="vehicleId">
                                                            <option value="">All Vehicles</option>
                                                            <option value="1">Vehicle Model 1</option>
                                                            <option value="2">Vehicle Model 2</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <button type="submit" class="btn btn-primary">Generate Report</button>
                                            </form>
                                            <script>
                                                // Booking tab: Enable/disable custom date fields and form validation
                                                document.querySelectorAll('#booking input[name="dateRangeType"]').forEach(function(radio) {
                                                    radio.addEventListener('change', function() {
                                                        const startDateInput = document.getElementById('bookingStartDate');
                                                        const endDateInput = document.getElementById('bookingEndDate');
                                                        const isCustom = this.value === 'custom';
                                                        startDateInput.disabled = !isCustom;
                                                        endDateInput.disabled = !isCustom;
                                                        if (isCustom) {
                                                            startDateInput.required = true;
                                                            endDateInput.required = true;
                                                        } else {
                                                            startDateInput.required = false;
                                                            endDateInput.required = false;
                                                            startDateInput.value = '';
                                                            endDateInput.value = '';
                                                        }
                                                    });
                                                });

                                                document.getElementById('bookingReportForm').addEventListener('submit', function(e) {
                                                    const reportType = document.getElementById('bookingReportType').value;
                                                    const dateRangeType = document.querySelector('input[name="dateRangeType"]:checked');
                                                    const startDate = document.getElementById('bookingStartDate');
                                                    const endDate = document.getElementById('bookingEndDate');

                                                    if (!reportType) {
                                                        e.preventDefault();
                                                        alert('Please select a report type.');
                                                        return;
                                                    }

                                                    if (!dateRangeType) {
                                                        e.preventDefault();
                                                        alert('Please select a date range.');
                                                        return;
                                                    }

                                                    if (dateRangeType.value === 'custom' && (!startDate.value || !endDate.value)) {
                                                        e.preventDefault();
                                                        alert('Please select both start and end dates for custom range.');
                                                        return;
                                                    }
                                                });
                                            </script>
                                        </div>
                                    </div>

                                    <%-- Vehicle Reports Tab --%>
                                    <div class="tab-pane fade" id="vehicle" role="tabpanel" aria-labelledby="vehicle-tab">
                                        <div class="report-form">
                                            <h5>Vehicle Reports</h5>
                                            <form action="<%= request.getContextPath()%>/GenerateReport" method="post" target="_blank" id="vehicleReportForm">
                                                <input type="hidden" name="reportCategory" value="vehicle">
                                                <div class="report-type-group">
                                                    <h6>Report Type</h6>
                                                    <div class="form-group">
                                                        <label for="vehicleReportType">Select Report Type</label>
                                                        <select class="form-control" id="vehicleReportType" name="reportType" required>
                                                            <option value="">Select a report type</option>
                                                            <option value="vehicle_list">Vehicle List</option>
                                                            <option value="availability_summary">Availability Summary</option>
                                                            <option value="usage_summary">Usage Summary</option>
                                                            <option value="mileage_report">Mileage Report</option>
                                                            <option value="rental_performance">Rental Performance</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="duration-group">
                                                    <h6>Duration</h6>
                                                    <div class="date-selection-group">
                                                        <label>Date Range (for Usage/Performance):</label>
                                                        <div>
                                                            <input type="radio" id="vehicleThisMonth" name="dateRangeType" value="this_month" required>
                                                            <label for="vehicleThisMonth">This Month</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="vehicleLastMonth" name="dateRangeType" value="last_month">
                                                            <label for="vehicleLastMonth">Last Month</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="vehicleCustom" name="dateRangeType" value="custom">
                                                            <label for="vehicleCustom">Custom</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="date-range-group">
                                                    <h6>Custom Date Range</h6>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label for="vehicleStartDate">Start Date (for Usage/Performance)</label>
                                                            <input type="text" class="form-control datepicker" id="vehicleStartDate" name="startDate" placeholder="Select start date" disabled>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <label for="vehicleEndDate">End Date (for Usage/Performance)</label>
                                                            <input type="text" class="form-control datepicker" id="vehicleEndDate" name="endDate" placeholder="Select end date" disabled>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="filter-group">
                                                    <h6>Optional Filters</h6>
                                                    <div class="form-group">
                                                        <label for="vehicleCategoryFilter">Category</label>
                                                        <select class="form-control" id="vehicleCategoryFilter" name="category">
                                                            <option value="">All</option>
                                                            <option value="Sedan">Sedan</option>
                                                            <option value="SUV">SUV</option>
                                                            <option value="Truck">Truck</option>
                                                        </select>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="vehicleAvailabilityFilter">Availability</label>
                                                        <select class="form-control" id="vehicleAvailabilityFilter" name="availability">
                                                            <option value="">All</option>
                                                            <option value="1">Available</option>
                                                            <option value="0">Not Available</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <button type="submit" class="btn btn-primary">Generate Report</button>
                                            </form>
                                            <script>
                                                // Vehicle tab: Enable/disable custom date fields and form validation
                                                document.querySelectorAll('#vehicle input[name="dateRangeType"]').forEach(function(radio) {
                                                    radio.addEventListener('change', function() {
                                                        const startDateInput = document.getElementById('vehicleStartDate');
                                                        const endDateInput = document.getElementById('vehicleEndDate');
                                                        const isCustom = this.value === 'custom';
                                                        startDateInput.disabled = !isCustom;
                                                        endDateInput.disabled = !isCustom;
                                                        if (isCustom) {
                                                            startDateInput.required = true;
                                                            endDateInput.required = true;
                                                        } else {
                                                            startDateInput.required = false;
                                                            endDateInput.required = false;
                                                            startDateInput.value = '';
                                                            endDateInput.value = '';
                                                        }
                                                    });
                                                });

                                                document.getElementById('vehicleReportForm').addEventListener('submit', function(e) {
                                                    const reportType = document.getElementById('vehicleReportType').value;
                                                    const dateRangeType = document.querySelector('#vehicle input[name="dateRangeType"]:checked');
                                                    const startDate = document.getElementById('vehicleStartDate');
                                                    const endDate = document.getElementById('vehicleEndDate');

                                                    if (!reportType) {
                                                        e.preventDefault();
                                                        alert('Please select a report type.');
                                                        return;
                                                    }

                                                    if (!dateRangeType) {
                                                        e.preventDefault();
                                                        alert('Please select a date range.');
                                                        return;
                                                    }

                                                    if (dateRangeType.value === 'custom' && (!startDate.value || !endDate.value)) {
                                                        e.preventDefault();
                                                        alert('Please select both start and end dates for custom range.');
                                                        return;
                                                    }
                                                });
                                            </script>
                                        </div>
                                    </div>

                                    <%-- Payment Reports Tab --%>
                                    <div class="tab-pane fade" id="payment" role="tabpanel" aria-labelledby="payment-tab">
                                        <div class="report-form">
                                            <h5>Payment Reports</h5>
                                            <form action="<%= request.getContextPath()%>/GenerateReport" method="post" target="_blank" id="paymentReportForm">
                                                <input type="hidden" name="reportCategory" value="payment">
                                                <div class="report-type-group">
                                                    <h6>Report Type</h6>
                                                    <div class="form-group">
                                                        <label for="paymentReportType">Select Report Type</label>
                                                        <select class="form-control" id="paymentReportType" name="reportType" required>
                                                            <option value="">Select a report type</option>
                                                            <option value="payment_details">Payment Details</option>
                                                            <option value="payment_summary">Payment Summary</option>
                                                            <option value="revenue_by_payment_type">Revenue by Payment Type</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="duration-group">
                                                    <h6>Duration</h6>
                                                    <div class="date-selection-group">
                                                        <label>Date Range:</label>
                                                        <div>
                                                            <input type="radio" id="paymentToday" name="dateRangeType" value="today" required>
                                                            <label for="paymentToday">Today</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="paymentYesterday" name="dateRangeType" value="yesterday">
                                                            <label for="paymentYesterday">Yesterday</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="paymentLast7Days" name="dateRangeType" value="last_7_days">
                                                            <label for="paymentLast7Days">Last 7 Days</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="paymentThisMonth" name="dateRangeType" value="this_month">
                                                            <label for="paymentThisMonth">This Month</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="paymentLastMonth" name="dateRangeType" value="last_month">
                                                            <label for="paymentLastMonth">Last Month</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="paymentCustom" name="dateRangeType" value="custom">
                                                            <label for="paymentCustom">Custom</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="date-range-group">
                                                    <h6>Custom Date Range</h6>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label for="paymentStartDate">Start Date</label>
                                                            <input type="text" class="form-control datepicker" id="paymentStartDate" name="startDate" placeholder="Select start date" disabled>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <label for="paymentEndDate">End Date</label>
                                                            <input type="text" class="form-control datepicker" id="paymentEndDate" name="endDate" placeholder="Select end date" disabled>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="filter-group">
                                                    <h6>Optional Filters</h6>
                                                    <div class="form-group">
                                                        <label for="paymentTypeFilter">Payment Type</label>
                                                        <select class="form-control" id="paymentTypeFilter" name="paymentType">
                                                            <option value="">All</option>
                                                            <option value="Credit Card">Credit Card</option>
                                                            <option value="Debit Card">Debit Card</option>
                                                            <option value="Cash">Cash</option>
                                                        </select>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="paymentStatusFilter">Payment Status</label>
                                                        <select class="form-control" id="paymentStatusFilter" name="paymentStatus">
                                                            <option value="">All</option>
                                                            <option value="Paid">Paid</option>
                                                            <option value="Pending">Pending</option>
                                                            <option value="Refunded">Refunded</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <button type="submit" class="btn btn-primary">Generate Report</button>
                                            </form>
                                            <script>
                                                // Payment tab: Enable/disable custom date fields and form validation
                                                document.querySelectorAll('#payment input[name="dateRangeType"]').forEach(function(radio) {
                                                    radio.addEventListener('change', function() {
                                                        const startDateInput = document.getElementById('paymentStartDate');
                                                        const endDateInput = document.getElementById('paymentEndDate');
                                                        const isCustom = this.value === 'custom';
                                                        startDateInput.disabled = !isCustom;
                                                        endDateInput.disabled = !isCustom;
                                                        if (isCustom) {
                                                            startDateInput.required = true;
                                                            endDateInput.required = true;
                                                        } else {
                                                            startDateInput.required = false;
                                                            endDateInput.required = false;
                                                            startDateInput.value = '';
                                                            endDateInput.value = '';
                                                        }
                                                    });
                                                });

                                                document.getElementById('paymentReportForm').addEventListener('submit', function(e) {
                                                    const reportType = document.getElementById('paymentReportType').value;
                                                    const dateRangeType = document.querySelector('#payment input[name="dateRangeType"]:checked');
                                                    const startDate = document.getElementById('paymentStartDate');
                                                    const endDate = document.getElementById('paymentEndDate');

                                                    if (!reportType) {
                                                        e.preventDefault();
                                                        alert('Please select a report type.');
                                                        return;
                                                    }

                                                    if (!dateRangeType) {
                                                        e.preventDefault();
                                                        alert('Please select a date range.');
                                                        return;
                                                    }

                                                    if (dateRangeType.value === 'custom' && (!startDate.value || !endDate.value)) {
                                                        e.preventDefault();
                                                        alert('Please select both start and end dates for custom range.');
                                                        return;
                                                    }
                                                });
                                            </script>
                                        </div>
                                    </div>

                                    <%-- Maintenance Reports Tab --%>
                                    <div class="tab-pane fade" id="maintenance" role="tabpanel" aria-labelledby="maintenance-tab">
                                        <div class="report-form">
                                            <h5>Maintenance Reports</h5>
                                            <form action="<%= request.getContextPath()%>/GenerateReport" method="post" target="_blank" id="maintenanceReportForm">
                                                <input type="hidden" name="reportCategory" value="maintenance">
                                                <div class="report-type-group">
                                                    <h6>Report Type</h6>
                                                    <div class="form-group">
                                                        <label for="maintenanceReportType">Select Report Type</label>
                                                        <select class="form-control" id="maintenanceReportType" name="reportType" required>
                                                            <option value="">Select a report type</option>
                                                            <option value="maintenance_schedule">Maintenance Schedule</option>
                                                            <option value="maintenance_history">Maintenance History</option>
                                                            <option value="cost_analysis">Maintenance Cost Analysis</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="duration-group">
                                                    <h6>Duration</h6>
                                                    <div class="date-selection-group">
                                                        <label>Date Range:</label>
                                                        <div>
                                                            <input type="radio" id="maintenanceThisMonth" name="dateRangeType" value="this_month" required>
                                                            <label for="maintenanceThisMonth">This Month</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="maintenanceLastMonth" name="dateRangeType" value="last_month">
                                                            <label for="maintenanceLastMonth">Last Month</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="maintenanceCustom" name="dateRangeType" value="custom">
                                                            <label for="maintenanceCustom">Custom</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="date-range-group">
                                                    <h6>Custom Date Range</h6>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label for="maintenanceStartDate">Start Date</label>
                                                            <input type="text" class="form-control datepicker" id="maintenanceStartDate" name="startDate" placeholder="Select start date" disabled>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <label for="maintenanceEndDate">End Date</label>
                                                            <input type="text" class="form-control datepicker" id="maintenanceEndDate" name="endDate" placeholder="Select end date" disabled>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="filter-group">
                                                    <h6>Optional Filters</h6>
                                                    <div class="form-group">
                                                        <label for="maintenanceVehicleFilter">Vehicle</label>
                                                        <select class="form-control" id="maintenanceVehicleFilter" name="vehicleId">
                                                            <option value="">All Vehicles</option>
                                                            <option value="1">Vehicle Model 1</option>
                                                            <option value="2">Vehicle Model 2</option>
                                                        </select>
                                                    </div>
                                                    <div class="form-group">
                                                        <label for="maintenanceStatusFilter">Status</label>
                                                        <select class="form-control" id="maintenanceStatusFilter" name="maintenanceStatus">
                                                            <option value="">All</option>
                                                            <option value="Pending">Pending</option>
                                                            <option value="Completed">Completed</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <button type="submit" class="btn btn-primary">Generate Report</button>
                                            </form>
                                            <script>
                                                // Maintenance tab: Enable/disable custom date fields and form validation
                                                document.querySelectorAll('#maintenance input[name="dateRangeType"]').forEach(function(radio) {
                                                    radio.addEventListener('change', function() {
                                                        const startDateInput = document.getElementById('maintenanceStartDate');
                                                        const endDateInput = document.getElementById('maintenanceEndDate');
                                                        const isCustom = this.value === 'custom';
                                                        startDateInput.disabled = !isCustom;
                                                        endDateInput.disabled = !isCustom;
                                                        if (isCustom) {
                                                            startDateInput.required = true;
                                                            endDateInput.required = true;
                                                        } else {
                                                            startDateInput.required = false;
                                                            endDateInput.required = false;
                                                            startDateInput.value = '';
                                                            endDateInput.value = '';
                                                        }
                                                    });
                                                });

                                                document.getElementById('maintenanceReportForm').addEventListener('submit', function(e) {
                                            </script>
                                        </div>
                                    </div>

                                    <%-- User Reports Tab --%>
                                    <div class="tab-pane fade" id="user" role="tabpanel" aria-labelledby="user-tab">
                                        <div class="report-form">
                                            <h5>User Reports</h5>
                                            <form action="<%= request.getContextPath()%>/GenerateReport" method="post" target="_blank" id="userReportForm">
                                                <input type="hidden" name="reportCategory" value="user">
                                                <div class="report-type-group">
                                                    <h6>Report Type</h6>
                                                    <div class="form-group">
                                                        <label for="userReportType">Select Report Type</label>
                                                        <select class="form-control" id="userReportType" name="reportType" required>
                                                            <option value="">Select a report type</option>
                                                            <option value="detailed_registrations">Detailed Registrations</option>
                                                            <option value="summary">User Summary</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="duration-group">
                                                    <h6>Duration</h6>
                                                    <div class="date-selection-group">
                                                        <label>Date Range:</label>
                                                        <div>
                                                            <input type="radio" id="userThisMonth" name="dateRangeType" value="this_month" required>
                                                            <label for="userThisMonth">This Month</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="userLastMonth" name="dateRangeType" value="last_month">
                                                            <label for="userLastMonth">Last Month</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="userLast7Days" name="dateRangeType" value="last_7_days">
                                                            <label for="userLast7Days">Last 7 Days</label>
                                                        </div>
                                                        <div>
                                                            <input type="radio" id="userCustom" name="dateRangeType" value="custom">
                                                            <label for="userCustom">Custom</label>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="date-range-group">
                                                    <h6>Custom Date Range</h6>
                                                    <div class="row">
                                                        <div class="col-md-6">
                                                            <label for="userStartDate">Start Date</label>
                                                            <input type="text" class="form-control datepicker" id="userStartDate" name="startDate" placeholder="Select start date" disabled>
                                                        </div>
                                                        <div class="col-md-6">
                                                            <label for="userEndDate">End Date</label>
                                                            <input type="text" class="form-control datepicker" id="userEndDate" name="endDate" placeholder="Select end date" disabled>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="filter-group">
                                                    <h6>Optional Filters</h6>
                                                    <div class="form-group">
                                                        <label for="userRoleFilter">User Role</label>
                                                        <select class="form-control" id="userRoleFilter" name="role">
                                                            <option value="">All</option>
                                                            <option value="client">Client</option>
                                                            <option value="administrator">Administrator</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <button type="submit" class="btn btn-primary">Generate Report</button>
                                            </form>
                                            <script>
                                                // User tab: Enable/disable custom date fields and form validation
                                                document.querySelectorAll('#user input[name="dateRangeType"]').forEach(function(radio) {
                                                    radio.addEventListener('change', function() {
                                                        const startDateInput = document.getElementById('userStartDate');
                                                        const endDateInput = document.getElementById('userEndDate');
                                                        const isCustom = this.value === 'custom';
                                                        startDateInput.disabled = !isCustom;
                                                        endDateInput.disabled = !isCustom;
                                                        if (isCustom) {
                                                            startDateInput.required = true;
                                                            endDateInput.required = true;
                                                        } else {
                                                            startDateInput.required = false;
                                                            endDateInput.required = false;
                                                            startDateInput.value = '';
                                                            endDateInput.value = '';
                                                        }
                                                    });
                                                });

                                                document.getElementById('userReportForm').addEventListener('submit', function(e) {
                                                    const reportType = document.getElementById('userReportType').value;
                                                    const dateRangeType = document.querySelector('input[name="dateRangeType"]:checked');
                                                    const startDate = document.getElementById('userStartDate');
                                                    const endDate = document.getElementById('userEndDate');

                                                    if (!reportType) {
                                                        e.preventDefault();
                                                        alert('Please select a report type.');
                                                        return;
                                                    }

                                                    if (!dateRangeType) {
                                                        e.preventDefault();
                                                        alert('Please select a date range.');
                                                        return;
                                                    }

                                                    if (dateRangeType.value === 'custom' && (!startDate.value || !endDate.value)) {
                                                        e.preventDefault();
                                                        alert('Please select both start and end dates for custom range.');
                                                        return;
                                                    }
                                                });
                                            </script>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>
                </div>
            </div>
            <%@ include file="../include/admin-js.html" %>
        </div>
    </body>
</html>