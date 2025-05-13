package User;

public class Admin {
    
    private String userID;
    private String adminID;
    private String name;
    private String email;
    private String profileImagePath;

    public Admin() {
    }


    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getAdminID() {
        return this.adminID;
    }

    public void setAdminID(String adminID) {
        this.adminID = adminID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Admin{" + "userID=" + userID + ", adminID=" + adminID + ", name=" + name + ", email=" + email + ", profileImagePath=" + profileImagePath + '}';
    }


}
