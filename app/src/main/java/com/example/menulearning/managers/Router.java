package com.example.menulearning.managers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.menulearning.constants.Routes;
import com.example.menulearning.fragments.BaseFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Router {
    private static Router router;
    private int root;
    private HashMap<Routes, BaseFragment> routeTable;
    private AppCompatActivity activityRef;

    private Router(AppCompatActivity activityRef, int root, List<BaseFragment> fragments) {
        this.activityRef = activityRef;
        this.root = root;
        routeTable = new HashMap<>();

        fragments.forEach(fragment -> {
            Routes fragmentViewId = fragment.getFragmentViewId();
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

    public boolean route(Routes route) {
        boolean hasKey = routeTable.containsKey(route);

        if (hasKey) {
            activityRef.getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(root, Objects.requireNonNull(routeTable.get(route)))
                    .commitNow();
        }

        return hasKey;
    }
}
