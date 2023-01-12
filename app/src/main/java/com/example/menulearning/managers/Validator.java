package com.example.menulearning.managers;

import com.example.menulearning.constants.ValidationTypes;
import com.example.menulearning.entities.ValidationCase;
import com.example.menulearning.entities.ValidationResult;

import java.util.HashMap;
import java.util.List;

public class Validator {
    private HashMap<ValidationTypes, ValidationCase> validationTable;
    
    public Validator(List<ValidationCase> cases) {
        validationTable = new HashMap<>();

        cases.forEach(validationCase -> {
            ValidationTypes key = validationCase.getKey();
            if (!validationTable.containsKey(key)) {
                validationTable.put(key, validationCase);
            }
        });
    }

    public ValidationResult validate(String field) {
        ValidationCase failedCase = null;
        for (ValidationTypes type : validationTable.keySet()) {
            ValidationCase validationCase = validationTable.get(type);
            if (!validationCase.getValidationFunc().test(field)) {
                failedCase = validationCase;
                break;
            }
        }

        return new ValidationResult(failedCase);
    }
}
