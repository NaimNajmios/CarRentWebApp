<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>CarRent - Welcome</title>
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
        header {
            padding: 1rem 0; /* Adjust as needed */
            background-color: #f8f9fa; /* Match body background or your header's */
            /* Add any other header styles from client-header.jsp here */
            /* Example: border-bottom: 1px solid #eee; */
        }
        .navbar {
            padding-top: 0.5rem; /* Adjust if needed */
            padding-bottom: 0.5rem; /* Adjust if needed */
        }
        .hero-section {
            flex: 1 0 auto;
            position: relative;
            background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('images/hero_1.jpg') no-repeat center center/cover;
            color: white;
            display: flex;
            align-items: center;
            text-align: center;
            /* Adjust the '80px' value to match your header's actual height */
            min-height: calc(100vh - 80px);
            padding: 2rem 0;
        }
        .hero-container {
            width: 100%;
            max-width: 800px;
            margin: 0 auto;
            padding: 0 1.5rem;
        }
        .hero-section h1 {
            font-size: 3rem;
            font-weight: 700;
            margin-bottom: 1rem;
        }
        .hero-section p {
            font-size: 1.15rem;
            margin-bottom: 2rem;
            color: #eee;
        }
        .hero-section .btn-primary {
            font-size: 1.1rem;
            padding: 0.75rem 2rem;
            border-radius: 8px;
            background-color: #007bff;
            border: none;
        }
        .hero-section .btn-primary:hover {
            background-color: #0056b3;
        }
        footer.sticky-footer {
            flex-shrink: 0;
            padding: 1rem 0;
            background-color: #343a40;
            color: #fff;
            text-align: center;
        }
    </style>
</head>
<body>
    <%@ include file="../include/client-header.jsp" %>

    <div class="hero-section">
        <div class="hero-container">
            <h1>Discover Your Perfect Ride</h1>
            <p>Unlock a world of possibilities with our diverse selection of rental cars. Adventure awaits!</p>
            <a href="cars.jsp" class="btn btn-primary">Explore Our Cars</a>
        </div>
    </div>

    <%@ include file="../include/client-footer.jsp" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>