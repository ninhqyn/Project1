package com.example.courseproject.Model;

public class FavoriteCourse {
    int id;
    int userId;
    int courseId;
    String createdAt;

    public FavoriteCourse(int id, int userId, int courseId, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
