package com.example.menulearning.entities;

import com.example.menulearning.constants.ValidationTypes;

import java.util.function.Predicate;

public class ValidationCase extends BaseEntity<ValidationTypes> {
    private Predicate<String> validationFunc;
    private String messageIfFails;

    public ValidationCase(ValidationTypes type, Predicate<String> validationFunc, String message) {
        key = type;
        this.validationFunc = validationFunc;
        messageIfFails = message;
    }

    public Predicate<String> getValidationFunc() {
        return validationFunc;
    }

    public String getMessageIfFails() {
        return messageIfFails;
    }
}
