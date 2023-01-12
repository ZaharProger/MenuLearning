package com.example.menulearning.entities;

public class PrefsItem<T> extends BaseEntity<String> {
    private T value;

    public PrefsItem(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
