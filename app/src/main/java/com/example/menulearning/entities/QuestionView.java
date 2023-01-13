package com.example.menulearning.entities;

public class QuestionView extends BaseEntity<Integer> {
    private int questionNumber;
    private Question question;

    public QuestionView(int questionNumber, Question question) {
        this.question = question;
        this.questionNumber = questionNumber;
    }

    public Question getQuestion() {
        return question;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }
}
