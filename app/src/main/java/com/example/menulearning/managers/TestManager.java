package com.example.menulearning.managers;

import com.example.menulearning.entities.Question;
import com.example.menulearning.entities.QuestionView;

import java.util.ArrayList;

public class TestManager {
    private int correctAnswers;
    private int currentQuestion;
    private ArrayList<Question> questions;

    public TestManager(ArrayList<Question> questions) {
        this.questions = questions;
        correctAnswers = 0;
        currentQuestion = 0;
    }

    public QuestionView getQuestionView() {
        Question question = null;
        if (currentQuestion < questions.size()) {
            question = questions.get(currentQuestion);
            currentQuestion += 1;
        }

        return question != null? new QuestionView(currentQuestion, question) : null;
    }

    public boolean answerQuestion(int answerId) {
        boolean isCorrect = questions.get(currentQuestion).getCorrectAnswerId() == answerId;
        correctAnswers += isCorrect? 1 : 0;

        return isCorrect;
    }

    public String makeStatistics() {
        return String.format("%d/%d", correctAnswers, questions.size());
    }
}
