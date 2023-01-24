package com.example.menulearning.fragments;

import android.view.View;

import com.example.menulearning.entities.BaseEntity;

public interface IUpdatable {
    void updateView(BaseEntity<Integer> relatedEntity, View relatedView);
}
