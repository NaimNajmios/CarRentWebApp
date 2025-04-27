package Report;

import User.Client;
import User.User;
import User.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

// Class to represent a report in the database

public class Report {
    
    // Variables
    // Booking : Booking summary, detailed booking, booking by client
    // Vehicles report : vehicle list, availability summary, usage summary, mileage report, rental performance
    // Payment report : payment details, payment summary, revenue by payment type
    // Maintenance report : maintenance schedule, maintenance history, maintenance cost analysis
    // User report : detailed registrations
    private String reportType;

    // Report duration, start date and end date
    private String startDate;
    private String endDate;

    // Duration (today/yesterday/last 7 days/this month/last month)
    private String duration;

    // Optional filter (depends on tpyes of report)

}


