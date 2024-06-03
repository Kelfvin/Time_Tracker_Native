package com.example.timetracker.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.timetracker.data.model.Group;
import com.example.timetracker.data.model.GroupAndActivities;
import com.example.timetracker.data.model.GroupAndActivitiesAndRecords;

import java.util.List;


@Dao
public interface GroupDao {
    @Insert // 插入数据
    void insertGroups(Group... group);

    @Update // 更新数据
    void updateGroups(Group... group);

    @Delete
        // 删除数据
    void deleteGroups(Group... group);

    @Query("SELECT * FROM 'group' ORDER BY id DESC")
    LiveData<List<Group>> getAllGroupsLiveData();

    @Query("SELECT * FROM 'group' ORDER BY id DESC")
    List<Group> getAllGroups();


    @Query("SELECT * FROM 'group' WHERE id = :id")
    LiveData<Group> getGroupLiveDataById(int id);

    @Query("SELECT * FROM 'group' WHERE id = :id")
    Group getGroupById(int id);


    @Transaction
    @Query("SELECT * FROM 'group'")
    LiveData<List<GroupAndActivities>> getAllGroupsAndActivitiesLiveData();

    @Transaction
    @Query("SELECT * FROM 'group'")
    List<GroupAndActivities> getAllGroupsAndActivities();

    @Transaction
    @Query("SELECT * FROM 'group' WHERE id = :id")
    LiveData<List<GroupAndActivities>> getGroupAndActivitiesLiveDataById(int id);

    @Transaction
    @Query("SELECT * FROM 'group' WHERE id = :id")
    List<GroupAndActivities> getGroupAndActivitiesById(int id);

    @Transaction
    @Query("SELECT * FROM 'group'")
    LiveData<List<GroupAndActivitiesAndRecords>> getAllGroupAndActivitiesAndRecordsLiveData();


    @Transaction
    @Query("SELECT * FROM `group`")
    LiveData<List<GroupAndActivitiesAndRecords>> getAllGroupsAndActivitiesAndRecordsLiveData();


    @Transaction
    @Query("SELECT * FROM `group`")
    List<GroupAndActivitiesAndRecords> getAllGroupsAndActivitiesAndRecords();
}
