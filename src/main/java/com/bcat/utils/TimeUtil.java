package com.bcat.utils;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    /**
     * 默认时间解析格式 "yyyy-MM-dd HH:mm:ss"。
     */
    public static final String DATE_PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认"日"时间解析格式 "HH:mm:ss"。
     */
    public static final String DATE_PATTERN_DAY = "HH:mm:ss";

    public static Long disTimeOfSec;

    public static Long disTimeOfMin;

    public static Long disTimeOfHour;

    public static Long disTimeOfDay;

    public static String getDayTime(){
        DateFormat timeFormat = new SimpleDateFormat(DATE_PATTERN_DAY);
        return timeFormat.format(new Date());
    }

     /**
      * 字符串时间转Date
      * @param date String时间
      * @param pattern 转换时间格式表达式
      * */
    public static Date toDate(String date, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 传入Date类型时间，判断是否在一个小时之内。
     */
    public static boolean isInOneHour(Date data){
        getDistanceTime(new Date().getTime(), data.getTime());
        return disTimeOfHour < 1 && disTimeOfMin <= 60;
    }

    public static boolean isInOneHour(String data){
        Date alarmDate = TimeUtil.toDate(data, TimeUtil.DATE_PATTERN_DEFAULT);
        return isInOneHour(alarmDate);
    }


    /**
     * 传入Date类型时间，判断是否超过指定min分钟。
     */
    public static boolean isOverMinutes(Date data, int min){
        getDistanceTime(new Date().getTime(), data.getTime());
        return disTimeOfDay < 1 && disTimeOfHour < 2 && disTimeOfMin > min;
    }

    public static boolean isOverMinutes(String data, int min){
        Date alarmDate = TimeUtil.toDate(data, TimeUtil.DATE_PATTERN_DEFAULT);
        return isOverMinutes(alarmDate, min);
    }

    /**
     * 返回给传入字符串时间中的小时值。
     */
    public static String getHourByDate(String dateTime){
        Date Date = TimeUtil.toDate(dateTime, DATE_PATTERN_DEFAULT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date);
        return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
    }

    /**
     * 比较两个timestamp的差值,并赋值给全局变量
     */
    private static void getDistanceTime(long time1, long time2){
        long diff;
        if (time1 > time2) {
            diff = time1 - time2;
        } else{
            diff = time2 - time1;
        }
        disTimeOfDay = diff / (24 * 60 * 60 * 1000);
        disTimeOfHour = (diff / (60 * 60 * 1000) - disTimeOfDay * 24);
        disTimeOfMin = ((diff / (60 * 1000)) - disTimeOfDay * 24 * 60 - disTimeOfHour * 60);
        disTimeOfSec = (diff / 1000 - disTimeOfDay * 24 * 60 * 60 - disTimeOfHour * 60 * 60 - disTimeOfMin * 60);
    }

}
