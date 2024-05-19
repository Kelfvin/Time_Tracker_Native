package com.example.timetracker.fragment.activityManage;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.timetracker.data.model.Group;
import com.example.timetracker.data.repository.GroupRespository;
import com.example.timetracker.result.Result;

public class AddGroupVieModel extends ViewModel {
    private GroupRespository groupRespository;
    private MutableLiveData<Group> groupLiveData;

    public AddGroupVieModel() {

    }

    public MutableLiveData<Group> getGroupLiveData() {
        return groupLiveData;
    }

    public void setGroupLiveData(MutableLiveData<Group> groupLiveData) {
        this.groupLiveData = groupLiveData;
    }

    public void init(Application application) {
        groupRespository = new GroupRespository(application);
        groupLiveData = new MutableLiveData<>(new Group());
    }

    public Result addGroup() {
        Group group = groupLiveData.getValue();
        if (group == null) {
            Log.e("AddGroupVieModel", "addGroup: group is null");
            return new Result.Error("分类不能为空");
        }
        groupRespository.insertGroups(group);
        return new Result.Success("添加成功");
    }

    public void setGroupColor(String hexColor) {
        Group group = groupLiveData.getValue();
        if (group == null) {
            Log.e("AddGroupVieModel", "setGroupColor: group is null");
            return;
        }
        Log.d("AddGroupVieModel", "setGroupColor: " + hexColor);
        group.setColor(hexColor);
        this.groupLiveData.setValue(group);
    }
}
