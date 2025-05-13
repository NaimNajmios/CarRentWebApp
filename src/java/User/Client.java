package User;

public class Client {
    
    private String userID;
    private String clientID;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private String profileImagePath;

    public Client() {
    }

    public Client(String userID, String clientID, String name, String address, String phoneNumber, String email) {
        this.userID = userID;
        this.clientID = clientID;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }


    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getClientID() {
        return this.clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfileImagePath() {
        return this.profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    @Override
    public String toString() {
        return "Client{" + "userID=" + userID + ", clientID=" + clientID + ", name=" + name + ", address=" + address + ", phoneNumber=" + phoneNumber + ", email=" + email + ", profileImagePath=" + profileImagePath + '}';
    }


}
