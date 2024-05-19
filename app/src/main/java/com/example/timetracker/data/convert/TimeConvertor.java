package com.example.timetracker.data.convert;

import androidx.room.TypeConverter;

import java.sql.Time;
import java.util.Calendar;

public class TimeConvertor {
    @TypeConverter
    public static Long fromTime(Calendar time) {
        return time == null ? null : time.getTimeInMillis();
    }

    @TypeConverter
    public static Calendar toTime(Long time) {
        if (time == null) {
            return null;
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            return calendar;
        }
    }
}
