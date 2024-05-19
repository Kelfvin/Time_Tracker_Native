package com.example.timetracker.diaglog;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.timetracker.data.model.Group;
import com.example.timetracker.data.repository.GroupRespository;

import java.util.List;

public class GroupSelectViewModel extends ViewModel {
    private static final String TAG = "GroupSelectViewModel";
    private LiveData<List<Group>> groupsLiveData;
    private GroupRespository groupRespository;

    public GroupSelectViewModel() {
        super();
    }

    public void init(Application context) {
        // 初始化
        groupRespository = new GroupRespository(context);
    }

    public LiveData<List<Group>> getGroupsLiveData() {
        if (groupsLiveData == null) {
            groupsLiveData = groupRespository.getAllGroupsLiveData();
        }
        return groupsLiveData;
    }

}
