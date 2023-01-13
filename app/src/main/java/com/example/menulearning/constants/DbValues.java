package com.example.menulearning.constants;

public enum DbValues implements EnumDecoder {
    DB_NAME("menu_learning.db"),
    DB_SCHEMA("1"),
    CREATE_QUESTIONS_TABLE("CREATE TABLE IF NOT EXISTS questions (" +
            "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "text VARCHAR(100) NOT NULL);"),
    CREATE_ANSWERS_TABLE("CREATE TABLE IF NOT EXISTS answers(" +
            "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "text VARCHAR(50) NOT NULL," +
            "question_id INTEGER," +
            "FOREIGN KEY (question_id) REFERENCES questions(id));"),
    CREATE_CORRECT_ANSWERS_TABLE("CREATE TABLE IF NOT EXISTS correct_answers(" +
            "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "question_id INTEGER," +
            "answer_id INTEGER," +
            "FOREIGN KEY (question_id) REFERENCES questions(id)," +
            "FOREIGN KEY (answer_id) REFERENCES answers(id));"),
    DROP_QUESTIONS_TABLE("DROP TABLE IF EXISTS questions;"),
    DROP_ANSWERS_TABLE("DROP TABLE IF EXISTS answers;"),
    DROP_CORRECT_ANSWERS_TABLE("DROP TABLE IF EXISTS correct_answers;");

    private String stringValue;

    DbValues(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }
}
