package com.example.timetracker.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.timetracker.data.ActivityDatabase;
import com.example.timetracker.data.dao.RecordDao;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.Record;
import com.example.timetracker.data.model.RecordWithActivity;

public class RecordRepository {

    private RecordDao recordDao;
    private ExecutorService executorService;


    public RecordRepository(Application application) {
        recordDao = ActivityDatabase.getDatabase(application).getRecordDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Record>> getAllRecordsLiveData() {
        return recordDao.getAllRecordsLiveData();
    }

    public void insertRecords(Record... record) {
        for (Record r : record) {
            Log.d("database", "insert record: " + r.toString());
        }

        executorService.execute(() -> {
            recordDao.insertRecords(record);
        });
    }

    private void finishRecord() {
        Record record = recordDao.getUnfinishedRecord();
        if (record != null) {
            record.setEndTime(Calendar.getInstance());
            recordDao.updateRecords(record);
        }
    }

    public void finishRecordAsync() {
        executorService.execute(this::finishRecord);
    }

    public LiveData<RecordWithActivity> getRunningRecordLiveData() {
        return recordDao.getRunningRecordAndActivityLiveData();
    }

    public void startRecordAsync(Activity activity) {
        executorService.execute(() -> {
            finishRecord();
            Record record = new Record();
            record.setActivityId(activity.getId());
            record.setStartTime(Calendar.getInstance());
            recordDao.insertRecords(record);
        });

    }



}
