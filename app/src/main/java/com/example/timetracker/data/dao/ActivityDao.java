package com.example.timetracker.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.ActivityAndRecords;
import com.example.timetracker.data.model.Record;

import java.util.List;
import java.util.Map;

@Dao // Data Access Object
public interface ActivityDao {
    // 所有数据库操作都在这里声明
    @Insert
    void insertActivities(Activity... activity);

    @Update
    void updateActivities(Activity... activity);

    @Delete
    void deleteActivities(Activity... activity);

    @Query("SELECT * FROM activity ORDER BY id DESC")
    LiveData<List<Activity>> getAllActivitiesLiveData();

    @Query("SELECT * FROM activity ORDER BY id DESC")
    List<Activity> getAllActivities();

    @Query("SELECT * FROM activity WHERE id = :id")
    LiveData <Activity> getActivityLiveDataById(int id);

    @Query("SELECT * FROM activity WHERE id = :id")
    Activity getActivityById(int id);


    @Transaction
    @Query("SELECT * FROM `activity`")
    LiveData<List<ActivityAndRecords>> getActivitiesAndRecordsLiveData();

    @Transaction
    @Query("SELECT * FROM activity")
    List<ActivityAndRecords> getActivitiesAndRecords();


    @Transaction
    @Query("SELECT * FROM activity WHERE id = :id")
    LiveData<List<ActivityAndRecords>> getActivitiesAndRecordsLiveDataById(int id);

    @Transaction
    @Query("SELECT * FROM activity WHERE id = :id")
    ActivityAndRecords getActivitiesAndRecordsById(int id);



}
