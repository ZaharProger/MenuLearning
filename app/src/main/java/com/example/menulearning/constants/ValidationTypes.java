package com.example.menulearning.constants;

public enum ValidationTypes implements EnumDecoder {
    EXTRA_CHARACTERS("[A-Za-zА-Яа-я-\\s]+"),
    EMPTY_STRING("[\\s-]+");

    private String stringValue;

    ValidationTypes(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String getStringValue() {
        return stringValue;
    }
}
