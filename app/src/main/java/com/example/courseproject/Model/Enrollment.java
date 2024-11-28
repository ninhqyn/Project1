package com.example.courseproject.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Enrollment implements Serializable {
    @SerializedName("enrollmentId")
    private int enrollmentId;

    @SerializedName("userId")
    private int userId;

    @SerializedName("courseId")
    private int courseId;

    @SerializedName("enrolledAt")
    private String enrolledAt;

    @SerializedName("status")
    private String status;

    @SerializedName("hasCertificate")
    private boolean hasCertificate;

    public Enrollment(int userId, int courseId,  String status, boolean hasCertificate) {
        this.userId = userId;
        this.courseId = courseId;
        //this.enrolledAt = enrolledAt;
        this.status = status;
        this.hasCertificate = hasCertificate;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(String enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isHasCertificate() {
        return hasCertificate;
    }

    public void setHasCertificate(boolean hasCertificate) {
        this.hasCertificate = hasCertificate;
    }
}
