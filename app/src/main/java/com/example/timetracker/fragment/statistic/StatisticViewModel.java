package com.example.timetracker.fragment.statistic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timetracker.data.model.ActivityAndRecords;
import com.example.timetracker.data.repository.ActivityRepository;
import com.example.timetracker.data.repository.GroupRepository;
import com.example.timetracker.utils.DateTimeUtils;

import java.util.Calendar;
import java.util.List;

public class StatisticViewModel extends ViewModel {

    public static final String MODE_DAY = "DAY";
    public static final String MODE_WEEK = "WEEK";
    public static final String MODE_MONTH = "MONTH";
    public static final String MODE_YEAR = "YEAR";
    private final MutableLiveData<List<ActivityAndRecords>> activitiesAndRecords;
    private Long startTime;
    private Long endTime;
    private String MODE = "DAY";
    private ActivityRepository activityRepository;
    private GroupRepository groupRepository;

    public StatisticViewModel() {
        activitiesAndRecords = new MutableLiveData<>();

        startTime =
                DateTimeUtils.getStartOfDay(Calendar.getInstance())
        ;
        endTime =
                DateTimeUtils.getEndOfDay(Calendar.getInstance())
        ;
    }

    public void init(@NonNull Application application) {
        activityRepository = new ActivityRepository(application);
        groupRepository = new GroupRepository(application);

        activityRepository.getActivitiesAndRecordsLiveDataByTime(
                startTime, endTime).observeForever(activitiesAndRecords::postValue
        );
    }


    // 切换到下一个时间段
    public void next() {
        switch (MODE) {
            case MODE_DAY:
                startTime = startTime + DateTimeUtils.DAY;
                endTime = endTime + DateTimeUtils.DAY;
                break;
            case MODE_WEEK:
                startTime = startTime + DateTimeUtils.WEEK;
                endTime = endTime + DateTimeUtils.WEEK;
                break;
            case MODE_MONTH:
                startTime = startTime + DateTimeUtils.MONTH;
                endTime = endTime + DateTimeUtils.MONTH;
                break;

            case MODE_YEAR:
                startTime = startTime + DateTimeUtils.YEAR;
                endTime = endTime + DateTimeUtils.YEAR;
                break;
        }

        fetchData();

    }


    public void fetchData() {
        activityRepository.getActivitiesAndRecordsLiveDataByTime(
                startTime, endTime).observeForever(activitiesAndRecords::postValue
        );
    }

    // 切换到上一个时间段
    public void previous() {
        switch (MODE) {
            case MODE_DAY:
                startTime = startTime - DateTimeUtils.DAY;
                endTime = endTime - DateTimeUtils.DAY;
                break;
            case MODE_WEEK:
                startTime = startTime - DateTimeUtils.WEEK;
                endTime = endTime - DateTimeUtils.WEEK;
                break;
            case MODE_MONTH:
                startTime = startTime - DateTimeUtils.MONTH;
                endTime = endTime - DateTimeUtils.MONTH;
                break;
            case MODE_YEAR:
                startTime = startTime - DateTimeUtils.YEAR;
                endTime = endTime - DateTimeUtils.YEAR;
                break;
        }

        fetchData();
    }

    private void onModeChanged() {
        switch (MODE) {
            case MODE_DAY:
                startTime = DateTimeUtils.getStartOfDay(Calendar.getInstance());
                endTime = DateTimeUtils.getEndOfDay(Calendar.getInstance());
                break;
            case MODE_WEEK:
                startTime = DateTimeUtils.getStartOfWeek(Calendar.getInstance());
                endTime = DateTimeUtils.getEndOfWeek(Calendar.getInstance());
                break;
            case MODE_MONTH:
                startTime = DateTimeUtils.getStartOfMonth(Calendar.getInstance());
                endTime = DateTimeUtils.getEndOfMonth(Calendar.getInstance());
                break;
            case MODE_YEAR:
                startTime = DateTimeUtils.getStartOfYear(Calendar.getInstance());
                endTime = DateTimeUtils.getEndOfYear(Calendar.getInstance());
                break;
        }
        fetchData();
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getMODE() {
        return MODE;
    }

    public void setMODE(String MODE) {
        this.MODE = MODE;
        onModeChanged();
    }

    public MutableLiveData<List<ActivityAndRecords>> getActivitiesAndRecords() {
        return activitiesAndRecords;
    }
}