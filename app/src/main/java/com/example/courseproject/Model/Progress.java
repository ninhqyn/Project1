package com.example.courseproject.Model;

public class Progress {
    int progressId;
    int enrollmentId;
    int lessonId;
    boolean completed;
    int progress1;
    String updateAt;

    public Progress(int progressId, int enrollmentId, int lessonId, boolean completed, int progress1, String updateAt) {
        this.progressId = progressId;
        this.enrollmentId = enrollmentId;
        this.lessonId = lessonId;
        this.completed = completed;
        this.progress1 = progress1;
        this.updateAt = updateAt;
    }

    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getLessonId() {
        return lessonId;
    }

    public void setLessonId(int lessonId) {
        this.lessonId = lessonId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public int getProgress1() {
        return progress1;
    }

    public void setProgress1(int progress1) {
        this.progress1 = progress1;
    }
}
