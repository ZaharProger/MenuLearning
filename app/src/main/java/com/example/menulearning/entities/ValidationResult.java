package com.example.menulearning.entities;

import com.example.menulearning.constants.ValidationTypes;

public class ValidationResult extends BaseEntity<ValidationTypes> {
    private boolean result;
    private String message;

    public ValidationResult(ValidationTypes type, boolean result, String message) {
        key = type;
        this.result = result;
        this.message = message;
    }

    public ValidationResult(ValidationCase validationCase) {
        result = validationCase == null;
        key = !result? validationCase.getKey() : null;
        message = !result? validationCase.getMessageIfFails() : "";
    }

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
