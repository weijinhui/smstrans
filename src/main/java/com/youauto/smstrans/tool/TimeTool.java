package com.youauto.smstrans.tool;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2016/9/28.
 */
@Component
public class TimeTool {
    public static final String simpleDateTime = "yyyy-MM-dd HH:mm:ss";
    public static final String simpleDate = "yyyy-MM-dd";
    private static final int DAY_OF_MONTH[] = {
            31, 28, 31, 30, 31, 30, 31, 31, 30, 31,
            30, 31
    };

    /**
     * 获取当前时间毫秒数
     *
     * @return
     */
    public static Long getCurrentTime() {

        return System.currentTimeMillis();

    }

    /**
     * 获取当前时间秒数
     *
     * @return
     */
    public static Long getCurrentTimeInSeconds() {

        return System.currentTimeMillis() / 1000L;

    }

    /**
     * 获取当天零时刻的时间
     *
     * @return
     */
    public static Date getTodayZeroTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date zero = calendar.getTime();
        return zero;
    }

    public static Date getZeroTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date = calendar.getTime();
        return date;
    }

    public static Date getNextDayZeroTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);//+1今天的时间加一天
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        date = calendar.getTime();
        return date;
    }


    /**
     * 根据时间毫秒数获取Date对象
     *
     * @param millisSeconds
     * @return
     */
    public static Date getCurrentTimeBySeconds(String millisSeconds) {
        try {
            long millisSecond = Long.parseLong(millisSeconds);
            Date date = new Date(millisSecond);
            return date;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据时间毫秒数获取Date对象
     *
     * @param millisSeconds
     * @return
     */
    public static Date getCurrentTimeBySeconds(Long millisSeconds) {
        try {
            Date date = new Date(millisSeconds);
            return date;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static long milliSecondsBetweenDates(Date date1, Date date2) {
        long interval = date1.getTime() - date2.getTime();
        return interval;
    }


    public static long minutesBetweenDates(Date beginDate, Date endDate) {
        Long miliSeconds = milliSecondsBetweenDates(beginDate, endDate);
        return (miliSeconds / 1000L) / 60L;
    }

    /**
     * 将日期转换为yyyy-MM-dd HH:mm:ss模式
     *
     * @param date
     * @return
     */
    public static String getFormatTime(Date date) {
        DateFormat format = new SimpleDateFormat(simpleDateTime);
        return format.format(date);
    }

    /**
     * 获取距离今天指定日期间隔的日期,正数表示往未来的间隔日期，负数表示过去的间隔日期
     * 返回形式为YYYY-MM-DD格式的日期
     *
     * @return String
     * @author mmq 2014年11月13日17:38:48
     */
    public static String getDateByDifferFromToday(int differ) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, differ);
        Date targetDate = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(targetDate);
        //dateString =dateString.substring(0,10);//获取格式为YYYY-MM-DD的日期。
        return dateString;
    }

    public static String getDateByDifferFromCertainDate(String date, int differ) {
        Calendar c = Calendar.getInstance();
        c.setTime(TimeTool.str2DateDay(date));
        c.add(Calendar.DAY_OF_MONTH, differ);
        Date targetDate = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(targetDate);
        return dateString;
    }

    public static Date getDateByDifferFromCertainDate(Date date, int differ) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, differ);
        Date targetDate = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(targetDate);
        return str2DateDay(dateString);
    }

    public static Long getMillisBetween(Long differ, long time) {
        return time + differ;
    }


    public static Date str2Date(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(simpleDateTime);
        Date date = null;
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date str2DateDay(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(simpleDate);
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Date str2DateDay(String str, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            return simpleDateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String date2Str(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(simpleDateTime);
        if (date == null) {
            return null;
        }
        return simpleDateFormat.format(date);
    }
    public static String simpleDate2Str(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(simpleDate);
        if (date == null) {
            return null;
        }
        return simpleDateFormat.format(date);
    }
    public static String getDataString(String formatstr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatstr);
        return simpleDateFormat.format(getCalendar().getTime());
    }

    public static Calendar getCalendar() {
        return Calendar.getInstance();
    }

    public static String isCertainDateBetween(Date certainDate, Date startDate, Date endDate) {

        if (certainDate.after(startDate) && certainDate.before(endDate)) {
            return "0";
        } else if (certainDate.before(startDate)) {
            return "1";
        } else if (certainDate.after(endDate)) {
            return "2";
        } else {
            return "3";
        }

    }

    // 获取传入日期X年之后本月最后一天的DATE
    public Date getDateAfterXYears(Date date, int x) {
        // Calendar calendar = new GregorianCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR) + x;// yy 直接计算年数+x
        int month = calendar.get(Calendar.MONTH);// MM
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int day = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(year, month, day);
        Date newdate = calendar.getTime();
        return newdate;
    }

    // 获取传入日期X月之后本月最后一天的DATE
    public Date getLastDateOfMonAfterXMonth(Date date, int x) {
        // Calendar calendar = new GregorianCalendar();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);// yy
        int month = calendar.get(Calendar.MONTH) + x;// MM直接计算月数+x
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int day = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(year, month, day);
        Date newdate = calendar.getTime();
        return newdate;
    }
    // 获取传入日期X月之后的DATE
    public Date getDateAfterXMonth(Date date, int x) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);// yy
        int month = calendar.get(Calendar.MONTH) + x;// MM直接计算月数+x
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(year, month, day);
        Date newdate = calendar.getTime();
        return newdate;
    }
    // 获取传入日期X日之后的DATE
    public Date getDateAfterXDay(Date date, int x) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);// yy
        int month = calendar.get(Calendar.MONTH);// MM直接计算月数+x
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        int day = calendar.get(Calendar.DAY_OF_MONTH) + x;
        calendar.set(year, month, day);
        Date newdate = calendar.getTime();
        return newdate;
    }

    public static boolean isLeapYear(int year)
    {
        Calendar calendar = Calendar.getInstance();
        return ((GregorianCalendar)calendar).isLeapYear(year);
    }

    public static int getMaxDayOfMonth(int year, int month)
    {
        if(month == 1 && isLeapYear(year))
            return 29;
        else
            return DAY_OF_MONTH[month];
    }
}
