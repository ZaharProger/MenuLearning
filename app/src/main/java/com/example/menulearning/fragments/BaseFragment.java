package com.example.menulearning.fragments;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.example.menulearning.entities.BaseEntity;

public abstract class BaseFragment extends Fragment {
    protected int fragmentViewId;
    protected View fragmentView;

    public int getFragmentViewId() {
        return fragmentViewId;
    }

    public abstract void updateView(BaseEntity<Integer> relatedEntity, View relatedView);
}
