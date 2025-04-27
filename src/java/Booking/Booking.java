package Booking;

// Class to represent a booking in the database
public class Booking {
    
    // Attributes
    private int bookingID;
    private String vehicleID;
    private String assignedDate;

    // Empty constructor 
    public Booking() {
        
    }

    // Constructor
    public Booking(int bookingID, String vehicleID, String assignedDate) {
        this.bookingID = bookingID;
        this.vehicleID = vehicleID;
        this.assignedDate = assignedDate;
    }

    // Getters and Setters

    public int getBookingID() {
        return this.bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public String getVehicleID() {
        return this.vehicleID;
    }

    public void setVehicleID(String vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getAssignedDate() {
        return this.assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingID=" + bookingID +
                ", vehicleID='" + vehicleID + '\'' +
                ", assignedDate='" + assignedDate + '\'' +
                '}';
    }

}
