package com.example.timetracker.utils;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {


    public static final Long DAY = 24 * 60 * 60 * 1000L;
    public static final Long WEEK = 7 * DAY;
    public static final Long MONTH = 30 * DAY;
    public static final Long YEAR = 365 * DAY;

    public static String format(Calendar calendar) {
        // 返回格式化后的日期
        return String.format("%d-%02d-%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH));
    }


    // 获取给定日期当天的开始时间（00:00:00）
    public static long getStartOfDay(Calendar calendar) {
        Calendar start = (Calendar) calendar.clone();
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTimeInMillis();
    }

    // 获取给定日期当天的结束时间（23:59:59）
    public static long getEndOfDay(Calendar calendar) {
        Calendar end = (Calendar) calendar.clone();
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 999);
        return end.getTimeInMillis();
    }

    // 获取给定日期所在周的开始时间（周一的00:00:00）
    public static long getStartOfWeek(Calendar calendar) {
        Calendar start = (Calendar) calendar.clone();
        start.set(Calendar.DAY_OF_WEEK, start.getFirstDayOfWeek());
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTimeInMillis();
    }

    // 获取给定日期所在周的结束时间（周日的23:59:59）
    public static long getEndOfWeek(Calendar calendar) {
        Calendar end = (Calendar) calendar.clone();
        end.set(Calendar.DAY_OF_WEEK, end.getFirstDayOfWeek());
        end.add(Calendar.WEEK_OF_YEAR, 1);
        end.add(Calendar.MILLISECOND, -1);
        return end.getTimeInMillis();
    }

    // 获取给定日期所在月的开始时间（该月1号的00:00:00）
    public static long getStartOfMonth(Calendar calendar) {
        Calendar start = (Calendar) calendar.clone();
        start.set(Calendar.DAY_OF_MONTH, 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTimeInMillis();
    }

    // 获取给定日期所在月的结束时间（该月最后一天的23:59:59）
    public static long getEndOfMonth(Calendar calendar) {
        Calendar end = (Calendar) calendar.clone();
        end.add(Calendar.MONTH, 1);
        end.set(Calendar.DAY_OF_MONTH, 1);
        end.add(Calendar.MILLISECOND, -1);
        return end.getTimeInMillis();
    }

    // 获取给定日期所在年的开始时间（该年1月1号的00:00:00）
    public static long getStartOfYear(Calendar calendar) {
        Calendar start = (Calendar) calendar.clone();
        start.set(Calendar.DAY_OF_YEAR, 1);
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        return start.getTimeInMillis();
    }

    // 获取给定日期所在年的结束时间（该年12月31号的23:59:59）
    public static long getEndOfYear(Calendar calendar) {
        Calendar end = (Calendar) calendar.clone();
        end.set(Calendar.DAY_OF_YEAR, end.getActualMaximum(Calendar.DAY_OF_YEAR));
        end.set(Calendar.HOUR_OF_DAY, 23);
        end.set(Calendar.MINUTE, 59);
        end.set(Calendar.SECOND, 59);
        end.set(Calendar.MILLISECOND, 999);
        return end.getTimeInMillis();
    }

    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JUNE, 20);

        System.out.println("给定日期: " + calendar.getTime());
        System.out.println("当天的开始时间: " + new Date(getStartOfDay(calendar)));
        System.out.println("当天的结束时间: " + new Date(getEndOfDay(calendar)));
        System.out.println("该周的开始时间: " + new Date(getStartOfWeek(calendar)));
        System.out.println("该周的结束时间: " + new Date(getEndOfWeek(calendar)));
        System.out.println("该月的开始时间: " + new Date(getStartOfMonth(calendar)));
        System.out.println("该月的结束时间: " + new Date(getEndOfMonth(calendar)));
        System.out.println("该年的开始时间: " + new Date(getStartOfYear(calendar)));
        System.out.println("该年的结束时间: " + new Date(getEndOfYear(calendar)));
    }
}
