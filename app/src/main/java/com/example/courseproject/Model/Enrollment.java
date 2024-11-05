package com.example.courseproject.Model;

public class Enrollment {
    private int enrollmentId;
    private int userId;
    private int courseId;
    private String enrolledAt;
    private String status;
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
