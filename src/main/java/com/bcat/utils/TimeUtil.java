package com.bcat.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public static String getDayTime(){
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        return timeFormat.format(new Date());
    }

    // 字符串时间转Date类型
    public static Date toDate(String date, String pattern) {

        DateFormat dateFormat = new SimpleDateFormat(pattern);

        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isInOneHour(Date data){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        if(calendar.get(Calendar.HOUR_OF_DAY) ==
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY)){
            return true;
        }
        if(Calendar.getInstance().get(Calendar.HOUR_OF_DAY) -
                calendar.get(Calendar.HOUR_OF_DAY) == 1){
            return true;
        }
        return false;


        /*long compareValue = (new Date().getTime() - alarmDate.getTime()) /
                (60 * 60 * 1000) % 24;

        return compareValue < 1L;*/
    }

    public static boolean isOverTenMinutes(Date data){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(data);
        if (Calendar.getInstance().get(Calendar.MINUTE) - calendar.get(Calendar.MINUTE) > 10){
            return true;
        }
        return false;
    }

    public static String getHourByDate(String dateTime){
        Date Date = TimeUtil.toDate(dateTime, "yyyy-MM-dd HH:ss:mm");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date);
        return String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
    }
}
