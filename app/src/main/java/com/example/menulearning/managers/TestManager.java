package com.example.menulearning.managers;

import com.example.menulearning.entities.Question;
import com.example.menulearning.entities.QuestionView;

import java.util.ArrayList;

public class TestManager {
    private static boolean isBuffered = false;
    private static TestManager testManager;
    private int correctAnswers;
    private int currentQuestion;
    private ArrayList<Question> questions;

    public static synchronized TestManager getInstance(ArrayList<Question> questions) {
        if (testManager == null) {
            testManager = new TestManager(questions);
        }
        else if (questions != null){
            testManager.questions = questions;
        }

        return testManager;
    }

    private TestManager(ArrayList<Question> questions) {
        this.questions = questions;
        correctAnswers = 0;
        currentQuestion = -1;
    }

    public static boolean isBuffered() {
        return isBuffered;
    }

    public void clearBuffer() {
        isBuffered = false;
        testManager.currentQuestion = -1;
        testManager.correctAnswers = 0;
    }

    public void setCurrentQuestion(int currentQuestion) {
        if (this.currentQuestion == -1) {
            clearBuffer();
        }
        else {
            isBuffered = true;
            testManager.currentQuestion = currentQuestion;
        }
    }

    public QuestionView getQuestionView() {
        Question question = null;
        if (currentQuestion < questions.size() - 1) {
            currentQuestion += 1;
            question = questions.get(currentQuestion);
        }
        else {
            currentQuestion = -1;
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

    public int getPercentage() {
        return correctAnswers * 100 / questions.size();
    }
}
