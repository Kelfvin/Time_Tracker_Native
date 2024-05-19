package com.example.timetracker.fragment.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.Group;
import com.example.timetracker.data.model.GroupAndActivities;
import com.example.timetracker.data.model.GroupAndActivitiesAndRecords;
import com.example.timetracker.data.model.RecordWithActivity;
import com.example.timetracker.data.repository.ActivityRepository;
import com.example.timetracker.data.repository.GroupRespository;
import com.example.timetracker.data.repository.RecordRepository;

import java.util.Calendar;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private ActivityRepository activityRepository;
    private GroupRespository groupRespository;
    private RecordRepository recordRepository;
    private MutableLiveData<Long> executeTime = new MutableLiveData<>();
    private MutableLiveData<RecordWithActivity> runningRecordWithActivityLiveData = new MutableLiveData<>();

    // 运行的线程
    private Thread timerThread;

    public HomeViewModel() {
    }

    public MutableLiveData<Long> getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(MutableLiveData<Long> executeTime) {
        this.executeTime = executeTime;
    }

    /**
     * 初始内部的repository，不初始化会报错
     *
     * @param application
     */
    public void init(@NonNull Application application) {
        // 添加数据库，第一个参数是上下文，第二个参数是数据库类，第三个参数是数据库名
        activityRepository = new ActivityRepository(application);
        groupRespository = new GroupRespository(application);
        recordRepository = new RecordRepository(application);

        recordRepository.getRunningRecordLiveData().observeForever(recordWithActivity -> {
            runningRecordWithActivityLiveData.setValue(recordWithActivity);

            // 如果线程为空，就开启线程
            if (timerThread == null) {
                timerThread = new Thread(() -> {
                    while (runningRecordWithActivityLiveData.getValue() != null) {
                        long startTime = runningRecordWithActivityLiveData.getValue().getRecord().getStartTime().getTimeInMillis();
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        long executeTime = currentTime - startTime;
                        this.executeTime.postValue(executeTime);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    timerThread = null;
                });
                timerThread.start();
                return;
            }

            // 如果线程不为空，就更新线程
            if (runningRecordWithActivityLiveData.getValue() != null) {
                timerThread.interrupt();
                timerThread = null;
                timerThread = new Thread(() -> {
                    while (runningRecordWithActivityLiveData.getValue() != null) {
                        long startTime = runningRecordWithActivityLiveData.getValue().getRecord().getStartTime().getTimeInMillis();
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        long executeTime = currentTime - startTime;
                        this.executeTime.postValue(executeTime);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    timerThread = null;
                });
                timerThread.start();
            }

        });
    }

    public LiveData<List<Group>> getGroups() {
        return groupRespository.getAllGroupsLiveData();
    }


    public LiveData<List<Activity>> getActivities() {
        return activityRepository.getAllActivitiesLiveData();
    }

    public void insertActivities(Activity... activity) {
        activityRepository.insertActivities(activity);
    }

    public void insertGroups(Group... group) {
        groupRespository.insertGroups(group);
    }

    public LiveData<List<GroupAndActivities>> getGroupsAndActivities() {
        return groupRespository.getAllGroupsAndActivitiesLiveData();
    }

    public LiveData<List<GroupAndActivitiesAndRecords>> getAllGroupsAndActivitiesAndRecordsLiveData() {
        return groupRespository.getAllGroupsAndActivitiesAndRecordsLiveData();
    }


    public MutableLiveData<RecordWithActivity> getRunningRecordWithActivityLiveData() {
        return runningRecordWithActivityLiveData;
    }

    public void setRunningRecordWithActivityLiveData(MutableLiveData<RecordWithActivity> runningRecordWithActivityLiveData) {
        this.runningRecordWithActivityLiveData = runningRecordWithActivityLiveData;
    }

    public void finishRecord() {
        recordRepository.finishRecordAsync();
    }
}
