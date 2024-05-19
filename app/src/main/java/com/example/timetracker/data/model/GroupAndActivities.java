package com.example.timetracker.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class GroupAndActivities {
    @Embedded
    private Group group;

    @Relation(
            parentColumn = "id",
            entityColumn = "group_id"
    )
    private List<Activity> activities;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
