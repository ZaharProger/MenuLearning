package com.example.menulearning.managers;

import androidx.appcompat.app.AppCompatActivity;

import com.example.menulearning.R;
import com.example.menulearning.fragments.BaseFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Router {
    private static Router router;
    private int root;
    private HashMap<Integer, BaseFragment> routeTable;
    private AppCompatActivity activityRef;

    private Router(AppCompatActivity activityRef, int root, List<BaseFragment> fragments) {
        this.activityRef = activityRef;
        this.root = root;
        routeTable = new HashMap<>();

        fragments.forEach(fragment -> {
            int fragmentViewId = fragment.getFragmentViewId();
            if (!routeTable.containsKey(fragmentViewId)) {
                routeTable.put(fragmentViewId, fragment);
            }
        });
    }

    public static synchronized Router getInstance(AppCompatActivity activityRef, int root,
                                           List<BaseFragment> fragments) {

        if (router == null) {
            router = new Router(activityRef, root, fragments);
        }

        return router;
    }

    public boolean route(int viewId) {
        boolean hasKey = routeTable.containsKey(viewId);

        if (hasKey) {
            try {
                activityRef.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(root, Objects.requireNonNull(routeTable.get(viewId)))
                        .commitNow();
            }
            catch (NullPointerException e) {
                hasKey = false;
            }
        }

        return hasKey;
    }
}
