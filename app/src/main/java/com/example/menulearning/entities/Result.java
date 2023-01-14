package com.example.menulearning.entities;

public class Result extends BaseEntity<Integer> {
    private String name;
    private String result;
    private long date;

    public Result(int key, String name, String result, long date) {
        this.key = key;
        this.name = name;
        this.result = result;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getResult() {
        return result;
    }

    public long getDate() {
        return date;
    }
}
