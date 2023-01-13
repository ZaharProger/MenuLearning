package com.example.menulearning.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.menulearning.constants.DbValues;
import com.example.menulearning.entities.Answer;
import com.example.menulearning.entities.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;


public class DbManager extends SQLiteOpenHelper {
    private static Random random = new Random();
    private static DbManager dbManager;
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;

    private DbManager(Context context) {
        super(context, DbValues.DB_NAME.getStringValue(), null,
                Integer.parseInt(DbValues.DB_SCHEMA.getStringValue()));

        questions = new ArrayList<>();
        answers = new ArrayList<>();
    }

    public static synchronized DbManager getInstance(Context context) {
        if (dbManager == null) {
            dbManager = new DbManager(context);
        }

        return dbManager;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        List<DbValues> requests = Arrays.asList(
                DbValues.CREATE_QUESTIONS_TABLE,
                DbValues.CREATE_ANSWERS_TABLE,
                DbValues.CREATE_CORRECT_ANSWERS_TABLE,
                DbValues.INSERT_QUESTIONS,
                DbValues.INSERT_ANSWERS,
                DbValues.INSERT_CORRECT_ANSWERS,
                DbValues.GET_ANSWERS,
                DbValues.GET_QUESTIONS,
                DbValues.GET_CORRECT_ANSWER_BY_QUESTION
        );

        for (DbValues request : requests) {
            if (request == DbValues.GET_QUESTIONS || request == DbValues.GET_ANSWERS ||
            request == DbValues.GET_CORRECT_ANSWER_BY_QUESTION) {
                fillRepositories(sqLiteDatabase, request);
            }
            else {
                sqLiteDatabase.execSQL(request.getStringValue());
            }
        }
    }

    private void fillRepositories(SQLiteDatabase database, DbValues requestType) {
        HashMap<Integer, Integer> correctAnswersIds = new HashMap<>();

        try {
            Cursor cursor = database.rawQuery(requestType.getStringValue(), null);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int idColumnIndex = cursor
                                .getColumnIndex(DbValues.ID_KEY.getStringValue());
                        int textColumnIndex = cursor
                                .getColumnIndex(DbValues.TEXT_KEY.getStringValue());
                        int questionIdColumnIndex = cursor
                                .getColumnIndex(DbValues.QUESTION_ID_KEY.getStringValue());
                        int answerIdColumnIndex = cursor
                                .getColumnIndex(DbValues.ANSWER_ID_KEY.getStringValue());

                        switch (requestType) {
                            case GET_QUESTIONS:
                                int questionId = cursor.getInt(idColumnIndex);
                                ArrayList<Answer> questionAnswers = (ArrayList<Answer>) answers
                                        .stream()
                                        .filter(answer -> answer.getQuestionId() == questionId)
                                        .collect(Collectors.toList());

                                questions.add(new Question(
                                        questionId,
                                        cursor.getString(textColumnIndex),
                                        0,
                                        questionAnswers
                                ));
                                break;
                            case GET_ANSWERS:
                                answers.add(new Answer(
                                        cursor.getInt(idColumnIndex),
                                        cursor.getString(textColumnIndex),
                                        cursor.getInt(questionIdColumnIndex)
                                ));
                                break;
                            case GET_CORRECT_ANSWER_BY_QUESTION:
                                int answerId = cursor.getInt(answerIdColumnIndex);
                                questionId = cursor.getInt(questionIdColumnIndex);

                                correctAnswersIds.put(questionId, answerId);
                                break;
                        }
                    } while (cursor.moveToNext());
                }

                cursor.close();
            }
        }
        catch (SQLException ignored) {

        }

        if (requestType == DbValues.GET_CORRECT_ANSWER_BY_QUESTION) {
            questions = (ArrayList<Question>) questions
                    .stream()
                    .map(question -> new Question(
                        question.getKey(),
                        question.getText(),
                        correctAnswersIds.get(question.getKey()),
                        question.getAnswers()
                    ))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion == oldVersion + 1) {
            oldVersion = newVersion;
            List<DbValues> deleteRequests = Arrays.asList(
                    DbValues.DROP_QUESTIONS_TABLE,
                    DbValues.DROP_ANSWERS_TABLE,
                    DbValues.DROP_CORRECT_ANSWERS_TABLE
            );
            for (DbValues deleteRequest : deleteRequests) {
                sqLiteDatabase.execSQL(deleteRequest.getStringValue());
            }

            onCreate(sqLiteDatabase);
        }
    }


    public ArrayList<Question> getQuestionsWithAnswers(int amount) {
        ArrayList<Question> chosenQuestions = new ArrayList<>();

        int i = 0;
        while (i < amount) {
            Question question = questions.get(random.nextInt(questions.size()));
            if (questions.stream().noneMatch(q -> Objects.equals(q.getKey(), question.getKey()))) {
                i += 1;
            }
        }

        return chosenQuestions;
    }
}
