package com.example.timetracker.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.timetracker.data.ActivityDatabase;
import com.example.timetracker.data.dao.GroupDao;
import com.example.timetracker.data.model.Group;
import com.example.timetracker.data.model.GroupAndActivities;
import com.example.timetracker.data.model.GroupAndActivitiesAndRecords;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GroupRespository {
    private LiveData<List<Group>> groups;

    private LiveData<List<GroupAndActivities>> groupsAndActivities;

    private List<Group> groupList;
    private List<GroupAndActivities> groupAndActivitiesList;

    private final GroupDao groupDao;
    private final ExecutorService executorService;

    public GroupRespository(Application application) {
        groupDao = ActivityDatabase.getDatabase(application).getGroupDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Group>> getAllGroupsLiveData() {
        groups = groupDao.getAllGroupsLiveData();
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
        groupsAndActivities = groupDao.getAllGroupsAndActivitiesLiveData();
        return groupsAndActivities;
    }

    public Group getGroupById(int id) {
        return groupDao.getGroupById(id);
    }


    public LiveData<List<GroupAndActivitiesAndRecords>> getAllGroupsAndActivitiesAndRecordsLiveData() {
        return groupDao.getAllGroupAndActivitiesAndRecordsLiveData();
    }
}
