package com.example.menulearning.activities;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.menulearning.R;
import com.example.menulearning.constants.PrefsValues;
import com.example.menulearning.constants.Routes;
import com.example.menulearning.fragments.AboutFragment;
import com.example.menulearning.fragments.BaseFragment;
import com.example.menulearning.fragments.ResultsFragment;
import com.example.menulearning.fragments.StartFragment;
import com.example.menulearning.fragments.TestFragment;
import com.example.menulearning.managers.Router;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Router router;
    private boolean asyncFlag;

    public Router getRouter() {
        return router;
    }

    public SharedPreferences getPrefs() {
        return sharedPreferences;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        asyncFlag = false;
        sharedPreferences = getSharedPreferences(
                PrefsValues.PREFS_NAME.getStringValue(),
                MODE_PRIVATE
        );

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

        prefsEditor.putString(PrefsValues.INTRO_TEXT_KEY.getStringValue(),
                PrefsValues.INTRO_TEXT_VALUE.getStringValue());
        prefsEditor.putString(PrefsValues.RULES_TEXT_KEY.getStringValue(),
                PrefsValues.RULES_TEXT_VALUE.getStringValue());
        prefsEditor.apply();

        List<BaseFragment> fragments = Arrays.asList(
                new StartFragment(Routes.START),
                new AboutFragment(Routes.ABOUT),
                new TestFragment(Routes.TEST),
                new ResultsFragment(Routes.RESULTS)
        );
        router = Router.getInstance(this, R.id.viewContainer, fragments);

        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        navigationBar.setOnItemSelectedListener((item) -> {
            Routes route = null;
            if (!asyncFlag) {
                int itemId = item.getItemId();
                route = itemId == R.id.startItem? Routes.START : itemId == R.id.historyItem?
                        Routes.RESULTS : Routes.ABOUT;

                String testFlagKey = PrefsValues.TEST_FLAG_KEY.getStringValue();
                if (route == Routes.START && sharedPreferences.contains(testFlagKey)) {
                    if (sharedPreferences.getBoolean(testFlagKey, false)) {
                        route = Routes.TEST;
                    }
                }
            }

            return route != null && router.route(route);
        });

        navigationBar.setSelectedItemId(R.id.startItem);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean(PrefsValues.TEST_FLAG_KEY.getStringValue(), false);
        prefsEditor.commit();
    }

    public void setAsyncFlag(boolean flag) {
        asyncFlag = flag;
    }
}