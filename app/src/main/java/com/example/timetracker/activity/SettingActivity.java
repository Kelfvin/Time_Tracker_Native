package com.example.timetracker.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timetracker.R;
import com.example.timetracker.fragment.SettingFragment;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 加载 SettingsFragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settingFragment, new SettingFragment())
                .commit();
    }

}