package com.example.courseproject.Model;

import java.io.Serializable;

public class Certificate implements Serializable {
    private int certificateId;
    private int enrollmentId;
    private double percentage;
    private String issuedAt;

    public Certificate(int certificateId, int enrollmentId, double percentage, String issuedAt) {
        this.certificateId = certificateId;
        this.enrollmentId = enrollmentId;
        this.percentage = percentage;
        this.issuedAt = issuedAt;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }


    public int getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(int certificateId) {
        this.certificateId = certificateId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }
}
