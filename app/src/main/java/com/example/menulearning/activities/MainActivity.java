package com.example.menulearning.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.menulearning.R;
import com.example.menulearning.constants.PrefsTypes;
import com.example.menulearning.constants.PrefsValues;
import com.example.menulearning.entities.PrefsItem;
import com.example.menulearning.fragments.AboutFragment;
import com.example.menulearning.fragments.BaseFragment;
import com.example.menulearning.fragments.StartFragment;
import com.example.menulearning.fragments.TestFragment;
import com.example.menulearning.managers.DbManager;
import com.example.menulearning.managers.PrefsManager;
import com.example.menulearning.managers.Router;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private PrefsManager<Boolean> booleanPrefsManager;
    private PrefsManager<String> stringPrefsManager;
    private Router router;

    public PrefsManager<Boolean> getBooleanPrefsManager() {
        return booleanPrefsManager;
    }

    public PrefsManager<String> getStringPrefsManager() {
        return stringPrefsManager;
    }

    public Router getRouter() {
        return router;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DbManager.getInstance(getApplicationContext());

        booleanPrefsManager = new PrefsManager<>(
                getApplicationContext(),
                PrefsValues.PREFS_NAME.getStringValue()
        );
        stringPrefsManager = new PrefsManager<>(
                getApplicationContext(),
                PrefsValues.PREFS_NAME.getStringValue()
        );

        stringPrefsManager.putItem(PrefsValues.INTRO_TEXT_KEY.getStringValue(),
                PrefsValues.INTRO_TEXT_VALUE.getStringValue());
        stringPrefsManager.putItem(PrefsValues.RULES_TEXT_KEY.getStringValue(),
                PrefsValues.RULES_TEXT_VALUE.getStringValue());

        List<BaseFragment> fragments = Arrays.asList(
                new StartFragment(R.id.startItem),
                new AboutFragment(R.id.aboutItem),
                new TestFragment(R.layout.fragment_test)
        );
        router = Router.getInstance(this, R.id.viewContainer, fragments);

        BottomNavigationView navigationBar = findViewById(R.id.navigationBar);
        navigationBar.setOnItemSelectedListener((item) -> {
            int itemId = item.getItemId();
            String testFlagKey = PrefsValues.TEST_FLAG_KEY.getStringValue();

            if (itemId == R.id.startItem && booleanPrefsManager.hasItem(testFlagKey)) {
                PrefsItem<Boolean> prefsItem = booleanPrefsManager
                        .getItem(testFlagKey, PrefsTypes.BOOLEAN);
                if (prefsItem.getValue()) {
                    itemId = R.layout.fragment_test;
                }
            }

            return router.route(itemId);
        });
        navigationBar.setSelectedItemId(R.id.startItem);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        booleanPrefsManager.removeItem(PrefsValues.TEST_FLAG_KEY.getStringValue());
    }
}