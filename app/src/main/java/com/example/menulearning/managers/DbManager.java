package com.example.menulearning.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.menulearning.constants.DbValues;
import com.example.menulearning.constants.PrefsValues;
import com.example.menulearning.entities.Answer;
import com.example.menulearning.entities.Question;
import com.example.menulearning.entities.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;


public class DbManager extends SQLiteOpenHelper {
    private Random random;
    private static DbManager dbManager;
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;
    private ArrayList<Result> results;
    private PrefsManager<Boolean> booleanPrefsManager;

    private DbManager(Context context) {
        super(context, DbValues.DB_NAME.getStringValue(), null,
                Integer.parseInt(DbValues.DB_SCHEMA.getStringValue()));

        questions = new ArrayList<>();
        answers = new ArrayList<>();
        results = new ArrayList<>();
        random = new Random();

        booleanPrefsManager = new PrefsManager<>(
                context,
                PrefsValues.PREFS_NAME.getStringValue()
        );
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
                DbValues.CREATE_RESULTS_TABLE
        );

        if (!booleanPrefsManager.hasItem(PrefsValues.DATA_INSERTED_FLAG_KEY.getStringValue())) {
            requests.add(DbValues.INSERT_QUESTIONS);
            requests.add(DbValues.INSERT_ANSWERS);
            requests.add(DbValues.INSERT_CORRECT_ANSWERS);
        }

        requests.add(DbValues.GET_ANSWERS);
        requests.add(DbValues.GET_QUESTIONS);
        requests.add(DbValues.GET_CORRECT_ANSWERS);
        

        for (DbValues request : requests) {
            if (request == DbValues.GET_QUESTIONS || request == DbValues.GET_ANSWERS ||
            request == DbValues.GET_CORRECT_ANSWERS) {
                fillRepositories(sqLiteDatabase, request);
            }
            else {
                sqLiteDatabase.execSQL(request.getStringValue());
            }
        }

        if (!booleanPrefsManager.hasItem(PrefsValues.DATA_INSERTED_FLAG_KEY.getStringValue())) {
            booleanPrefsManager.putItem(PrefsValues.DATA_INSERTED_FLAG_KEY.getStringValue(), true);
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
                            case GET_CORRECT_ANSWERS:
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

        if (requestType == DbValues.GET_CORRECT_ANSWERS) {
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

    public void addResult(Result result) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(DbValues.INSERT_RESULT.getStringValue(), new Object[]{
                result.getName(),
                result.getResult(),
                result.getDate()
        });
    }

    public ArrayList<Result> getResultsByName(String name) {
        if (results.size() != 0) {
            results.clear();
        }

        try {
            SQLiteDatabase database = getReadableDatabase();
            Cursor cursor = database.rawQuery(DbValues.GET_RESULTS_BY_NAME.getStringValue(),
                    new String[]{
                            name
                    });

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int idColumnIndex = cursor
                                .getColumnIndex(DbValues.ID_KEY.getStringValue());
                        int nameColumnIndex = cursor
                                .getColumnIndex(DbValues.NAME_KEY.getStringValue());
                        int resultColumnIndex = cursor
                                .getColumnIndex(DbValues.RESULT_KEY.getStringValue());
                        int dateColumnIndex = cursor
                                .getColumnIndex(DbValues.DATE_KEY.getStringValue());

                        results.add(new Result(
                                cursor.getInt(idColumnIndex),
                                cursor.getString(nameColumnIndex),
                                cursor.getString(resultColumnIndex),
                                cursor.getLong(dateColumnIndex)
                        ));

                    } while (cursor.moveToNext());
                }

                cursor.close();
            }
        }
        catch (SQLException ignored) {

        }

        return results;
    }
}
