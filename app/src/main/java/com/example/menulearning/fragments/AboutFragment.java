package com.example.menulearning.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.menulearning.R;
import com.example.menulearning.constants.PrefsTypes;
import com.example.menulearning.constants.PrefsValues;
import com.example.menulearning.managers.PrefsManager;

public class AboutFragment extends BaseFragment {
    private PrefsManager<String> prefsManager;

    public AboutFragment(int fragmentViewId, PrefsManager<String> prefsManager) {
        super(fragmentViewId);
        this.prefsManager = prefsManager;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_about, container, false);

        String prefsKey = PrefsValues.INTRO_TEXT_KEY.getStringValue();
        String textFromPrefs = prefsManager.getItem(prefsKey, PrefsTypes.STRING).getValue();
        ((TextView) fragmentView.findViewById(R.id.introText))
                .setText((CharSequence) textFromPrefs);

        prefsKey = PrefsValues.RULES_TEXT_KEY.getStringValue();
        textFromPrefs = prefsManager.getItem(prefsKey, PrefsTypes.STRING).getValue();
        ((TextView) fragmentView.findViewById(R.id.rulesText))
                .setText((CharSequence) textFromPrefs);

        return fragmentView;
    }
}
