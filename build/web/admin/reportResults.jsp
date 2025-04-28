<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>CarRent Admin - Report Results</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css2?family=Times+New+Roman:wght@400;700&display=swap" rel="stylesheet">
    <style>
        body {
            font-family: 'Times New Roman', Times, serif;
            background-color: #e9ecef;
            color: #000;
            margin: 0;
            padding: 0;
        }
        .site-wrap {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 20px;
            min-height: 100vh;
        }
        .report-container {
            background-color: #fff;
            width: 210mm; /* A4 width */
            min-height: 297mm; /* A4 height */
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            padding: 20mm;
            box-sizing: border-box;
            position: relative;
            margin-bottom: 20px;
            overflow: hidden; /* Prevent content from overflowing the container */
        }
        .report-header {
            text-align: center;
            border-bottom: 2px solid #000;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        .report-header h1 {
            font-size: 1.5rem;
            font-weight: 700;
            margin: 0;
            color: #000;
        }
        .report-header .report-date {
            font-size: 0.9rem;
            color: #555;
            margin-top: 5px;
        }
        .report-body {
            margin-bottom: 20px;
        }
        .table-container {
            max-width: 100%; /* Ensure the table container doesn't exceed the parent */
            overflow-x: auto; /* Enable horizontal scrolling on screen */
        }
        .table {
            width: 100%;
            border-collapse: collapse;
            font-size: 0.75rem; /* Smaller font size to fit more content */
        }
        .table th, .table td {
            border: 1px solid #000;
            padding: 4px; /* Reduced padding to save space */
            text-align: left;
            white-space: nowrap; /* Prevent text wrapping in cells */
        }
        .table th {
            background-color: #f2f2f2;
            font-weight: 700;
            color: #000;
        }
        .table td {
            background-color: #fff;
        }
        .no-data {
            text-align: center;
            font-style: italic;
            color: #555;
            padding: 20px;
        }
        .report-footer {
            position: absolute;
            bottom: 10mm;
            width: calc(100% - 40mm);
            text-align: center;
            font-size: 0.8rem;
            color: #555;
            border-top: 1px solid #000;
            padding-top: 5px;
        }
        .action-buttons {
            margin-bottom: 20px;
            text-align: center;
        }
        .btn-primary {
            background-color: #1a73e8;
            color: #fff;
            border: none;
            padding: 8px 20px;
            font-size: 1rem;
            font-weight: 500;
            transition: background-color 0.3s;
            cursor: pointer;
        }
        .btn-primary:hover {
            background-color: #1557b0;
        }
        @media print {
            body {
                background-color: #fff;
            }
            .site-wrap {
                padding: 0;
            }
            .report-container {
                box-shadow: none;
                width: 100%;
                min-height: auto;
                overflow: visible; /* Allow overflow for print */
            }
            .table-container {
                overflow-x: visible; /* Disable scrolling for print */
            }
            .table {
                font-size: 0.65rem; /* Further reduce font size for print */
                width: auto; /* Allow table to shrink if possible */
            }
            .table th, .table td {
                padding: 3px;
            }
            .action-buttons {
                display: none;
            }
            .report-footer {
                position: fixed;
                bottom: 0;
            }
            @page {
                size: A4;
                margin: 20mm;
            }
        }
    </style>
</head>
<body>
    <div class="site-wrap">
        <div class="report-container">
            <!-- Report Header -->
            <div class="report-header">
                <h1><%= request.getAttribute("reportTitle") != null ? request.getAttribute("reportTitle") : "Report" %></h1>
                <div class="report-date">
                    Generated on: <%= new java.text.SimpleDateFormat("MMMM dd, yyyy").format(new java.util.Date()) %>
                </div>
            </div>

            <!-- Action Buttons -->
            <div class="action-buttons">
                <button id="exportPdfBtn" class="btn-primary">Export to PDF</button>
            </div>

            <!-- Report Body -->
            <div class="report-body">
                <%
                    List<Map<String, Object>> results = (List<Map<String, Object>>) request.getAttribute("results");
                    Map<String, String> headers = (Map<String, String>) request.getAttribute("headers");

                    if (results != null && !results.isEmpty() && headers != null) {
                        out.println("<div class='table-container'>");
                        out.println("<table class='table'>");
                        out.println("<thead><tr>");
                        for (String column : headers.keySet()) {
                            out.println("<th>" + headers.get(column) + "</th>");
                        }
                        out.println("</tr></thead>");
                        out.println("<tbody>");
                        for (Map<String, Object> row : results) {
                            out.println("<tr>");
                            for (String column : headers.keySet()) {
                                Object value = row.get(column);
                                out.println("<td>" + (value != null ? value : "") + "</td>");
                            }
                            out.println("</tr>");
                        }
                        out.println("</tbody>");
                        out.println("</table>");
                        out.println("</div>");
                    } else {
                        out.println("<div class='no-data'>No data found for the selected criteria.</div>");
                    }
                %>
            </div>

            <!-- Report Footer -->
            <div class="report-footer">
                CarRent Admin - Page <span class="page-number">1</span>
            </div>
        </div>
    </div>

    <!-- JavaScript for PDF Export -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>
    <script>
        document.getElementById('exportPdfBtn').addEventListener('click', function() {
            const element = document.querySelector('.report-container');
            const opt = {
                margin: 20,
                filename: 'Report_Results.pdf',
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 2, width: 794 }, // 210mm at 96dpi
                jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
            };
            html2pdf().set(opt).from(element).save();
        });
    </script>
</body>
</html>