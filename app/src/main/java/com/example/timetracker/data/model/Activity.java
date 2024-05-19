package com.example.timetracker.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.sql.Time;
import java.time.Duration;
import java.util.List;

@Entity // 表示当前类是一个实体类
        (       tableName = "activity", // 表示当前实体类对应的表名
                foreignKeys =
                @ForeignKey(entity = Group.class,
                parentColumns = "id",
                childColumns = "group_id",
                onDelete = ForeignKey.CASCADE)
                // 表示当前表有一个外键，外键关联的表是Group表，
                // parentColumns表示Group表的主键，childColumns表示当前表的外键，
                // onDelete = ForeignKey.CASCADE表示级联删除
        )
public class Activity {
    @PrimaryKey(autoGenerate = true) // 表示当前字段是主键，autoGenerate = true表示自动生成
    private Integer id;


    @ColumnInfo(name = "group_id")
    private Integer groupId;

    @ColumnInfo(name = "name") // 表示当前字段在数据库中的列名
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "color")
    private String color;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }



    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Activity(String name, String description, String color, Integer groupId) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.groupId = groupId;
    }

    public Activity() {
        super();
        this.color = "#000000";

    }

    @NonNull
    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
