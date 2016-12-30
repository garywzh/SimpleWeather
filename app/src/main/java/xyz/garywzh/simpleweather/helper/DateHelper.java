package xyz.garywzh.simpleweather.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by garywzh on 2016/7/19.
 */
public class DateHelper {

    public static String getDayOfWeek(int timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("E");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(new Date(timeStamp * 1000L));
    }

    public static String getDailyDate(int timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("MMMd E");
        format.setTimeZone(TimeZone.getDefault());
        return format.format(new Date(timeStamp * 1000L));
    }
}
