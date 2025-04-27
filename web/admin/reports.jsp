<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>CarRent Admin - Reports</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link href="https://fonts.googleapis.com/css?family=DM+Sans:300,400,700&display=swap" rel="stylesheet">
        <!-- Include existing admin styling -->
        <%@ include file="../include/admin-styling.html" %>
        <style>
            .tab-content {
                margin-top: 20px;
            }
            .report-form {
                padding: 20px;
                border: 1px solid #dee2e6;
                border-radius: 8px;
            }
            .report-preview {
                margin-top: 20px;
            }
        </style>
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
                            <a href="Logout" class="btn btn-light">Logout</a>
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
                                        <a class="nav-link active" id="user-tab" data-toggle="tab" href="#user" role="tab" aria-controls="user" aria-selected="true">User Reports</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="booking-tab" data-toggle="tab" href="#booking" role="tab" aria-controls="booking" aria-selected="false">Booking Reports</a>
                                    </li>
                                    <li class="nav-item">
                                        <a class="nav-link" id="vehicle-tab" data-toggle="tab" href="#vehicle" role="tab" aria-controls="vehicle" aria-selected="false">Vehicle Reports</a>
                                    </li>
                                </ul>
                                <div class="tab-content" id="reportTabsContent">
                                    <!-- User Reports Tab -->
                                    <div class="tab-pane fade show active" id="user" role="tabpanel" aria-labelledby="user-tab">
                                        <div class="report-form">
                                            <h5>User Reports</h5>
                                            <form action="GenerateReportServlet" method="post">
                                                <input type="hidden" name="reportCategory" value="user">
                                                <div class="form-group">
                                                    <label for="userReportType">Report Type</label>
                                                    <select class="form-control" id="userReportType" name="reportType">
                                                        <option value="registrations">New Registrations</option>
                                                        <option value="logins">Login Activity</option>
                                                        <option value="updates">Profile Updates</option>
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label for="userStartDate">Start Date</label>
                                                    <input type="text" class="form-control datepicker" id="userStartDate" name="startDate" placeholder="Select start date">
                                                </div>
                                                <div class="form-group">
                                                    <label for="userEndDate">End Date</label>
                                                    <input type="text" class="form-control datepicker" id="userEndDate" name="endDate" placeholder="Select end date">
                                                </div>
                                                <button type="submit" class="btn btn-primary">Generate Report</button>
                                            </form>
                                        </div>
                                    </div>
                                    <!-- Booking Reports Tab -->
                                    <div class="tab-pane fade" id="booking" role="tabpanel" aria-labelledby="booking-tab">
                                        <div class="report-form">
                                            <h5>Booking Reports</h5>
                                            <form action="GenerateReportServlet" method="post">
                                                <input type="hidden" name="reportCategory" value="booking">
                                                <div class="form-group">
                                                    <label for="bookingReportType">Report Type</label>
                                                    <select class="form-control" id="bookingReportType" name="reportType">
                                                        <option value="statistics">Booking Statistics</option>
                                                        <option value="cancellations">Cancellations</option>
                                                        <option value="payments">Payment Transactions</option>
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label for="bookingStartDate">Start Date</label>
                                                    <input type="text" class="form-control datepicker" id="bookingStartDate" name="startDate" placeholder="Select start date">
                                                </div>
                                                <div class="form-group">
                                                    <label for="bookingEndDate">End Date</label>
                                                    <input type="text" class="form-control datepicker" id="bookingEndDate" name="endDate" placeholder="Select end date">
                                                </div>
                                                <button type="submit" class="btn btn-primary">Generate Report</button>
                                            </form>
                                        </div>
                                    </div>
                                    <!-- Vehicle Reports Tab -->
                                    <div class="tab-pane fade" id="vehicle" role="tabpanel" aria-labelledby="vehicle-tab">
                                        <div class="report-form">
                                            <h5>Vehicle Reports</h5>
                                            <form action="GenerateReportServlet" method="post">
                                                <input type="hidden" name="reportCategory" value="vehicle">
                                                <div class="form-group">
                                                    <label for="vehicleReportType">Report Type</label>
                                                    <select class="form-control" id="vehicleReportType" name="reportType">
                                                        <option value="availability">Vehicle Availability</option>
                                                        <option value="usage">Vehicle Usage</option>
                                                        <option value="maintenance">Maintenance Schedules</option>
                                                    </select>
                                                </div>
                                                <div class="form-group">
                                                    <label for="vehicleStartDate">Start Date</label>
                                                    <input type="text" class="form-control datepicker" id="vehicleStartDate" name="startDate" placeholder="Select start date">
                                                </div>
                                                <div class="form-group">
                                                    <label for="vehicleEndDate">End Date</label>
                                                    <input type="text" class="form-control datepicker" id="vehicleEndDate" name="endDate" placeholder="Select end date">
                                                </div>
                                                <button type="submit" class="btn btn-primary">Generate Report</button>
                                            </form>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </main>
                </div>
            </div>
        </div>
        <!-- Include existing admin JavaScript -->
        <%@ include file="../include/admin-js.html" %>
        <script>
            $(document).ready(function () {
                $('.datepicker').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true
                });
            });
        </script>
    </body>
</html>