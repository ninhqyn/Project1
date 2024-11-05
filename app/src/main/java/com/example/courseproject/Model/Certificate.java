package com.example.courseproject.Model;

import java.io.Serializable;

public class Certificate implements Serializable {
    private int certificateId;
    private int enrollmentId;
    private int templateId;
    private String issuedAt;

    public Certificate(int certificateId, int enrollmentId, int templateId, String issuedAt) {
        this.certificateId = certificateId;
        this.enrollmentId = enrollmentId;
        this.templateId = templateId;
        this.issuedAt = issuedAt;
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

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }
}
