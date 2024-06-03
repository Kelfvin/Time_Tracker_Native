package com.example.timetracker.fragment.home;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.GroupAndActivitiesAndRecords;
import com.example.timetracker.data.model.Record;
import com.example.timetracker.data.model.RecordWithActivity;
import com.example.timetracker.data.repository.ActivityRepository;
import com.example.timetracker.data.repository.GroupRepository;
import com.example.timetracker.data.repository.RecordRepository;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class HomeViewModel extends ViewModel {

    private ActivityRepository activityRepository;
    private GroupRepository groupRepository;
    private RecordRepository recordRepository;
    private MutableLiveData<Long> executeTime = new MutableLiveData<>();

    private final MutableLiveData<List<GroupAndActivitiesAndRecords>> allGroupsAndActivitiesAndRecordsLiveData = new MutableLiveData<>();

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
        groupRepository = new GroupRepository(application);
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



        // 获取所有的组和活动和记录
        // 获取当天的所有记录
        // 开始时间 当天的0点
        // 结束时间 当天的24点
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR_OF_DAY, 0);
        startTime.set(Calendar.MINUTE, 0);
        startTime.set(Calendar.SECOND, 0);
        long startTimeLong = startTime.getTimeInMillis();

        Calendar endTime = Calendar.getInstance();
        endTime.set(Calendar.HOUR_OF_DAY, 23);
        endTime.set(Calendar.MINUTE, 59);
        endTime.set(Calendar.SECOND, 59);
        long endTimeLong = endTime.getTimeInMillis();
        groupRepository.getAllGroupsAndActivitiesAndRecordsLiveData(startTimeLong,endTimeLong)
                .observeForever(allGroupsAndActivitiesAndRecordsLiveData::postValue);

    }

    public void fetchAllGroupsAndActivitiesAndRecords() {
        Observable.fromCallable(() -> groupRepository.getAllGroupsAndActivitiesAndRecords())
                .subscribeOn(io.reactivex.rxjava3.schedulers.Schedulers.io())
                .subscribe(new Observer<List<GroupAndActivitiesAndRecords>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<GroupAndActivitiesAndRecords> groupAndActivitiesAndRecords) {
                        allGroupsAndActivitiesAndRecordsLiveData.postValue(groupAndActivitiesAndRecords);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("HomeViewModel", "fetchAllGroupsAndActivitiesAndRecords: ", e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public LiveData<List<GroupAndActivitiesAndRecords>> getAllGroupsAndActivitiesAndRecordsLiveData() {
        return allGroupsAndActivitiesAndRecordsLiveData;
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
