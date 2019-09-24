package org.javatribe.lottery.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 处理时间工具类
 *
 * @author jimzising
 */
public class DateTimeUtil {

    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    /**
     * 获取当前时间字符串
     *
     * @return currentTime
     */
    public static Long getCurrentTimeToLong() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间字符串
     *
     * @return currentTime
     */
    public static Date getCurrentTimeToDate() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 获取当前时间字符串
     *
     * @return currentTime
     */
    public static String getCurrentTimeToStr() {
        return simpleDateFormat.format(new Date());
    }

    /**
     * 格式化时间字符串
     *
     * @return
     */
    public static Date formatDateTimeToDate(String timeStr) {
        return new Date(formatDateTimeToLong(timeStr));
    }

    /**
     * 时间转为long
     * @param datetime
     * @return
     */
    public static long formatDateTimeToLong(String datetime) {
        long time = 0;
        try {
            time = simpleDateFormat.parse(datetime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }
}
