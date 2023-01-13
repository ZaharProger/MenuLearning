package com.example.menulearning.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.menulearning.R;
import com.example.menulearning.activities.MainActivity;
import com.example.menulearning.constants.PrefsTypes;
import com.example.menulearning.constants.PrefsValues;
import com.example.menulearning.entities.BaseEntity;
import com.example.menulearning.managers.PrefsManager;

public class AboutFragment extends BaseFragment {

    public AboutFragment(int fragmentViewId) {
        this.fragmentViewId = fragmentViewId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_about, container, false);

        MainActivity parentActivity = (MainActivity) getActivity();
        PrefsManager<String> stringPrefsManager = parentActivity.getStringPrefsManager();

        String prefsKey = PrefsValues.INTRO_TEXT_KEY.getStringValue();
        String textFromPrefs = stringPrefsManager.getItem(prefsKey, PrefsTypes.STRING).getValue();
        ((TextView) fragmentView.findViewById(R.id.introText))
                .setText((CharSequence) textFromPrefs);

        prefsKey = PrefsValues.RULES_TEXT_KEY.getStringValue();
        textFromPrefs = stringPrefsManager.getItem(prefsKey, PrefsTypes.STRING).getValue();
        ((TextView) fragmentView.findViewById(R.id.rulesText))
                .setText((CharSequence) textFromPrefs);

        return fragmentView;
    }

    @Override
    public void updateView(BaseEntity<Integer> relatedEntity, View relatedView) {

    }
}
