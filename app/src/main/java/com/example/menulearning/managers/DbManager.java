package com.example.menulearning.managers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import com.example.menulearning.constants.DbValues;
import com.example.menulearning.entities.Answer;
import com.example.menulearning.entities.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DbManager extends SQLiteOpenHelper {
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
        List<DbValues> createRequests = Arrays.asList(
                DbValues.CREATE_QUESTIONS_TABLE,
                DbValues.CREATE_ANSWERS_TABLE,
                DbValues.CREATE_CORRECT_ANSWERS_TABLE
        );

        for (DbValues createRequest : createRequests) {
            sqLiteDatabase.execSQL(createRequest.getStringValue());
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
        if (questions.size() != 0) {
            questions.clear();
        }
        //TODO: Допилить метод получения списка вопросов с ответами
        try {
            SQLiteDatabase database = getReadableDatabase();
            SQLiteQueryBuilder builder = new SQLiteQueryBuilder();

        }
        catch (SQLException ignored) {

        }

        return questions;
    }
}
