package com.example.timetracker.data.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "group")
public class Group {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "color")
    private String color;


    @Ignore
    private Long timeCost;

    public Group(String name, String description, String color) {
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public Group() {
        this.color = "#000000";
    }

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

    public Long getTimeCost() {
        return timeCost;
    }

    public void setTimeCost(Long timeCoast) {
        this.timeCost = timeCoast;
    }
}
