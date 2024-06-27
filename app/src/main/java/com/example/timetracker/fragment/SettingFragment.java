package com.example.timetracker.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.timetracker.R;


public class SettingFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.setting, rootKey);
        // 利用sharedPreferences加载xml
        setPreferencesFromResource(R.xml.setting, rootKey);
    }
}