package com.example.menulearning.constants;

public enum DbValues implements EnumDecoder {
    DB_NAME("menu_learning.db"),
    DB_SCHEMA("1");

    private String stringValue;

    DbValues(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }
}
