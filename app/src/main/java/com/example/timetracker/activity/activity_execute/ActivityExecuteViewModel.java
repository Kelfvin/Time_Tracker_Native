package com.example.timetracker.activity.activity_execute;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timetracker.data.model.RecordWithActivity;
import com.example.timetracker.data.repository.ActivityRepository;
import com.example.timetracker.data.repository.GroupRepository;
import com.example.timetracker.data.repository.RecordRepository;

public class ActivityExecuteViewModel extends ViewModel {


    private ActivityRepository activityRepository;
    private GroupRepository groupRespository;
    private RecordRepository recordRepository;
    private MutableLiveData<Long> executeTime = new MutableLiveData<>();
    private MutableLiveData<RecordWithActivity> runningRecordWithActivityLiveData = new MutableLiveData<>();


    private Thread timerThread;

    public ActivityExecuteViewModel() {

    }

    public void init(Application application) {
        activityRepository = new ActivityRepository(application);
        groupRespository = new GroupRepository(application);
        recordRepository = new RecordRepository(application);

        recordRepository.getRunningRecordLiveData().observeForever(recordWithActivity -> {
            runningRecordWithActivityLiveData.setValue(recordWithActivity);

            if (timerThread == null) {
                timerThread = new Thread(() -> {
                    while (true) {
                        long startTime = recordWithActivity.getRecord().getStartTime().getTimeInMillis();
                        long currentTime = System.currentTimeMillis();
                        long executeTime = currentTime - startTime;
                        this.executeTime.postValue(executeTime);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                timerThread.start();
            }


        });
    }

    public MutableLiveData<Long> getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(MutableLiveData<Long> executeTime) {
        this.executeTime = executeTime;
    }

    public LiveData<RecordWithActivity> getRunningRecordWithActivityLiveData() {
        return runningRecordWithActivityLiveData;
    }

    public void setRunningRecordWithActivityLiveData(MutableLiveData<RecordWithActivity> runningRecordWithActivityLiveData) {
        this.runningRecordWithActivityLiveData = runningRecordWithActivityLiveData;
    }


    public void finishRecord() {
        recordRepository.finishRecordAsync();
    }
}