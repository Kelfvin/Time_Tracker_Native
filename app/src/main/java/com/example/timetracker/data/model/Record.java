package com.example.timetracker.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;


@Entity(
        tableName = "record",
        foreignKeys = @ForeignKey(
                entity = Activity.class,
                parentColumns = "id",
                childColumns = "activity_id",
                onDelete = ForeignKey.CASCADE
        )
)
public class Record {
    @PrimaryKey
    private Integer id;
    @ColumnInfo(name = "activity_id")
    private Integer activityId;
    // 保存记录开始时间
    @ColumnInfo(name = "start_time")
    private Calendar startTime;
    // 保存记录结束时间
    @ColumnInfo(name = "end_time")
    private Calendar endTime;
    @ColumnInfo(name = "description")
    private String description;


    @Ignore
    private Long timeCoast;


    @NonNull
    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", activityId=" + activityId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", description='" + description + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Long getTimeCoast() {
        return timeCoast;
    }

    public void setTimeCoast(Long timeCoast) {
        this.timeCoast = timeCoast;
    }

}
