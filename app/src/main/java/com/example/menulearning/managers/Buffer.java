package com.example.menulearning.managers;

public class Buffer {
    private static TestManager testManager = null;

    public static TestManager getTestManager() {
        return testManager;
    }

    public static void setTestManager(TestManager testManager) {
        Buffer.testManager = testManager;
    }
}
