package com.example.timetracker.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.Calendar;
import java.util.List;

public class ActivityAndRecords {
    @Embedded
    private Activity activity;

    @Relation(
            parentColumn = "id",
            entityColumn = "activity_id"
    )
    private List<Record> records;


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public List<Record> getRecords() {
        return records;
    }

    public void setRecords(List<Record> records) {
        this.records = records;
    }

    public Long getTotalTimeCost() {
        long totalTimeCost = 0;
        for (Record record : records) {
            Calendar startTime = record.getStartTime();
            Calendar endTime = record.getEndTime();
            if (startTime == null) {
                return 0L;
            }

            if (endTime == null) {
                endTime = Calendar.getInstance();
            }
            totalTimeCost += endTime.getTimeInMillis() - startTime.getTimeInMillis();
        }
        return totalTimeCost;
    }
}
