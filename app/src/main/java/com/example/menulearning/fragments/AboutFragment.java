package com.example.menulearning.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.menulearning.R;
import com.example.menulearning.activities.MainActivity;
import com.example.menulearning.constants.PrefsValues;
import com.example.menulearning.constants.Routes;
import com.example.menulearning.entities.BaseEntity;

public class AboutFragment extends BaseFragment {

    public AboutFragment(Routes fragmentViewId) {
        this.fragmentViewId = fragmentViewId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_about, container, false);

        SharedPreferences prefs = ((MainActivity) getActivity()).getPrefs();

        String prefsKey = PrefsValues.INTRO_TEXT_KEY.getStringValue();
        ((TextView) fragmentView.findViewById(R.id.introText))
                .setText(prefs.getString(prefsKey, ""));

        prefsKey = PrefsValues.RULES_TEXT_KEY.getStringValue();
        ((TextView) fragmentView.findViewById(R.id.rulesText))
                .setText(prefs.getString(prefsKey, ""));

        return fragmentView;
    }

    @Override
    public void updateView(BaseEntity<Integer> relatedEntity, View relatedView) {

    }
}
