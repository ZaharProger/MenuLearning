package com.example.menulearning.fragments;

import android.content.SharedPreferences;
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
import com.example.menulearning.constants.PrefsValues;
import com.example.menulearning.constants.Routes;
import com.example.menulearning.constants.ValidationTypes;
import com.example.menulearning.entities.BaseEntity;
import com.example.menulearning.entities.ValidationCase;
import com.example.menulearning.entities.ValidationResult;
import com.example.menulearning.managers.Validator;

import java.util.ArrayList;

public class StartFragment extends BaseFragment {

    public StartFragment(Routes fragmentViewId) {
        this.fragmentViewId = fragmentViewId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_start, container, false);

        EditText nameField = fragmentView.findViewById(R.id.nameField);
        nameField.setBackgroundTintList(ColorStateList
                .valueOf(getContext().getColor(R.color.primary)));
        TextView errorText = fragmentView.findViewById(R.id.errorText);

        ArrayList<ValidationCase> validationCases = new ArrayList<>();
        validationCases.add(new ValidationCase(
                ValidationTypes.VALID_CHARACTERS,
                (field) -> field.matches(ValidationTypes.VALID_CHARACTERS.getStringValue()),
                getString(R.string.extra_characters_error)
        ));
        validationCases.add(new ValidationCase(
                ValidationTypes.EMPTY_STRING,
                (field) -> !(field.matches(ValidationTypes.EMPTY_STRING.getStringValue())),
                getString(R.string.empty_string_error)
        ));

        Validator validator = new Validator(validationCases);

        Button startButton = fragmentView.findViewById(R.id.startButton);
        startButton.setOnClickListener(view -> {
            String name = nameField.getText().toString();
            ValidationResult result = validator.validate(name);
            int colorId = result.isResult()? R.color.primary : R.color.bad_result;

            nameField.setBackgroundTintList(ColorStateList
                    .valueOf(getContext().getColor(colorId)));
            errorText.setText(result.getMessage());

            if (result.isResult()) {
                MainActivity parentActivity = (MainActivity) getActivity();

                SharedPreferences prefs = parentActivity.getPrefs();
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putBoolean(PrefsValues.TEST_FLAG_KEY.getStringValue(), true);
                prefsEditor.putString(PrefsValues.USER_NAME.getStringValue(), name.trim());
                prefsEditor.apply();

                parentActivity.getRouter().route(Routes.TEST);
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
