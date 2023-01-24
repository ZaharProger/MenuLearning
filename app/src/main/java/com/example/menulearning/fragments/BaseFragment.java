package com.example.menulearning.fragments;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.menulearning.constants.Routes;
import com.example.menulearning.entities.BaseEntity;

public class BaseFragment extends Fragment {
    protected Routes fragmentViewId;
    protected View fragmentView;

    public Routes getFragmentViewId() {
        return fragmentViewId;
    }
}
