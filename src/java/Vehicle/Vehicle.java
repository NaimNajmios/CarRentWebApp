package Vehicle;
import java.util.Objects;

// Class to represent a vehicle in the database
public class Vehicle {

    // Attributes
    private int vehicleId;
    private String vehicleModel;
    private String vehicleBrand;
    private int vehicleYear;
    private boolean vehicleAvailablity;
    private String vehicleCategory;
    private String vehicleFuelType;
    private String transmissionType;
    private int vehicleMileage;
    private String vehicleRatePerDay;
    private String vehicleRegistrationNo;

    // Empty constructor
    public Vehicle() {
    }

    // Constructor
    public Vehicle(int vehicleId, String vehicleModel, String vehicleBrand, int vehicleYear, boolean vehicleAvailablity, String vehicleCategory, String vehicleFuelType, String transmissionType, int vehicleMileage, String vehicleRatePerDay, String vehicleRegistrationNo) {
        this.vehicleId = vehicleId;
        this.vehicleModel = vehicleModel;
        this.vehicleBrand = vehicleBrand;
        this.vehicleYear = vehicleYear;
        this.vehicleAvailablity = vehicleAvailablity;
        this.vehicleCategory = vehicleCategory;
        this.vehicleFuelType = vehicleFuelType;
        this.transmissionType = transmissionType;
        this.vehicleMileage = vehicleMileage;
        this.vehicleRatePerDay = vehicleRatePerDay;
        this.vehicleRegistrationNo = vehicleRegistrationNo;
    }


    public int getVehicleId() {
        return this.vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getVehicleModel() {
        return this.vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleBrand() {
        return this.vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public int getVehicleYear() {
        return this.vehicleYear;
    }

    public void setVehicleYear(int vehicleYear) {
        this.vehicleYear = vehicleYear;
    }

    public boolean isVehicleAvailablity() {
        return this.vehicleAvailablity;
    }

    public boolean getVehicleAvailablity() {
        return this.vehicleAvailablity;
    }

    public void setVehicleAvailablity(boolean vehicleAvailablity) {
        this.vehicleAvailablity = vehicleAvailablity;
    }

    public String getVehicleCategory() {
        return this.vehicleCategory;
    }

    public void setVehicleCategory(String vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    public String getVehicleFuelType() {
        return this.vehicleFuelType;
    }

    public void setVehicleFuelType(String vehicleFuelType) {
        this.vehicleFuelType = vehicleFuelType;
    }

    public String getTransmissionType() {
        return this.transmissionType;
    }

    public void setTransmissionType(String transmissionType) {
        this.transmissionType = transmissionType;
    }

    public int getVehicleMileage() {
        return this.vehicleMileage;
    }

    public void setVehicleMileage(int vehicleMileage) {
        this.vehicleMileage = vehicleMileage;
    }

    public String getVehicleRatePerDay() {
        return this.vehicleRatePerDay;
    }

    public void setVehicleRatePerDay(String vehicleRatePerDay) {
        this.vehicleRatePerDay = vehicleRatePerDay;
    }

    public String getVehicleRegistrationNo() {
        return this.vehicleRegistrationNo;
    }

    public void setVehicleRegistrationNo(String vehicleRegistrationNo) {
        this.vehicleRegistrationNo = vehicleRegistrationNo;
    }
    
    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleId=" + vehicleId +
                ", vehicleModel='" + vehicleModel + '\'' +
                ", vehicleBrand='" + vehicleBrand + '\'' +
                ", vehicleYear=" + vehicleYear +
                ", vehicleAvailablity=" + vehicleAvailablity +
                ", vehicleCategory='" + vehicleCategory + '\'' +
                ", vehicleFuelType='" + vehicleFuelType + '\'' +
                ", transmissionType='" + transmissionType + '\'' +
                ", vehicleMileage=" + vehicleMileage +
                ", vehicleRatePerDay='" + vehicleRatePerDay + '\'' +
                ", vehicleRegistrationNo='" + vehicleRegistrationNo + '\'' +
                '}';
    }
    
}
