package com.example.menulearning.entities;

public class BaseEntity<T> {
    protected T key;

    public void setKey(T key) {
        this.key = key;
    }

    public T getKey() {
        return key;
    }
}
