package com.example.timetracker.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class GroupAndActivitiesAndRecords {
    @Embedded
    private Group group;

    @Relation(
            entity = Activity.class,
            parentColumn = "id",
            entityColumn = "group_id"
    )
    private List<ActivityAndRecords> activitiesAndRecords;


    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<ActivityAndRecords> getActivitiesAndRecords() {
        return activitiesAndRecords;
    }

    public void setActivitiesAndRecords(List<ActivityAndRecords> activitiesAndRecords) {
        this.activitiesAndRecords = activitiesAndRecords;
    }

    public Long getTotalTimeCost() {
        return group.getTimeCost();
    }

    // 遍历所有的ActivityAndRecords，计算每个Group的总时间
    public void calculateGroupTimeCost(){
        long totalTimeCost = 0;
        for (ActivityAndRecords activityAndRecords : activitiesAndRecords) {
            activityAndRecords.calculateActivityTimeCost();
            Activity activity = activityAndRecords.getActivity();
            totalTimeCost+=activityAndRecords.getActivity().getTimeCost();
        }
        group.setTimeCost(totalTimeCost);
    }

    public void filtByTime(long startTime, long endTime){
        for (ActivityAndRecords activityAndRecords : activitiesAndRecords) {
            activityAndRecords.filtByTime(startTime, endTime);
        }
    }
}
