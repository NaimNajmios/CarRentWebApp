package User;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Naim Najmi
 */
public class User {
    
    private String userID;
    private String username;
    private String role;

    public User() {
    }

    public User(String userID, String username, String role) {
        this.userID = userID;
        this.username = username;
        this.role = role;
    }


    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" + "userID=" + userID + ", username=" + username + ", role=" + role + '}';
    }
    

}
