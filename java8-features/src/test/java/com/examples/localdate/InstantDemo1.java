package com.examples.localdate;

import java.time.*;

/**
 * 时间戳测试
 */
public class InstantDemo1 {
    public static void test1(){
        Instant in=Instant.now();
        System.out.println("当前时间戳:" + in);
        LocalDate ld=in.atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println("GMT+8日期："+ld);
        LocalDateTime ldt=in.atZone(ZoneId.systemDefault()).toLocalDateTime();
        System.out.println("GMT+8时间："+ldt);
        ld=in.atZone(ZoneId.of("UTC")).toLocalDate();
        System.out.println("UTC日期："+ld);
        ldt=in.atZone(ZoneId.of("UTC")).toLocalDateTime();
        System.out.println("UTC时间："+ldt);

        in=Instant.ofEpochMilli(System.currentTimeMillis());
        System.out.println("System.currentTimeMillis()转换的时间戳："+in);
        in=Instant.parse("2017-02-03T10:37:30.00Z");
        System.out.println(in);
        //处理偏移时间，UTC时间比东八区要早8小时
        OffsetDateTime odt=in.atOffset(ZoneOffset.ofHours(-8));//东八区-8=UTC时间
        System.out.println(odt);
    }

    public static void main(String[] args){
        test1();
    }
}
