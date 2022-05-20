package com.examples.localdate;

import lombok.SneakyThrows;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * 日期测试
 */
public class LocalDateDemo1 {
    public void test1(){
        LocalDate now= LocalDate.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String dateStr=formatter.format(now);
        System.out.println(dateStr);
        TemporalAccessor ta=formatter.parse("2021-12-10");
        LocalDate ld=LocalDate.from(ta);
        LocalDate.from(ld);
        System.out.println("getChronology(年代)："+ld.getChronology());
        System.out.println("getDayOfMonth："+ld.getDayOfMonth());
        System.out.println("getEra："+ld.getEra());
        System.out.println("getDayOfYear："+ld.getDayOfYear());
        System.out.println("getDayOfMonth："+ld.getDayOfMonth());
        System.out.println("getDayOfWeek："+ld.getDayOfWeek());
        System.out.println("getMonthValue："+ld.getMonthValue());
        Period pr=Period.between(now, ld);//获取时间段对象
        System.out.println("时间段："+ pr);
        System.out.println("两个时间相差日："+ pr.getDays());
        System.out.println("两个时间相差月："+ pr.getMonths());
        ld=ld.plusMonths(3);
        System.out.println("延后3个月：" + ld);
    }

    public Date date2LocalDate(LocalDate ld){
        ZonedDateTime zonedDateTime = LocalDate.MAX.atStartOfDay(ZoneId.systemDefault());
        Date result=Date.from(zonedDateTime.toInstant());
        return result;
    }

    public LocalDate localDate2Date(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @SneakyThrows
    public static void main(String[] args){
        LocalDateDemo1 demo=new LocalDateDemo1();
        demo.test1();
    }
}
