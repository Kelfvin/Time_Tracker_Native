package com.example.timetracker.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Calendar;
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
        long totalTimeCost = 0;
        for (ActivityAndRecords activityAndRecords : activitiesAndRecords) {
            totalTimeCost+=activityAndRecords.getTotalTimeCost();
        }
        return totalTimeCost;
    }

}
