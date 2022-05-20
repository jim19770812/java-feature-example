package org.jim.utils;

import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.lang.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;

/**
 * 链式日期处理工具，通过封装apache.common.lang3.DateUtils实现 1.链式语法 2.提供了安全的日期格式解析 3.支持向java
 * 8的新版日期API转换 例子
 * System.out.println(DT.ofNow().format_yyyy_MM_ddThh_mm_ss_gmt());
 *
 * Optional
 * <DT>date=DT.of_yyyy_MM_ddThh_mm_ss_gmt("2021-05-03T13:12:39+08:00");
 * date.ifPresent(d->{ System.out.println(d.format_yyyy_MM_ddThh_mm_ss_gmt());
 * });
 *
 */
@Accessors(chain = true)
public class DT {
    /**
     * 旧风格的日期时间格式
     */
    private static final FastDateFormat OLD_STYLE_yyyy_MM_DD_HH_mm_ss_PATTERN = FastDateFormat
            .getInstance("yyyy-MM-dd HH:mm:ss");
    /**
     * 旧风格的日期时间格式（带毫秒）
     */
    private static final FastDateFormat OLD_STYLE_yyyy_MM_DD_HH_mm_ss_SSS_PATTERN = FastDateFormat
            .getInstance("yyyy-MM-dd HH:mm:ss.SSS");
    /**
     * 旧风格的压缩日期时间格式
     */
    private static final FastDateFormat OLD_STYLE_yyyyMMDDHHmmss_PATTERN = FastDateFormat.getInstance("yyyyMMddHHmmss");
    /**
     * 旧更个的压缩日期时间格式（带毫秒）
     */
    private static final FastDateFormat OLD_STYLE_yyyyMMDDHHmmssSSS_PATTERN = FastDateFormat
            .getInstance("yyyyMMddHHmmssSSS");

    @Setter
    private Date time;
    @Setter
    private TimeZone zone = TimeZone.getTimeZone("Asia/Shanghai"); // 默认是中国时间，东8区
    @Setter
    private Locale locale = Locale.CHINA; // 默认中国

    private DT() {
        this.time = new Date();
    }

    private DT(@NonNull Date time) {
        this.time = time;
    }

    /**
     * 创建空白DT实例
     * 
     * @return
     */
    @NonNull
    public static DT ofEmpty() {
        DT result = new DT();
        return result;
    }

    /**
     * 创建当前日期的DT实例
     * 
     * @return
     */
    @NonNull
    public static DT ofNow() {
        return DT.ofDate(new Date());
    }

    /**
     * 从日期类型获取DT实例
     * 
     * @param date
     * @return
     */
    @NonNull
    public static DT ofDate(@NonNull Date date) {
        DT result = new DT();
        result.setTime(date);
        return result;
    }

    /**
     * 从LocalDate创建DT实例
     * 
     * @param ld
     * @return
     */
    @NonNull
    public static DT ofLocalDate(@NonNull LocalDate ld) {
        ZonedDateTime zonedDateTime = LocalDate.MAX.atStartOfDay(ZoneId.systemDefault());
        return ofDate(Date.from(zonedDateTime.toInstant()));
    }

    /**
     * 从LocalDateTime创建DT实例
     * 
     * @param ldt
     * @return
     */
    @NonNull
    public static DT ofLocalDateTime(@NonNull LocalDateTime ldt) {
        return ofLocalDate(ldt.toLocalDate());
    }

    /**
     * 从OffsetDateTime创建DT实例
     * 
     * @param odt
     * @return
     */
    @NonNull
    public static DT ofOffsetDateTime(@NonNull OffsetDateTime odt) {
        DT result = ofDate(Date.from(odt.toInstant()));
        return result;
    }

    /**
     * 从ZoneDateTime创建DT实例
     * 
     * @param zdt
     * @return
     */
    @NonNull
    public static DT ofZoneDateTime(@NonNull ZonedDateTime zdt) {
        DT result = ofDate(Date.from(zdt.toInstant()));
        result.setZone(TimeZone.getTimeZone(zdt.getZone()));
        return result;
    }

