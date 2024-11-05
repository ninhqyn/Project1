package com.example.courseproject.Model;

import java.io.Serializable;

public class CertificateTemplate implements Serializable {
    private int templateId;
    private String templateName;
    private String templateContent;
    private String image;

    public CertificateTemplate(int templateId, String templateName, String templateContent, String image) {
        this.templateId = templateId;
        this.templateName = templateName;
        this.templateContent = templateContent;
        this.image = image;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateContent() {
        return templateContent;
    }

    public void setTemplateContent(String templateContent) {
        this.templateContent = templateContent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
