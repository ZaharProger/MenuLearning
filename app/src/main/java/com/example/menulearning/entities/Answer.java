package com.example.menulearning.entities;

public class Answer extends BaseEntity<Integer> {
    private String text;
    private int questionId;

    public Answer(int key, String text, int questionId) {
        this.key = key;
        this.text = text;
        this.questionId = questionId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getText() {
        return text;
    }
}