    public static Optional<DT> of_yyyy_MM_dd(@NonNull String source) {
        try {
            Date result = DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.parse(source);
            return Optional.of(ofDate(result));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 2021-05-03
     * 
     * @return
     */
    public String format_yyyy_MM_dd() {
        return DateFormatUtils.format(this.time, DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern(), this.zone,
                this.locale);
    }

    /**
     * 2021-05-03 10:10:10
     * 
     * @param source
     * @return
     */
    public static Optional<DT> of_yyyy_MM_dd_HH_mm_ss(@NonNull String source) {
        try {
            Date result = OLD_STYLE_yyyy_MM_DD_HH_mm_ss_PATTERN.parse(source);
            return Optional.of(ofDate(result));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 2021-05-03 10:10:10
     * 
     * @return
     */
    public String format_yyyy_MM_dd_HH_mm_ss() {
        return DateFormatUtils.format(this.time, OLD_STYLE_yyyy_MM_DD_HH_mm_ss_PATTERN.getPattern(), this.zone,
                this.locale);
    }

    /**
     * 2021-05-03T11:20:28
     * 
     * @param source
     * @return
     */
    public static Optional<DT> of_ISO8601_yyyy_MM_dd_HH_mm_ss(@NonNull String source) {
        try {
            Date result = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.parse(source);
            return Optional.of(ofDate(result));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 2021-05-03T11:20:28
     * 
     * @return
     */
    public String format_ISO8601_yyyy_MM_dd_HH_mm_ss() {
        return DateFormatUtils.format(this.time, DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern(),
                this.zone, this.locale);
    }

    public static Optional<DT> of_yyyy_MM_dd_HH_mm_ss_SSS(@NonNull String source) {
        try {
            Date result = OLD_STYLE_yyyy_MM_DD_HH_mm_ss_SSS_PATTERN.parse(source);
            return Optional.of(ofDate(result));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 2021-05-03 10:10:10.999
     * 
     * @return
     */
    public String format_yyyy_MM_dd_HH_mm_ss_SSS() {
        return DateFormatUtils.format(this.time, OLD_STYLE_yyyy_MM_DD_HH_mm_ss_SSS_PATTERN.getPattern(), this.zone,
                this.locale);
    }

    /**
     * 2021-05-03T11:20:28+08:00
     * 
     * @param source
     * @return
     */
    public static Optional<DT> of_ISO8601_yyyy_MM_dd_HH_mm_ss_gmt(@NonNull String source) {
        try {
            Date result = DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.parse(source);
            return Optional.of(ofDate(result));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 2021-05-03T11:20:28+08:00
     * 
     * @return
     */
    public String format_ISO8601_yyyy_MM_dd_HH_mm_ss_gmt() {
        return DateFormatUtils.format(this.time,
                DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.getPattern(), this.zone, this.locale);
    }

    /**
     * 20210503T112028
     * 
     * @param source
     * @return
     */
    public Optional<DT> of_yyyyMMddHHmmss(@NonNull String source) {
        try {
            Date result = OLD_STYLE_yyyyMMDDHHmmss_PATTERN.parse(source);
            return Optional.of(ofDate(result));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 20210503T112028
     * 
     * @return
     */
    public String format_yyyyMMddHHmmss() {
        return OLD_STYLE_yyyyMMDDHHmmss_PATTERN.format(this.time);
    }

    /**
     * 20210503T112028999
     * 
     * @param source
     * @return
     */
    public Optional<DT> of_yyyyMMddHHmmssSSS(@NonNull String source) {
        try {
            Date result = OLD_STYLE_yyyyMMDDHHmmssSSS_PATTERN.parse(source);
            return Optional.of(ofDate(result));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 20210503T112028999
     * 
     * @return
     */
    public String format_yyyyMMddHHmmssSSS() {
        return OLD_STYLE_yyyyMMDDHHmmssSSS_PATTERN.format(this.time);
    }

    /**
     * 11:20:28
     * 
     * @param source
     * @return
     */
    public static Optional<DT> of_HH_mm_ss(@NonNull String source) {
        try {
            Date result = DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.parse(source);
            return Optional.of(ofDate(result));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 11:20:28
     * 
     * @return
     */
    public String format_HH_mm_ss() {
        return DateFormatUtils.format(this.time, DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.getPattern(), this.zone,
                this.locale);
    }

    /**
     * T11:20:28
     * 
     * @param source
     * @return
     */
    public static Optional<DT> of_ISO8601_HH_mm_ss(@NonNull String source) {
        try {
            Date result = FastDateFormat.getInstance("'T'HH:mm:ss").parse(source);
            return Optional.of(ofDate(result));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * T11:20:28
     * 
     * @return
     */
    public String format_ISO8601_HH_mm_ss() {
        return DateFormatUtils.format(this.time, "'T'HH:mm:ss", this.zone, this.locale);
    }

    /**
     * 从日期字符串和模板创建DT实例，如果格式有误会返回null
     * 
     * @param dateString
     * @param pattern
     * @return
     */
    @Nullable
    public static DT ofPattern(@NonNull String dateString, @NonNull String pattern) {
        DT result = new DT();
        try {
            Date dt = DateUtils.parseDate(dateString, pattern);
            result.setTime(dt);
            return result;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 小时部分清零
     * 
     * @return
     */
    @NonNull
    public DT truncateHour() {
        this.time = DateUtils.truncate(this.time, Calendar.HOUR);
        return this;
    }

    /**
     * 分钟部分清零
     * 
     * @return
     */
    @NonNull
    public DT truncateMinute() {
        this.time = DateUtils.truncate(this.time, Calendar.MINUTE);
        return this;
    }

    /**
     * 分钟清零
     * 
     * @return
     */
    @NonNull
    public DT truncateSecond() {
        this.time = DateUtils.truncate(this.time, Calendar.SECOND);
        return this;
    }

    /**
     * 毫秒清零
     * 
     * @return
     */
    @NonNull
    public DT truncateMillisecond() {
        this.time = DateUtils.truncate(this.time, Calendar.MILLISECOND);
        return this;
    }

    /**
     * 时间部分清零（小时，分钟，秒，毫秒）
     * 
     * @return
     */
    @NonNull
    public DT truncateTimes() {
        return this.truncateHour().truncateMinute().truncateSecond().truncateMillisecond();
    }

    /**
     * 日期部分清零（年份，月，日）
     * 
     * @return
     */
    @NonNull
    public DT truncateDates() {
        return this.truncateYear().truncateMonth().truncateDay();
    }

    @NonNull
    public DT truncateYear() {
        this.time = DateUtils.truncate(this.time, Calendar.YEAR);
        return this;
    }

    @NonNull
    public DT truncateMonth() {
        this.time = DateUtils.truncate(this.time, Calendar.MONTH);
        return this;
    }

    @NonNull
    public DT truncateDay() {
        this.time = DateUtils.truncate(this.time, Calendar.DATE);
        return this;
    }

    @NonNull
    public DT addYears(@NonNull int years) {
        this.time = DateUtils.addYears(this.time, years);
        return this;

    }

    @NonNull
    public DT addMonths(@NonNull int months) {
        this.time = DateUtils.addMonths(this.time, months);
        return this;
    }

    @NonNull
    public DT addDays(@NonNull int days) {
        this.time = DateUtils.addDays(this.time, days);
        return this;
    }

    @NonNull
    public DT addHours(@NonNull int hours) {
        this.time = DateUtils.addHours(this.time, hours);
        return this;
    }

    @NonNull
    public DT addMinutes(@NonNull int minutes) {
        this.time = DateUtils.addMinutes(this.time, minutes);
        return this;
    }

    @NonNull
    public DT addSeconds(@NonNull int seconds) {
        this.time = DateUtils.addSeconds(this.time, seconds);
        return this;
    }

    @NonNull
    public DT addMilliseconds(@NonNull int milliseconds) {
        this.time = DateUtils.addMilliseconds(this.time, milliseconds);
        return this;
    }

    /**
     * 向上舍入年份 2018-11-23 15:59:29 - > 2019-01-01 00:00:00，截取到年，年向上取整（即+1）
     * 
     * @return
     */
    @NonNull
    public DT ceilingYear() {
        this.time = DateUtils.ceiling(this.time, Calendar.YEAR);
        return this;
    }

    /**
     * 向上舍入月份 2018-11-23 15:59:29 - > 2018-12-01
     * 00:00:00，截取到月，月向上取整（即+1）；如果是12月的话，月进1变成01，年进1
     * 
     * @return
     */
    @NonNull
    public DT ceilingMonth() {
        this.time = DateUtils.ceiling(this.time, Calendar.MONTH);
        return this;
    }

    /**
     * 向上舍入日期 2018-11-23 15:59:29 - > 2018-11-24
     * 00:00:00，截取到日，日向上取整（即+1）；如果是30（或28/31）的话，日进1变成01，月进1
     * 
     * @return
     */
    @NonNull
    public DT ceilingDay() {
        this.time = DateUtils.ceiling(this.time, Calendar.DATE);
        return this;
    }

    /**
     * 向上舍入小时 2018-11-23 15:59:29 - > 2018-11-23
     * 12:00:00，截取到时，时向上取整（即+1）；如果是23点的话，时进1变成00，日进1
     * 
     * @return
     */
    @NonNull
    public DT ceilingHour() {
        this.time = DateUtils.ceiling(this.time, Calendar.HOUR);
        return this;
    }

    /**
     * 向上舍入分钟 2018-11-23 15:59:29 - > 2018-11-23
     * 11:33:00，截取到分，分向上取整（即+1）；如果是59分的话，分进1变成00，时进1
     * 
     * @return
     */
    @NonNull
    public DT ceilingMinute() {
        this.time = DateUtils.ceiling(this.time, Calendar.MINUTE);
        return this;
    }

    /**
     * 向上舍入秒 2018-11-23 15:59:29 - > 2018-11-23 11:32:13，截取到分，分向上取整（即+1）
     * 
     * @return
     */
    @NonNull
    public DT ceilingSecond() {
        this.time = DateUtils.ceiling(this.time, Calendar.SECOND);
        return this;
    }

    /**
     * 向上舍入毫秒
     * 
     * @return
     */
    @NonNull
    public DT ceilingMilliseconds() {
        this.time = DateUtils.ceiling(this.time, Calendar.MILLISECOND);
        return this;
    }

    /**
     * 四舍五入年份
     * 
     * @return
     */
    @NonNull
    public DT roundYear() {
        this.time = DateUtils.round(this.time, Calendar.YEAR);
        return this;
    }

    /**
     * 四舍五入年份
     * 
     * @return
     */
    @NonNull
    public DT roundMonth() {
        this.time = DateUtils.round(this.time, Calendar.MONTH);
        return this;
    }

    /**
     * 四舍五入年份
     * 
     * @return
     */
    @NonNull
    public DT roundDay() {
        this.time = DateUtils.round(this.time, Calendar.DATE);
        return this;
    }

    /**
     * 四舍五入年份
     * 
     * @return
     */
    @NonNull
    public DT roundHour() {
        this.time = DateUtils.round(this.time, Calendar.HOUR);
        return this;
    }

    /**
     * 四舍五入年份
     * 
     * @return
     */
    @NonNull
    public DT roundMinute() {
        this.time = DateUtils.round(this.time, Calendar.MINUTE);
        return this;
    }

    /**
     * 四舍五入年份
     * 
     * @return
     */
    @NonNull
    public DT roundSecond() {
        this.time = DateUtils.round(this.time, Calendar.SECOND);
        return this;
    }

    /**
     * 四舍五入年份
     * 
     * @return
     */
    @NonNull
    public DT roundMillisecond() {
        this.time = DateUtils.round(this.time, Calendar.MILLISECOND);
        return this;
    }

    /**
     * 修改年份
     * 
     * @param year
     * @return
     */
    @NonNull
    public DT setYear(@NonNull int year) {
        this.time = DateUtils.setYears(this.time, year);
        return this;
    }

    /**
     * 修改月份
     * 
     * @param month
     * @return
     */
    @NonNull
    public DT setMonth(@NonNull int month) {
        this.time = DateUtils.setMonths(this.time, month);
        return this;
    }

    /**
     * 修改日期
     * 
     * @param day
     * @return
     */
    @NonNull
    public DT setDay(@NonNull Integer day) {
        this.time = DateUtils.setDays(this.time, day);
        return this;
    }

    /**
     * 修改小时
     * 
     * @param hour
     * @return
     */
    @NonNull
    public DT setHour(@NonNull int hour) {
        this.time = DateUtils.setHours(this.time, hour);
        return this;
    }

    /**
     * 修改分钟
     * 
     * @param minute
     * @return
     */
    @NonNull
    public DT setMinute(@NonNull int minute) {
        this.time = DateUtils.setMinutes(this.time, minute);
        return this;
    }

    /**
     * 修改秒数
     * 
     * @param second
     * @return
     */
    @NonNull
    public DT setSecond(@NonNull int second) {
        this.time = DateUtils.setSeconds(this.time, second);
        return this;
    }

    /**
     * 修改毫秒数
     * 
     * @param millsecond
     * @return
     */
    @NonNull
    public DT setMillsecond(@NonNull int millsecond) {
        this.time = DateUtils.setMilliseconds(this.time, millsecond);
        return this;
    }

    /**
     * 获取当前时间距指定时间的天数
     * 
     * @param date
     * @return
     */
    public long betweenDays(@NonNull Date date) {
        long val1 = DateUtils.getFragmentInDays(this.time, Calendar.YEAR);
        long val2 = DateUtils.getFragmentInDays(date, Calendar.YEAR);
        return val2 - val1;
    }

    /**
     * 获取当前时间距指定时间的小时数
     * 
     * @param date
     * @return
     */
    public long betweenHours(@NonNull Date date) {
        long val1 = DateUtils.getFragmentInHours(this.time, Calendar.YEAR);
        long val2 = DateUtils.getFragmentInHours(date, Calendar.YEAR);
        return val2 - val1;
    }

    /**
     * 获取当前时间距指定时间的分钟
     * 
     * @param date
     * @return
     */
    public long betweenMinutes(@NonNull Date date) {
        long val1 = DateUtils.getFragmentInMinutes(this.time, Calendar.YEAR);
        long val2 = DateUtils.getFragmentInMinutes(date, Calendar.YEAR);
        return val2 - val1;
    }

    /**
     * 获取当前时间距指定时间的秒数
     * 
     * @param date
     * @return
     */
    public long betweenSeconds(@NonNull Date date) {
        long val1 = DateUtils.getFragmentInSeconds(this.time, Calendar.YEAR);
        long val2 = DateUtils.getFragmentInSeconds(date, Calendar.YEAR);
        return val2 - val1;
    }

    /**
     * 获取当前时间距指定时间的毫秒数
     * 
     * @param date
     * @return
     */
    public long betweenMillsecond(@NonNull Date date) {
        long val1 = DateUtils.getFragmentInMilliseconds(this.time, Calendar.YEAR);
        long val2 = DateUtils.getFragmentInMilliseconds(date, Calendar.YEAR);
        return val2 - val1;
    }

    /**
     * 获取当前时间的Calendar实例
     * 
     * @return
     */
    @NonNull
    public Calendar calendar() {
        return DateUtils.toCalendar(this.time);
    }

    public int year() {
        return this.calendar().get(Calendar.YEAR);
    }

    public int month() {
        return this.calendar().get(Calendar.MONDAY) + 1;
    }

    public int day() {
        return this.calendar().get(Calendar.DAY_OF_MONTH);
    }

    public int hour() {
        return this.calendar().get(Calendar.HOUR);
    }

    public int minute() {
        return this.calendar().get(Calendar.MINUTE);
    }

    public int second() {
        return this.calendar().get(Calendar.SECOND);
    }

    public int millsecond() {
        return this.calendar().get(Calendar.MILLISECOND);
    }

    /**
     * 判断时间是否为空,如果毫秒数为0则也当做空
     *
     * @return
     */
    @NonNull
    public boolean isBlank() {
        return null == this.time || this.time.getTime() == 0 || this.format_yyyy_MM_dd().equalsIgnoreCase("1970-01-01");
    }

    /**
     * 根据模板格式化日期时间
     * 
     * @param pattern
     * @return
     */
    @NonNull
    public String format(@NonNull String pattern) {
        String result = new SimpleDateFormat(pattern).format(this.time);
        return result;
    }

    @Override
    public String toString() {
        return this.format_ISO8601_yyyy_MM_dd_HH_mm_ss();
    }

    @NonNull
    public Date date() {
        return this.time;
    }

    /**
     * 生成日期字符串，不含时间部分
     * 
     * @return
     */
    @NonNull
    public String dateStr() {
        return this.format_yyyy_MM_dd();
    }

    /**
     * 生成旧格式日期时间字符串
     * 
     * @return
     */
    @NonNull
    public String dateTimeStr() {
        return this.format_yyyy_MM_dd_HH_mm_ss();
    }

    /**
     * 生成时间字符串，旧格式，不含毫秒
     * 
     * @return
     */
    @NonNull
    public String timeStr() {
        return this.format_HH_mm_ss();
    }

    /**
     * 生成LocalDate实例
     * 
     * @param zoneId
     * @return
     */
    @NonNull
    public LocalDate localDate(@NonNull ZoneId zoneId) {
        return this.time.toInstant().atZone(zoneId).toLocalDate();
    }

    /**
     * 生成LocalDate实例
     * 
     * @return
     */
    @NonNull
    public LocalDate localDate() {
        return this.localDate(ZoneId.systemDefault());
    }

    /**
     * 生成LocalDateTime实例
     * 
     * @param zoneId
     * @return
     */
    @NonNull
    public LocalDateTime localDateTime(@NonNull ZoneId zoneId) {
        return this.time.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * 生成LocalDateTime实例
     * 
     * @return
     */
    @NonNull
    public LocalDateTime localDateTime() {
        return this.localDateTime(ZoneId.systemDefault());
    }

    /**
     * 生成时区日期时间
     * 
     * @param zoneId
     * @return
     */
    @NonNull
    public ZonedDateTime zonedDateTime(@NonNull ZoneId zoneId) {
        return ZonedDateTime.ofInstant(this.time.toInstant(), zoneId);
    }

    /**
     * 生成时区日期时间
     * 
     * @return
     */
    @NonNull
    public ZonedDateTime zonedDateTime() {
        return this.zonedDateTime(this.zone.toZoneId());
    }

    /**
     * 生成时间戳
     * 
     * @return
     */
    @NonNull
    public Instant instant() {
        return this.time.toInstant();
    }

    /**
     * 生成毫秒
     * 
     * @return
     */
    @NonNull
    public long epochSecond() {
        return this.time.getTime();
    }

    public static void main(String[] args) {
        System.out.println(DT.ofNow().betweenDays(DT.ofNow().addDays(1).date()));
        System.out.println(DT.ofNow().betweenHours(DT.ofNow().addDays(1).addHours(1).date()));
        System.out.println(DT.ofNow().betweenMinutes(DT.ofNow().addDays(1).addHours(1).addMinutes(1).date()));
        System.out.println(DT.ofNow()
                .betweenMillsecond(DT.ofNow().addDays(1).addHours(1).addMinutes(1).addMilliseconds(1).date()));
        System.out.println(DT.ofNow().date());
        System.out.println(DT.ofNow().localDate());
        System.out.println(DT.ofNow().localDateTime());
        System.out.println(DT.ofNow());
        DT dt = DT.ofNow();
        System.out.println("year: " + dt.year());
        System.out.println("month: " + dt.month());
        System.out.println("day: " + dt.day());
        System.out.println("hour: " + dt.hour());
        System.out.println("minute: " + dt.minute());
        System.out.println("second: " + dt.second());
        System.out.println("millisecond: " + dt.millsecond());
        System.out.println(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()));
        ;
        System.out.println(DT.ofNow().format_ISO8601_yyyy_MM_dd_HH_mm_ss_gmt());
        System.out.println(DT.ofNow().format_yyyy_MM_dd());
        System.out.println(DT.ofNow().format_ISO8601_yyyy_MM_dd_HH_mm_ss());
        System.out.println(DT.ofNow().format_ISO8601_yyyy_MM_dd_HH_mm_ss_gmt());
        System.out.println(DT.ofNow().format_yyyy_MM_dd_HH_mm_ss_SSS());
        Optional<DT> date = DT.of_ISO8601_yyyy_MM_dd_HH_mm_ss_gmt("2021-05-03T13:12:39+08:00");
        date.ifPresent(d -> {
            System.out.println(d.format_ISO8601_yyyy_MM_dd_HH_mm_ss_gmt());
        });
    }
}
