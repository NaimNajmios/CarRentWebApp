package Vehicle;

// Class to represent a vehicle in the database
public class VehicleMaintenance {

    private int maintenanceId;
    private String vehicleId;
    private String maintenanceDate;
    private String maintenanceDesc;
    private double maintenanceCost;
    private String maintenanceStatus;
    private int handledBy;

    // Empty constructor
    public VehicleMaintenance() {
    }

    // Constructor
    public VehicleMaintenance(int maintenanceId, String vehicleId, String maintenanceDate, String maintenanceDesc, double maintenanceCost, String maintenanceStatus, int handledBy) {
        this.maintenanceId = maintenanceId;
        this.vehicleId = vehicleId;
        this.maintenanceDate = maintenanceDate;
        this.maintenanceDesc = maintenanceDesc;
        this.maintenanceCost = maintenanceCost;
        this.maintenanceStatus = maintenanceStatus;
        this.handledBy = handledBy;
    }

    // Getters and setters

    public int getMaintenanceId() {
        return this.maintenanceId;
    }

    public void setMaintenanceId(int maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public String getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getMaintenanceDate() {
        return this.maintenanceDate;
    }

    public void setMaintenanceDate(String maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public String getMaintenanceDesc() {
        return this.maintenanceDesc;
    }

    public void setMaintenanceDesc(String maintenanceDesc) {
        this.maintenanceDesc = maintenanceDesc;
    }

    public double getMaintenanceCost() {
        return this.maintenanceCost;
    }

    public void setMaintenanceCost(double maintenanceCost) {
        this.maintenanceCost = maintenanceCost;
    }

    public String getMaintenanceStatus() {
        return this.maintenanceStatus;
    }

    public void setMaintenanceStatus(String maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }

    public int getHandledBy() {
        return this.handledBy;
    }

    public void setHandledBy(int handledBy) {
        this.handledBy = handledBy;
    }

    @Override
    public String toString() {
        return "VehicleMaintenance{" +
                "maintenanceId=" + maintenanceId +
                ", vehicleId='" + vehicleId + '\'' +
                ", maintenanceDate='" + maintenanceDate + '\'' +
                ", maintenanceDesc='" + maintenanceDesc + '\'' +
                ", maintenanceCost=" + maintenanceCost +
                ", maintenanceStatus='" + maintenanceStatus + '\'' +
                ", handledBy=" + handledBy +
                '}';
    }
    
}
