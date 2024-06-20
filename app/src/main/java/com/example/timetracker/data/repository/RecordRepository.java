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

    public void insertRecord(Record record) {

        Log.d("database", "insert record: " + record.toString());

        // 判断结束时间是否大于现在的时间
        if (record.getEndTime() != null && record.getEndTime().getTimeInMillis() > Calendar.getInstance().getTimeInMillis()) {
            Log.d("database", "record end time is in the future");
            return;
        }

        executorService.execute(() -> {
            long newStartTimeLong = record.getStartTime().getTimeInMillis();
            long newEndTimeLong = record.getEndTime().getTimeInMillis();

            // 先查询与该记录时间重叠的记录
            List<Record> records = recordDao.selectRecordByConflictTimeRange(newStartTimeLong, newEndTimeLong);

            // 遍历每一条记录，如果有重叠的进行处理
            // 特别的，如果记录是正在进行的记录，如果本次我们添加的记录结束时间大于正在进行的记录的开始时间，那么就报错
            // 1. 若该记录的开始时间和结束时间都在本次记录的时间范围内，则删除该记录
            // 2. 若该记录的开始时间在本次记录开始时间之前，结束时间在本次记录结束时间之后，则将该记录的结束时间改为本次记录的开始时间，当然要考虑这个长短是多少，如果长度小于一个范围，就删除该记录
            // 3. 若该记录的开始时间在本次记录开始时间之后，结束时间在本次记录结束时间之前，则将该记录的开始时间改为本次记录的结束时间，当然要考虑这个长短是多少，如果长度小于一个范围，就删除该记录


            
            for (Record oldRecord:records){
                Calendar oldStartTime = oldRecord.getStartTime();
                Calendar oldEndTime = oldRecord.getEndTime();
                long oldStartTimeLong = oldStartTime.getTimeInMillis();
                long oldEndTimeLong = oldEndTime.getTimeInMillis();

                // 如果是正在进行的记录
                if (oldRecord.getEndTime() == null){
                    // 将正在进行的记录的开始时间改为本次记录的结束时间
                    oldRecord.setStartTime(record.getEndTime());
                    // 写入
                    recordDao.updateRecords(oldRecord);
                }

                // 如果是已经结束的
                else {
                    // 如果该记录的开始时间和结束时间都在本次记录的时间范围内，则删除该记录
                    if (oldStartTimeLong >= newStartTimeLong&& oldEndTime.getTimeInMillis() <= newEndTimeLong){
                        recordDao.deleteRecords(oldRecord);
                        Log.d("database", "delete record: " + oldRecord.toString());
                    }
                    // 如果该记录的开始时间在本次记录开始时间之前，结束时间在本次记录结束时间之前，则将该记录的结束事件修改，如果长度小于一个范围，就删除该记录
                    else if (oldStartTimeLong <= newStartTimeLong && oldEndTime.getTimeInMillis() <= newEndTimeLong){
                        oldRecord.setEndTime(record.getStartTime());
                        if (oldRecord.getDuration() < 1000){
                            recordDao.deleteRecords(oldRecord);
                            Log.d("database", "delete record: " + oldRecord.toString());
                        }else {
                            recordDao.updateRecords(oldRecord);
                            Log.d("database", "update record: " + oldRecord.toString());
                        }
                    }
                    // 如果该记录的开始时间在本次记录开始时间之后，结束时间在本次记录结束时间之后，则将该记录的开始时间改为本次记录的结束时间，当然要考虑这个长短是多少，如果长度小于一个范围，就删除该记录
                    else if (oldStartTimeLong >= newStartTimeLong && oldEndTimeLong >= newEndTimeLong){
                        oldRecord.setStartTime(record.getEndTime());
                        if (oldRecord.getDuration() < 1000){
                            recordDao.deleteRecords(oldRecord);
                            Log.d("database", "delete record: " + oldRecord.toString());
                        }else {
                            recordDao.updateRecords(oldRecord);
                            Log.d("database", "update record: " + oldRecord.toString());
                        }
                    }

                    // （本次记录在原来的记录中间），拆分为两条记录
                    else if (oldStartTimeLong <= newEndTimeLong && oldEndTimeLong >= newEndTimeLong){

                        // 创建新的记录
                        Record newRecord = new Record();
                        newRecord.setActivityId(oldRecord.getActivityId());
                        newRecord.setStartTime(record.getEndTime());
                        newRecord.setEndTime(oldEndTime);
                        recordDao.insertRecords(newRecord);
                        Log.d("database", "insert record: " + newRecord.toString());
                        // 先将原来的记录的结束时间改为本次记录的开始时间
                        oldRecord.setEndTime(record.getStartTime());
                        // 写入
                        recordDao.updateRecords(oldRecord);
                        Log.d("database", "update record: " + oldRecord.toString());
                    }

                }

            }

            Log.d("database", "insert record: " + record.toString());
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
