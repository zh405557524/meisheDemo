package com.soul.mediapicker.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by
 *
 * @author luopeng
 * @date 2020-06-04.
 * from FuYao company
 */
public class TimeUtil {
    /**
     * 时间戳转换成标准格式09:44:44
     * @param timeStamp 时间戳
     * @return 标准格式
     */
    public static String stamp2StandardFormat(long timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        format.setTimeZone(TimeZone.getTimeZone("ETC/GMT-8"));
        return format.format(new Date(1000 * timeStamp));
    }


    public static String stamp2LyricFormat(long timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss.SS", Locale.CHINA);
        format.setTimeZone(TimeZone.getTimeZone("ETC/GMT-8"));
        return format.format(new Date(timeStamp));
    }

    public static String stamp2timeFormat(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        return format.format(new Date(milliseconds));
    }

    public static String stamp2timeFormat1(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.CHINA);
        return format.format(new Date(milliseconds));
    }

    public static String stamp2timeFormat2(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd HH:mm", Locale.CHINA);
        return format.format(new Date(milliseconds));
    }

    public static String stamp2timeFormat3(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("MM.dd HH:mm", Locale.CHINA);
        return format.format(new Date(milliseconds));
    }

    public static String stamp2timeFormat4(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.CHINA);
        return format.format(new Date(milliseconds));
    }

    public static String getTodayDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        return format.format(new Date());
    }

    public static String getDate(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        return format.format(new Date(milliseconds));
    }

    public static boolean isToday(long milliseconds) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        String now = format.format(new Date());
        String compare = format.format(new Date(milliseconds));
        return now.equals(compare);
    }

    public static String parseMinuteSecond(long seconds) {
        long minutes = (seconds % 3600) / 60;
        long second = (seconds % 3600) % 60;
        return String.format("%s:%s", StringUtil.padLeft(String.valueOf(minutes), 2, '0'), StringUtil.padLeft(String.valueOf(second), 2, '0'));
    }

    public static String parseHourMinuteSecond(long seconds, boolean shorten) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long second = (seconds % 3600) % 60;
        return (shorten && hours <= 0) ?
                String.format("%s:%s", StringUtil.padLeft(String.valueOf(minutes), 2, '0'), StringUtil.padLeft(String.valueOf(second), 2, '0')) :
                String.format("%s:%s:%s", StringUtil.padLeft(String.valueOf(hours), 2, '0'), StringUtil.padLeft(String.valueOf(minutes), 2, '0'), StringUtil.padLeft(String.valueOf(second), 2, '0'));
    }

    public static String getTimeDesc() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 0 && hour <= 8) {
            return "早上";
        }

        if (hour < 12) {
            return "上午";
        }

        if (hour < 13) {
            return "中午";
        }

        if (hour < 18) {
            return "下午";
        }

        if (hour < 19) {
            return "傍晚";
        }

        return "晚上";
    }
}
