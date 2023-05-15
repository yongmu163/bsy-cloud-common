package cn.bsy.cloud.common.core.utils;


import cn.hutool.core.date.DateUtil;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * 时间格式转换类
 *
 * @Cl工具assName:DateUtils
 * @Date:2022年6月22日 下午9:30:00
 * @Author:gaoheng
 */

@UtilityClass
public class DateUtils {
    /**
     * GB/T 7408-2005 日期格式
     * 字符串
     */
    public static final String DATE_TIME_PATTERN = "yyyyMMdd";

    /**
     * GB/T 7408-2005 日期格式 只有年份
     * 字符串
     */
    public static final String DATE_TIME_YYYY = "yyyy";
    /**
     * 年月日，日期格式：yyyy-MM-dd
     * 字符串
     */
    public static final String YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd";
    /**
     * 年月日时分秒，日期格式：yyyy-MM-dd HH:mm:ss
     * 字符串
     */
    public static final String YEAR_MONTH_DAY_HOUR_MIN_SEC_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * GB/T 7408-2005 日期格式
     */
    static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
    /**
     * 年月日，日期格式：yyyy-MM-dd
     */
    static DateTimeFormatter yearMonthDayBarFormatter = DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_PATTERN);

    /**
     * Date转换为LocalDateTime
     */
    public LocalDateTime convertDateToLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     */
    public Date convertLocalDateTimeToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 获取指定日期的毫秒
     */
    public Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的毫秒
     */
    public Long getMilliByTime(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的秒
     */
    public Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 将Date转为时间戳(秒)
     */
    public Long getMilliByTime(Date startTime) {
        return DateUtils.getSecondsByTime(DateUtils.convertDateToLocalDateTime(startTime));
    }

    /**
     * 获取指定时间的指定格式
     */
    public String formatTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取年月日日期
     */
    public String formatTime(LocalDate time) {
        return time.format(DateTimeFormatter.ofPattern(YEAR_MONTH_DAY_PATTERN));
    }

    /**
     * 日期字符串转换时间 eg:yyyy-MM-dd
     */
    public LocalDate parseDateToLocalDate(String date) {
        return LocalDate.parse(date);
    }

    /**
     * 日期字符串转换时间 eg:yyyyMMdd
     */
    @SuppressWarnings("all")
    public LocalDate parseToLocalDateForGB7405(String date) {
        return LocalDate.parse(date, dateTimeFormatter);
    }

    /**
     * 获取当前时间的指定格式
     */
    public String formatNow(String pattern) {
        return formatTime(LocalDateTime.now(), pattern);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     */
    public LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
     */
    public LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field) {
        return time.minus(number, field);
    }

    /**
     * 获取两个日期的差
     */
    public long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime) {
        return getSecondsByTime(startTime) - getSecondsByTime(endTime);
    }

    /**
     * 获取两个日期的差
     *
     * @param startTime
     * @param endTime
     * @param pattern
     * @return
     */
    public long betweenTwoTime(String startTime, String endTime, String pattern) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        return getSecondsByTime(LocalDateTime.parse(startTime, df)) - getSecondsByTime(LocalDateTime.parse(endTime, df));
    }

    /**
     * 获取一天的开始时间
     */
    public LocalDateTime getDayStart() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    }

    /**
     * 获取一天的结束时间
     */
    public LocalDateTime getDayEnd() {
        return LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
    }

    /**
     * 日期转换，将LocalDateTime类型yyyy-MM-dd HH:mm:ss转为String类型的yyyy-MM-dd
     *
     * @param time 时间
     * @return String yyyy-MM-dd
     */
    public static String localDateTimeToStringYearMonthDayBar(LocalDateTime time) {
        return time.format(yearMonthDayBarFormatter);
    }

    /**
     * 将字符串格式时间转换为固定格式时间
     *
     * @param dateTime
     * @param format
     * @return
     */
    public static LocalDateTime parseDateTime(String dateTime, String format) {
        return DateUtil.toLocalDateTime(DateUtil.parse(dateTime, format));
    }
}
