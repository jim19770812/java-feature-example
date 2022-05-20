package com.examples.localdate;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 时区偏移量测试
 */
public class ZonedDateTimeDemo1 {
    public static void test1(){
        ZonedDateTime zdt=ZonedDateTime.now(ZoneId.systemDefault());
        System.out.println(zdt);
        System.out.println(zdt.toInstant());
        System.out.println(zdt.toLocalDate());
        System.out.println(zdt.toLocalDateTime());
        System.out.println(zdt.withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        System.out.println(zdt.withZoneSameInstant(ZoneId.of("UTC")).toOffsetDateTime());
        System.out.println(zdt.withZoneSameInstant(ZoneId.of("UTC+3")).toLocalDateTime());
        System.out.println(zdt.withZoneSameInstant(ZoneId.of("UTC+3")).toOffsetDateTime());
    }

    public static void main(String[] args){
        test1();
    }
}
