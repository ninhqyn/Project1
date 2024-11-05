package com.example.courseproject.Model;

public class Notification {
   private int id;
   private int userId;
   private int courseId;
   private String title;
   private String message;
   private String createdAt;
   private boolean isRead;

    public Notification(int id, int userId, int courseId, String title, String message, String createdAt, boolean isRead) {
        this.id = id;
        this.userId = userId;
        this.courseId = courseId;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public int getCourseId() {
        return courseId;
    }

    public String getMessage() {
        return message;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public boolean isRead() {
        return isRead;
    }
}
