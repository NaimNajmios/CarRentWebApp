<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>CarRent - Welcome</title>
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
        .hero-section {
            position: relative;
            height: 100vh;
            background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('images/hero_1.jpg') no-repeat center center/cover;
            color: white;
            display: flex;
            align-items: center;
            text-align: center;
        }
        .hero-section h1 {
            font-size: 3.5rem;
            font-weight: 700;
            margin-bottom: 1rem;
        }
        .hero-section p {
            font-size: 1.25rem;
            margin-bottom: 2rem;
        }
        .hero-section .btn-primary {
            font-size: 1.1rem;
            padding: 0.75rem 2rem;
        }
    </style>
</head>
<body>
    <!-- Header Include -->
    <%@ include file="../include/client-header.jsp" %>

    <!-- Hero Section -->
    <div class="hero-section">
        <div class="container">
            <h1>Welcome to CarRent</h1>
            <p>Discover the freedom of the open road with our premium car rental service.</p>
            <a href="cars.jsp" class="btn btn-primary">Browse Cars Now</a>
        </div>
    </div>

    <!-- Footer Include -->
    <%@ include file="../include/client-footer.jsp" %>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>