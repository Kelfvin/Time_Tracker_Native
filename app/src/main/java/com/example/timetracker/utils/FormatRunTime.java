package com.example.timetracker.utils;

public class FormatRunTime {
    public static String format(long time) {

        //  传入的是 Millisecond
        long second = time / 1000;
        long minute = second / 60;
        long hour = minute / 60;
        long day = hour / 24;


        return String.format("%02d:%02d:%02d", hour % 24, minute % 60, second % 60);
    }
}
