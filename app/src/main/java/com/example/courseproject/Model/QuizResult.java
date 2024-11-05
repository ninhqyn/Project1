package com.example.courseproject.Model;

public class QuizResult {
    int quizResultId;
    int quizId;
    int enrollmentId;
    int score;
    String createdAt;

    public QuizResult(int quizResultId, int quizId, int enrollmentId, int score, String createdAt) {
        this.quizResultId = quizResultId;
        this.quizId = quizId;
        this.enrollmentId = enrollmentId;
        this.score = score;
        this.createdAt = createdAt;
    }

    public int getQuizResultId() {
        return quizResultId;
    }

    public void setQuizResultId(int quizResultId) {
        this.quizResultId = quizResultId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
