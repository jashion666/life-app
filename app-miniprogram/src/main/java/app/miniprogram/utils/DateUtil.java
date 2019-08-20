package app.miniprogram.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class DateUtil {

    /**
     * 日期格式化.
     */
    public static String format(LocalDate date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 日期格式化.
     */
    public static String format(LocalDateTime date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }

    /**
     * 文字格式化
     */
    public static LocalDate parse(String strDate, String format) {
        return LocalDate.parse(strDate, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 文字格式化
     */
    public static LocalDateTime parseDateTime(String strDate, String format) {
        return LocalDateTime.parse(strDate, DateTimeFormatter.ofPattern(format));
    }

    /**
     * 文字格式化
     */
    public static String formatTargetStr(String strDate, String format, String targetForamt) {
        return format(parse(strDate, format), targetForamt);
    }

    /**
     * 两个日期的天数差.
     *
     * @param startDate 开始日
     * @param endDate   结束日
     * @return 日数
     */
    public static long diffDay(LocalDate startDate, LocalDate endDate) {
        return endDate.toEpochDay() - startDate.toEpochDay();
    }

    /**
     * 两个日期的月份差
     *
     * @param startDate 开始日
     * @param endDate   结束日
     */
    public static long diffMonth(LocalDate startDate, LocalDate endDate) {
        Period periodTime = Period.between(startDate, endDate);
        return periodTime.getMonths() + (periodTime.getDays() > 0 ? 1 : 0);
    }

    /**
     * 获取该月第一天.
     */
    public static LocalDate getFirstDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * 获取该月最后一天
     */
    public static LocalDate getLastDayOfMonth(LocalDate date) {
        return date.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取该月的天数.
     */
    public static int getLengthOfMonth(LocalDate date) {
        return date.lengthOfMonth();
    }

    /**
     * 判断是否为日期
     */
    public static boolean isDate(final String date, String format) {
        try {
            return date.equals(format(parse(date, format), format));
        } catch (Exception e) {
            return false;
        }
    }


}
