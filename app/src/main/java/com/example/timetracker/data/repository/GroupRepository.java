package com.example.timetracker.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.timetracker.data.ActivityDatabase;
import com.example.timetracker.data.dao.GroupDao;
import com.example.timetracker.data.dao.RecordDao;
import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.Group;
import com.example.timetracker.data.model.GroupAndActivities;
import com.example.timetracker.data.model.GroupAndActivitiesAndRecords;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupRepository {
    private final GroupDao groupDao;
    private final RecordDao recordDao;
    private final ExecutorService executorService;

    public GroupRepository(Application application) {
        groupDao = ActivityDatabase.getDatabase(application).getGroupDao();
        recordDao = ActivityDatabase.getDatabase(application).getRecordDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Group>> getAllGroupsLiveData() {
        LiveData<List<Group>> groups = groupDao.getAllGroupsLiveData();
        return groups;
    }

    public void insertGroups(Group... group) {
        executorService.execute(() -> {
            groupDao.insertGroups(group);
        });
    }

    public void updateGroups(Group... group) {
        executorService.execute(() -> {
            groupDao.updateGroups(group);
        });
    }

    public void deleteGroups(Group... group) {
        executorService.execute(() -> {
            groupDao.deleteGroups(group);
        });
    }

    public LiveData<Group> getGroupLiveDataById(int id) {
        return groupDao.getGroupLiveDataById(id);
    }

    public LiveData<List<GroupAndActivities>> getAllGroupsAndActivitiesLiveData() {
        return groupDao.getAllGroupsAndActivitiesLiveData();
    }

    public Group getGroupById(int id) {
        return groupDao.getGroupById(id);
    }

    public List<GroupAndActivities> getAllGroupsAndActivities() {
        return groupDao.getAllGroupsAndActivities();
    }




    public LiveData<List<GroupAndActivitiesAndRecords>> getAllGroupsAndActivitiesAndRecordsLiveData(long startTime, long endTime) {

        LiveData<List<GroupAndActivitiesAndRecords>> groupsAndActivitiesAndRecords = groupDao.getAllGroupsAndActivitiesAndRecordsLiveData();

        groupsAndActivitiesAndRecords.observeForever(
            new Observer<List<GroupAndActivitiesAndRecords>>() {
                @Override
                public void onChanged(List<GroupAndActivitiesAndRecords> groupAndActivitiesAndRecords) {
                    for (GroupAndActivitiesAndRecords item : groupAndActivitiesAndRecords) {
                        item.filtByTime(startTime, endTime);
                        item.calculateGroupTimeCost();
                    }
                }
            }
        );

        return groupsAndActivitiesAndRecords;

    }

    public List<GroupAndActivitiesAndRecords> getAllGroupsAndActivitiesAndRecords() {
        List<GroupAndActivitiesAndRecords> groupsAndActivitiesAndRecords = groupDao.getAllGroupsAndActivitiesAndRecords();
        for (GroupAndActivitiesAndRecords item : groupsAndActivitiesAndRecords) {
            item.calculateGroupTimeCost();
        }
        return groupsAndActivitiesAndRecords;
    }

    public void deleteGroupAsync(Group group) {
        executorService.execute(() -> {
            groupDao.deleteGroups(group);
        });
    }
}
