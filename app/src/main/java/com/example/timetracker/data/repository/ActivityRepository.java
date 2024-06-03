package com.example.timetracker.data.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.timetracker.data.ActivityDatabase;
import com.example.timetracker.data.dao.ActivityDao;
import com.example.timetracker.data.model.Activity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.timetracker.data.model.ActivityAndRecords;
import com.example.timetracker.data.model.Record;

public class ActivityRepository {
    private LiveData<List<Activity>> activities;
    private ActivityDao activityDao;
    private ExecutorService executorService;

    public ActivityRepository(Context context) {
        ActivityDatabase activityDatabase = ActivityDatabase.getDatabase(context);
        activityDao = activityDatabase.getActivityDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Activity>> getAllActivitiesLiveData() {
        activities = activityDao.getAllActivitiesLiveData();
        return activities;
    }


    public void insertActivities(Activity... activity) {
        // 打印
        for (Activity a : activity) {
            Log.d("database", a.toString());
        }
        executorService.execute(() -> {
            activityDao.insertActivities(activity);
        });
    }

    public void updateActivities(Activity... activity) {
        executorService.execute(() -> {
            activityDao.updateActivities(activity);
        });
    }

    public void deleteActivities(Activity... activity) {
        executorService.execute(() -> {
            activityDao.deleteActivities(activity);
        });
    }

    public LiveData<Activity> getActivityLiveDataById(int id) {
        return activityDao.getActivityLiveDataById(id);
    }

    public LiveData<List<ActivityAndRecords>> getActivitiesAndRecordsLiveDataByTime(long startTime, long endTime) {

        LiveData<List<ActivityAndRecords>> activitiesAndRecords = activityDao.getActivitiesAndRecordsLiveData();
        activitiesAndRecords.observeForever(activityAndRecords -> {
            for (ActivityAndRecords aar : activityAndRecords) {
                // 过滤时间
                aar.filtByTime(startTime, endTime);
            }
        });

        return activitiesAndRecords;
    }




}
