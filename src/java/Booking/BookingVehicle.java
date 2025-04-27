package Booking;
import java.util.Objects;

// Class to represent a booking in the database
public class BookingVehicle {

    // Variables
    private String bookingID;
    private String clientID;
    private String bookingDate;
    private String startDate;
    private String endDate;
    private String actualRetualDate;
    private double totalCost;
    private String bookingStatus;
    private int createdBy;

    // Empty constructor
    public BookingVehicle() {
    }

    // Constructor
    public BookingVehicle(String bookingID, String clientID, String bookingDate, String startDate, String endDate, String actualRetualDate, double totalCost, String bookingStatus, int createdBy) {
        this.bookingID = bookingID;
        this.clientID = clientID;
        this.bookingDate = bookingDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.actualRetualDate = actualRetualDate;
        this.totalCost = totalCost;
        this.bookingStatus = bookingStatus;
        this.createdBy = createdBy;
    }

    // Getters and setters
    public String getBookingID() {
        return this.bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getClientID() {
        return this.clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getBookingDate() {
        return this.bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getActualRetualDate() {
        return this.actualRetualDate;
    }

    public void setActualRetualDate(String actualRetualDate) {
        this.actualRetualDate = actualRetualDate;
    }

    public double getTotalCost() {
        return this.totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getBookingStatus() {
        return this.bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public int getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }
    
    
}
