package com.example.courseproject.Model;

public class Quiz {
    private int quizId;
    private int courseId;
    private String title;
    private int time;
    private String createdAt;

    public Quiz(int quizId, int courseId, String title, int time, String createdAt) {
        this.quizId = quizId;
        this.courseId = courseId;
        this.title = title;
        this.time = time;
        this.createdAt = createdAt;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}