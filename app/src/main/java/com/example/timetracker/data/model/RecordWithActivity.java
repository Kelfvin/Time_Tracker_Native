package com.example.timetracker.data.model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class RecordWithActivity {
    @Embedded
    private Record record;

    @Relation(
            parentColumn = "activity_id",
            entityColumn = "id"
    )
    private Activity activity;

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
