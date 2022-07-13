package com.mg.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author WANGHT ON 2021年8月31日 16:47:33
 */
@Slf4j
public class DateUtils implements Serializable {

    /**
     * 今日日期
     *
     * @return
     */
    public static Date todayDate() {
        return getBeforeDayDate(0);
    }

    /**
     * 今天
     *
     * @param format 格式
     * @return
     */
    public static String today(String format) {
        if (StringUtils.isBlank(format)) {
            format = Constants.DATE_FORMAT;
        }
        return toString(todayDate(), format);
    }

    /**
     * 今天(yyyy-MM-dd)
     *
     * @return
     */
    public static String today() {
        return today(Constants.DATE_FORMAT);
    }

    /**
     * 昨日日期
     *
     * @return
     */
    public static Date yesterdayDate() {
        return getBeforeDayDate(1);
    }

    /**
     * 昨天
     *
     * @param format 格式
     * @return
     */
    public static String yesterday(String format) {
        if (StringUtils.isBlank(format)) {
            format = Constants.DATE_FORMAT;
        }
        return toString(yesterdayDate(), format);
    }

    /**
     * 昨天(yyyy-MM-dd)
     *
     * @return
     */
    public static String yesterday() {
        return yesterday(Constants.DATE_FORMAT);
    }

    /**
     * 转字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String toString(Date date, String format) {
        return DateUtil.format(date, format);
    }

    /**
     * 转字符串
     *
     * @param time
     * @param format
     * @return
     */
    public static String toString(long time, String format) {
        return DateUtil.format(new Date(time), format);
    }



    // 获得某天最大时间 2020-02-19 23:59:59
    public static Date getEndOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
        return Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    // 获得某天最小时间 2020-02-17 00:00:00
    public static Date getStartOfDay(Date date) {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
        LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
        return Date.from(startOfDay.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取距离当前日期前day天数的日期
     * @param day
     * @return
     */
    public static Date getBeforeDayDate(int day){
        return DateTime.from(Instant.from(
                LocalDate.now().minusDays(day).atStartOfDay(ZoneId.systemDefault())));

    }

    /**
     * 获取距离当前日期后day天数的日期
     * @param day
     * @return
     */
    public static Date getAfterDayDate(int day){
        return DateTime.from(Instant.from(
                LocalDate.now().plusDays(day).atStartOfDay(ZoneId.systemDefault())));

    }

    /**
     * 获取距离当前日期后day天数的日期
     * @param day
     * @return
     */
    public static String getAfterDay(int day){
        return toString(getAfterDayDate(day), Constants.DATE_FORMAT);

    }

    /**
     * 获取距离指定日期后day天数的日期
     * @param day
     * @return
     */
    public static String getAfterDayForDate(String date, int day){
        Date after = DateTime.from(Instant.from(
                LocalDate.parse(date).plusDays(day).atStartOfDay(ZoneId.systemDefault())));
        return toString(after, Constants.DATE_FORMAT);
    }

    /**
     * 转换为日期
     *
     * @param dateValue
     * @param format
     * @return
     */
    public static Date toDate(String dateValue, String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateValue);
        } catch (ParseException e) {
            log.error("===========toDate-data:[{}]-format[{}]-error:", dateValue, format, e);
        }
        return null;
    }

    /**
     * 获取本月开始日期
     * @return String
     * **/
    public static Date getMonthStart(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date time=cal.getTime();
        return toDate(new SimpleDateFormat("yyyy-MM-dd").format(time)+" 00:00:00","yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取本月最后一天
     * @return String
     * **/
    public static Date getMonthEnd(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date time=cal.getTime();
        return toDate(new SimpleDateFormat("yyyy-MM-dd").format(time)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取本周的第一天
     * @return String
     * **/
    public static Date getWeekStart(){
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_MONTH, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        Date time=cal.getTime();
        return toDate(new SimpleDateFormat("yyyy-MM-dd").format(time)+" 00:00:00","yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取本周的最后一天
     * @return String
     * **/
    public static Date getWeekEnd(){
        Calendar cal=Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
        cal.add(Calendar.DAY_OF_WEEK, 1);
        Date time=cal.getTime();
        return toDate(new SimpleDateFormat("yyyy-MM-dd").format(time)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取本年的第一天
     * @return String
     * **/
    public static Date getYearStart(){
        return toDate(new SimpleDateFormat("yyyy").format(new Date())+"-01-01 00:00:00","yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取本年的最后一天
     * @return String
     * **/
    public static Date getYearEnd(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,calendar.getActualMaximum(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date currYearLast = calendar.getTime();
        return toDate(new SimpleDateFormat("yyyy-MM-dd").format(currYearLast)+" 23:59:59","yyyy-MM-dd HH:mm:ss");
    }
}
