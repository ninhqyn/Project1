package com.example.courseproject.Model;

public class UserUpdateRequest {
    int userId;
    String image;
    String fullname;
    String phoneNumber;
    String location;

    public UserUpdateRequest(int userId, String image, String fullname, String phoneNumber, String location) {
        this.userId = userId;
        this.image = image;
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



    public UserUpdateRequest(int userId, String image, String fullname) {
        this.userId = userId;
        this.image = image;
        this.fullname = fullname;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
