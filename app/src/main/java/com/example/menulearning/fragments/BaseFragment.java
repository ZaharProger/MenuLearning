package com.example.menulearning.fragments;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {
    protected int fragmentViewId;

    public BaseFragment(int fragmentViewId) {
        this.fragmentViewId = fragmentViewId;
    }

    public int getFragmentViewId() {
        return fragmentViewId;
    }
}
