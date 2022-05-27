package org.jim.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * 链式日期处理工具，通过封装apache.common.lang3.DateUtils实现
 * 1.链式语法
 * 2.提供了安全的日期格式解析
 * 3.支持向java 8的新版日期API转换
 * 例子
 * System.out.println(DT.ofNow().format_yyyy_MM_ddThh_mm_ss_ZZ());
 *
 * Optional<DT> date=DT.of_yyyy_MM_ddThh_mm_ss_gmt("2021-05-03T13:12:39+08:00");
 * date.ifPresent(d->{
 *    System.out.println(d.format_yyyy_MM_ddThh_mm_ss_ZZ());
 * });
 *
 */
@Accessors(chain = true)
public class DT implements Cloneable, Comparable {
    private final static String ZONE_OFFSET_GMT8="+8";
    /**
     * 旧风格的日期时间格式
     */
    private static final FastDateFormat OLD_STYLE_yyyy_MM_dd_HH_mm_ss_PATTERN =FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    /**
     * 旧风格的日期时间格式（带毫秒）
     */
    private static final FastDateFormat OLD_STYLE_yyyy_MM_dd_HH_mm_ss_SSS_PATTERN =FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");
    /**
     * 完整的带时区的日期时间（带毫秒）
     */
    private static final FastDateFormat STYLE_yyyy_MM_ddTHH_mm_ss_SSS_ZZ_PATTERN =FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    /**
     * 旧风格的压缩日期时间格式
     */
    private static final FastDateFormat OLD_STYLE_yyyyMMddHHmmss_PATTERN =FastDateFormat.getInstance("yyyyMMddHHmmss");
    /**
     * 旧更个的压缩日期时间格式（带毫秒）
     */
    private static final FastDateFormat OLD_STYLE_yyyyMMddHHmmssSSS_PATTERN =FastDateFormat.getInstance("yyyyMMddHHmmssSSS");

    @Setter
    private Date time;
    @Setter
    private TimeZone zone=TimeZone.getTimeZone("Asia/Shanghai"); //默认是中国时间，东8区
    @Setter
    private Locale locale=Locale.CHINA; //默认中国

    private DT() {
        this.time =new Date();
    }

    private DT(@NonNull Date time) {
        this.time = time;
    }

    /**
     * 创建空白DT实例
     * @return
     */
    @NonNull
    public static DT ofEmpty(){
        DT result=new DT();
        return result;
    }

    /**
     * 创建DT实例的的副本
     * @param dt
     * @return
     */
    public static DT ofDT(@NonNull DT dt){
        return DT.ofEpochSecond(dt.epochSecond());
    }

    /**
     * 创建当前日期的DT实例
     * @return
     */
    @NonNull
    public static DT ofNow(){
        return DT.ofDate(new Date());
    }

    /**
     * 从日期类型获取DT实例
     * @param date
     * @return
     */
    @NonNull
    public static DT ofDate(@NonNull Date date){
        DT result=new DT();
        result.setTime(date);
        return result;
    }

    /**
     * 从LocalDate创建DT实例
     * @param ld
     * @return
     */
    @NonNull
    public static DT ofLocalDate(@NonNull LocalDate ld){
        return of_yyyy_MM_dd(ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());
    }

    /**
     * 从LocalDateTime创建DT实例
     * @param ldt
     * @return
     */
    @NonNull
    public static DT ofLocalDateTime(@NonNull LocalDateTime ldt){
        long epochSecond=ldt.toEpochSecond(ZoneOffset.of(ZONE_OFFSET_GMT8));
        return ofEpochSecond(epochSecond);
    }

    /**
     * 从OffsetDateTime创建DT实例
     * @param odt
     * @return
     */
    @NonNull
    public static DT ofOffsetDateTime(@NonNull OffsetDateTime odt){
        DT result=ofDate(Date.from(odt.toInstant()));
        return result;
    }

    /**
     * 从ZoneDateTime创建DT实例
     * @param zdt
     * @return
     */
    @NonNull
    public static DT ofZoneDateTime(@NonNull ZonedDateTime zdt){
        DT result = ofDate(Date.from(zdt.toInstant()));
        result.setZone(TimeZone.getTimeZone(zdt.getZone()));
        return result;
    }

