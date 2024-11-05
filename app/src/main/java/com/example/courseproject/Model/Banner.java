package com.example.courseproject.Model;

public class Banner {
    private int bannerId;
    private String title;
    private String description;
    private String image;
    private String buttonUrl;

    public Banner(int bannerId, String title, String description, String image, String buttonUrl) {
        this.bannerId = bannerId;
        this.title = title;
        this.description = description;
        this.image = image;
        this.buttonUrl = buttonUrl;
    }

    public Banner() {
    }

    public int getBannerId() {
        return bannerId;
    }

    public void setBannerId(int bannerId) {
        this.bannerId = bannerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(String buttonUrl) {
        this.buttonUrl = buttonUrl;
    }
}
