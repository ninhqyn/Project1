package com.example.courseproject.Model;

public class Answer {
    int answerId;
    int questionId;
    String content;
    boolean isCorrect;

    public Answer(int answerId, int questionId, String content, boolean isCorrect) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.content = content;
        this.isCorrect = isCorrect;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }
}