    /**
     * 当前日期设置成当日的开始时间
     * @return
     */
    public DT setBeginOfDay(){
        Calendar calTemp = Calendar.getInstance();
        calTemp.setTime(this.time);
        Calendar calendar = Calendar.getInstance();
        calendar.set(calTemp.get(Calendar.YEAR), calTemp.get(Calendar.MONTH), calTemp.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        this.time= calendar.getTime();
        return this;
    }

    /**
     * 当前日期设置成当日的结束时间
     * @return
     */
    public DT setEndOfDay(){
        Calendar calTemp = Calendar.getInstance();
        calTemp.setTime(this.time);
        Calendar calendar = Calendar.getInstance();
        calendar.set(calTemp.get(Calendar.YEAR), calTemp.get(Calendar.MONTH), calTemp.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        this.time=calendar.getTime();
        return this;
    }

    public static Optional<DT> of_yyyy_MM_dd(@NonNull String source) {
        try {
            Date result= DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.parse(source);
            return Optional.of(ofDate(result));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * yyyyMMdd格式的日期转换成DT对象
     * 20210503
     * @param source
     * @return
     */
    public static Optional<DT> of_yyyyMMdd(@NonNull Integer source) {
        try {
            DT result=ofPattern(Integer.toString(source), "yyyyMMdd");
            return Optional.of(result);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 把yyyy-MM-dd格式的日期转换曾DT对象
     * 2021-05-03
     * @return
     */
    public String format_yyyy_MM_dd(){
        return DateFormatUtils.format(this.time, DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.getPattern(), this.zone, this.locale);
    }

    /**
     * 2021-05-03 10:10:10
     * @param source
     * @return
     */
    public static Optional<DT> of_yyyy_MM_dd_HH_mm_ss(@NonNull String source){
        try {
            Date result= OLD_STYLE_yyyy_MM_dd_HH_mm_ss_PATTERN.parse(source);
            return Optional.of(ofDate(result));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 根据年月日时分秒创建DT
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static DT of_yyyy_mm_dd_HH_mm_ss(@NonNull Integer year, @NonNull Integer month, @NonNull Integer day, @NonNull Integer hour, @NonNull Integer minute, @NonNull Integer second){
        LocalDateTime ldt=LocalDateTime.of(year, month, day, hour, minute, second);
        return ofLocalDateTime(ldt);
    }

    /**
     * 2021-05-03 10:10:10
     * @return
     */
    public String format_yyyy_MM_dd_HH_mm_ss(){
        return DateFormatUtils.format(this.time, OLD_STYLE_yyyy_MM_dd_HH_mm_ss_PATTERN.getPattern(), this.zone, this.locale);
    }

    /**
     * 2021-05-03T11:20:28
     * @param source
     * @return
     */
    public static Optional<DT> of_ISO8601_yyyy_MM_ddTHH_mm_ss(@NonNull String source){
        try{
            Date result=DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.parse(source);
            return Optional.of(ofDate(result));
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 根据unix时间戳对象创建DT
     * @param instant
     * @return
     */
    public static DT ofInstant(@NonNull Instant instant){
        return ofEpochSecond(instant.toEpochMilli());
    }

    /**
     * 根据unix时间戳创建DT
     */
    public static DT ofEpochSecond(@NonNull Long epochSecond){
        Date dt=new Date();
        dt.setTime(epochSecond);
        return DT.ofDate(dt);
    }

    /**
     * 根据年月日创建DT
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static DT of_yyyy_MM_dd(@NonNull Integer year, @NonNull Integer month, @NonNull Integer day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Math.max(month-1, 0), day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return ofDate(calendar.getTime());
    }

    /**
     * 2021-05-03T11:20:28
     * @return
     */
    public String format_ISO8601_yyyy_MM_ddTHH_mm_ss(){
        return DateFormatUtils.format(this.time, DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern(), this.zone, this.locale);
    }

    /**
     * 2021-05-03T11:20:28.999
     * @return
     */
    public String format_ISO8601_yyyy_MM_ddTHH_mm_ss_SSS(){
        return DateFormatUtils.format(this.time, OLD_STYLE_yyyy_MM_dd_HH_mm_ss_SSS_PATTERN.getPattern(), this.zone, this.locale);
    }

    public static Optional<DT> of_yyyy_MM_dd_HH_mm_ss_SSS(@NonNull String source){
        try {
            Date result= OLD_STYLE_yyyy_MM_dd_HH_mm_ss_SSS_PATTERN.parse(source);
            return Optional.of(ofDate(result));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 根据年月日时分秒毫秒创建DT
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @param millsecond
     * @return
     */
    public static DT of_yyyy_mm_dd_HH_mm_ss_SSS(@NonNull Integer year, @NonNull Integer month, @NonNull Integer day, @NonNull Integer hour, @NonNull Integer minute, @NonNull Integer second, @NonNull Integer millsecond){
        LocalDateTime ldt=LocalDateTime.of(year, month, day, hour, minute, second, millsecond);
        return ofLocalDateTime(ldt);
    }

    /**
     * 2021-05-03 10:10:10.999
     * @return
     */
    public String format_yyyy_MM_dd_HH_mm_ss_SSS(){
        return DateFormatUtils.format(this.time, OLD_STYLE_yyyy_MM_dd_HH_mm_ss_SSS_PATTERN.getPattern(), this.zone, this.locale);
    }

    /**
     * 2021-05-03T11:20:28+08:00
     * @param source
     * @return
     */
    public static Optional<DT> of_ISO8601_yyyy_MM_ddTHH_mm_ss_ZZ(@NonNull String source){
        try{
            Date result=DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.parse(source);
            return Optional.of(ofDate(result));
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 2021-05-03T11:20:28+08:00
     * @return
     */
    public String format_ISO8601_yyyy_MM_ddTHH_mm_ss_SSS_ZZ(){
//        return DateFormatUtils.format(this.time, DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.getPattern(), this.zone, this.locale);
        return DateFormatUtils.format(this.time, STYLE_yyyy_MM_ddTHH_mm_ss_SSS_ZZ_PATTERN.getPattern(), this.zone, this.locale);
    }

    /**
     * 2021-05-03T11:20:28+08:00
     * @param source
     * @return
     */
    public static Optional<DT> of_ISO8601_yyyy_MM_ddTHH_mm_ss_SSS_ZZ(@NonNull String source){
        try{
            //Date result=DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.parse(source);
            Date result= STYLE_yyyy_MM_ddTHH_mm_ss_SSS_ZZ_PATTERN.parse(source);
            return Optional.of(ofDate(result));
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 2021-05-03T11:20:28+08:00
     * @return
     */
    public String format_ISO8601_yyyy_MM_ddTHH_mm_ss_ZZ(){
        return DateFormatUtils.format(this.time, DateFormatUtils.ISO_8601_EXTENDED_DATETIME_TIME_ZONE_FORMAT.getPattern(), this.zone, this.locale);
    }


    /**
     * 20210503T112028
     * @param source
     * @return
     */
    public Optional<DT> of_yyyyMMddHHmmss(@NonNull String source){
        try {
            Date result= OLD_STYLE_yyyyMMddHHmmss_PATTERN.parse(source);
            return Optional.of(ofDate(result));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 20210503T112028
     * @return
     */
    public String format_yyyyMMddHHmmss(){
        return OLD_STYLE_yyyyMMddHHmmss_PATTERN.format(this.time);
    }

    /**
     * 20210503T112028999
     * @param source
     * @return
     */
    public Optional<DT> of_yyyyMMddHHmmssSSS(@NonNull String source){
        try {
            Date result= OLD_STYLE_yyyyMMddHHmmssSSS_PATTERN.parse(source);
            return Optional.of(ofDate(result));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 20210503T112028999
     * @return
     */
    public String format_yyyyMMddHHmmssSSS(){
        return OLD_STYLE_yyyyMMddHHmmssSSS_PATTERN.format(this.time);
    }

    /**
     * 11:20:28
     * @param source
     * @return
     */
    public static Optional<DT> of_HH_mm_ss(@NonNull String source){
        try{
            Date result=DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.parse(source);
            return Optional.of(ofDate(result));
        }catch (Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * 11:20:28
     * @return
     */
    public String format_HH_mm_ss(){
        return DateFormatUtils.format(this.time, DateFormatUtils.ISO_8601_EXTENDED_TIME_FORMAT.getPattern(), this.zone, this.locale);
    }

    /**
     * T11:20:28
     * @param source
     * @return
     */
    public static Optional<DT> of_ISO8601_HH_mm_ss(@NonNull String source){
        try {
            Date result=FastDateFormat.getInstance("'T'HH:mm:ss").parse(source);
            return Optional.of(ofDate(result));
        } catch (ParseException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * T11:20:28
     * @return
     */
    public String format_ISO8601_HH_mm_ss(){
        return DateFormatUtils.format(this.time, "'T'HH:mm:ss", this.zone, this.locale);
    }

    /**
     * 从日期字符串和模板创建DT实例，如果格式有误会返回null
     * @param dateString
     * @param pattern
     * @return
     */
    @Nullable
    public static DT ofPattern(@NonNull String dateString, @NonNull String pattern){
        DT result=new DT();
        try {
            Date dt= DateUtils.parseDate(dateString, pattern);
            result.setTime(dt);
            return result;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 小时部分清零
     * @return
     */
    @NonNull
    public DT truncateHour(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(this.time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        this.time=calendar.getTime();
        return this;
    }

    /**
     * 分钟部分清零
     * @return
     */
    @NonNull
    public DT truncateMinute(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(this.time);
        calendar.set(Calendar.MINUTE, 0);
        this.time=calendar.getTime();
        return this;
    }

    /**
     * 分钟清零
     * @return
     */
    @NonNull
    public DT truncateSecond(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(this.time);
        calendar.set(Calendar.SECOND, 0);
        this.time=calendar.getTime();
        return this;
    }

    /**
     * 毫秒清零
     * @return
     */
    @NonNull
    public DT truncateMillisecond(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(this.time);
        calendar.set(Calendar.MILLISECOND, 0);
        this.time=calendar.getTime();
        return this;
    }

    /**
     * 时间部分清零（小时，分钟，秒，毫秒）
     * @return
     */
    @NonNull
    public DT truncateTimes(){
        return this.truncateHour()
                .truncateMinute()
                .truncateSecond()
                .truncateMillisecond();
    }

    /**
     * 日期部分清零（年份，月，日）
     * @return
     */
    @NonNull
    public DT truncateDates(){
        return this.truncateYear()
                .truncateMonth()
                .truncateDay();
    }

    @NonNull
    public DT truncateYear(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(this.time);
        calendar.set(Calendar.YEAR, 0);
        this.time=calendar.getTime();
        return this;
    }

    @NonNull
    public DT truncateMonth(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(this.time);
        calendar.set(Calendar.MONTH, 0);
        this.time=calendar.getTime();
        return this;
    }

    @NonNull
    public DT truncateDay(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(this.time);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        this.time=calendar.getTime();
        return this;
    }

    @NonNull
    public DT addYears(@NonNull Integer years){
        this.time=DateUtils.addYears(this.time, years);
        return this;
    }

    @NonNull
    public DT addMonths(@NonNull Integer months){
        this.time=DateUtils.addMonths(this.time, months);
        return this;
    }

    @NonNull
    public DT addDays(@NonNull Integer days){
        this.time=DateUtils.addDays(this.time, days);
        return this;
    }

    @NonNull
    public DT addHours(@NonNull Integer hours){
        this.time=DateUtils.addHours(this.time, hours);
        return this;
    }

    @NonNull
    public DT addMinutes(@NonNull Integer minutes){
        this.time=DateUtils.addMinutes(this.time, minutes);
        return this;
    }

    @NonNull
    public DT addSeconds(@NonNull Integer seconds){
        this.time=DateUtils.addSeconds(this.time, seconds);
        return this;
    }

    @NonNull
    public DT addMilliseconds(@NonNull Integer milliseconds){
        this.time=DateUtils.addMilliseconds(this.time, milliseconds);
        return this;
    }

    /**
     * 向上舍入年份
     * 2018-11-23 15:59:29 - > 2019-01-01 00:00:00，截取到年，年向上取整（即+1）
     * @return
     */
    @NonNull
    public DT ceilingYear(){
        this.time=DateUtils.ceiling(this.time, Calendar.YEAR);
        return this;
    }

    /**
     * 向上舍入月份
     * 2018-11-23 15:59:29 - > 2018-12-01 00:00:00，截取到月，月向上取整（即+1）；如果是12月的话，月进1变成01，年进1
     * @return
     */
    @NonNull
    public DT ceilingMonth(){
        this.time=DateUtils.ceiling(this.time, Calendar.MONTH);
        return this;
    }

    /**
     * 向上舍入日期
     * 2018-11-23 15:59:29 - > 2018-11-24 00:00:00，截取到日，日向上取整（即+1）；如果是30（或28/31）的话，日进1变成01，月进1
     * @return
     */
    @NonNull
    public DT ceilingDay(){
        this.time=DateUtils.ceiling(this.time, Calendar.DATE);
        return this;
    }

    /**
     * 向上舍入小时
     * 2018-11-23 15:59:29 - > 2018-11-23 12:00:00，截取到时，时向上取整（即+1）；如果是23点的话，时进1变成00，日进1
     * @return
     */
    @NonNull
    public DT ceilingHour(){
        this.time=DateUtils.ceiling(this.time, Calendar.HOUR);
        return this;
    }

    /**
     * 向上舍入分钟
     * 2018-11-23 15:59:29 - > 2018-11-23 11:33:00，截取到分，分向上取整（即+1）；如果是59分的话，分进1变成00，时进1
     * @return
     */
    @NonNull
    public DT ceilingMinute(){
        this.time=DateUtils.ceiling(this.time, Calendar.MINUTE);
        return this;
    }

    /**
     * 向上舍入秒
     * 2018-11-23 15:59:29 - > 2018-11-23 11:32:13，截取到分，分向上取整（即+1）
     * @return
     */
    @NonNull
    public DT ceilingSecond(){
        this.time=DateUtils.ceiling(this.time, Calendar.SECOND);
        return this;
    }

    /**
     * 向上舍入毫秒
     * @return
     */
    @NonNull
    public DT ceilingMilliseconds(){
        this.time=DateUtils.ceiling(this.time, Calendar.MILLISECOND);
        return this;
    }

    /**
     * 四舍五入年份
     * @return
     */
    @NonNull
    public DT roundYear(){
        this.time=DateUtils.round(this.time, Calendar.YEAR);
        return this;
    }

    /**
     * 四舍五入月份
     * @return
     */
    @NonNull
    public DT roundMonth(){
        this.time=DateUtils.round(this.time, Calendar.MONTH);
        return this;
    }

    /**
     * 四舍五入天
     * @return
     */
    @NonNull
    public DT roundDay(){
        this.time=DateUtils.round(this.time, Calendar.DATE);
        return this;
    }

    /**
     * 四舍五入小时
     * @return
     */
    @NonNull
    public DT roundHour(){
        this.time=DateUtils.round(this.time, Calendar.HOUR);
        return this;
    }

    /**
     * 四舍五入分钟
     * @return
     */
    @NonNull
    public DT roundMinute(){
        this.time=DateUtils.round(this.time, Calendar.MINUTE);
        return this;
    }

    /**
     * 四舍五入秒
     * @return
     */
    @NonNull
    public DT roundSecond(){
        this.time=DateUtils.round(this.time, Calendar.SECOND);
        return this;
    }

    /**
     * 四舍五入毫秒
     * @return
     */
    @NonNull
    public DT roundMillisecond(){
        this.time=DateUtils.round(this.time, Calendar.MILLISECOND);
        return this;
    }

    /**
     * 修改年份
     * @param year
     * @return
     */
    @NonNull
    public DT setYear(@NonNull Integer year){
        this.time=DateUtils.setYears(this.time, year);
        return this;
    }

    /**
     * 修改月份
     * @param month
     * @return
     */
    @NonNull
    public DT setMonth(@NonNull Integer month){
        this.time=DateUtils.setMonths(this.time, month);
        return this;
    }

    /**
     * 修改日期
     * @param day
     * @return
     */
    @NonNull
    public DT setDay(@NonNull Integer day){
        DateUtils.setDays(this.time, day);
        return this;
    }

    /**
     * 修改小时
     * @param hour
     * @return
     */
    @NonNull
    public DT setHour(@NonNull Integer hour){
        this.time=DateUtils.setHours(this.time, hour);
        return this;
    }

    /**
     * 修改分钟
     * @param minute
     * @return
     */
    @NonNull
    public DT setMinute(@NonNull Integer minute){
        this.time=DateUtils.setMinutes(this.time, minute);
        return this;
    }

    /**
     * 修改秒数
     * @param second
     * @return
     */
    @NonNull
    public DT setSecond(@NonNull Integer second){
        this.time=DateUtils.setSeconds(this.time, second);
        return this;
    }

    /**
     * 修改毫秒数
     * @param millsecond
     * @return
     */
    @NonNull
    public DT setMillsecond(@NonNull Integer millsecond){
        this.time=DateUtils.setMilliseconds(this.time, millsecond);
        return this;
    }

    /**
     * 获取当前时间距指定时间的天数
     * @param date
     * @return
     */
    public long betweenDays(@NonNull Date date){
        long val1=DateUtils.getFragmentInDays(this.time, Calendar.YEAR);
        long val2=DateUtils.getFragmentInDays(date, Calendar.YEAR);
        return val2-val1;
    }

    /**
     * 获取当前时间距指定时间的小时数
     * @param date
     * @return
     */
    public long betweenHours(@NonNull Date date){
        long val1=DateUtils.getFragmentInHours(this.time, Calendar.YEAR);
        long val2=DateUtils.getFragmentInHours(date, Calendar.YEAR);
        return val2-val1;
    }

    /**
     * 获取当前时间距指定时间的分钟
     * @param date
     * @return
     */
    public long betweenMinutes(@NonNull Date date){
        long val1=DateUtils.getFragmentInMinutes(this.time, Calendar.YEAR);
        long val2=DateUtils.getFragmentInMinutes(date, Calendar.YEAR);
        return val2-val1;
    }

    /**
     * 获取当前时间距指定时间的秒数
     * @param date
     * @return
     */
    public long betweenSeconds(@NonNull Date date){
        long val1=DateUtils.getFragmentInSeconds(this.time, Calendar.YEAR);
        long val2=DateUtils.getFragmentInSeconds(date, Calendar.YEAR);
        return val2-val1;
    }

    /**
     * 获取当前时间距指定时间的毫秒数
     * @param date
     * @return
     */
    public long betweenMillsecond(@NonNull Date date){
        long val1=DateUtils.getFragmentInMilliseconds(this.time, Calendar.YEAR);
        long val2=DateUtils.getFragmentInMilliseconds(date, Calendar.YEAR);
        return val2-val1;
    }

    /**
     * 获取当前时间的Calendar实例
     * @return
     */
    @NonNull
    public Calendar calendar(){
        return DateUtils.toCalendar(this.time);
    }

    public int year(){
        return this.calendar().get(Calendar.YEAR);
    }

    public int month(){
        return this.calendar().get(Calendar.MONDAY)+1;
    }

    public int day(){
        return this.calendar().get(Calendar.DAY_OF_MONTH);
    }

    public int hour(){
        return this.calendar().get(Calendar.HOUR);
    }

    public int minute(){
        return this.calendar().get(Calendar.MINUTE);
    }

    public int second(){
        return this.calendar().get(Calendar.SECOND);
    }

    public int millsecond(){
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
     * @param pattern
     * @return
     */
    @NonNull
    public String format(@NonNull String pattern){
        String result = new SimpleDateFormat(pattern).format(this.time);
        return result;
    }

    @Override
    public String toString() {
        return this.format_ISO8601_yyyy_MM_ddTHH_mm_ss_SSS();
    }

    @NonNull
    public Date date(){
        return this.time;
    }

    /**
     * 生成日期字符串，不含时间部分
     * @return
     */
    @NonNull
    public String dateStr(){
        return this.format_yyyy_MM_dd();
    }

    /**
     * 生成旧格式日期时间字符串
     * @return
     */
    @NonNull
    public String dateTimeStr(){
        return this.format_yyyy_MM_dd_HH_mm_ss();
    }

    /**
     * 生成时间字符串，旧格式，不含毫秒
     * @return
     */
    @NonNull
    public String timeStr(){
        return this.format_HH_mm_ss();
    }

    /**
     * 生成LocalDate实例
     * @param zoneId
     * @return
     */
    @NonNull
    public LocalDate localDate(@NonNull ZoneId zoneId){
        return this.time.toInstant().atZone(zoneId).toLocalDate();
    }

    /**
     * 生成yyyyMMdd格式的整数
     * @return
     */
    @NonNull
    public Long yyyyMMdd(){
        return Long.valueOf(format("yyyyMMdd"));
    }

    /**
     * 生成yyyyMMddhhmmss格式的整数
     * @return
     */
    @NonNull
    public Long yyyyMMddHHmmss(){
        return Long.valueOf(format("yyyyMMddHHmmss"));
    }

    /**
     * 生成LocalDate实例
     * @return
     */
    @NonNull
    public LocalDate localDate(){
        return this.localDate(ZoneId.systemDefault());
    }

    /**
     * 生成LocalDateTime实例
     * @param zoneId
     * @return
     */
    @NonNull
    public LocalDateTime localDateTime(@NonNull ZoneId zoneId){
        return this.time.toInstant().atZone(zoneId).toLocalDateTime();
    }

    /**
     * 生成LocalDateTime实例
     * @return
     */
    @NonNull
    public LocalDateTime localDateTime(){
        return this.localDateTime(ZoneId.systemDefault());
    }

    /**
     * 生成时区日期时间
     * @param zoneId
     * @return
     */
    @NonNull
    public ZonedDateTime zonedDateTime(@NonNull ZoneId zoneId){
        return ZonedDateTime.ofInstant(this.time.toInstant(), zoneId);
    }

    /**
     * 生成时区日期时间
     * @return
     */
    @NonNull
    public ZonedDateTime zonedDateTime(){
        return this.zonedDateTime(this.zone.toZoneId());
    }

    /**
     * 生成时间戳
     * @return
     */
    @NonNull
    public Instant instant(){
        return this.time.toInstant();
    }

    /**
     * 生成毫秒
     * @return
     */
    @NonNull
    public long epochSecond(){
        return this.time.getTime();
    }

    @Override
    @SneakyThrows
    public DT clone() {
        return DT.ofDT(this);
    }

    @NonNull
    public Boolean before(@NonNull DT dt){
        return this.compareTo(dt)<0;
    }

    @NonNull
    public Boolean after(@NonNull DT dt){
        return this.compareTo(dt)>0;
    }

    @Override
    public int compareTo(Object o) {
        Assert.isInstanceOf(DT.class, o);
        return this.time.compareTo(((DT)o).time);
    }

    public static void main(String[] args){
        System.out.println(DT.of_yyyyMMdd(20220503));
        System.out.println(DT.of_yyyy_MM_dd(2022, 5, 03));
//        System.out.println(DT.ofNow().yyyyMMdd());
//        System.out.println(DT.ofNow().yyyyMMddHHmmss());
//        System.out.println(DT.ofNow().format_ISO8601_yyyy_MM_ddTHH_mm_ss_ZZ());
//        String s= DT.ofNow().format_ISO8601_yyyy_MM_ddTHH_mm_ss_SSS_ZZ();
//        System.out.println(s);
//        System.out.println(DT.of_ISO8601_yyyy_MM_ddTHH_mm_ss_SSS_ZZ(s).get());
//        System.out.println(DT.ofNow().betweenDays(DT.ofNow().addDays(1).date()));
//        System.out.println(DT.ofNow().betweenHours(DT.ofNow().addDays(1).addHours(1).date()));
//        System.out.println(DT.ofNow().betweenMinutes(DT.ofNow().addDays(1).addHours(1).addMinutes(1).date()));
//        System.out.println(DT.ofNow().betweenMillsecond(DT.ofNow().addDays(1).addHours(1).addMinutes(1).addMilliseconds(1).date()));
//        System.out.println(DT.ofNow().date());
//        System.out.println(DT.ofNow().localDate());
//        System.out.println(DT.ofNow().localDateTime());
//        System.out.println(DT.ofNow());
//        DT dt= DT.ofNow();
//        System.out.println("year: " + dt.year());
//        System.out.println("month: " + dt.month());
//        System.out.println("day: " + dt.day());
//        System.out.println("hour: " + dt.hour());
//        System.out.println("minute: " + dt.minute());
//        System.out.println("second: " + dt.second());
//        System.out.println("millisecond: " + dt.millsecond());
//        System.out.println(DateFormatUtils.ISO_8601_EXTENDED_DATE_FORMAT.format(new Date()));
//        System.out.println(DT.ofNow().format_ISO8601_yyyy_MM_ddTHH_mm_ss_ZZ());
//        System.out.println(DT.ofNow().format_yyyy_MM_dd());
//        System.out.println(DT.ofNow().format_ISO8601_yyyy_MM_ddTHH_mm_ss());
//        System.out.println(DT.ofNow().format_ISO8601_yyyy_MM_ddTHH_mm_ss_ZZ());
//        System.out.println(DT.ofNow().format_yyyy_MM_dd_HH_mm_ss_SSS());
//        Optional<DT> date= DT.of_ISO8601_yyyy_MM_ddTHH_mm_ss_ZZ("2021-05-03T13:12:39+08:00");
//        date.ifPresent(d->{
//            System.out.println(d.format_ISO8601_yyyy_MM_ddTHH_mm_ss_ZZ());
//        });
    }
}
