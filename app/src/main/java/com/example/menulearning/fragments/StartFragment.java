package com.example.menulearning.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.menulearning.R;
import com.example.menulearning.activities.MainActivity;
import com.example.menulearning.constants.ValidationTypes;
import com.example.menulearning.entities.ValidationCase;
import com.example.menulearning.entities.ValidationResult;
import com.example.menulearning.managers.Validator;

import java.util.ArrayList;

public class StartFragment extends BaseFragment {

    public StartFragment(int fragmentViewId) {
        super(fragmentViewId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_start, container, false);

        EditText nameField = fragmentView.findViewById(R.id.nameField);
        nameField.setBackgroundTintList(ColorStateList
                .valueOf(getContext().getColor(R.color.primary)));
        TextView errorText = fragmentView.findViewById(R.id.errorText);

        ArrayList<ValidationCase> validationCases = new ArrayList<>();
        validationCases.add(new ValidationCase(
                ValidationTypes.EMPTY_STRING,
                (field) -> !(field.equals("") ||
                        field.split(ValidationTypes.EMPTY_STRING.getStringValue()).length == 0),
                getString(R.string.empty_string_error)
        ));
        validationCases.add(new ValidationCase(
                ValidationTypes.EXTRA_CHARACTERS,
                (field) -> field.matches(ValidationTypes.EXTRA_CHARACTERS.getStringValue()),
                getString(R.string.extra_characters_error)
        ));

        Validator validator = new Validator(validationCases);

        Button startButton = fragmentView.findViewById(R.id.startButton);
        startButton.setOnClickListener(view -> {
            ValidationResult result = validator.validate(nameField.getText().toString());
            int colorId = result.isResult()? R.color.primary : R.color.wrong_answer;

            nameField.setBackgroundTintList(ColorStateList
                    .valueOf(getContext().getColor(colorId)));
            errorText.setText(result.getMessage());

            if (result.isResult()) {
                ((MainActivity) getActivity()).updateView(R.id.startItem, true);
            }
        });

        return fragmentView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        ((EditText) getView().findViewById(R.id.nameField)).setText("");
    }
}
