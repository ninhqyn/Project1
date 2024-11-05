package com.example.courseproject.Model;

public class Instructor {

    private int instructorId;
    private String name;
    private String email;
    private String phone;
    private String major;
    private String university;
    private String image;


    public Instructor(int instructorId, String name, String email, String phone, String major, String university,String image) {
        this.instructorId = instructorId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.major = major;
        this.university = university;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }
}
