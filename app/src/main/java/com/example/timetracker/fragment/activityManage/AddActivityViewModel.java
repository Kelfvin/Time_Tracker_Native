package com.example.timetracker.fragment.activityManage;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.timetracker.data.model.Activity;
import com.example.timetracker.data.model.Group;
import com.example.timetracker.data.repository.ActivityRepository;
import com.example.timetracker.data.repository.GroupRepository;
import com.example.timetracker.result.Result;

public class AddActivityViewModel extends ViewModel {

    private ActivityRepository activityRepository;
    private GroupRepository groupRespository;

    private final MutableLiveData<Activity> activity;
    private MutableLiveData<Group> groupLiveData;


    public AddActivityViewModel() {
        super();
        activity = new MutableLiveData<Activity>(new Activity());
    }

    public void init(Application application) {
        activityRepository = new ActivityRepository(application);
        groupRespository = new GroupRepository(application);
    }

    private Result validateActivity() {
        if (activity.getValue().getName() == null || activity.getValue().getName().isEmpty()) {
            return new Result.Error("请输入活动名称");
        }
        if (groupLiveData.getValue() == null || groupLiveData.getValue().getId() == null){
            return new Result.Error("请选择分组");
        }

        return new Result.Success("验证通过");
    }

    public Result insertActivity() {
        Result validateResult = validateActivity();
        if (validateResult.isFail()) {
            return validateResult;
        }

        try {
            activityRepository.insertActivities(activity.getValue());
            return new Result.Success("添加成功");
        } catch (Exception e) {
            return new Result.Error("添加失败");
        }

    }

    public void setActivity(Activity activity) {
        this.activity.setValue(activity);
    }

    public LiveData<Activity> getActivity() {
        return activity;
    }

    public LiveData<Group> getGroupLiveData() {
        if (groupLiveData == null) {
            groupLiveData = new MutableLiveData<>();
        }
        return groupLiveData;
    }


    public void setGroupById(int id) {
        Activity activity = this.activity.getValue();
        activity.setGroupId(id);
        this.activity.setValue(activity);
        groupRespository.getGroupLiveDataById(id).observeForever(new Observer<Group>() {
            @Override
            public void onChanged(Group group) {
                groupLiveData.setValue(group);
            }
        });
    }

    public void setActivityColor(String color) {
        if (activity.getValue() != null) {
            Activity activity = this.activity.getValue();
            activity.setColor(color);
            this.activity.setValue(activity);

            Log.d("AddActivityViewModel", "set color: " + color);
        }else {
            Log.e("AddActivityViewModel", "activity is null");
        }
    }
}
