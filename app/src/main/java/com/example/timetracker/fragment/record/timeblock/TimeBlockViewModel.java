package com.example.timetracker.fragment.record.timeblock;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.ActivityAndRecords;
import com.example.timetracker.data.model.GroupAndActivities;
import com.example.timetracker.data.model.Record;
import com.example.timetracker.data.repository.ActivityRepository;
import com.example.timetracker.data.repository.GroupRepository;
import com.example.timetracker.data.repository.RecordRepository;

import java.util.Calendar;
import java.util.List;

public class TimeBlockViewModel extends ViewModel implements TimeGridView.OnTimeSelectedListener, ActivityButtonAdapter.OnActivityClickListener{
    private ActivityRepository activityRepository;
    private GroupRepository groupRepository;
    private RecordRepository recordRepository;

    private Calendar startTime;
    private Calendar endTime;

    private  MutableLiveData<Calendar> selectedDate = new MutableLiveData<Calendar>();


    private final MutableLiveData<List<GroupAndActivities>> allGroupsAndActivitiesLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<ActivityAndRecords>> activitiesAndRecordsLiveData = new MutableLiveData<List<ActivityAndRecords>>();


    public TimeBlockViewModel() {
        super();

        selectedDate.setValue(Calendar.getInstance());
    }


    // 初始化
    public void init(Application application) {
        activityRepository = new ActivityRepository(application);
        groupRepository = new GroupRepository(application);
        recordRepository = new RecordRepository(application);

        groupRepository.getAllGroupsAndActivitiesLiveData().observeForever(allGroupsAndActivitiesLiveData::postValue);


        Calendar todayStartTime = Calendar.getInstance();
        todayStartTime.set(Calendar.HOUR_OF_DAY, 0);
        todayStartTime.set(Calendar.MINUTE, 0);
        todayStartTime.set(Calendar.SECOND, 0);
        todayStartTime.set(Calendar.MILLISECOND, 0);

        Calendar todayEndTime = Calendar.getInstance();
        todayEndTime.set(Calendar.HOUR_OF_DAY, 23);
        todayEndTime.set(Calendar.MINUTE, 59);
        todayEndTime.set(Calendar.SECOND, 59);
        todayEndTime.set(Calendar.MILLISECOND, 999);


        activityRepository.getActivitiesAndRecordsLiveDataByTime(
                todayStartTime.getTimeInMillis(),
                todayEndTime.getTimeInMillis()
        ).observeForever(activities -> {
            activitiesAndRecordsLiveData.postValue(activities);
        });

        selectedDate.observeForever(new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar calendar) {
                Log.d("TimeBlockViewModel", "onChanged: " + calendar.getTime());
                Calendar startTime = (Calendar) calendar.clone();
                startTime.set(Calendar.HOUR_OF_DAY, 0);
                startTime.set(Calendar.MINUTE, 0);
                startTime.set(Calendar.SECOND, 0);
                startTime.set(Calendar.MILLISECOND, 0);

                Calendar endTime = (Calendar) calendar.clone();
                endTime.set(Calendar.HOUR_OF_DAY, 23);
                endTime.set(Calendar.MINUTE, 59);
                endTime.set(Calendar.SECOND, 59);
                endTime.set(Calendar.MILLISECOND, 999);

                activityRepository.getActivitiesAndRecordsLiveDataByTime(
                        startTime.getTimeInMillis(),
                        endTime.getTimeInMillis()
                ).observeForever(activities -> {
                    activitiesAndRecordsLiveData.postValue(activities);
                });
            }
        });


    }

    public LiveData<List<GroupAndActivities>> getAllGroupsAndActivitiesLiveData() {
        return allGroupsAndActivitiesLiveData;
    }



    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    @Override
    public void onTimeSelected(int startHour, int startMinute, int endHour, int endMinute) {

        // 如果全是-1，说明没有选择时间
        if (startHour == -1 && startMinute == -1 && endHour == -1 && endMinute == -1) {
            startTime = null;
            endTime = null;
            Log.d("TimeBlockViewModel", "onTimeSelected: 清空时间");
            return;
        }

        startTime = (Calendar) selectedDate.getValue().clone();
        startTime.set(Calendar.HOUR_OF_DAY, startHour);
        startTime.set(Calendar.MINUTE, startMinute);

        endTime = (Calendar) selectedDate.getValue().clone();
        endTime.set(Calendar.HOUR_OF_DAY, endHour);
        endTime.set(Calendar.MINUTE, endMinute);

        Log.d("TimeBlockViewModel", "onTimeSelected: " + startTime.getTime() + " " + endTime.getTime());
    }


    @Override
    public void onActivityClick(Activity activity) {
        Log.d("TimeBlockViewModel", "onActivityClick: " + activity.getName());

        if (startTime == null || endTime == null) {
            Log.d("TimeBlockViewModel", "onActivityClick: 时间为空");
            return;
        }

        Record record = new Record();
        record.setActivityId(activity.getId());
        record.setStartTime(startTime);
        record.setEndTime(endTime);
        recordRepository.insertRecord(record);

        // 清空时间
        startTime = null;
        endTime = null;

    }

    public LiveData<List<ActivityAndRecords>> getActivitiesAndRecordsLiveData() {
        return activitiesAndRecordsLiveData;
    }

    public void setSelectDate(Calendar date) {
        selectedDate.postValue(date);
    }
}
