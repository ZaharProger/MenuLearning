package com.example.menulearning.entities;

import java.util.ArrayList;

public class Question extends BaseEntity<Integer> {
    private String text;
    private int correctAnswerId;
    private ArrayList<Answer> answers;

    public Question(int key, String text, int correctAnswerId, ArrayList<Answer> answers) {
        this.key = key;
        this.text = text;
        this.correctAnswerId = correctAnswerId;
        this.answers = answers;
    }

    public String getText() {
        return text;
    }

    public int getCorrectAnswerId() {
        return correctAnswerId;
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }
}
