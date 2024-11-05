package com.example.courseproject.Model;

public class Question {
    int questionId;
    String content;
    int quizId;
    double points;

    public Question(int questionId, String content, int quizId, double points) {
        this.questionId = questionId;
        this.content = content;
        this.quizId = quizId;
        this.points = points;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getContent() {
        return content;
    }

    public int getQuizId() {
        return quizId;
    }

    public double getPoints() {
        return points;
    }
}
