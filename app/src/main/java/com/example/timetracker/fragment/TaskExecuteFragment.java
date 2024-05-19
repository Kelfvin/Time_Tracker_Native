package com.example.timetracker.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.timetracker.R;
import com.example.timetracker.activity.activity_execute.ActivityExecuteViewModel;

public class TaskExecuteFragment extends Fragment {

    private ActivityExecuteViewModel mViewModel;

    public static TaskExecuteFragment newInstance() {
        return new TaskExecuteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activity_execute, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ActivityExecuteViewModel.class);
        // TODO: Use the ViewModel
    }

}