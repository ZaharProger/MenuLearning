package com.example.menulearning.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.menulearning.constants.PrefsTypes;
import com.example.menulearning.entities.BaseEntity;
import com.example.menulearning.entities.PrefsItem;

public class PrefsManager<T> {
    private Context context;
    private String prefsName;

    public PrefsManager(Context context, String prefsName) {
        this.context = context;
        this.prefsName = prefsName;
    }

    public void putItem(String key, T value) {
        SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        if (value instanceof String) {
            prefsEditor.putString(key, value.toString());
        }
        else if (value instanceof Integer) {
            prefsEditor.putInt(key, (int) value);
        }
        else if (value instanceof Float) {
            prefsEditor.putFloat(key, (float) value);
        }
        else if (value instanceof Boolean) {
            prefsEditor.putBoolean(key, (boolean) value);
        }
        else if (value instanceof Long) {
            prefsEditor.putLong(key, (long) value);
        }

        prefsEditor.apply();
    }

    public PrefsItem<T> getItem(String key, PrefsTypes type) {
        SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);

        BaseEntity<String> prefsItem = null;
        switch (type) {
            case INT:
                prefsItem = new PrefsItem<>(key, prefs.getInt(key, 0));
                break;
            case LONG:
                prefsItem = new PrefsItem<>(key, prefs.getLong(key, 0L));
                break;
            case FLOAT:
                prefsItem = new PrefsItem<>(key, prefs.getFloat(key, 0.f));
                break;
            case STRING:
                prefsItem = new PrefsItem<>(key, prefs.getString(key, ""));
                break;
            case BOOLEAN:
                prefsItem = new PrefsItem<>(key, prefs.getBoolean(key, false));
                break;
        }

        return (PrefsItem<T>) ((PrefsItem<?>) prefsItem);
    }

    public boolean hasItem(String key) {
        SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);

        return prefs.contains(key);
    }

    public void removeItem(String key) {
        SharedPreferences prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();

        if (hasItem(key)) {
            prefsEditor.remove(key);
            prefsEditor.apply();
        }
    }
}
