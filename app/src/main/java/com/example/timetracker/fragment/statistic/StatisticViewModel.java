package com.example.timetracker.activity.statistic;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.example.timetracker.data.repository.ActivityRepository;
import com.example.timetracker.data.repository.GroupRepository;

public class StatisticViewModel extends ViewModel {

    private ActivityRepository activityRepository;
    private GroupRepository  groupRepository;

    public StatisticViewModel(Application application) {
        activityRepository = new ActivityRepository(application);
        groupRepository = new GroupRepository(application);
    }
}