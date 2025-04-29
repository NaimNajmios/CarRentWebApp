<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <title>CarRent Admin - Report Results</title>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
        <style>
            body {
                font-family: 'Roboto', sans-serif;
                background-color: #f4f6f8;
                color: #333;
                margin: 0;
                padding: 0;
                display: flex;
                justify-content: center;
                align-items: center;
                min-height: 100vh;
            }
            .report-container {
                background-color: #fff;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
                border-radius: 8px;
                padding: 30px;
                width: 90%;
                max-width: 1200px;
                margin-bottom: 30px;
            }
            .report-header {
                text-align: left;
                border-bottom: 1px solid #eee;
                padding-bottom: 20px;
                margin-bottom: 30px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .report-header h1 {
                font-size: 1.75rem;
                font-weight: 700;
                color: #2c3e50;
                margin: 0;
            }
            .report-header .report-date {
                font-size: 0.9rem;
                color: #777;
            }
            .action-buttons {
                margin-bottom: 25px;
                text-align: right;
            }
            .btn-primary {
                background-color: #3498db;
                color: #fff;
                border: none;
                padding: 10px 20px;
                font-size: 1rem;
                font-weight: 500;
                border-radius: 6px;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }
            .btn-primary:hover {
                background-color: #2980b9;
            }
            .table-container {
                overflow-x: auto;
                border: 1px solid #ddd;
                border-radius: 6px;
            }
            .table {
                width: 100%;
                border-collapse: collapse;
                font-size: 0.9rem;
                white-space: nowrap;
            }
            .table th, .table td {
                border: 1px solid #ddd;
                padding: 12px 15px;
                text-align: left;
            }
            .table th {
                background-color: #f9f9f9;
                font-weight: 600;
                color: #555;
            }
            .table tbody tr:nth-child(even) {
                background-color: #f2f2f2;
            }
            .no-data {
                text-align: center;
                font-style: italic;
                color: #777;
                padding: 30px;
            }
            .report-footer {
                text-align: center;
                padding-top: 20px;
                color: #777;
                font-size: 0.85rem;
                border-top: 1px solid #eee;
                margin-top: 30px;
            }
            @media print {
                body {
                    background-color: #fff;
                }
                .report-container {
                    box-shadow: none;
                    width: 100%;
                    max-width: none;
                }
                .action-buttons {
                    display: none;
                }
                .table-container {
                    overflow-x: visible;
                }
                .table {
                    font-size: 0.8rem;
                }
                .report-footer {
                    position: fixed;
                    bottom: 0;
                    left: 0;
                    width: 100%;
                    background-color: #fff;
                    padding: 10px 0;
                    border-top: 1px solid #eee;
                    text-align: center;
                    font-size: 0.75rem;
                    color: #555;
                }
                @page {
                    size: A4;
                    margin: 15mm;
                }
            }
        </style>
    </head>
    <body>
        <div class="report-container">
            <div class="report-header">
                <h1><%= request.getAttribute("reportTitle") != null ? request.getAttribute("reportTitle") : "Report"%></h1>
                <div class="report-date">
                    Generated on: <%= new java.text.SimpleDateFormat("MMMM dd, yyyy").format(new java.util.Date())%>
                </div>
            </div>

            <div class="action-buttons">
                <button id="exportPdfBtn" class="btn-primary">Export to PDF</button>
            </div>

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

            <div class="report-footer">
                CarRent Admin - Report generated on <%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date())%>
            </div>
        </div>

        <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>
        <script>
            document.getElementById('exportPdfBtn').addEventListener('click', function () {
                const element = document.querySelector('.report-container');
                const opt = {
                    margin: 15,
                    filename: 'CarRent_Report.pdf',
                    image: {type: 'jpeg', quality: 0.98},
                    html2canvas: {scale: 2, width: 1123},
                    jsPDF: {unit: 'mm', format: 'a4', orientation: 'landscape'}
                };
                html2pdf().set(opt).from(element).save();
            });
        </script>
    </body>
</html>