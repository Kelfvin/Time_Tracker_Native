package com.example.timetracker.data.dao;


import com.example.timetracker.data.model.Record;
import com.example.timetracker.data.model.RecordWithActivity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Transaction;
import androidx.room.Update;
import androidx.room.Query;

import java.util.List;


@Dao
public interface RecordDao {
// 所有数据库操作都在这里声明
    @Insert
    void insertRecords(Record... record);

    @Update
    void updateRecords(Record... record);

    @Delete
    void deleteRecords(Record... record);

    @Query("SELECT * FROM record ORDER BY id DESC")
    LiveData<List<Record>> getAllRecordsLiveData();

    @Query("SELECT * FROM record ORDER BY id DESC")
    List<Record> getAllRecords();

    @Query("SELECT * FROM record WHERE id = :id")
    LiveData<Record> getRecordLiveDataById(int id);

    @Query("SELECT * FROM record WHERE id = :id")
    Record getRecordById(int id);

    @Query("SELECT * FROM record WHERE end_time IS NULL")
    Record getUnfinishedRecord();


    @Query("SELECT * FROM record WHERE end_time IS NULL")
    LiveData<Record> getRunningRecordLiveData();

    @Transaction
    @Query("SELECT * FROM record WHERE end_time IS NULL")
    LiveData<RecordWithActivity> getRunningRecordAndActivityLiveData();

    @Transaction
    @Query("SELECT * FROM record WHERE id = :recordId")
    LiveData<RecordWithActivity> getRecordWithActivityLiveDataByRecordId(int recordId);

}
