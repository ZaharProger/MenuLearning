package com.example.menulearning.managers;

import androidx.appcompat.app.AppCompatActivity;

import com.example.menulearning.fragments.BaseFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ViewRenderer {
    private int root;
    private HashMap<Integer, BaseFragment> renderTable;
    private AppCompatActivity activityRef;

    public ViewRenderer(AppCompatActivity activityRef, int root, List<BaseFragment> fragments) {
        this.activityRef = activityRef;
        this.root = root;
        renderTable = new HashMap<>();

        fragments.forEach(fragment -> {
            int fragmentViewId = fragment.getFragmentViewId();
            if (!renderTable.containsKey(fragmentViewId)) {
                renderTable.put(fragmentViewId, fragment);
            }
        });
    }

    public boolean render(int viewId) {
        boolean hasKey = renderTable.containsKey(viewId);

        if (hasKey) {
            try {
                activityRef.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(root, Objects.requireNonNull(renderTable.get(viewId)))
                        .commitNow();
            }
            catch (NullPointerException e) {
                hasKey = false;
            }
        }

        return hasKey;
    }
}
