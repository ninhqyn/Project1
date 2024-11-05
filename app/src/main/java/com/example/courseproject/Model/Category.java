package com.example.courseproject.Model;

import java.io.Serializable;

public class Category implements Serializable {
    private  int categoryId;
    private  String name;
    private String image;
    private String description;

    public Category(int categoryId, String name,String image, String description) {
        this.categoryId = categoryId;
        this.name = name;
        this.image = image;
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
